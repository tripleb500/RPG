package com.example.rpg.ui.parent.addquest

import androidx.lifecycle.ViewModel
import com.example.rpg.data.repository.AuthRepository
import com.example.rpg.data.repository.ParentRepository
import com.example.rpg.data.repository.QuestRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ParentAddQuestViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val questRepository : QuestRepository,
    private val parentRepository :  ParentRepository

) : ViewModel(){
}