package com.dipdev.themutemaster.ui.screens.schedules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dipdev.themutemaster.data.local.ScheduleDao
import com.dipdev.themutemaster.data.local.ScheduleEntity
import com.dipdev.themutemaster.utils.AlarmScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val scheduleDao: ScheduleDao,
    private val alarmScheduler: AlarmScheduler
) : ViewModel() {

    val schedules: StateFlow<List<ScheduleEntity>> = scheduleDao.getAllSchedules()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun toggleSchedule(schedule: ScheduleEntity, isEnabled: Boolean) {
        viewModelScope.launch {
            val updated = schedule.copy(isEnabled = isEnabled)
            scheduleDao.updateSchedule(updated)
            
            if (isEnabled) {
                alarmScheduler.schedule(updated)
            } else {
                alarmScheduler.cancel(updated)
            }
        }
    }

    fun deleteSchedule(schedule: ScheduleEntity) {
        viewModelScope.launch {
            alarmScheduler.cancel(schedule)
            scheduleDao.deleteSchedule(schedule)
        }
    }
}
