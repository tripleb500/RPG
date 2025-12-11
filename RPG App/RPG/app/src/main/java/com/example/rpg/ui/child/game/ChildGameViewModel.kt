package com.example.rpg.ui.child.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rpg.data.model.ScreenTimeRecord
import com.example.rpg.data.model.User
import com.example.rpg.data.repository.AuthRepository
import com.example.rpg.data.repository.ScreenUsageStatsRepository
import com.example.rpg.data.repository.StatsRepository
import com.example.rpg.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChildGameViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val screenStatsRepository: ScreenUsageStatsRepository,
    private val userRepository: UserRepository,
    private val statsRepository: StatsRepository
) : ViewModel() {
    private val _limit = MutableStateFlow(0)
    val limit: StateFlow<Int?> = _limit

    private val _screenTime = MutableStateFlow<ScreenTimeRecord?>(null)
    val screenTime: StateFlow<ScreenTimeRecord?> = _screenTime.asStateFlow()

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
            authRepository.currentUser?.uid?.let { uid ->
                getScreenTimeLimit(uid)
                getCurrentDay(uid)
            }
        }
    }

    fun isValid(): Boolean {
        val currentTime = _screenTime.value?.screenTimeMs
        val screenLimit = _limit.value
        return currentTime == null || screenLimit == 0 || currentTime <= screenLimit
    }

    val currentLevel: StateFlow<Int> =
        statsRepository.getStatsFlow(authRepository.currentUserIdFlow)
            .map { stats -> stats.totalXP / 100 }
            .catch { emit(0) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val currentUserFlow: Flow<User?> = authRepository.currentUserIdFlow
        .map { uid ->
            try { uid?.let { userRepository.getUserByUid(it) } }
            catch (e: Exception) { null }
        }
}
