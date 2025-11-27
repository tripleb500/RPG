package com.example.rpg.data.repository

import android.graphics.Bitmap
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
    fun getQuestsByStatus(uid: String, status: Status): Flow<List<Quest>> =
        questRemoteDataSource.getQuests(uid).map { it.filter { q -> q.status == status } }

    fun getChildQuests(uid: String): Flow<List<Quest>> =
        questRemoteDataSource.getQuests(uid)

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

    fun getAvailableQuests(currentUserIdFlow: Flow<String?>): Flow<List<Quest>> =
        questRemoteDataSource.getAvailableQuests(currentUserIdFlow)

    fun getAvailableQuestsForChild(currentUserIdFlow: Flow<String?>): Flow<List<Quest>> =
        questRemoteDataSource.getAvailableQuestsForChild(currentUserIdFlow)

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

    suspend fun setAssignDate(questId: String) =
        questRemoteDataSource.setAssignDate(questId)

    suspend fun updateQuest(questId: String, updatedQuest: Quest) {
        questRemoteDataSource.updateQuest(questId, updatedQuest)
    }

    suspend fun uploadBitmapAndGetUrl(bitmap: Bitmap, questId: String): String? {
        return questRemoteDataSource.uploadBitmapAndGetUrl(bitmap, questId)
    }

    suspend fun updateQuestImage(questId: String, newImage: String) =
        questRemoteDataSource.updateQuestImage(questId, newImage)

    suspend fun delete(questId: String) = questRemoteDataSource.delete(questId)
}