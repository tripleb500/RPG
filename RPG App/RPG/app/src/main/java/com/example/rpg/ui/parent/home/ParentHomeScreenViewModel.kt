package com.example.rpg.ui.parent.home

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rpg.data.model.User
import com.example.rpg.data.repository.AuthRepository
import com.example.rpg.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ParentHomeScreenViewModel @Inject constructor(
    private val repository: UserRepository,
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _children = MutableStateFlow<List<User>>(emptyList())
    val children: StateFlow<List<User>> = _children

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun loadChildren(parentId: String) {
        viewModelScope.launch {
            val result = repository.getChildren(parentId)
            _children.value = result
        }
    }

    fun addChildByUsername(parentId: String, username: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            try {
                val child = repository.getUserByUsername(username)
                if (child != null) {
                    println("Found child with id: ${child.id}")
                    repository.addChild(parentId, child.id)
                    println("Added child ${child.id} to parent $parentId")
                    // Reload children after adding
                    loadChildren(parentId)
                    onSuccess()
                } else {
                    errorMessage = "No user found with that username."
                }
            } catch (e: Exception) {
                errorMessage = e.message ?: "Something went wrong."
            } finally {
                isLoading = false
            }
        }
    }
    private val _isLoadingP = MutableStateFlow(false)
    val isLoadingP: StateFlow<Boolean> = _isLoadingP.asStateFlow()

    //Variables and function to save profile image into Firebase
    private val _submittedImage = MutableStateFlow<String?>(null)
    val submittedImage: StateFlow<String?> = _submittedImage.asStateFlow()

    fun uploadImageForProfile(userId: String?, bitmap: Bitmap) {
        if (userId != null) {
            _submittedImage.value = null
            viewModelScope.launch {
                _isLoadingP.value = true
                _submittedImage.value = repository.uploadBitmapAndGetUrl(bitmap, userId)
                _isLoadingP.value = false
            }
        }
    }

    fun updateProfilePicture(userId: String?) {
        viewModelScope.launch {
            try {
                val image = _submittedImage.value
                if (image != null && userId != null) {
                    repository.updateProfilePicture(userId, image)
                }
                delay(100)
            } catch (e: Exception) {
                Log.e("QuestVM", "Error updating quest", e)
            }
        }
    }

    val currentUserFlow: Flow<User?> = authRepository.currentUserIdFlow
        .map { uid ->
            try {
                uid?.let { repository.getUserByUid(it) }
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
