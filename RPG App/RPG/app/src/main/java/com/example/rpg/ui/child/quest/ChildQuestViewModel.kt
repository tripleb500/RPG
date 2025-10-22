package com.example.rpg.ui.child.quest

import androidx.lifecycle.ViewModel
import com.example.rpg.data.repository.AuthRepository
import com.example.rpg.data.repository.QuestRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChildQuestViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val questRepository: QuestRepository
) : ViewModel() {
    val quests = questRepository.getUserQuests(authRepository.currentUserIdFlow)
}