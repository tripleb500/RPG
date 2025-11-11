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
import com.example.rpg.data.model.Status
import com.example.rpg.data.model.User
import com.example.rpg.data.repository.AuthRepository
import com.example.rpg.data.repository.QuestRepository
import com.example.rpg.data.repository.StatsRepository
import com.example.rpg.data.repository.UserRepository
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
    // Flow of all in-progress quests for the current child


    val inProgressQuestsFlow: StateFlow<List<Quest>> = authRepository.currentUserIdFlow
        .filterNotNull()
        .flatMapLatest { uid ->
            println("User ID received: $uid")
            questRepository.getQuestsByStatus(flowOf(uid), Status.INPROGRESS)
                .catch { error ->
                    Log.e("ChildHomeScreenVM", "Error loading quests: ${error.message}")
                    emit(emptyList())
                }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _imageUrl = MutableStateFlow<String?>(null)
    val imageUrl: StateFlow<String?> = _imageUrl.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun uploadImageForQuest(questId: String, bitmap: Bitmap) {
        viewModelScope.launch {
            _isLoading.value = true
            _imageUrl.value = questRepository.uploadBitmapAndGetUrl(bitmap, questId)
            _isLoading.value = false
        }
    }

    val childQuests: StateFlow<List<Quest>> =
        questRepository.getChildQuests(authRepository.currentUserIdFlow)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )


    // Count of completed quests for the current child account
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
    private val questParentCache = mutableMapOf<String, User>()



    // Function to mark a quest as pending
    fun markQuestAsPending(quest: Quest) {
        viewModelScope.launch {
            try {
                println("Marking quest as PENDING: ${quest.title} (ID: ${quest.id})")
                questRepository.updateQuestStatus(quest.id, Status.PENDING)
                // Add a small delay to see if the update propagates correctly
                delay(100)
            } catch (e: Exception) {
                Log.e("QuestVM", "Error updating quest", e)
            }
        }
    }

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