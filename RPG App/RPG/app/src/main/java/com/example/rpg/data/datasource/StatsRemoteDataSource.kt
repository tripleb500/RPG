package com.example.rpg.data.datasource

import com.example.rpg.data.model.Quest
import com.example.rpg.data.model.Stats
import com.example.rpg.data.model.Status
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.dataObjects
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class StatsRemoteDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    suspend fun createStats(Stats: Stats): String {
        return firestore.collection(STAT_ITEMS_COLLECTION).add(Stats).await().id
    }
    /*suspend fun updateStats(statItem: String, newCount: Stats) {
        firestore.collection(STAT_ITEMS_COLLECTION)
            .document(statItem)
            .update("questsCompleted", newCount)
            .await()

    }*/

    companion object {
        private const val STAT_ITEMS_COLLECTION = "statItem" //Name of the collection for quest items
    }
}