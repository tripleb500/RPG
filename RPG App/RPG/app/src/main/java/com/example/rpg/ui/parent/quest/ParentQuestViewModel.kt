package com.example.rpg.ui.parent.quest

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rpg.data.model.Quest
import com.example.rpg.data.model.User
import com.example.rpg.data.repository.AuthRepository
import com.example.rpg.data.repository.QuestRepository
import com.example.rpg.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ParentQuestViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val questRepository: QuestRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    // Retrieve all the quests
    private val questsFlow: Flow<List<Quest>> =
        questRepository.getParentQuests(authRepository.currentUserIdFlow)

    private val _selectedChild = MutableStateFlow<User?>(null)
    val selectedChild = _selectedChild.asStateFlow()

    // Group the quests by assignee (Child.id)
    val questsByAssignee: StateFlow<Map<String, List<Quest>>> = questsFlow.map { quests ->
        quests.groupBy { it.assignee } // 🔹 groups quests by child ID
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyMap()
    )

    private val questChildCache = mutableMapOf<String, User>()

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
                        nameState.value = "Unknown"
                    }
                } catch (e: Exception) {
                    nameState.value = "Unknown"
                }
            }
        }

        return nameState
    }

    fun fetchUserForQuest(quest: Quest) {
        viewModelScope.launch {
            val user = userRepository.getUserByUid(quest.assignedTo)
            _selectedChild.value = user
        }
    }
}