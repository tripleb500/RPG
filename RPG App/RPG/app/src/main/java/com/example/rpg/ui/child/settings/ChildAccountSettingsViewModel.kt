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
class ChildAccountSettingsViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    private val _userEmail = mutableStateOf<String?>(null)
    val userEmail: State<String?> = _userEmail

    private val _username = mutableStateOf<String?>(null)
    val username: State<String?> = _username

    init {
        loadUsername()
        loadUserEmail()
    }

    private fun loadUsername() {
        viewModelScope.launch {
            val uid = authRepository.currentUser?.uid
            if(uid != null) {
                _username.value = userRepository.getUserByUid(uid)?.username
            }
        }
    }

    private fun loadUserEmail() {
        _userEmail.value = authRepository.currentUser?.email
    }
}