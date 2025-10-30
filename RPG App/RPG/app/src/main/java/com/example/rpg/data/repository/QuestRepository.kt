package com.example.rpg.data.repository

import com.example.rpg.data.datasource.QuestRemoteDataSource
import com.example.rpg.data.model.Quest
import com.example.rpg.data.model.Status
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class QuestRepository @Inject constructor(
    private val questRemoteDataSource: QuestRemoteDataSource
) {
    fun getQuests(
        currentUserIdFlow: Flow<String?>,
        status: Status? = null
    ) = questRemoteDataSource.getQuests(currentUserIdFlow, status)

    fun getAllQuests(currentUserIdFlow: Flow<String?>) =
        questRemoteDataSource.getAllQuests(currentUserIdFlow)

//    suspend fun getQuestItem(questId: String) = questRemoteDataSource.getQuestItem(questId)

    suspend fun create(questItem: Quest) = questRemoteDataSource.create(questItem)

//    suspend fun update(questItem: Quest) = questRemoteDataSource.update(questItem)

    suspend fun updateQuestStatus(questId: String, newStatus: Status) =
        questRemoteDataSource.updateQuestStatus(questId, newStatus)

//    suspend fun delete(questId: String) = questRemoteDataSource.delete(questId)
}