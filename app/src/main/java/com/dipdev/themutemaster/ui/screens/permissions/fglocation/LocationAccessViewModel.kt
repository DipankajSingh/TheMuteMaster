package com.dipdev.themutemaster.ui.screens.permissions.fglocation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ForegroundLocationAccessViewModel: ViewModel() {
    sealed class PermissionState{
        object NotRequested: PermissionState()
        object Granted: PermissionState()
        object DeniedOnce: PermissionState()
        object PermanentDenied: PermissionState()
    }

    var foregroundLocationState by mutableStateOf<PermissionState>(PermissionState.NotRequested)
        private set

    var shouldShowDialog by mutableStateOf(false)
        private set
    var shouldShowPermanentDeniedDialog by mutableStateOf(false)
        private set

    fun onPermissionResult(isGranted: Boolean,isPermanentDenied: Boolean){
        foregroundLocationState=when{
            isGranted-> PermissionState.Granted
            isPermanentDenied->{
                shouldShowDialog=true
                shouldShowPermanentDeniedDialog=true
                println("perma denied in vm")
                PermissionState.PermanentDenied
            }
            else -> {
                shouldShowPermanentDeniedDialog=false
                shouldShowDialog=true
                println("denied in vm")
                PermissionState.DeniedOnce
            }
        }
    }

    fun setPermissionGranted(){
        foregroundLocationState= PermissionState.Granted
        println("Permission granted vm")
    }

    fun requestPermanentDeniedPermission(){
        shouldShowDialog=true
        shouldShowPermanentDeniedDialog=true
        println("requestPermanentDeniedPermission called vm")

    }

    fun onDismissRequest(){
        shouldShowDialog=false
        shouldShowPermanentDeniedDialog=false

        println("dialog closed vm")

    }

}