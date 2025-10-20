package com.example.rpg.ui.parent.addchild

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rpg.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddChildDialogViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    fun addChildByUsername(parentId: String, username: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            val child = userRepository.getUserByUsername(username)
            if (child != null) {
                userRepository.addChild(parentId, child.id)
                onSuccess()
            } else {
                errorMessage = "User not found"
            }
            isLoading = false
        }
    }
}