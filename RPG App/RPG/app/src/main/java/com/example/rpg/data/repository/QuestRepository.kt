package com.example.rpg.data.repository

import android.net.Uri
import com.example.rpg.data.datasource.QuestRemoteDataSource
import com.example.rpg.data.model.Quest
import com.example.rpg.data.model.Status
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class QuestRepository @Inject constructor(
    private val questRemoteDataSource: QuestRemoteDataSource
) {
    fun getQuestsByStatus(
        currentUserIdFlow: Flow<String?>,
        status: Status
    ): Flow<List<Quest>> =
        questRemoteDataSource.getQuests(currentUserIdFlow)
            .map { quests ->
                val filteredQuests = quests.filter { it.status == status }
                println("Filtering quests - Total: ${quests.size}, $status: ${filteredQuests.size}")
                quests.forEach { quest ->
                    println("   - Quest: ${quest.title}, Status: ${quest.status}, ID: ${quest.id}")
                }
                filteredQuests
            }

    fun getCompletedQuestsByChild(
        currentUserIdFlow: Flow<String?>,
    ): Flow<List<Quest>> =
        questRemoteDataSource.getQuests(currentUserIdFlow)
            .map { quests ->
                val filteredQuests = quests.filter { it.status == Status.COMPLETED }

                filteredQuests
            }

    // TODO: Delete, this gets all quests even if not a child
    // This function is useful for parents to view all their children's quests
    fun getAllQuests(currentUserIdFlow: Flow<String?>) =
        questRemoteDataSource.getAllQuests(currentUserIdFlow)

    fun getAssignedQuests(currentUserIdFlow: Flow<String?>) =
        questRemoteDataSource.getAssignedQuests(currentUserIdFlow)

    fun getChildQuests(currentUserIdFlow: Flow<String?>) =
        questRemoteDataSource.getChildQuests(currentUserIdFlow)

//    suspend fun getQuestItem(questId: String) = questRemoteDataSource.getQuestItem(questId)

    suspend fun create(questItem: Quest) = questRemoteDataSource.create(questItem)

    suspend fun uploadImage(imageUri: Uri) = questRemoteDataSource.uploadImage(imageUri)

//    suspend fun update(questItem: Quest) = questRemoteDataSource.update(questItem)

    fun getAvailableQuests(currentUserIdFlow: Flow<String>): Flow<List<Quest>> =
        questRemoteDataSource.getAvailableQuests(currentUserIdFlow)

    suspend fun claimQuest(questId: String, childId: String) =
        questRemoteDataSource.updateQuestAssignment(
            questId = questId,
            childId = childId,
            newStatus = Status.INPROGRESS
        )

    suspend fun createAvailableQuest(questItem: Quest, parentId: String): String {
        val availableQuest = questItem.copy(
            assignee = parentId,
            assignedTo = "",
            status = Status.AVAILABLE
        )
        return questRemoteDataSource.create(availableQuest)
    }

    suspend fun updateQuestStatus(questId: String, newStatus: Status) =
        questRemoteDataSource.updateQuestStatus(questId, newStatus)

    suspend fun completeQuest(questId: String) =
        questRemoteDataSource.completeQuest(questId)

    suspend fun updateQuest(questId: String, updatedQuest: Quest) {
        questRemoteDataSource.updateQuest(questId, updatedQuest)
    }

//    suspend fun delete(questId: String) = questRemoteDataSource.delete(questId)
}