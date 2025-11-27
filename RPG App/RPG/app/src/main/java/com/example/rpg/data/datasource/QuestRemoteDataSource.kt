package com.example.rpg.data.datasource

import android.graphics.Bitmap
import android.net.Uri
import com.example.rpg.data.model.Quest
import com.example.rpg.data.model.Status
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.dataObjects
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.util.Date
import java.util.UUID
import javax.inject.Inject

/**
 * Communicates directly with Firebase for quest collection.
 * Handles operations such as: getting quests, creating, updating, deleting(?).
 * Keeps Firebase-specific logic in one place.
 */

class QuestRemoteDataSource @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage

) {
    // ===========================
    // GET QUESTS
    // ===========================

    // this generic function is used to get quests for a specified user, along with optional status filter
    @OptIn(ExperimentalCoroutinesApi::class)
    fun getQuests(uid: String): Flow<List<Quest>> = callbackFlow {
        val query = firestore.collection(QUEST_ITEMS_COLLECTION)
            .whereEqualTo(ASSIGNED_TO_ID_FIELD, uid)

        val listener = query.addSnapshotListener { snapshot, error ->
            if (error != null) {
                trySend(emptyList())
                return@addSnapshotListener
            }

            val quests = snapshot?.toObjects(Quest::class.java) ?: emptyList()

            trySend(
                quests.sortedWith(
                    compareBy<Quest> { it.deadlineDate == null }
                        .thenBy { it.deadlineDate ?: Date(Long.MAX_VALUE) }
                )
            )
        }

        awaitClose { listener.remove() }
    }


    // TODO: Delete, this gets all quests even if not a child
    // This function is useful for parents to view all their children's quests
    @OptIn(ExperimentalCoroutinesApi::class)
    fun getAllQuests(currentUserIdFlow: Flow<String?>): Flow<List<Quest>> {
        return currentUserIdFlow.flatMapLatest { ownerId ->
            firestore
                .collection(QUEST_ITEMS_COLLECTION)
                .dataObjects()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getAssignedQuests(currentUserIdFlow: Flow<String?>): Flow<List<Quest>> {
        return currentUserIdFlow.flatMapLatest { parentId ->
            firestore
                .collection(QUEST_ITEMS_COLLECTION)
                .whereEqualTo("assignee", parentId)
                .dataObjects()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getChildQuests(currentUserIdFlow: Flow<String?>): Flow<List<Quest>> {
        return currentUserIdFlow.flatMapLatest { childId ->
            firestore
                .collection(QUEST_ITEMS_COLLECTION)
                .whereEqualTo("assignedTo", childId)
                .dataObjects()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getAvailableQuests(currentUserIdFlow: Flow<String?>): Flow<List<Quest>> {
        return currentUserIdFlow.flatMapLatest { parentId ->
            if (parentId == null) {
                flowOf(emptyList())
            } else {
                firestore.collection(QUEST_ITEMS_COLLECTION)
                    .whereEqualTo("assignee", parentId)
                    .whereEqualTo("status", Status.AVAILABLE.name)
                    .dataObjects()
            }
        }

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getAvailableQuestsForChild(currentUserIdFlow: Flow<String?>): Flow<List<Quest>> {
        return currentUserIdFlow.flatMapLatest { childId ->
            if (childId == null) {
                flowOf(emptyList())
            } else {
                firestore.collection(QUEST_ITEMS_COLLECTION)
                    .whereEqualTo("status", Status.AVAILABLE.name)
                    .whereIn("assignedTo", listOf("", childId))
                    .dataObjects()
            }
        }
    }

    // ===========================
    // UPDATE QUESTS
    // ===========================

    suspend fun updateQuestStatus(questId: String, newStatus: Status) {
        firestore.collection(QUEST_ITEMS_COLLECTION)
            .document(questId)
            .update("status", newStatus.name)
            .await()
    }

    suspend fun updateQuestImage(questId: String, newImage: String) {
        firestore.collection(QUEST_ITEMS_COLLECTION)
            .document(questId)
            .update("submittedImageUrl", newImage)
            .await()
    }

    // TODO: add more fields as needed
    suspend fun updateQuest(questId: String, updatedQuest: Quest) {
        val questRef = firestore.collection(QUEST_ITEMS_COLLECTION).document(questId)

        // Build a map of fields to update (skip nulls to avoid overwriting valid data)
        val updateMap = mutableMapOf<String, Any?>().apply {
            updatedQuest.title.let { this["title"] = it }
            updatedQuest.description.let { this["description"] = it }
            updatedQuest.rewardType.let { this["reward"] = it }
            updatedQuest.deadlineDate?.let { this["deadlineDate"] = it }
            updatedQuest.status.let { this["status"] = it.name }
            updatedQuest.assignedTo.let { this["assignedTo"] = it }
        }

        questRef.update(updateMap).await()
    }

    suspend fun setAssignDate(questId: String) {
        firestore.collection(QUEST_ITEMS_COLLECTION)
            .document(questId)
            .update("assignDate", Timestamp.now())
            .await()
    }

    suspend fun completeQuest(questId: String) {
        firestore.collection(QUEST_ITEMS_COLLECTION)
            .document(questId)
            .update("completionDate", Timestamp.now())
            .await()
    }

    suspend fun updateQuestAssignment(questId: String, childId: String, newStatus: Status) {
        firestore.collection(QUEST_ITEMS_COLLECTION)
            .document(questId)
            .update(
                mapOf(
                    "assignedTo" to childId,
                    "status" to newStatus.name
                )
            ).await()
    }

    // ===========================
    // BASIC CRUD
    // ===========================

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
        firestore.collection(QUEST_ITEMS_COLLECTION)
            .document(itemId)
            .delete()
            .await()
    }

    /*
    fun uploadImage(uri: Uri?): Task<Uri?> {
        val storageReference = storage.reference
        val imageReference = storageReference.child("images/" + uri!!.lastPathSegment)
        imageReference.putFile(uri)
        return imageReference.downloadUrl
        //val uploadtask = uri.let { imageReference.putFile(it) }
    }

     */

    suspend fun uploadImage(uri: Uri?): String {
        if (uri == null) throw IllegalArgumentException("URI cannot be null")

        val fileName = "images/${UUID.randomUUID()}.jpg" // generate unique name
        val imageRef = storage.reference.child(fileName)

        // Upload the file
        imageRef.putFile(uri).await()

        // Get the download URL after successful upload
        return imageRef.downloadUrl.await().toString()
    }


    companion object {
        //        private const val OWNER_ID_FIELD = "ownerId"
        private const val ASSIGNED_TO_ID_FIELD = "assignedTo"
        private const val QUEST_ITEMS_COLLECTION = "quests" //Name of the collection for quest items
    }

    suspend fun uploadBitmapAndGetUrl(
        bitmap: Bitmap,
        questId: String
    ): String? { // Just return String? (null if failed)
        return try {
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos)
            val imageData = baos.toByteArray()

            val path = "quests/$questId/images/camera_${System.currentTimeMillis()}.jpg"
            val storageRef = storage.reference.child(path)

            storageRef.putBytes(imageData).await()
            storageRef.downloadUrl.await().toString()

        } catch (e: Exception) {
            null // Just return null if anything fails
        }
    }
}