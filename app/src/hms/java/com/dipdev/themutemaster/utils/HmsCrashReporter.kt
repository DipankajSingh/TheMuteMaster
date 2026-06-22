package com.dipdev.themutemaster.utils

import javax.inject.Inject
import javax.inject.Singleton

/**
 * A No-op CrashReporter for the HMS flavor because Huawei's AppGallery Connect
 * Crash Service is deprecated for new apps as of mid-2024.
 *
 * We do not use Firebase Crashlytics here to avoid relying on Google Play Services
 * elements or pinging Firebase endpoints on pure HMS devices.
 */
@Singleton
class HmsCrashReporter @Inject constructor() : CrashReporter {
    
    override fun log(message: String) {
        // No-op
    }

    override fun recordNonFatal(throwable: Throwable, context: String) {
        // No-op
    }

    override fun setKey(key: String, value: String) {
        // No-op
    }

    override fun setKey(key: String, value: Boolean) {
        // No-op
    }

    override fun setKey(key: String, value: Int) {
        // No-op
    }
}
