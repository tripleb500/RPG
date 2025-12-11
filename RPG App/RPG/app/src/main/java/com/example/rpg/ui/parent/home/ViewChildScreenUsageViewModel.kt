package com.example.rpg.ui.parent.home

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.rpg.data.model.ScreenTimeRecord
import com.example.rpg.data.repository.ScreenUsageStatsRepository
import com.example.rpg.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewChildScreenUsageViewModel @Inject constructor(
    private val screenStatsRepository: ScreenUsageStatsRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    private val _childUsage = MutableStateFlow<List<ScreenTimeRecord>>(emptyList())
    val childUsage: StateFlow<List<ScreenTimeRecord>> = _childUsage

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _isEnabled = MutableStateFlow(false)
    val isEnabled: StateFlow<Boolean> = _isEnabled

    /**
     * Observe the child's daily screen time from firestore
     */
    fun observeChildScreenUsage(childId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Collect both values from DataStore
                val user = userRepository.getUserByUid(childId)
                if (user?.screenTimeLimit != null && user.screenTimeLimit > 0) {
                    _isEnabled.value = true
                }

                screenStatsRepository.syncToday(childId).collect {success ->
                    if(!success) {
                        _error.value = "Failed to sync today's usage"
                    }
                }

                screenStatsRepository.observeChildUsage(childId).collect { usage ->
                    _childUsage.value = usage
                    if(_isLoading.value) _isLoading.value = false
                }
            } catch (e: Exception) {
                _error.value = e.message
                _isLoading.value = false
            }
        }
    }

    fun updateScreenTimeLimit(childId: String, minutes: String) {
        val min = minutes.toInt()
        viewModelScope.launch {
            try {
                if (min > 0 && min <= 1440) {
                    userRepository.updateScreenTimeLimit(childId, min)
                    _isEnabled.value = true
                }
                else if (min == 0) {
                    userRepository.updateScreenTimeLimit(childId, min)
                    _isEnabled.value = false
                }
            } catch (e: Exception) {
                Log.e("QuestVM", "Error updating Screen Time Limit", e)
            }

        }
    }

}