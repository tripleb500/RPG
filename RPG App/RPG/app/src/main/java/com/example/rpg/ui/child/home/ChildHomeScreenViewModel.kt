package com.example.rpg.ui.child.home

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rpg.data.model.Quest
import com.example.rpg.data.model.Stats
import com.example.rpg.data.model.Status
import com.example.rpg.data.model.User
import com.example.rpg.data.repository.AuthRepository
import com.example.rpg.data.repository.QuestRepository
import com.example.rpg.data.repository.StatsRepository
import com.example.rpg.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ChildHomeScreenViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val questRepository: QuestRepository,
    private val statsRepository: StatsRepository
) : ViewModel() {
    val childQuests = authRepository.currentUserIdFlow
        .filterNotNull()
        .flatMapLatest { uid -> questRepository.getChildQuests(uid) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Count of completed quests for the current child account
    val completedQuestsCount = childQuests
        .map { list -> list.count { it.status == Status.COMPLETED } }
        .stateIn(viewModelScope, SharingStarted.Eagerly, 0)

    val questsInProgCount = childQuests
        .map { list -> list.count { it.status == Status.INPROGRESS } }
        .stateIn(viewModelScope, SharingStarted.Eagerly, 0)

    // Flow of all in-progress quests for the current child
    val inProgressQuestsFlow = childQuests
        .map { list -> list.filter { it.status == Status.INPROGRESS } }
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val questParentCache = mutableMapOf<String, User>()

    // Function to mark a quest as pending

    // retrieves parent's name when loading quest
    fun getQuestParentName(userId: String): State<String?> {
        val nameState = mutableStateOf<String?>(null)

        viewModelScope.launch {
            // Check cache first
            val cached = questParentCache[userId]
            if (cached != null) {
                nameState.value = "${cached.firstname} ${cached.lastname}"
            } else {
                try {
                    val user = userRepository.getUserByUid(userId)
                    if (user != null) {
                        questParentCache[userId] = user
                        nameState.value = "${user.firstname} ${user.lastname}"
                    } else {
                        nameState.value = "Unknown"
                    }
                } catch (e: Exception) {
                    nameState.value = "Unknown"
                }
            }
        }

        return nameState
    }

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

    val statsFlow: StateFlow<Stats> = authRepository.currentUserIdFlow
        .flatMapLatest { uid ->
            if (uid == null) {
                flowOf(Stats()) // default stats if user not signed in
            } else {
                statsRepository.getStatsFlow(flowOf(uid))
                    .catch { e ->
                        Log.e("ChildHomeScreenVM", "Error loading stats: ${e.message}")
                        emit(Stats()) // fallback in case of error
                    }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = Stats() // default initial value
        )

    // Achievement and Stats Dialogs
    var isLoadingAchievements by mutableStateOf(false)
        private set
    var errorMessageAchievements by mutableStateOf<String?>(null)
        private set
    var isLoadingStats by mutableStateOf(false)
        private set
    var errorMessageStats by mutableStateOf<String?>(null)
        private set

    // FIX: Make currentUserFlow more robust
    val currentUserFlow: Flow<User?> = authRepository.currentUserIdFlow
        .map { uid ->
            try {
                uid?.let { userRepository.getUserByUid(it) }
            } catch (e: Exception) {
                Log.e("ChildHomeScreenVM", "Error loading user: ${e.message}")
                null
            }
        }
        .catch { error ->
            Log.e("ChildHomeScreenVM", "Error in user flow: ${error.message}")
            emit(null)
        }
}