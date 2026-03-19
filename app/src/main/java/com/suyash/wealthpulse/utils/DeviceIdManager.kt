package com.suyash.wealthpulse.utils

import android.content.Context
import android.content.SharedPreferences
import java.util.UUID

class DeviceIdManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("wealthpulse_device_prefs", Context.MODE_PRIVATE)

    fun getDeviceId(): String {
        var deviceId = prefs.getString(KEY_DEVICE_ID, null)
        if (deviceId == null) {
            deviceId = UUID.randomUUID().toString()
            prefs.edit().putString(KEY_DEVICE_ID, deviceId).apply()
        }
        return deviceId
    }

    companion object {
        private const val KEY_DEVICE_ID = "device_id"
    }
}
