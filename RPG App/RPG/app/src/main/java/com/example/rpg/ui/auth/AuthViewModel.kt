package com.example.rpg.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rpg.data.repository.AuthRepository
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)

    val currentUser: FirebaseUser? get() = authRepository.currentUser
    val currentUserIdFlow: Flow<String?> = authRepository.currentUserIdFlow
    // endregion

    // region - Actions
    fun signUp(email: String, password: String) {
        viewModelScope.launch {
            try {
                _authState.value = AuthState.Loading
                authRepository.signUp(email, password)
                _authState.value = AuthState.Authenticated(authRepository.currentUser)
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Sign up failed")
            }
        }
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            try {
                _authState.value = AuthState.Loading
                authRepository.signIn(email, password)
                _authState.value = AuthState.Authenticated(authRepository.currentUser)
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Sign in failed")
            }
        }
    }

    fun signOut() {
        authRepository.signOut()
        _authState.value = AuthState.Unauthenticated
    }
    // endregion

    // region - Initialization
    init {
        // Automatically observe auth state changes (if user logs in/out)
        viewModelScope.launch {
            currentUserIdFlow.collect { uid ->
                _authState.value = if (uid != null) {
                    AuthState.Authenticated(authRepository.currentUser)
                } else {
                    AuthState.Unauthenticated
                }
            }
        }
    }
    // endregion
}

/**
 * Represents the different authentication states of the app.
 */
sealed class AuthState {
    data object Loading : AuthState()
    data class Authenticated(val user: FirebaseUser?) : AuthState()
    data object Unauthenticated : AuthState()
    data class Error(val message: String) : AuthState()
}
