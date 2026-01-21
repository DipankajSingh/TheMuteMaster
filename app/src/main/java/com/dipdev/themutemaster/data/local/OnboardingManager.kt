package com.dipdev.themutemaster.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OnboardingManager @Inject constructor(
    private val dataStore: DataStore<Preferences> // Hilt gets this from DataStoreModule
) {
    companion object {
        private val IS_WELCOME_SEEN = booleanPreferencesKey("is_welcome_seen")
        private val IS_SETUP_COMPLETE = booleanPreferencesKey("is_setup_complete")
    }

    val isWelcomeSeen: Flow<Boolean> = dataStore.data
        .map { it[IS_WELCOME_SEEN] ?: false }

    val isSetupComplete: Flow<Boolean> = dataStore.data
        .map { it[IS_SETUP_COMPLETE] ?: false }

    suspend fun setWelcomeSeen() {
        dataStore.edit { it[IS_WELCOME_SEEN] = true }
    }

    suspend fun setSetupComplete() {
        dataStore.edit { it[IS_SETUP_COMPLETE] = true }
    }
}