package com.dipdev.themutemaster.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

enum class AppThemeMode {
    LIGHT, DARK, SYSTEM
}

@Singleton
class PreferencesManager @Inject constructor(
    private val dataStore: DataStore<Preferences> // Hilt gets this from DataStoreModule
) {
    companion object {
        val KEY_THEME_MODE = stringPreferencesKey("app_theme_mode")
        val KEY_DEFAULT_RADIUS = floatPreferencesKey("default_radius")
    }

    // --- READ ---
    val themeModeFlow: Flow<AppThemeMode> = dataStore.data.map { preferences ->
        val savedValue = preferences[KEY_THEME_MODE]
        if (savedValue != null) {
            try {
                AppThemeMode.valueOf(savedValue)
            } catch (e: IllegalArgumentException) {
                AppThemeMode.SYSTEM
            }
        } else {
            AppThemeMode.SYSTEM
        }
    }

    val defaultRadiusFlow: Flow<Float> = dataStore.data.map { preferences ->
        preferences[KEY_DEFAULT_RADIUS] ?: 300f
    }

    // --- WRITE ---
    suspend fun setThemeMode(mode: AppThemeMode) {
        dataStore.edit { preferences ->
            preferences[KEY_THEME_MODE] = mode.name
        }
    }

    suspend fun setDefaultRadius(radius: Float) {
        dataStore.edit { preferences ->
            preferences[KEY_DEFAULT_RADIUS] = radius
        }
    }
}