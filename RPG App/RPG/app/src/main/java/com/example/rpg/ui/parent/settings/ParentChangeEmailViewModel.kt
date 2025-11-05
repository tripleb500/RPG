package com.example.rpg.ui.parent.settings

import android.util.Log
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
class ParentChangeEmailViewModel @Inject constructor (
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
): ViewModel() {
    private val _currentEmail = mutableStateOf<String?>(null)
    val currentEmail: State<String?> = _currentEmail

    var newEmail = mutableStateOf("")
    var confirmEmail = mutableStateOf("")
    var password = mutableStateOf("")

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    private val _success = mutableStateOf(false)
    val success: State<Boolean> = _success

    init {
        _currentEmail.value = authRepository.currentUser?.email
    }

    fun updateEmail() {

        val newEmailValue = newEmail.value
        val confirmEmailValue = confirmEmail.value
        val passwordValue = password.value
        val email = authRepository.currentUser?.email

        if (email.isNullOrEmpty()) {
            _errorMessage.value = "Current mail is not available."
            return
        }
        if (newEmailValue.isEmpty() || confirmEmailValue.isEmpty()) {
            _errorMessage.value  = "Email fields cannot be empty."
            return
        }

        if (newEmailValue.isEmpty()) {
            _errorMessage.value = "New email field cannot be empty."
            return
        }

        if (confirmEmailValue.isEmpty()) {
            _errorMessage.value = "Confirm Email field cannot be empty."
            return
        }

        if (newEmailValue != confirmEmailValue) {
            _errorMessage.value = "Emails do not match."
            return
        }
        if (passwordValue.isEmpty()) {
            _errorMessage.value = "Password cannot be empty."
            return
        }

        viewModelScope.launch {
            try {

                authRepository.reauthenticate(email, passwordValue)
                authRepository.updateEmail(newEmailValue)

                val uid = authRepository.currentUser?.uid
                if(uid != null) {
                    val user = userRepository.getUserByUid(uid)
                    if(user != null) {
                        val updatedUser = user.copy(email = newEmailValue)
                        userRepository.createProfile(updatedUser)
                    }
                }
                _currentEmail.value = newEmailValue
                _success.value = true
            } catch (e: Exception) {
                Log.e("EmailUpdate", "Failed to update email", e)
                _errorMessage.value = e.message ?: "Failed to update email."
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}

