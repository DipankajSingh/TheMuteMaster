package com.dipdev.themutemaster.ui


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dipdev.themutemaster.data.local.OnboardingManager
import com.dipdev.themutemaster.ui.navigation.AppRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val onboardingManager: OnboardingManager
) : ViewModel() {

        private val _isLoading = MutableStateFlow(true)
        val isLoading = _isLoading.asStateFlow()

        private val _startDestination = MutableStateFlow<String?>(null)
        val startDestination = _startDestination.asStateFlow()

        init {
            viewModelScope.launch {
                // Combine both flows to make a decision
                combine(
                    onboardingManager.isWelcomeSeen,
                    onboardingManager.isSetupComplete
                ) { welcomeSeen, setupComplete ->
                    when {
                        setupComplete -> AppRoute.MAIN_APP_CONTAINER // Done? -> Home
                        welcomeSeen -> AppRoute.PERMISSION_FLOW      // Started but not done? -> Resume Perms
                        else -> AppRoute.WELCOME                     // New? -> Welcome
                    }
                }.collect { route ->
                    _startDestination.value = route
                    _isLoading.value = false
                }
            }
        }
    }