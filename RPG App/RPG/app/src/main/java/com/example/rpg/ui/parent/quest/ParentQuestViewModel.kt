package com.example.rpg.ui.parent.quest

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rpg.data.model.Quest
import com.example.rpg.data.model.Status
import com.example.rpg.data.model.User
import com.example.rpg.data.repository.AuthRepository
import com.example.rpg.data.repository.QuestRepository
import com.example.rpg.data.repository.StatsRepository
import com.example.rpg.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ParentQuestViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val questRepository: QuestRepository,
    private val userRepository: UserRepository,
    private val statsRepository: StatsRepository
) : ViewModel() {
    // Retrieve assignedQuests
    val assignedQuests: StateFlow<List<Quest>> =
        questRepository.getAssignedQuests(authRepository.currentUserIdFlow)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    val availableQuests: StateFlow<List<Quest>> =
        questRepository.getAvailableQuests(authRepository.currentUserIdFlow.filterNotNull())
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )
    private val _selectedChild = MutableStateFlow<User?>(null)
    val selectedChild = _selectedChild.asStateFlow()

    private val questChildCache = mutableMapOf<String, User>()

    // Function to update a quest’s status
    fun updateQuestStatus(questId: String, newStatus: Status) {
        viewModelScope.launch {
            try {
                questRepository.updateQuestStatus(questId, newStatus)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    fun completeQuest(questId: String, childId: String, reward: Int) {
        viewModelScope.launch {
            try {
                questRepository.completeQuest(questId)

                statsRepository.incrementStats(
                    userId = childId,
                    questsCompletedInc = 1,
                    questsStreakInc = 1,
                    totalXPInc = 10, // 10 XP per quest
                    gameRewardsInc = reward
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun setAssignDate(questId: String) {
        viewModelScope.launch {
            try {
                questRepository.setAssignDate(questId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateQuestDetails(updatedQuest: Quest) {
        viewModelScope.launch {
            questRepository.updateQuest(updatedQuest.id, updatedQuest)
        }
    }

    // retrieves child's name when loading quest
    fun getQuestChildName(userId: String): State<String?> {
        val nameState = mutableStateOf<String?>(null)

        viewModelScope.launch {
            // Check cache first
            val cached = questChildCache[userId]
            if (cached != null) {
                nameState.value = "${cached.firstname} ${cached.lastname}"
            } else {
                try {
                    val user = userRepository.getUserByUid(userId)
                    if (user != null) {
                        questChildCache[userId] = user
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

    fun deleteQuest(questId: String) {
        viewModelScope.launch {
            try {
                questRepository.delete(questId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun fetchUserForQuest(quest: Quest) {
        viewModelScope.launch {
            val user = userRepository.getUserByUid(quest.assignedTo)
            _selectedChild.value = user
        }
    }
}