package com.suyash.wealthpulse.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Index

@Entity(
    tableName = "pending_notifications",
    indices = [Index(value = ["local_dedup_key"], unique = true)]
)
data class PendingNotification(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val app_package: String,
    val title: String,
    val body: String,
    val received_at: String,
    val local_dedup_key: String,
    val retry_count: Int = 0,
    val status: String = "pending" // pending, failed, sent
)
