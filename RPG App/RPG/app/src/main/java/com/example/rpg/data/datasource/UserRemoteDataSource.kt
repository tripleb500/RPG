package com.example.rpg.data.datasource


import android.graphics.Bitmap
import com.example.rpg.data.injection.FireBaseHiltModule.storage
import com.example.rpg.data.model.User
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import javax.inject.Inject

/**
 *  This data source is responsible for saving userprofile to Firebase Firestore.
 */

class UserRemoteDataSource @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) {
    private val usersCollection = firestore.collection(USERS_COLLECTION)

    suspend fun createProfile(user: User) {
        usersCollection
            .document(user.id)
            .set(user)
            .await()
    }

    suspend fun getProfile(id: String): User? {
        return usersCollection
            .document(id)
            .get()
            .await()
            .toObject(User::class.java)
    }

    fun getProfileFlow(id: String): Flow<User?> = callbackFlow {
        val listener = usersCollection.document(id)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                trySend(snapshot?.toObject(User::class.java))
            }

        awaitClose { listener.remove() }
    }

    suspend fun getUserByUsername(username: String): User? {
        val result = usersCollection
            .whereEqualTo("username", username)
            .get()
            .await()

        return result.documents.firstOrNull()?.toObject(User::class.java)
    }

    fun getUserByUsernameFlow(username: String): Flow<User?> = callbackFlow {
        val listener = usersCollection
            .whereEqualTo("username", username)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val user = snapshot?.documents?.firstOrNull()?.toObject(User::class.java)
                trySend(user)
            }

        awaitClose { listener.remove() }
    }

    suspend fun getUserByUid(uid: String): User? {
        val doc = usersCollection
            .document(uid)
            .get()
            .await()

        return doc.toObject(User::class.java)
    }

    fun getUserByUidFlow(uid: String): Flow<User?> = callbackFlow {
        val listener = usersCollection.document(uid)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                trySend(snapshot?.toObject(User::class.java))
            }

        awaitClose { listener.remove() }
    }

    suspend fun addChild(parentId: String, childId: String) {
        val parentRef = usersCollection.document(parentId)

        firestore.runTransaction { transaction ->
            val snapshot = transaction.get(parentRef)
            val currentList = snapshot.get("childrenIds") as? List<*> ?: emptyList<Any>()
            val safeList = currentList.mapNotNull { it as? String }

            if (childId !in safeList) {
                transaction.update(parentRef, "childrenIds", safeList + childId)
            }
        }.await()
    }

    suspend fun getChildren(parentId: String): List<User> {
        val parent = getProfile(parentId)
        val childrenIds = parent?.childrenIds ?: emptyList()

        if (childrenIds.isEmpty()) return emptyList()

        return usersCollection
            .whereIn(FieldPath.documentId(), childrenIds)
            .get()
            .await()
            .toObjects(User::class.java)
    }

    fun getChildrenFlow(parentId: String): Flow<List<User>> = callbackFlow {
        val listener = usersCollection.document(parentId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val ids = snapshot?.get("childrenIds") as? List<String> ?: emptyList()

                if (ids.isEmpty()) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }

                // Firestore WHERE IN supports up to 10 IDs per query
                val batches = ids.chunked(10)

                val tasks = batches.map { chunk ->
                    usersCollection.whereIn(FieldPath.documentId(), chunk).get()
                }

                Tasks.whenAllSuccess<QuerySnapshot>(tasks)
                    .addOnSuccessListener { results ->
                        val users = results.flatMap { it.toObjects(User::class.java) }
                        trySend(users)
                    }
                    .addOnFailureListener { e -> close(e) }
            }

        awaitClose { listener.remove() }
    }


    fun getFriends(userId: String): Flow<List<User>> = callbackFlow {
        val listener = usersCollection.document(userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val friendsIds = snapshot?.get("friends") as? List<String> ?: emptyList()

                if (friendsIds.isEmpty()) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }

                val batches = friendsIds.chunked(10)

                val tasks = batches.map { chunk ->
                    usersCollection.whereIn(FieldPath.documentId(), chunk).get()
                }

                Tasks.whenAllSuccess<QuerySnapshot>(tasks)
                    .addOnSuccessListener { results ->
                        val friends = results.flatMap { it.toObjects(User::class.java) }
                        trySend(friends)
                    }
                    .addOnFailureListener { e -> close(e) }
            }

        awaitClose { listener.remove() }
    }

    suspend fun addFriend(userId: String, friendId: String) {
        usersCollection.document(userId)
            .update("friends", FieldValue.arrayUnion(friendId))
            .await()
    }

    companion object {
        private const val USERS_COLLECTION = "users"
    }

    suspend fun uploadBitmapAndGetUrl(
        bitmap: Bitmap,
        userId: String
    ): String? { // Just return String? (null if failed)
        return try {
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos)
            val imageData = baos.toByteArray()

            val path = "users/$userId/images/camera_${System.currentTimeMillis()}.jpg"
            val storageRef = storage.reference.child(path)

            storageRef.putBytes(imageData).await()
            storageRef.downloadUrl.await().toString()

        } catch (e: Exception) {
            null // Just return null if anything fails
        }
    }

    suspend fun updateProfilePicture(userId: String, newImage: String) {
        firestore.collection(USERS_COLLECTION)
            .document(userId)
            .update("profilePicture", newImage)
            .await()
    }
}
