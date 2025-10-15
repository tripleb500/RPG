package com.example.rpg.data.repository

import com.example.rpg.data.model.Child
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class ChildRepository {
    private val db = Firebase.firestore

    private val childCollection = db.collection("child")

    suspend fun addChild(child : Child) {
        val childRef = childCollection.document()
        childRef.set(child.copy(id = childRef.id)).await()
    }

    suspend fun getChildByUsername(username : String): Child? {
        return try {
            val querySnapshot = childCollection
                .whereEqualTo("username", username)
                .get()
                .await()

            if (!querySnapshot.isEmpty) {
                querySnapshot.documents.first().toObject(Child::class.java)
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}