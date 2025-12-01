package com.example.rpg.ui.auth.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rpg.data.repository.AuthRepository
import com.example.rpg.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun signIn(email: String, password: String, onSuccess: (Boolean, String?) -> Unit) {
        if (email.isBlank()) {
            _errorMessage.value = "Email empty, Please enter an email."
            return
        }
        if (password.isBlank()) {
            _errorMessage.value = "Password empty, Please enter a password"
            return
        }

        viewModelScope.launch {
            try {
                authRepository.signIn(email, password)
                val userId = authRepository.currentUser?.uid
                if (userId != null) {
                    userRepository.updateFCMToken(userId)
                    val user = userRepository.getProfile(userId)
                    val role = user?.familyRole
                    onSuccess(true, role)
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Sign in failed."
                onSuccess(false, null)
            }
        }
    }
}