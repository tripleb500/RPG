package com.example.rpg.data.datasource

import com.example.rpg.data.model.Quest
import com.example.rpg.data.model.Stats
import com.example.rpg.data.model.Status
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.dataObjects
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class StatsRemoteDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    private val statsCollection = firestore.collection("stats")

    suspend fun createStatsForUser(userId: String) {
        val stats = Stats()
        statsCollection.document(userId).set(stats).await()
    }

    suspend fun incrementQuestsInProgress(userId: String, delta: Int = 1) {
        statsCollection.document(userId)
            .update("questsInProgress", FieldValue.increment(delta.toLong()))
            .await()
    }

    suspend fun completeQuest(userId: String) {
        statsCollection.document(userId)
            .update(
                mapOf(
                    "questsInProgress" to FieldValue.increment(-1),
                    "questsCompleted" to FieldValue.increment(1)
                )
            ).await()
    }

    suspend fun getStats(userId: String): Stats? {
        val snapshot = statsCollection.document(userId).get().await()
        return snapshot.toObject(Stats::class.java)
    }

    suspend fun updateStats(userId: String, completed: Int, inProgress: Int) {
        firestore.collection("stats")
            .document(userId)
            .update(
                mapOf(
                    "questsCompleted" to completed,
                    "questsInProgress" to inProgress
                )
            ).await()
    }
}