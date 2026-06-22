package com.dipdev.themutemaster.utils

import android.app.Activity

interface DrmManager {
    fun checkDrm(activity: Activity, onCheckResult: (Boolean) -> Unit)
}
