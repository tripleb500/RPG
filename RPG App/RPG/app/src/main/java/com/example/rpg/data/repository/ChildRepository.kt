package com.example.rpg.data.repository

import com.example.rpg.data.model.Parent
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class ChildRepository {
    private val db = Firebase.firestore

    private val childCollection = db.collection("child")

    suspend fun addParent(parent: Parent) {
        val parentRef = childCollection.document()
        parentRef.set(parent.copy(id = parentRef.id)).await()
    }
}