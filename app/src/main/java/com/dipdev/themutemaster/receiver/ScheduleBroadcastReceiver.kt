package com.dipdev.themutemaster.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.dipdev.themutemaster.data.local.ScheduleDao
import com.dipdev.themutemaster.data.local.MuteStateManager
import com.dipdev.themutemaster.utils.AlarmScheduler
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ScheduleBroadcastReceiver : BroadcastReceiver() {

    @Inject lateinit var muteStateManager: MuteStateManager
    @Inject lateinit var scheduleDao: ScheduleDao

    companion object {
        const val EXTRA_SCHEDULE_ID = "extra_schedule_id"
        const val EXTRA_IS_START = "extra_is_start"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val scheduleId = intent.getIntExtra(EXTRA_SCHEDULE_ID, -1)
        val isStart = intent.getBooleanExtra(EXTRA_IS_START, false)

        if (scheduleId == -1) return

        Log.d("ScheduleReceiver", "Alarm triggered for Schedule $scheduleId (isStart=$isStart)")

        CoroutineScope(Dispatchers.IO).launch {
            val schedule = scheduleDao.getScheduleById(scheduleId)
            
            if (schedule == null || !schedule.isEnabled) {
                Log.d("ScheduleReceiver", "Schedule $scheduleId not found or disabled. Ignoring.")
                return@launch
            }

            val triggerId = "SCHEDULE_$scheduleId"

            if (isStart) {
                val profile = com.dipdev.themutemaster.data.local.SoundProfile(
                    ringerMode = schedule.ringerMode,
                    muteMedia = schedule.muteMedia,
                    customMediaVolumePercent = schedule.customMediaVolumePercent
                )
                val wasMuted = muteStateManager.attemptMute(triggerId, profile)
                if (wasMuted) {
                    startMuteService(context)
                }
            } else {
                val wasRestored = muteStateManager.attemptRestore(triggerId)
                if (wasRestored) {
                    stopMuteService(context)
                }
            }

            // Reschedule the alarm for the next week/occurrence
            val alarmScheduler = AlarmScheduler(context)
            alarmScheduler.schedule(schedule)
        }
    }

    private fun startMuteService(context: Context) {
        try {
            val serviceIntent = Intent(context, com.dipdev.themutemaster.service.MuteService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(serviceIntent)
            } else {
                context.startService(serviceIntent)
            }
        } catch (e: Exception) {
            Log.e("ScheduleReceiver", "Failed to start MuteService: ${e.message}")
        }
    }

    private fun stopMuteService(context: Context) {
        val serviceIntent = Intent(context, com.dipdev.themutemaster.service.MuteService::class.java)
        context.stopService(serviceIntent)
    }
}
