package com.dipdev.themutemaster.ui.screens.generalSettings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dipdev.themutemaster.data.local.AppThemeMode
import com.dipdev.themutemaster.data.local.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GeneralSettingsViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    // 1. Updated: Observes the Enum (Light, Dark, System) instead of just Boolean
    val themeMode: StateFlow<AppThemeMode> = preferencesManager.themeModeFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AppThemeMode.SYSTEM
        )

    val defaultRadius: StateFlow<Float> = preferencesManager.defaultRadiusFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 100f
        )

    val muteMediaVolume: StateFlow<Boolean> = preferencesManager.muteMediaVolumeFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    // 2. Updated: Sets the Enum mode
    fun setThemeMode(mode: AppThemeMode) {
        viewModelScope.launch {
            preferencesManager.setThemeMode(mode)
        }
    }

    fun updateRadius(newRadius: Float) {
        viewModelScope.launch {
            preferencesManager.setDefaultRadius(newRadius)
        }
    }

    fun setMuteMediaVolume(mute: Boolean) {
        viewModelScope.launch {
            preferencesManager.setMuteMediaVolume(mute)
        }
    }
}