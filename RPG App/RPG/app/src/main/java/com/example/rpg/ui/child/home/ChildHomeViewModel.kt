package com.example.rpg.ui.child.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import com.example.rpg.data.model.Quest
import androidx.lifecycle.viewModelScope
import com.example.rpg.data.model.Reward
import com.example.rpg.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import javax.inject.Inject

/**
@HiltViewModel
class ChildHomeViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel(){

}
**/


class ChildHomeViewModel : ViewModel() {

    private val _quests = mutableStateListOf(
        Quest("", "Dishes", "Wash the dishes", null,
            null, null, 20,
            Reward.OTHER, false, true, false, ""),
    )
    var isLoadingAchievements by mutableStateOf(false)
        private set

    var errorMessageAchievements by mutableStateOf<String?>(null)
        private set

    var isLoadingStats by mutableStateOf(false)
        private set

    var errorMessageStats by mutableStateOf<String?>(null)
        private set

    val quests: SnapshotStateList<Quest> = _quests
}

