package com.dipdev.themutemaster.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dipdev.themutemaster.data.local.OnboardingManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val onboardingManager: OnboardingManager
) : ViewModel() {

    fun markWelcomeSeen() {
        viewModelScope.launch { onboardingManager.setWelcomeSeen() }
    }

    fun markSetupComplete() {
        viewModelScope.launch { onboardingManager.setSetupComplete() }
    }
}