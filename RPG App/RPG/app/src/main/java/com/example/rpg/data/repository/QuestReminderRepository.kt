package com.example.rpg.data.repository

import com.example.rpg.data.datasource.QuestReminderRemoteDataSource
import com.example.rpg.data.model.Quest
import javax.inject.Inject

class QuestReminderRepository @Inject constructor(
    private val questReminderRemoteDataSource: QuestReminderRemoteDataSource
) {
    // sends a reminder for the given quest.
    suspend fun sendQuestReminder(quest: Quest) {
        questReminderRemoteDataSource.createQuestReminderDoc(quest)
    }
}