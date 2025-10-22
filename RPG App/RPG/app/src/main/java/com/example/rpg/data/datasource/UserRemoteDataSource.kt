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
            .toObject(User::class.java)
    }

    suspend fun addChild(parentId: String, childId: String) {
        val parentRef = firestore.collection(USERS_COLLECTION).document(parentId)
        firestore.runTransaction { transaction ->
            val snapshot = transaction.get(parentRef)
            val currentList = (snapshot.get("childrenIds") as? List<*>)   // Use star projection
                ?.mapNotNull { it as? String } ?: emptyList()            // Safely map to String

            if (childId !in currentList) {
                transaction.update(parentRef, "childrenIds", currentList + childId)
            }
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

    suspend fun getUserByUid(uid: String): User? {
        val doc = FirebaseFirestore.getInstance()
            .collection("users")
            .document(uid)
            .get()
            .await()  // from kotlinx-coroutines-play-services

        return if (doc.exists()) doc.toObject(User::class.java) else null
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