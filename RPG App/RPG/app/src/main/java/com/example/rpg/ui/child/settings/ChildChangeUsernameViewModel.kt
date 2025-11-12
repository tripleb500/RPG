package com.example.rpg.ui.child.settings

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rpg.data.repository.AuthRepository
import com.example.rpg.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChildChangeUsernameViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
): ViewModel() {
    private val _currentUsername = mutableStateOf<String?>(null)
    val currentUsername: State<String?> = _currentUsername

    var newUsername = mutableStateOf("")

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    private val _success = mutableStateOf(false)
    val success: State<Boolean> = _success

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    init {
        viewModelScope.launch {
            val uid = authRepository.currentUser?.uid
            if (uid != null) {
                val user = userRepository.getUserByUid(uid)
                _currentUsername.value = user?.username ?: ""
            }
        }
    }

    fun updateUsername() {
        val uid = authRepository.currentUser?.uid
        val newUsernameValue = newUsername.value.trim()

        if (uid == null) {
            _errorMessage.value = "User not logged in"
            return
        }
        if(newUsernameValue.isEmpty()) {
            _errorMessage.value = "Username cannot be empty."
            return
        }

        viewModelScope.launch {
            try {
                _isLoading.value = true
                _errorMessage.value = null


                //val uid = authRepository.currentUser?.uid ?: throw Exception("User not logged in")
                userRepository.updateUsername(uid, newUsernameValue)
                _currentUsername.value = newUsernameValue

                _success.value = true
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Failed to update username."
            } finally {
                _isLoading.value = false
            }
        }
    }

}