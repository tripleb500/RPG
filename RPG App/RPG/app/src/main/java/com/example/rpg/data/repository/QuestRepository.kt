package com.example.rpg.data.repository

import com.example.rpg.data.datasource.QuestRemoteDataSource
import com.example.rpg.data.model.Quest
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class QuestRepository @Inject constructor(
    private val questRemoteDataSource : QuestRemoteDataSource
){
    fun getUserQuests(currentUserIdFlow: Flow<String?>): Flow<List<Quest>>{
        return questRemoteDataSource.getUserQuests(currentUserIdFlow)
    }

    suspend fun getQuestItem(questId: String): Quest? {
        return questRemoteDataSource.getQuestItem(questId)
    }

    suspend fun create(questItem: Quest): String? {
        return questRemoteDataSource.create(questItem)
    }

    suspend fun update(questItem: Quest){
        return questRemoteDataSource.update(questItem)
    }

    suspend fun delete(questId: String){
        return questRemoteDataSource.delete(questId)
    }
}