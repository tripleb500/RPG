package com.example.rpg.ui.parent.addchild

import com.example.rpg.data.repository.ChildRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import android.util.Log

class ParentAddChildViewModel(private val repository: ChildRepository) : ViewModel() {

    fun onAddChildButtonClicked(username : String) {
        viewModelScope.launch {
            val child = repository.getChildByUsername(username)
            if (child != null) {
                Log.d("ChildViewModel", "Found: ${child.username}")
            } else {
                Log.d("ChildViewModel", "No child found with username: $username")
            }
        }
    }
}