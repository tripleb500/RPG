package com.example.rpg.ui.parent.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rpg.data.model.User
import com.example.rpg.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ParentHomeScreenViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {
    private val _children = MutableStateFlow<List<User>>(emptyList())
    val children: StateFlow<List<User>> = _children

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun loadChildren(parentId: String) {
        viewModelScope.launch {
            val result = repository.getChildren(parentId)
            _children.value = result
        }
    }

    fun addChildByUsername(parentId: String, username: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            try {
                val child = repository.getUserByUsername(username)
                if (child != null) {
                    println("Found child with id: ${child.id}")
                    repository.addChild(parentId, child.id)
                    println("Added child ${child.id} to parent $parentId")
                    // Reload children after adding
                    loadChildren(parentId)
                    onSuccess()
                } else {
                    errorMessage = "No user found with that username."
                }
            } catch (e: Exception) {
                errorMessage = e.message ?: "Something went wrong."
            } finally {
                isLoading = false
            }
        }
    }
}
