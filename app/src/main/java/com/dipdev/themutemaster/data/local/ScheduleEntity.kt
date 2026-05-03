package com.dipdev.themutemaster.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import android.media.AudioManager

@Entity(tableName = "schedules")
data class ScheduleEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    
    // Time in minutes from midnight (0 to 1439)
    val startTimeMins: Int,
    val endTimeMins: Int,
    
    // Days of the week this schedule is active (e.g., "1,2,3,4,5" for Mon-Fri)
    // 1 = Monday, 7 = Sunday (matching java.time.DayOfWeek)
    val daysOfWeek: String,
    
    val isEnabled: Boolean = true,
    
    // Sound Profile Settings
    val ringerMode: Int? = AudioManager.RINGER_MODE_VIBRATE, // null = don't change
    val muteMedia: Boolean = false,
    val customMediaVolumePercent: Int? = null // 0-100, null = don't change or just mute if muteMedia is true
)
