package com.dipdev.themutemaster.ui.screens.schedules

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dipdev.themutemaster.data.local.ScheduleDao
import com.dipdev.themutemaster.data.local.ScheduleEntity
import com.dipdev.themutemaster.utils.AlarmScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddScheduleViewModel @Inject constructor(
    private val scheduleDao: ScheduleDao,
    private val alarmScheduler: AlarmScheduler,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val scheduleId: String? = savedStateHandle.get<String>("id")
    private var existingSchedule: ScheduleEntity? = null
    
    val isEditing: Boolean
        get() = scheduleId != null && scheduleId != "null"

    var scheduleName by mutableStateOf("")
    var startTimeMins by mutableIntStateOf(540) // 9:00 AM
    var endTimeMins by mutableIntStateOf(1020) // 5:00 PM
    var activeDays by mutableStateOf(setOf(1, 2, 3, 4, 5)) // Mon-Fri
    var isEnabled by mutableStateOf(true)

    // Sound Profile Settings
    var muteMedia by mutableStateOf(false)
    var customMediaVolumePercent by mutableStateOf<Int?>(null)

    init {
        scheduleId?.let { idStr ->
            if (idStr != "null") {
                viewModelScope.launch {
                    val id = idStr.toIntOrNull() ?: return@launch
                    existingSchedule = scheduleDao.getScheduleById(id)
                    existingSchedule?.let { schedule ->
                        scheduleName = schedule.name
                        startTimeMins = schedule.startTimeMins
                        endTimeMins = schedule.endTimeMins
                        activeDays = if (schedule.daysOfWeek.isEmpty()) emptySet() else schedule.daysOfWeek.split(",").mapNotNull { it.toIntOrNull() }.toSet()
                        isEnabled = schedule.isEnabled
                        muteMedia = schedule.muteMedia
                        customMediaVolumePercent = schedule.customMediaVolumePercent
                    }
                }
            }
        }
    }

    fun toggleDay(day: Int) {
        val updated = activeDays.toMutableSet()
        if (updated.contains(day)) updated.remove(day) else updated.add(day)
        activeDays = updated
    }

    fun saveChanges(): Boolean {
        if (activeDays.isEmpty()) return false // At least one day required
        val daysStr = activeDays.sorted().joinToString(",")
        val schedule = ScheduleEntity(
            id = existingSchedule?.id ?: 0,
            name = scheduleName.ifEmpty { "My Schedule" },
            startTimeMins = startTimeMins,
            endTimeMins = endTimeMins,
            daysOfWeek = daysStr,
            isEnabled = isEnabled,
            ringerMode = android.media.AudioManager.RINGER_MODE_VIBRATE,
            muteMedia = muteMedia,
            customMediaVolumePercent = customMediaVolumePercent
        )

        viewModelScope.launch {
            if (schedule.id == 0) {
                val newId = scheduleDao.insertSchedule(schedule)
                if (schedule.isEnabled) {
                    alarmScheduler.schedule(schedule.copy(id = newId.toInt()))
                }
            } else {
                scheduleDao.updateSchedule(schedule)
                if (schedule.isEnabled) {
                    alarmScheduler.schedule(schedule)
                } else {
                    alarmScheduler.cancel(schedule)
                }
            }
        }
        return true
    }

    fun deleteSchedule() {
        existingSchedule?.let {
            viewModelScope.launch {
                alarmScheduler.cancel(it)
                scheduleDao.deleteSchedule(it)
            }
        }
    }
}
