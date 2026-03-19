package com.suyash.wealthpulse.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.suyash.wealthpulse.auth.SupabaseAuthManager
import com.suyash.wealthpulse.data.local.AppDatabase
import com.suyash.wealthpulse.utils.DeviceIdManager
import kotlinx.coroutines.delay
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class NotificationSyncWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    private val db = AppDatabase.getDatabase(appContext)
    private val authManager = SupabaseAuthManager(appContext)
    private val deviceIdManager = DeviceIdManager(appContext)
    private val okHttpClient = OkHttpClient()

    override suspend fun doWork(): Result {
        Log.d("NotificationSyncWorker", "Worker execution START. Retrieving pending queues...")
        
        val token = authManager.getAccessToken()
        if (token.isNullOrEmpty()) {
            Log.e("NotificationSyncWorker", "ABORT: Auth token fetch failed. Either not signed in or session expired.")
            return Result.failure()
        }

        val deviceId = deviceIdManager.getDeviceId()
        val dao = db.pendingNotificationDao()
        val batch = dao.getPendingBatch(5)

        if (batch.isEmpty()) {
            Log.d("NotificationSyncWorker", "Worker STOP. Nothing to process.")
            return Result.success()
        }
        Log.d("NotificationSyncWorker", "Successfully initiated extraction batch length of ${batch.size}.")

        var allSuccess = true

        for (notification in batch) {
            val payload = JSONObject().apply {
                put("app_package", notification.app_package)
                put("title", notification.title)
                put("body", notification.body)
                put("received_at", notification.received_at)
                put("device_id", deviceId)
            }

            val requestBody = payload.toString().toRequestBody("application/json".toMediaType())

            val request = Request.Builder()
                .url("https://oaatlzoqtmwqwzmzmvqm.supabase.co/functions/v1/notifications")
                .addHeader("Authorization", "Bearer $token")
                .addHeader("Content-Type", "application/json")
                .post(requestBody)
                .build()

            try {
                val response = okHttpClient.newCall(request).execute()
                if (response.isSuccessful) {
                    dao.updateStatus(notification.id, "sent", notification.retry_count)
                    Log.d("NotificationSyncWorker", "Notification ID ${notification.id} payload delivered seamlessly.")
                } else if (response.code == 401) {
                    Log.e("NotificationSyncWorker", "HTTP 401 UNAUTHORIZED on ID ${notification.id}. Session invalid. Marking as 'failed_auth' securely and terminating specific retry attempt.")
                    dao.updateStatus(notification.id, "failed_auth", notification.retry_count)
                    allSuccess = false
                } else {
                    Log.e("NotificationSyncWorker", "Upload failed on notification ${notification.id} Code: ${response.code}")
                    handleFailure(notification)
                    allSuccess = false
                }
                response.close()
            } catch (e: Exception) {
                Log.e("NotificationSyncWorker", "Critical Network Exception for ID ${notification.id}", e)
                handleFailure(notification)
                allSuccess = false
            }
        }

        Log.d("NotificationSyncWorker", "Batch Loop Complete. Status: allSuccess = $allSuccess")

        // 3-SECOND DELAY SAFEGUARD
        val remainingCount = dao.getCount() // Generic validation 
        if (remainingCount > 0 && batch.isNotEmpty()) {
            delay(3000)
            Log.d("NotificationSyncWorker", "Items still pending locally. 3 second backoff safely passed. Re-issuing job.")
            WorkManager.getInstance(applicationContext).enqueueUniqueWork(
                "notification_sync_worker",
                ExistingWorkPolicy.KEEP,
                OneTimeWorkRequestBuilder<NotificationSyncWorker>().build()
            )
        } else {
            Log.d("NotificationSyncWorker", "System clean. Queue finished.")
        }

        return if (allSuccess) Result.success() else Result.retry()
    }

    private suspend fun handleFailure(notification: com.suyash.wealthpulse.data.local.PendingNotification) {
        val newRetryCount = notification.retry_count + 1
        if (newRetryCount >= 5) {
            db.pendingNotificationDao().updateStatus(notification.id, "failed", newRetryCount)
            Log.e("NotificationSyncWorker", "Failed fully: ID ${notification.id} exceeded limit maxing out at 5. Permanently dropping.")
        } else {
            db.pendingNotificationDao().updateStatus(notification.id, "pending", newRetryCount)
            Log.d("NotificationSyncWorker", "Queued up ID ${notification.id} mapping back to pending mode. Will retry later. Currently at attempt $newRetryCount out of 5")
        }
    }
}
