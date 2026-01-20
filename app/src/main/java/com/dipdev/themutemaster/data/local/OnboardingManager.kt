package com.dipdev.themutemaster.data.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "settings")

@Singleton
class OnboardingManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val IS_WELCOME_SEEN = booleanPreferencesKey("is_welcome_seen")
    private val IS_SETUP_COMPLETE = booleanPreferencesKey("is_setup_complete")

    // Read the "Welcome Seen" flag
    val isWelcomeSeen: Flow<Boolean> = context.dataStore.data
        .map { it[IS_WELCOME_SEEN] ?: false }

    // Read the "Setup Complete" flag
    val isSetupComplete: Flow<Boolean> = context.dataStore.data
        .map { it[IS_SETUP_COMPLETE] ?: false }

    // Milestone 1: User passed Welcome Screen
    suspend fun setWelcomeSeen() {
        context.dataStore.edit { it[IS_WELCOME_SEEN] = true }
    }

    // Milestone 2: User finished EVERYTHING
    suspend fun setSetupComplete() {
        context.dataStore.edit { it[IS_SETUP_COMPLETE] = true }
    }
}