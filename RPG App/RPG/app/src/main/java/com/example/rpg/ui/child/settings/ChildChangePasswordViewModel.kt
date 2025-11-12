package com.example.rpg.ui.child.settings

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rpg.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChildChangePasswordViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _currentPassword = mutableStateOf("")
    val currentPassword: State<String> = _currentPassword

    private val _newPassword = mutableStateOf("")
    val newPassword: State<String> = _newPassword

    private val _confirmNewPassword = mutableStateOf("")
    val confirmNewPassword: State<String> = _confirmNewPassword

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    private val _success = mutableStateOf(false)
    val success: State<Boolean> = _success

    fun onCurrentPasswordChange(value: String) {
        _currentPassword.value = value
    }

    fun onNewPasswordChange(value: String){
        _newPassword.value = value
    }

    fun onConfirmNewPasswordChange(value: String) {
        _confirmNewPassword.value = value
    }

    fun updatePassword() {
        val currentPW = _currentPassword.value
        val newPW = _newPassword.value
        val confirmNewPW = _confirmNewPassword.value
        val email = authRepository.currentUser?.email

        when {
            email.isNullOrEmpty() -> _errorMessage.value = "Current user email not available."
            currentPW.isEmpty() || newPW.isEmpty() || confirmNewPW.isEmpty() -> _errorMessage.value = "One of your password fields is empty."
            newPW != confirmNewPW -> _errorMessage.value = "New passwords do not match."
            else -> {
                viewModelScope.launch {
                    try {
                        _isLoading.value = true
                        _errorMessage.value = null

                        authRepository.reauthenticate(email, currentPW)
                        authRepository.updatePassword(newPW)

                        _success.value = true

                        _currentPassword.value = ""
                        _newPassword.value = ""
                        _confirmNewPassword.value  = ""
                    } catch (e: Exception) {
                        _errorMessage.value = e.message ?: "Failed to update password."
                    } finally {
                        _isLoading.value = false
                    }
                }
            }
        }
    }
}