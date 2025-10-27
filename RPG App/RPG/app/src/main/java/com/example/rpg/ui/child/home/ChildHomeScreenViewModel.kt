package com.example.rpg.ui.child.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.rpg.data.model.Quest
import com.example.rpg.data.model.Reward
import com.example.rpg.data.repository.AuthRepository
import com.example.rpg.data.repository.QuestRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
@HiltViewModel
class ChildHomeViewModel @Inject constructor(
private val authRepository: AuthRepository
) : ViewModel(){

}
 **/

@HiltViewModel
class ChildHomeScreenViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val questRepository : QuestRepository
) : ViewModel(){
    private val _quests = mutableStateListOf(
        Quest(
            "", "Dishes", "Wash the dishes", null,
            null, null, 20,
            Reward.OTHER, repeat = false, allDay = true, completed = false, ""
        ),
    )
    var isLoadingAchievements by mutableStateOf(false)
        private set

    var errorMessageAchievements by mutableStateOf<String?>(null)
        private set

    var isLoadingStats by mutableStateOf(false)
        private set

    var errorMessageStats by mutableStateOf<String?>(null)
        private set

    //val quests: SnapshotStateList<Quest> = _quests
    val quests = questRepository.getUserQuests(authRepository.currentUserIdFlow)

}

