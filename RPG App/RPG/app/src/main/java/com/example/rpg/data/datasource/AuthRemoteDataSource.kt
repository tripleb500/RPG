package com.example.rpg.data.datasource

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * Communicates directly with Firebase for authentication.
 * Handles operations such as: signing up, signing in, and signing out.
 * Keeps Firebase-specific logic in one place.
 */

class AuthRemoteDataSource @Inject constructor(private val auth: FirebaseAuth) {

    suspend fun signUp(email: String, password: String )
    {
        auth.createUserWithEmailAndPassword(email, password).await()
    }

    suspend fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).await()
    }

    fun signOut() {
        auth.signOut()
    }
}
