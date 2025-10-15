package com.example.rpg.data.repository
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.example.rpg.data.model.Parent
import com.example.rpg.data.model.Child
import kotlinx.coroutines.tasks.await

class ParentRepository {
    private val db = Firebase.firestore
    private val parentCollection = db.collection("parents")

    suspend fun addParent(parent: Parent) {
        val parentRef = parentCollection.document()
        parentRef.set(parent.copy(id = parentRef.id)).await()
    }
    suspend fun addChildToParent(parentId: String, child: Child) {
        parentCollection.document(parentId)
            .collection("children")
            .add(child)
            .await()
    }

}