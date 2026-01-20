package com.dipdev.themutemaster.ui.screens.permissions.fglocation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ForegroundLocationAccessViewModel @Inject constructor(): ViewModel() {
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
                PermissionState.PermanentDenied
            }
            else -> {
                shouldShowPermanentDeniedDialog=false
                shouldShowDialog=true
                PermissionState.DeniedOnce
            }
        }
    }

    fun setPermissionGranted(){
        foregroundLocationState= PermissionState.Granted
    }

    fun requestPermanentDeniedPermission(){
        shouldShowDialog=true
        shouldShowPermanentDeniedDialog=true

    }

    fun onDismissRequest(){
        shouldShowDialog=false
        shouldShowPermanentDeniedDialog=false
    }

}