package com.example.rpg.data.repository
/*import com.google.firebase.firestore.FirebaseFirestore
import com.example.rpg.data.model.User

class UserRepository {
    private val db = FirebaseFirestore.getInstance()
    private val usersCollection = db.collection("users")

    suspend fun addUser(user: User) {
        usersCollection.add(user).await()
    }

    suspend fun getAllUsers(): List<User> {
        val snapshot = usersCollection.get().await()
        return snapshot.documents.mapNotNull { it.toObject<User>() }
    }

}*/