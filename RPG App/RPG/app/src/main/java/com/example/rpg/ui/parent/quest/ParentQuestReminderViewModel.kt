package com.example.rpg.ui.parent.quest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rpg.data.model.Quest
import com.example.rpg.data.repository.QuestReminderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ParentQuestReminderViewModel @Inject constructor(
    private val questReminderRepository: QuestReminderRepository
) : ViewModel() {
    fun createQuestReminderDoc(quest: Quest) {
        viewModelScope.launch {
            try {
                questReminderRepository.sendQuestReminder(quest)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}