package com.example.rpg.ui.parent.settings

import androidx.lifecycle.ViewModel
import com.example.rpg.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ParentSettingsViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    fun onCardClicked(id: Int) {
        authRepository.signOut()
    }
}
