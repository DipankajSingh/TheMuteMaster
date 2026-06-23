package com.dipdev.themutemaster.utils

import android.app.Activity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DrmManagerImpl @Inject constructor() : DrmManager {
    override fun checkDrm(activity: Activity, onCheckResult: (Boolean) -> Unit) {
        // GMS/Google Play version does not use Huawei DRM.
        // It uses standard Play Licensing if any, or no DRM.
        onCheckResult(true)
    }
}
