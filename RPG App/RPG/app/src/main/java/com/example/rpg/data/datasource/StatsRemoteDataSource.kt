package com.example.rpg.data.datasource

import com.example.rpg.data.model.Quest
import com.example.rpg.data.model.Statistics
import com.example.rpg.data.model.User
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class StatsRemoteDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    suspend fun create(statItem: Statistics): String {
        return firestore.collection(STAT_ITEMS_COLLECTION).add(statItem).await().id
    }

    companion object {
        private const val STAT_ITEMS_COLLECTION = "stats" //Name of the collection for quest items
    }
}