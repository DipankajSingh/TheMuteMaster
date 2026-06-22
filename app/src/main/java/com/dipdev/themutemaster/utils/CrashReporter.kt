package com.dipdev.themutemaster.utils

import android.os.Build
import com.google.firebase.crashlytics.FirebaseCrashlytics
import javax.inject.Inject
import javax.inject.Singleton

/**
 * A centralized, Hilt-injectable wrapper around Firebase Crashlytics.
 *
 * Use this to:
 *  - [log]           Leave a breadcrumb so we know what happened before an error.
 *  - [recordNonFatal] Capture a caught (silent) exception without crashing the app.
 *  - [setKey]         Attach custom key-value state to every report.
 */
@Singleton
class CrashReporter @Inject constructor() {

    private val crashlytics = FirebaseCrashlytics.getInstance()

    init {
        // Attach device context to every single crash/non-fatal report automatically.
        crashlytics.setCustomKey("device_manufacturer", Build.MANUFACTURER)
        crashlytics.setCustomKey("device_model", Build.MODEL)
        crashlytics.setCustomKey("android_version", Build.VERSION.RELEASE)
        crashlytics.setCustomKey("android_sdk_int", Build.VERSION.SDK_INT)
    }

    /**
     * Adds a breadcrumb log message. These appear in the "Logs" tab in the Crashlytics
     * dashboard and help trace the sequence of events leading up to an error.
     */
    fun log(message: String) {
        crashlytics.log(message)
    }

    /**
     * Records a caught exception as a non-fatal error in Firebase.
     * These appear under the "Non-fatals" filter in the Crashlytics dashboard.
     * Use this wherever you have a try-catch that swallows a real error silently.
     *
     * @param throwable The caught exception.
     * @param context   A short human-readable description of where this happened.
     */
    fun recordNonFatal(throwable: Throwable, context: String = "") {
        if (context.isNotBlank()) {
            crashlytics.log("Non-fatal context: $context")
        }
        crashlytics.recordException(throwable)
    }

    /**
     * Attaches an arbitrary key-value pair to all future crash reports from this session.
     * Useful for tagging app state (e.g., which screen the user is on).
     */
    fun setKey(key: String, value: String) {
        crashlytics.setCustomKey(key, value)
    }

    fun setKey(key: String, value: Boolean) {
        crashlytics.setCustomKey(key, value)
    }

    fun setKey(key: String, value: Int) {
        crashlytics.setCustomKey(key, value)
    }
}
