package com.example.rpg.ui.child.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rpg.data.model.ScreenTimeRecord
import com.example.rpg.data.repository.AuthRepository
import com.example.rpg.data.repository.ScreenUsageStatsRepository
import com.example.rpg.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChildGameViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val screenStatsRepository: ScreenUsageStatsRepository,
    private val userRepository: UserRepository
) : ViewModel() {


    private val _childUsage = MutableStateFlow<List<ScreenTimeRecord>>(emptyList())
    val childUsage: StateFlow<List<ScreenTimeRecord>> = _childUsage

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _limit = MutableStateFlow(0)
    val limit: StateFlow<Int?> = _limit

    private val _valid = MutableStateFlow(0)
    val valid: StateFlow<Int?> = _valid

    private val _screenTime = MutableStateFlow<ScreenTimeRecord?>(null)
    val screenTime: StateFlow<ScreenTimeRecord?> = _screenTime.asStateFlow()

    fun getChildScreenUsage(childId: String) {
        viewModelScope.launch {
            try {
                screenStatsRepository.syncToday(childId).collect {success ->
                    if(!success) {
                        _error.value = "Failed to sync today's usage"
                    }
                }

                screenStatsRepository.observeChildUsage(childId).collect { usage ->
                    _childUsage.value = usage
                }
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun getCurrentDay(childId: String) {
        viewModelScope.launch {
            _screenTime.value = screenStatsRepository.getCurrentDay(childId)
        }
    }

    fun getScreenTimeLimit(childId: String) {
        viewModelScope.launch {
            val child = userRepository.getUserByUid(childId)
            if (child?.screenTimeLimit != null)
                _limit.value = child.screenTimeLimit
        }
    }

    fun checkChildScreen() {
        viewModelScope.launch {
            val uid = authRepository.currentUser?.uid

            if (uid != null) {
                getScreenTimeLimit(uid)
                getCurrentDay(uid)

            }
        }
    }

    fun isValid(): Boolean {
        checkChildScreen()
        val currentTime = _screenTime.value?.screenTimeMs
        val screenLimit = _limit.value
        if (currentTime != null && screenLimit > 0 && screenLimit < currentTime)
            return false
        return true
    }
}