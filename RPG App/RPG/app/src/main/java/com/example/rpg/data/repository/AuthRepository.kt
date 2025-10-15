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
) {

    val currentUser: FirebaseUser? = authRemoteDataSource.currentUser
    val currentUserIdFlow: Flow<String?> = authRemoteDataSource.currentUserIdFlow

    suspend fun signUp(email:String, password: String): Result<Unit> {
        return try {
            authRemoteDataSource.signUp(email, password)
            Result.success(Unit)
        } catch (e: Exception) {
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