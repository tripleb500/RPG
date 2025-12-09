package com.example.rpg.ui.child.game

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rpg.data.model.Stats
import com.example.rpg.data.model.User
import com.example.rpg.data.repository.AuthRepository
import com.example.rpg.data.repository.StatsRepository
import com.example.rpg.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ChildGameViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val statsRepository: StatsRepository
) : ViewModel() {

    val currentStats: StateFlow<Stats> = authRepository.currentUserIdFlow
        .filterNotNull()
        .flatMapLatest { uid ->
            statsRepository.getStatsFlow(flowOf(uid))
                .catch { e ->
                    errorMessageStats = e.message
                    emit(
                        Stats(
                            id = uid,
                            questsCompleted = 0,
                            questsStreak = 0,
                            totalXP = 0,
                            rewardsEarned = emptyList(),
                            gameRewards = 0
                        )
                    )
                }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = Stats(
                id = "", questsCompleted = 0, questsStreak = 0, totalXP = 0, rewardsEarned = emptyList()
            )
        )

    val currentLevel: StateFlow<Int> =
        statsRepository.getStatsFlow(authRepository.currentUserIdFlow)
            .map { stats ->
                stats.totalXP / 100
            }
            .catch { e ->
                Log.e("ChildHomeVM", "Stats error: ${e.message}")
                emit(0)
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                0
            )
    val currentUserFlow: Flow<User?> = authRepository.currentUserIdFlow
        .map { uid ->
            try {
                uid?.let { userRepository.getUserByUid(it) }
            } catch (e: Exception) {
                Log.e("ChildHomeScreenVM", "Error loading user: ${e.message}")
                null
            }
        }

    var errorMessageStats by mutableStateOf<String?>(null)
        private set
}