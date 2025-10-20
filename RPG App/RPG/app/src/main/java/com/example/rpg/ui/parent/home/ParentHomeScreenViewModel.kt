package com.example.rpg.ui.parent.home

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

    fun loadChildren(parentId: String) {
        viewModelScope.launch {
            val result = repository.getChildren(parentId)
            _children.value = result
        }
    }
}
