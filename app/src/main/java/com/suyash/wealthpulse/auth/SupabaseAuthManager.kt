package com.suyash.wealthpulse.auth

import android.content.Context
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.auth
import org.json.JSONObject

class SupabaseAuthManager(context: Context) {

    val client: SupabaseClient = createSupabaseClient(
        supabaseUrl = "https://oaatlzoqtmwqwzmzmvqm.supabase.co",
        supabaseKey = "YOUR_ANON_KEY"
    ) {
        install(Auth)
    }

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        "supabase_auth_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    suspend fun getAccessToken(): String? {
        try {
            // Check supabase-kt internal session wrapper first
            val session = client.auth.currentSessionOrNull()
            if (session != null) {
                Log.d("SupabaseAuth", "Successfully fetched valid auth session from Supabase Client.")
                return session.accessToken
            }
        } catch (e: Exception) {
            Log.e("SupabaseAuth", "Supabase Client Auth check failed. Falling back to explicit storage.", e)
        }

        // Check fallback explicit encrypted session string
        val sessionJson = sharedPreferences.getString("supabase_session", null)
        if (sessionJson != null) {
            try {
                val jsonObject = JSONObject(sessionJson)
                val token = jsonObject.optString("access_token", null)
                if (token.isNullOrEmpty()) {
                    Log.e("SupabaseAuth", "Supabase auth mapping failure: session stored dynamically but extracted token is empty.")
                    return null
                }
                Log.d("SupabaseAuth", "Successfully extracted access_token dynamically from stored JSON session.")
                return token
            } catch (e: Exception) {
                Log.e("SupabaseAuth", "Failed to decode the securely stored session JSON", e)
            }
        } else {
            Log.e("SupabaseAuth", "Auth failure: No valid session structure found in encrypted storage.")
        }
        return null
    }

    fun mockLogin(sessionJson: String) {
        sharedPreferences.edit().putString("supabase_session", sessionJson).apply()
        Log.d("SupabaseAuth", "Mock login performed. Full session JSON securely stored to disk.")
    }
}
