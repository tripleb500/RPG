package com.example.rpg.ui.parent.quest

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
        questRepository.getAllQuests(authRepository.currentUserIdFlow)

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

    fun fetchUserForQuest(quest: Quest) {
        viewModelScope.launch {
            val user = userRepository.getUserByUid(quest.assignedTo)
            _selectedChild.value = user
        }
    }
}