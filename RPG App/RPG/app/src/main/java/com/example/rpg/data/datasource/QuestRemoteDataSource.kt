package com.example.rpg.data.datasource

import com.example.rpg.data.model.Quest
import com.example.rpg.data.model.Status
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject
import com.google.firebase.firestore.dataObjects
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.tasks.await

/**
 * Communicates directly with Firebase for quest collection.
 * Handles operations such as: getting quests, creating, updating, deleting(?).
 * Keeps Firebase-specific logic in one place.
 */

class QuestRemoteDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    // this function is used to get quests for a specified user, along with optional status filter
    @OptIn(ExperimentalCoroutinesApi::class)
    fun getQuests(
        currentUserIdFlow: Flow<String?>,
        status: Status? = null // optional status filter
    ): Flow<List<Quest>> {
        return currentUserIdFlow.flatMapLatest { ownerId ->
            var query = firestore
                .collection(QUEST_ITEMS_COLLECTION)
                .whereEqualTo(ASSIGNED_TO_ID_FIELD, ownerId)

            if (status != null) {
                query = query.whereEqualTo("status", status)
            }

            query.dataObjects()
        }
    }

    // This function is useful for parents to view all their children's quests
    @OptIn(ExperimentalCoroutinesApi::class)
    fun getAllQuests(currentUserIdFlow: Flow<String?>): Flow<List<Quest>> {
        return currentUserIdFlow.flatMapLatest { ownerId ->
            firestore
                .collection(QUEST_ITEMS_COLLECTION)
                .dataObjects()
        }
    }

    suspend fun getQuestItem(questId: String): Quest? {
        return firestore.collection(QUEST_ITEMS_COLLECTION).document(questId).get().await()
            .toObject()
    }

    suspend fun create(questItem: Quest): String {
        return firestore.collection(QUEST_ITEMS_COLLECTION).add(questItem).await().id
    }

    suspend fun update(questItem: Quest) {
        firestore.collection(QUEST_ITEMS_COLLECTION).document(questItem.id).set(questItem).await()
    }

    suspend fun delete(itemId: String) {
        firestore.collection(QUEST_ITEMS_COLLECTION).document(itemId).delete().await()
    }

    companion object {
//        private const val OWNER_ID_FIELD = "ownerId"
        private const val ASSIGNED_TO_ID_FIELD = "assignedTo"
        private const val QUEST_ITEMS_COLLECTION = "quests" //Name of the collection for quest items
    }
}

