package com.example.rpg.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rpg.data.model.Stats
import com.example.rpg.data.model.User
import com.example.rpg.data.repository.AuthRepository
import com.example.rpg.data.repository.StatsRepository
import com.example.rpg.data.repository.UserRepository
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val statsRepository: StatsRepository

) : ViewModel() {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState: StateFlow<AuthState> = _authState

    val currentUser: FirebaseUser? get() = authRepository.currentUser
    val currentUserIdFlow: Flow<String?> = authRepository.currentUserIdFlow

    private val _currentUserData = MutableStateFlow<User?>(null)
    val currentUserData: StateFlow<User?> = _currentUserData

    // endregion

    // region - Actions
    fun signUp(email: String, password: String, firstname: String, lastname: String, username: String, role: String) {
        viewModelScope.launch {
            try {
                authRepository.signUp(email, password)
                val userId = authRepository.currentUser?.uid ?: throw Exception("User ID is null")
                val user = User(
                    id = userId,
                    firstname = firstname,
                    lastname = lastname,
                    username = username,
                    email = email,
                    familyRole = role
                )
                //_authState.value = AuthState.Loading
                //authRepository.signUp(email, password)
                userRepository.createProfile(user)
                if(role.lowercase() == "child") {
                    statsRepository.createStats(userId, Stats())
                }
                _authState.value = AuthState.Authenticated(user)
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

                val uid = authRepository.currentUser?.uid
                val user = uid?.let {userRepository.getUserByUid(it) }

                if (user != null) {
                    _currentUserData.value = user
                    _authState.value = AuthState.Authenticated(user)
                } else {
                    _authState.value = AuthState.Unauthenticated
                }


               //_authState.value = AuthState.Authenticated(authRepository.currentUser)
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
            authRepository.currentUserIdFlow.collect { uid ->
                if (uid != null) {
                    try {
                        val user = userRepository.getUserByUid(uid)
                        if(user != null) {
                            _currentUserData.value = user
                            _authState.value = AuthState.Authenticated(user)
                        } else {
                            _authState.value = AuthState.Unauthenticated
                        }
                    } catch (e: Exception) {
                        _authState.value = AuthState.Error(e.message ?: "Failed to fetch user")
                    }
                } else {
                    _authState.value = AuthState.Unauthenticated
                    _currentUserData.value = null
                }
            }
            /**
            currentUserIdFlow.collect { uid ->
                _authState.value = if (uid != null) {
                    AuthState.Authenticated(authRepository.currentUser)
                } else {
                    AuthState.Unauthenticated
                }
            }
            */
        }
    }
    // endregion
}

/**
 * Represents the different authentication states of the app.
 */
sealed class AuthState {
    data object Loading : AuthState()
    data class Authenticated(val user: User) : AuthState()
    data object Unauthenticated : AuthState()
    data class Error(val message: String) : AuthState()
}
