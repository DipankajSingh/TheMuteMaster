package com.dipdev.themutemaster.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.dipdev.themutemaster.data.GeofenceManager
import com.dipdev.themutemaster.data.local.GeofenceDao
import com.dipdev.themutemaster.data.local.ScheduleDao
import com.dipdev.themutemaster.utils.AlarmScheduler
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.CoroutineExceptionHandler
import android.util.Log
import com.dipdev.themutemaster.utils.CrashReporter
import javax.inject.Inject

@AndroidEntryPoint // Helper to let us inject dependencies into a Receiver
class BootDeviceReceiver : BroadcastReceiver() {

    @Inject
    lateinit var geofenceManager: GeofenceManager

    @Inject
    lateinit var dao: GeofenceDao

    @Inject
    lateinit var scheduleDao: ScheduleDao

    @Inject
    lateinit var alarmScheduler: AlarmScheduler

    @Inject
    lateinit var crashReporter: CrashReporter

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            val pendingResult = goAsync()
            val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
                Log.e("BootDeviceReceiver", "Coroutine exception", throwable)
                crashReporter.recordNonFatal(throwable, context = "BootDeviceReceiver coroutine crash")
                pendingResult.finish()
            }
            // We need a Coroutine to read from the DB
            CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                try {
                    // Re-register all active geofences
                    val activeGeofences = dao.getAllEnabledGeofencesOneShot()
                    activeGeofences.forEach { geofence ->
                        geofenceManager.addGeofence(geofence)
                    }

                    // Re-schedule all enabled time schedules (alarms are wiped on reboot)
                    val enabledSchedules = scheduleDao.getEnabledSchedules()
                    enabledSchedules.forEach { schedule ->
                        alarmScheduler.schedule(schedule)
                    }
                } catch (e: Exception) {
                    Log.e("BootDeviceReceiver", "Error on boot processing", e)
                    crashReporter.recordNonFatal(e, context = "BootDeviceReceiver internal error")
                } finally {
                    pendingResult.finish()
                }
            }
        }
    }
}