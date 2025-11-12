package com.example.rpg.ui.child.home

import android.graphics.Bitmap
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
import com.example.rpg.ui.child.quest.ChildQuestViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
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

/**
@HiltViewModel
class ChildHomeViewModel @Inject constructor(
private val authRepository: AuthRepository
) : ViewModel(){

}
 **/

@HiltViewModel
class ChildHomeScreenViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val questRepository: QuestRepository,
    private val statsRepository: StatsRepository
) : ViewModel() {

    // -------------------
    // Stats
    // -------------------
    private val _stats = MutableStateFlow<Stats?>(null)
    val stats: StateFlow<Stats?> = _stats

    var isLoadingStats by mutableStateOf(false)
        private set
    var errorMessageStats by mutableStateOf<String?>(null)
        private set

    fun loadStats(userId: String) {
        viewModelScope.launch {
            try {
                isLoadingStats = true
                val result = statsRepository.getStats(userId)
                _stats.value = result
                errorMessageStats = null
            } catch (e: Exception) {
                errorMessageStats = e.message
            } finally {
                isLoadingStats = false
            }
        }
    }

    fun recalculateStats(userId: String, quests: List<Quest>) {
        viewModelScope.launch {
            statsRepository.recalculateStats(userId, quests)
        }
    }

    // -------------------
    // Child Quests
    // -------------------
    private val _isLoadingQuests = MutableStateFlow(false)
    val isLoadingQuests: StateFlow<Boolean> = _isLoadingQuests.asStateFlow()

    val childQuests: StateFlow<List<Quest>> =
        questRepository.getChildQuests(authRepository.currentUserIdFlow)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    // Count of completed quests
    val completedQuestsCount: StateFlow<Int> = authRepository.currentUserIdFlow
        .filterNotNull()
        .flatMapLatest { uid ->
            questRepository.getQuestsByStatus(flowOf(uid), Status.COMPLETED)
                .map { it.size } // count them
                .catch { error ->
                    Log.e("ChildHomeScreenVM", "Error loading completed quests: ${error.message}")
                    emit(0)
                }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    // -------------------
    // Quest parent cache
    // -------------------
    private val questParentCache = mutableMapOf<String, User>()

    fun getQuestParentName(userId: String): State<String?> {
        val nameState = mutableStateOf<String?>(null)

        viewModelScope.launch {
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
                        nameState.value = null
                    }
                } catch (e: Exception) {
                    nameState.value = null
                }
            }
        }

        return nameState
    }

    // -------------------
    // Current user flow
    // -------------------
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
