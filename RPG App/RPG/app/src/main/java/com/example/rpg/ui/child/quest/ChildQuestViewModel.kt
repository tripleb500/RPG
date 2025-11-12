package com.example.rpg.ui.child.quest

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rpg.data.model.Quest
import com.example.rpg.data.model.Status
import com.example.rpg.data.model.User
import com.example.rpg.data.repository.AuthRepository
import com.example.rpg.data.repository.QuestRepository
import com.example.rpg.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
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
class ChildQuestViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val questRepository: QuestRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    private val _capturedImage = MutableStateFlow<Bitmap?>(null)
    val capturedImage = _capturedImage.asStateFlow()


    fun setCapturedImage(bitmap: Bitmap?) {
        _capturedImage.value = bitmap
    }

    private val _imageUrl = MutableStateFlow<String?>(null)
    val imageUrl: StateFlow<String?> = _imageUrl.asStateFlow()
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    fun uploadImageForQuest(questId: String, bitmap: Bitmap) {
        _imageUrl.value = null
        viewModelScope.launch {
            _isLoading.value = true
            _imageUrl.value = questRepository.uploadBitmapAndGetUrl(bitmap, questId)
            _isLoading.value = false
        }
    }
    private val questParentCache = mutableMapOf<String, User>()

    fun markQuestAsPending(quest: Quest) {
        viewModelScope.launch {
            try {
                var image = _imageUrl.value
                if (image != null) {
                    questRepository.updateQuestImage(quest.id, image)
                }
                println("Marking quest as PENDING: ${quest.title} (ID: ${quest.id})")
                questRepository.updateQuestStatus(quest.id, Status.PENDING)
                // Add a small delay to see if the update propagates correctly
                delay(100)
            } catch (e: Exception) {
                Log.e("QuestVM", "Error updating quest", e)
            }
        }
    }

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

    val childQuests: StateFlow<List<Quest>> =
        questRepository.getChildQuests(authRepository.currentUserIdFlow)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )
}