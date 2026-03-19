package com.suyash.wealthpulse.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface PendingNotificationDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(notification: PendingNotification): Long

    @Query("SELECT * FROM pending_notifications WHERE status = 'pending' ORDER BY received_at ASC LIMIT :limit")
    suspend fun getPendingBatch(limit: Int): List<PendingNotification>

    @Query("UPDATE pending_notifications SET status = :status, retry_count = :retryCount WHERE id = :id")
    suspend fun updateStatus(id: Long, status: String, retryCount: Int)

    @Query("SELECT COUNT(*) FROM pending_notifications")
    suspend fun getCount(): Int

    @Query("DELETE FROM pending_notifications WHERE id IN (SELECT id FROM pending_notifications ORDER BY received_at ASC LIMIT :countToRemove)")
    suspend fun removeOldest(countToRemove: Int)

    @Transaction
    suspend fun insertWithLimit(notification: PendingNotification) {
        val insertedId = insert(notification)
        if (insertedId != -1L) {
            // Check size
            val currentCount = getCount()
            if (currentCount > 500) {
                removeOldest(currentCount - 500)
            }
        }
    }
}
