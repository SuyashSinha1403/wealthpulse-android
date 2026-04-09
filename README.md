# WealthPulse Android

WealthPulse Android is a native Android companion app for WealthPulse. It combines a WebView-based product surface with on-device notification ingestion so financial and transaction-related notifications can be captured on the device and forwarded to a backend for processing.

## Highlights

- Native Android app built with Kotlin and Jetpack Compose
- WebView entry point for the WealthPulse web experience
- Notification Listener service for device-side ingestion
- Local persistence with Room for queued notification delivery
- Background sync with WorkManager
- Secure local session storage using `EncryptedSharedPreferences`
- Supabase-backed authentication and server communication

## Tech Stack

- Kotlin
- Jetpack Compose
- AndroidX WorkManager
- Room
- OkHttp
- Supabase Kotlin SDK
- Android Security Crypto

## Project Structure

```text
app/src/main/java/com/suyash/wealthpulse/
|- auth/       # Supabase auth/session handling
|- data/local/ # Room database and DAO
|- service/    # Notification listener service
|- ui/         # Compose screens
|- utils/      # Device-level helpers
|- worker/     # Background sync worker
```

## Requirements

- Android Studio Hedgehog or newer
- JDK 11+
- Android SDK with `compileSdk 35`
- Minimum Android version: API 24

## Getting Started

1. Clone the repository.
2. Open the project in Android Studio.
3. Sync Gradle dependencies.
4. Configure your Supabase credentials before running the app.
5. Run the app on a device or emulator.

## Configuration

The repository does not include private credentials.

Before running the ingestion flow, update the placeholder values in `app/src/main/java/com/suyash/wealthpulse/auth/SupabaseAuthManager.kt` with your own project settings:

- `supabaseUrl`
- `supabaseKey`

Use a safe client-side key only. Do not commit service-role keys, `.env` files, signing keys, or local machine config.

## How It Works

1. The app opens a native entry screen and routes users into the WealthPulse web experience.
2. Users grant Notification Listener access on-device.
3. Incoming notifications are captured by the listener service.
4. Notifications are stored locally in Room.
5. A background worker batches and forwards pending notifications to the backend.

## Security Notes

- Local auth session data is stored using encrypted shared preferences.
- Sensitive machine-specific files such as `local.properties` should remain untracked.
- Review backend auth rules and database policies before deploying to production.

## Status

This project is an early-stage Android client and ingestion companion for the broader WealthPulse product.

## License

No license has been added yet. If you plan to make this repository public for broader use, consider adding a license file.
