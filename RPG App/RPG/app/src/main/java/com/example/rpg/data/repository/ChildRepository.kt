package com.example.rpg.data.repository
import com.example.rpg.data.model.Child

import com.example.rpg.data.model.Parent
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class ChildRepository {
    private val db = Firebase.firestore

    private val childCollection = db.collection("child")

    suspend fun addChild(child: Child) {
        val childRef = childCollection.document()
        childRef.set(child.copy(id = childRef.id)).await()
    }
}