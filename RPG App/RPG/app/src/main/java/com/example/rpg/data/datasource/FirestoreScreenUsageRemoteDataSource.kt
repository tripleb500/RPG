package com.example.rpg.data.datasource

import com.example.rpg.data.model.ScreenTimeRecord
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class FirestoreScreenUsageRemoteDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    fun observeDailyUsage(childId: String): Flow<List<ScreenTimeRecord>> = callbackFlow {
        val ref = firestore.collection("users")
            .document(childId)
            .collection("usage")

        val listener = ref
            .orderBy("date")
            .addSnapshotListener { snapshot, error ->
                if(error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val list = snapshot?.documents?.mapNotNull {
                    it.toObject(ScreenTimeRecord::class.java)
                } ?: emptyList()
                trySend(list)
            }
        awaitClose { listener.remove() }
    }

    suspend fun getCurrentDay(childId: String): ScreenTimeRecord? {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

         val document = firestore.collection("users")
             .document(childId)
             .collection("usage")
             .document(today)
             .get()
             .await()

        if (document.exists()) {
            return document.toObject(ScreenTimeRecord::class.java)
        } else {
            return null // Document doesn't exist
        }
    }

    suspend fun  uploadDailyUsage(
        childId: String,
        date: String,
        screenTimeMs: Long
    ) {
        val usageData = ScreenTimeRecord(
            date = date,
            screenTimeMs = screenTimeMs
        )

        firestore.collection("users")
            .document(childId)
            .collection("usage")
            .document(date)
            .set(usageData, SetOptions.merge())
            .await()
    }
}