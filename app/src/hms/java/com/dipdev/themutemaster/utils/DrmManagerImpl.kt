package com.dipdev.themutemaster.utils

import android.app.Activity
import android.util.Log
import com.huawei.android.sdk.drm.Drm
import com.huawei.android.sdk.drm.DrmCheckCallback
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DrmManagerImpl @Inject constructor() : DrmManager {
    
    companion object {
        private const val DRM_ID = "30091000028604323"
        private const val DRM_KEY = "MIIBojANBgkqhkiG9w0BAQEFAAOCAY8AMIIBigKCAYEAsR1manDjv45U3epJFFo6es3J4Jp9axpU7L6tIaxtqw5FlaHTlScCadRSHvBfJCEIHAFSCdjHxHFU2rBuJov3mBp0dYDTXEmL7KufTZ4rzQMFBPKLKjGtzgI+PB/vaiLQQTufYBNIr2dJIDhPIgL/Zc3JROd6lDut+kj7Aqxenc1cLLT10HEeaSyI9F/4iXe5q7FsTUs5Ke3BTXLlsBqIX1qinqm8fuTgr+5EL11DRrNgpHdx7lBXd5P/zSMAyt3Bql3BEJ6hDZHotKJAfd1CV53rtLn+y+gdS82j/wyHil6d01hbJMpDVb6Aytf828+aa3s9PYYvL/jPEVoMdtQ5vqHkc+g9aFbMXlqTiove5RDLYjsXsKuRyUUlhe9aIzyivQRpCJrttTSuAHSrGN71PDhfqkW7El6130aPbi25fv435raK8wuSgvYA9HYK2Av1P9k6sNMdWu9tQoAxIMK4Xxqh6uPm6AC0VrJt3IIsrdFPpgd+WTM4XTvfISyFcuGVAgMBAAE="
        private const val TAG = "DrmManagerImpl"
    }

    override fun checkDrm(activity: Activity, onCheckResult: (Boolean) -> Unit) {
        Drm.check(activity, activity.packageName, DRM_ID, DRM_KEY, object : DrmCheckCallback {
            override fun onCheckSuccess() {
                Log.i(TAG, "DRM Check Success")
                onCheckResult(true)
            }

            override fun onCheckFailed(errorCode: Int) {
                Log.e(TAG, "DRM Check Failed with error code: $errorCode")
                onCheckResult(false)
            }

            override fun onCheckFailed(errorCode: Int, errorMessage: String?) {
                Log.e(TAG, "DRM Check Failed with error code: $errorCode, message: $errorMessage")
                onCheckResult(false)
            }
        })
    }
}
