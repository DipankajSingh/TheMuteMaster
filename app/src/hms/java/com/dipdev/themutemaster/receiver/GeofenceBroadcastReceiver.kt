package com.dipdev.themutemaster.receiver

import kotlinx.coroutines.launch
import kotlinx.coroutines.CoroutineExceptionHandler
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.dipdev.themutemaster.R
import com.dipdev.themutemaster.data.local.MuteStateManager
import com.dipdev.themutemaster.utils.CrashReporter
import com.dipdev.themutemaster.utils.NotificationConstants
import com.dipdev.themutemaster.utils.hasNotificationPermission
import com.huawei.hms.location.Geofence
import com.huawei.hms.location.GeofenceData
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class GeofenceBroadcastReceiver : BroadcastReceiver() {

    @Inject lateinit var muteStateManager: MuteStateManager
    @Inject lateinit var crashReporter: CrashReporter

    override fun onReceive(context: Context, intent: Intent) {
        val geofenceData = GeofenceData.getDataFromIntent(intent) ?: return
        if (geofenceData.isFailure) {
            val errorCode = geofenceData.errorCode
            Log.e("GeofenceReceiver", "Error Code: $errorCode")
            crashReporter.log("GeofenceEvent error received")
            crashReporter.setKey("geofence_error_code", errorCode)
            crashReporter.recordNonFatal(
                RuntimeException("GeofenceData error: code=$errorCode"),
                context = "GeofenceBroadcastReceiver.onReceive"
            )
            return
        }

        val pendingResult = goAsync()
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            crashReporter.recordNonFatal(throwable, context = "GeofenceBroadcastReceiver coroutine crash")
            pendingResult.finish()
        }
        kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.IO + exceptionHandler).launch {
            crashReporter.log("GeofenceBroadcastReceiver: conversion=${geofenceData.conversion}, geofences=${geofenceData.convertingGeofenceList?.map { it.uniqueId }}")        
            try {
                when (geofenceData.conversion) {
                    Geofence.ENTER_GEOFENCE_CONVERSION -> {
                        geofenceData.convertingGeofenceList?.forEach { geofence ->
                            val wasMuted = muteStateManager.attemptMute("GEOFENCE_${geofence.uniqueId}")
                            if (wasMuted) {
                                startMuteService(context)
                            } else {
                                Log.d("GeofenceReceiver", "Entered zone ${geofence.uniqueId}, but phone was already silent or we are already muting it.")
                            }
                        }
                    }
                    Geofence.EXIT_GEOFENCE_CONVERSION -> {
                        geofenceData.convertingGeofenceList?.forEach { geofence ->
                            val wasRestored = muteStateManager.attemptRestore("GEOFENCE_${geofence.uniqueId}")
                            if (wasRestored) {
                                stopMuteService(context)
                            }
                        }
                    }
                    else -> {
                        val transition = geofenceData.conversion
                        Log.e("GeofenceReceiver", "Unknown transition: $transition")
                        crashReporter.recordNonFatal(
                            RuntimeException("Unknown geofence conversion type: $transition"),
                            context = "GeofenceBroadcastReceiver.onReceive"
                        )
                    }
                }
            } catch (e: Exception) {
                Log.e("GeofenceReceiver", "Error processing geofence transition", e)
                crashReporter.recordNonFatal(e, context = "GeofenceBroadcastReceiver internal error")
            } finally {
                pendingResult.finish()
            }
        }
    }

    private fun startMuteService(context: Context) {
        try {
            val intent = Intent(context, com.dipdev.themutemaster.service.MuteService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        } catch (e: Exception) {
            Log.e("GeofenceReceiver", "Failed to start MuteService: ${e.message}")
            crashReporter.recordNonFatal(e, context = "GeofenceBroadcastReceiver.startMuteService")
        }
    }

    private fun stopMuteService(context: Context) {
        val intent = Intent(context, com.dipdev.themutemaster.service.MuteService::class.java)
        context.stopService(intent)
    }
}