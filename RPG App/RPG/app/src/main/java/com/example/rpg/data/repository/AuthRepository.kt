package com.example.rpg.data.repository

import com.example.rpg.data.datasource.AuthRemoteDataSource
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * Middleman between Datasource's and rest of app.
 * Handles data-fetching patterns, so viewmodel or components don't directly deal with datasource logic
 */
class AuthRepository @Inject constructor(
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val firestore: FirebaseFirestore
) {

    val currentUser: FirebaseUser? = authRemoteDataSource.currentUser
    val currentUserIdFlow: Flow<String?> = authRemoteDataSource.currentUserIdFlow

    suspend fun signUp(email:String, password: String, username: String, familyRole: String):Result<FirebaseUser?> {
        return try {
            val user = authRemoteDataSource.signUp(email, password) ?:
            return Result.failure(Exception("User null after sign-up"))

            val uid = user.uid

            user?.uid?.let { uid ->
                val userProfile = mapOf(
                    "uid" to uid,
                    "email" to email,
                    "username" to username,
                    "familyRole" to familyRole
                )
                firestore.collection("users")
                    .document(uid)
                    .set(userProfile)
                    .await()
            }
            Result.success(user)
        } catch(e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signIn(email: String, password: String) {
        authRemoteDataSource.signIn(email, password)
    }

    fun signOut() {
        authRemoteDataSource.signOut()
    }
}