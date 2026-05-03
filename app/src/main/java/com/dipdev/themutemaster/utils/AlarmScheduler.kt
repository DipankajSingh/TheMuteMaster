package com.dipdev.themutemaster.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.dipdev.themutemaster.data.local.ScheduleEntity
import com.dipdev.themutemaster.receiver.ScheduleBroadcastReceiver
import java.util.Calendar

class AlarmScheduler(private val context: Context) {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun schedule(scheduleEntity: ScheduleEntity) {
        if (!scheduleEntity.isEnabled) {
            cancel(scheduleEntity)
            return
        }

        scheduleNextAlarm(scheduleEntity, isStart = true)
        scheduleNextAlarm(scheduleEntity, isStart = false)
    }

    fun cancel(scheduleEntity: ScheduleEntity) {
        val startIntent = createPendingIntent(scheduleEntity, isStart = true)
        val endIntent = createPendingIntent(scheduleEntity, isStart = false)

        alarmManager.cancel(startIntent)
        alarmManager.cancel(endIntent)
        Log.d("AlarmScheduler", "Cancelled alarms for Schedule ${scheduleEntity.id}")
    }

    private fun scheduleNextAlarm(scheduleEntity: ScheduleEntity, isStart: Boolean) {
        val timeMins = if (isStart) scheduleEntity.startTimeMins else scheduleEntity.endTimeMins
        val calendar = calculateNextOccurrence(timeMins, scheduleEntity.daysOfWeek)

        if (calendar == null) {
            Log.w("AlarmScheduler", "No valid next occurrence found for Schedule ${scheduleEntity.id} ($isStart)")
            return
        }

        val pendingIntent = createPendingIntent(scheduleEntity, isStart)

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis,
                        pendingIntent
                    )
                } else {
                    // Fallback to inexact if permission not granted
                    alarmManager.setAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis,
                        pendingIntent
                    )
                }
            } else {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            }
            Log.d("AlarmScheduler", "Scheduled alarm for Schedule ${scheduleEntity.id} (isStart=$isStart) at ${calendar.time}")
        } catch (e: SecurityException) {
            Log.e("AlarmScheduler", "SecurityException scheduling exact alarm: ${e.message}")
        }
    }

    private fun createPendingIntent(scheduleEntity: ScheduleEntity, isStart: Boolean): PendingIntent {
        val intent = Intent(context, ScheduleBroadcastReceiver::class.java).apply {
            putExtra(ScheduleBroadcastReceiver.EXTRA_SCHEDULE_ID, scheduleEntity.id)
            putExtra(ScheduleBroadcastReceiver.EXTRA_IS_START, isStart)
        }
        
        // Use a unique request code by combining ID and start/end state
        val requestCode = (scheduleEntity.id * 10) + (if (isStart) 1 else 0)

        val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }

        return PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            flags
        )
    }

    private fun calculateNextOccurrence(timeMins: Int, daysOfWeekStr: String): Calendar? {
        if (daysOfWeekStr.isEmpty()) return null

        val activeDays = daysOfWeekStr.split(",").mapNotNull { it.toIntOrNull() }.toSet()
        if (activeDays.isEmpty()) return null

        val hour = timeMins / 60
        val minute = timeMins % 60

        val now = Calendar.getInstance()
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        // If the time has already passed today, advance to tomorrow
        if (calendar.before(now)) {
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        // Find the next active day
        for (i in 0..7) {
            // Calendar.DAY_OF_WEEK uses 1=Sun, 2=Mon... 7=Sat
            // Our activeDays uses 1=Mon, 2=Tue... 7=Sun
            val calDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
            val javaDayOfWeek = if (calDayOfWeek == Calendar.SUNDAY) 7 else calDayOfWeek - 1

            if (activeDays.contains(javaDayOfWeek)) {
                return calendar
            }
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        return null
    }
}
