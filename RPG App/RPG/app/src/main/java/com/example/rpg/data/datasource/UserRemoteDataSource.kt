package com.example.rpg.data.datasource


import com.example.rpg.data.model.User
import com.google.firebase.firestore.FieldPath
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

    suspend fun addChild(parentId: String, childId: String) {
        val parentRef = firestore.collection(USERS_COLLECTION).document(parentId)
        firestore.runTransaction { transaction ->
            val snapshot = transaction.get(parentRef)
            val currentList = snapshot.toObject(User::class.java)?.childrenIds ?: emptyList()
            val updatedList = if (childId in currentList) currentList else currentList + childId
            transaction.update(parentRef, "childrenIds", updatedList)
        }.await()
    }

    suspend fun getUserByUsername(username: String): User? {
        val result = firestore.collection(USERS_COLLECTION)
            .whereEqualTo("username", username)
            .get()
            .await()
        return if (result.documents.isNotEmpty()) {
            result.documents[0].toObject(User::class.java)
        } else {
            null
        }
    }

    suspend fun getChildren(parentId: String): List<User> {
        val parent = getProfile(parentId)
        val childrenIds = parent?.childrenIds ?: emptyList()

        if (childrenIds.isEmpty()) return emptyList()

        val children = firestore.collection(USERS_COLLECTION)
            .whereIn(FieldPath.documentId(), childrenIds)
            .get()
            .await()
            .toObjects(User::class.java)

        return children
    }

    companion object {  // Defines constant for Firestore collection name: "users".
        private const val USERS_COLLECTION = "users"
    }
}