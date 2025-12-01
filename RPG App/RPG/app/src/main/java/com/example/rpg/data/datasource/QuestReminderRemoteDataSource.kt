package com.example.rpg.data.datasource

import com.example.rpg.data.model.Quest
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * Communicates directly with Firebase for quest reminder notifications.
 * Handles creating documents with reminder data.
 * Keeps Firebase-specific logic in one place.
 */

class QuestReminderRemoteDataSource @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {

    // Create quest reminder doc, firebase messaging & google cloud scheduler will send notification
    suspend fun createQuestReminderDoc(quest: Quest) {
        val parentId = auth.currentUser?.uid

        if (quest.assignedTo.isBlank()) {
            throw IllegalStateException("Quest has no assigned childId")
        }

        val reminder = hashMapOf(
            "questId" to quest.id,
            "childId" to quest.assignedTo,
            "parentId" to parentId,
            "notificationMessage" to "Reminder: ${quest.title}",
            "createdAt" to FieldValue.serverTimestamp()
        )

        firestore.collection("questReminders")
            .add(reminder)
            .await()
    }
}
