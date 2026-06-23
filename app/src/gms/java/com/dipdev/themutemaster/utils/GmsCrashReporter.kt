package com.dipdev.themutemaster.utils

import android.os.Build
import com.google.firebase.crashlytics.FirebaseCrashlytics
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GmsCrashReporter @Inject constructor() : CrashReporter {

    private val crashlytics = FirebaseCrashlytics.getInstance()

    init {
        crashlytics.setCustomKey("device_manufacturer", Build.MANUFACTURER)
        crashlytics.setCustomKey("device_model", Build.MODEL)
        crashlytics.setCustomKey("android_version", Build.VERSION.RELEASE)
        crashlytics.setCustomKey("android_sdk_int", Build.VERSION.SDK_INT)
    }

    override fun log(message: String) {
        crashlytics.log(message)
    }

    override fun recordNonFatal(throwable: Throwable, context: String) {
        if (context.isNotBlank()) {
            crashlytics.log("Non-fatal context: $context")
        }
        crashlytics.recordException(throwable)
    }

    override fun setKey(key: String, value: String) {
        crashlytics.setCustomKey(key, value)
    }

    override fun setKey(key: String, value: Boolean) {
        crashlytics.setCustomKey(key, value)
    }

    override fun setKey(key: String, value: Int) {
        crashlytics.setCustomKey(key, value)
    }
}
