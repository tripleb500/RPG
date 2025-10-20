package com.example.rpg.data.datasource


import com.example.rpg.data.model.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


/**
 *  This data source is responsible for saving userprofile to Firebase Firestore.
 */
class UserRemoteDataSource @Inject constructor(private val firestore: FirebaseFirestore) {
    suspend fun createProfile (user: User) {  // Saves user object into Firestore
        firestore.collection(USERS_COLLECTION)  // "users" collection in firestore
            .document(user.id) // Specifies document ID is equal to user's ID.
            .set(user)  // Saves user object as document
            .await()
    }

    suspend fun getProfile (id: String): User? {
        return firestore.collection(USERS_COLLECTION)
            .document(id)
            .get()
            .await()
            .toObject()
    }


    companion object {  // Defines constant for Firestore collection name: "users".
        private const val USERS_COLLECTION = "users"
    }
}