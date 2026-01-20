package com.dipdev.themutemaster.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.dipdev.themutemaster.data.GeofenceManager
import com.dipdev.themutemaster.data.local.GeofenceDao
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint // Helper to let us inject dependencies into a Receiver
class BootDeviceReceiver : BroadcastReceiver() {

    @Inject
    lateinit var geofenceManager: GeofenceManager

    @Inject
    lateinit var dao: GeofenceDao

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {

            // We need a Coroutine to read from the DB
            CoroutineScope(Dispatchers.IO).launch {
                // Get all active locations
                val activeGeofences = dao.getAllEnabledGeofencesOneShot()

                // Re-register them all
                activeGeofences.forEach { geofence ->
                    geofenceManager.addGeofence(geofence)
                }
            }
        }
    }
}