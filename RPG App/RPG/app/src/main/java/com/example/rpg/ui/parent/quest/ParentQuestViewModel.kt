package com.example.rpg.ui.parent.quest

import androidx.lifecycle.ViewModel
import com.example.rpg.data.model.Quest
import com.example.rpg.data.repository.AuthRepository
import com.example.rpg.data.repository.QuestRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ParentQuestViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val questRepository : QuestRepository
) : ViewModel(){
    val quests = questRepository.getAllQuests(authRepository.currentUserIdFlow)
}