package com.example.rpg.ui.signup

import com.example.rpg.data.repository.AuthRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {
    private val _shouldRestartApp = MutableStateFlow(false)
    val shouldRestartApp: StateFlow<Boolean>
        get() = _shouldRestartApp.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun errorCleared () {
        _errorMessage.value = null
    }

    fun signUp(email: String, password: String ) {

        if (email.isBlank() ) {
            _errorMessage.value = "Please enter a email"
            return
        }

        if (password.isBlank() ) {
            _errorMessage.value = "Please enter a password"
            return
        }

        viewModelScope.launch {
            try {
                authRepository.signUp(email, password)
                _shouldRestartApp.value = true
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "An unknown error occurred"
            }
        }
    }
}