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

    private val _valid = MutableStateFlow(false)
    val valid: StateFlow<Boolean> = _valid

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


    fun isValid() {
        viewModelScope.launch {
            val uid = authRepository.currentUser?.uid

            println("Uid is: " + uid)

            if (uid != null) {
                _screenTime.value = screenStatsRepository.getCurrentDay(uid)

                val child = userRepository.getUserByUid(uid)
                if (child?.screenTimeLimit != null) {
                    _limit.value = child.screenTimeLimit

                    println("Screen limit value is: " + _limit.value)
                    println("Current scren time amount is: " + _screenTime.value?.screenTimeMs)

                    val currentTime = _screenTime.value?.screenTimeMs
                    val screenLimit = _limit.value
                    println(currentTime)
                    println(screenLimit)
                    if (currentTime != null) {
                        val currentMinutes = currentTime / 1000 / 60
                        if (screenLimit == 0 || screenLimit < currentMinutes)
                            _valid.value = true
                    }

                }
            }

        }
    }
}