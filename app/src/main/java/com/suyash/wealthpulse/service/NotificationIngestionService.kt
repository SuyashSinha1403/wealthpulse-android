package com.suyash.wealthpulse.service

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.suyash.wealthpulse.data.local.AppDatabase
import com.suyash.wealthpulse.data.local.PendingNotification
import com.suyash.wealthpulse.worker.NotificationSyncWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.time.Instant

class NotificationIngestionService : NotificationListenerService() {

    private val validPackages = setOf(
        "com.phonepe.app",
        "com.google.android.apps.nbu.paisa.user",
        "net.one97.paytm"
    )

    private val requiredKeywords = listOf(
        "₹", "debited", "credited", "paid", "received", "upi", "txn"
    )

    private val rejectionKeywords = listOf(
        "cashback", "offer", "reward", "promo", "discount"
    )

    private val scope = CoroutineScope(Dispatchers.IO)

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)
        sbn ?: return

        val packageName = sbn.packageName
        val title = sbn.notification.extras.getString("android.title")
        val text = sbn.notification.extras.getString("android.text")

        if (title.isNullOrEmpty() || text.isNullOrEmpty()) return
        if (packageName !in validPackages) return

        val combinedText = "$title $text".lowercase()

        // Reject if it matches any rejection words
        if (rejectionKeywords.any { combinedText.contains(it) }) {
            Log.d("NotificationIngestion", "Filtered out promotional/spam: $title")
            return
        }

        // Accept if it contains at least one target financial keyword
        if (requiredKeywords.any { combinedText.contains(it) }) {
            val normalizedBody = text.lowercase().trim().replace("\\s+".toRegex(), " ")
            val dedupKeyStr = packageName + normalizedBody
            val localDedupKey = hashString(dedupKeyStr)

            val pendingNotification = PendingNotification(
                app_package = packageName,
                title = title,
                body = text,
                received_at = Instant.now().toString(),
                local_dedup_key = localDedupKey
            )

            scope.launch {
                try {
                    val db = AppDatabase.getDatabase(applicationContext)
                    db.pendingNotificationDao().insertWithLimit(pendingNotification)
                    Log.d("NotificationIngestion", "Inserted to Room. Triggering Worker.")
                    
                    // Trigger Immediate Sync
                    triggerSyncWorker()
                } catch (e: Exception) {
                    Log.e("NotificationIngestion", "Failed to insert into database", e)
                }
            }
        }
    }

    private fun triggerSyncWorker() {
        val workRequest = OneTimeWorkRequestBuilder<NotificationSyncWorker>().build()
        WorkManager.getInstance(applicationContext).enqueueUniqueWork(
            "notification_sync_worker",
            ExistingWorkPolicy.KEEP,
            workRequest
        )
    }

    private fun hashString(input: String): String {
        return MessageDigest.getInstance("SHA-256")
            .digest(input.toByteArray())
            .joinToString("") { "%02x".format(it) }
    }
}
