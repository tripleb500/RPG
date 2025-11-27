package com.example.rpg.data.datasource

import com.example.rpg.data.model.Quest
import com.example.rpg.data.model.Stats
import com.example.rpg.data.model.Status
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.dataObjects
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class StatsRemoteDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    suspend fun createStats(userId: String, stats: Stats) {
        firestore.collection(STAT_ITEMS_COLLECTION)
            .document(userId)      // SAME ID as user
            .set(stats)
            .await()
    }

    suspend fun updateStats(userId: String, updates: Map<String, Any>) {
        firestore.collection(STAT_ITEMS_COLLECTION)
            .document(userId)
            .update(updates)
            .await()
    }

    suspend fun getStats(userId: String): Stats? {
        return firestore.collection(STAT_ITEMS_COLLECTION)
            .document(userId)
            .get()
            .await()
            .toObject(Stats::class.java)
    }

    fun getStatsFlow(userIdFlow: Flow<String?>): Flow<Stats> =
        userIdFlow.flatMapLatest { uid ->
            if (uid == null) {
                flowOf(Stats(id = ""))
            } else {
                callbackFlow {
                    val docRef = firestore.collection("stats").document(uid)

                    val listener = docRef.addSnapshotListener { snapshot, error ->
                        if (error != null) {
                            trySend(Stats(id = ""))
                            return@addSnapshotListener
                        }

                        val stats = snapshot?.toObject(Stats::class.java) ?: Stats(id = "")
                        trySend(stats)
                    }

                    awaitClose { listener.remove() }
                }
            }
        }

    companion object {
        private const val STAT_ITEMS_COLLECTION = "stats" //Name of the collection for quest items
    }
}