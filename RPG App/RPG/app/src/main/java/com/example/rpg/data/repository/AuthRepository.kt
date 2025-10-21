package com.example.rpg.data.repository
import com.example.rpg.data.datasource.AuthRemoteDataSource
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Acts as a middleman between Datasource's' and rest of app.
 * Handles data-fetching patters, so, viewmodel or components don't directly deal with datasource logic
 */

class AuthRepository @Inject constructor(
    private val authRemoteDataSource: AuthRemoteDataSource
) {
    val currentUser: FirebaseUser?
        get() = authRemoteDataSource.currentUser
    val currentUserIdFlow: Flow<String?> = authRemoteDataSource.currentIdFlow

    suspend fun signUp(email: String, password: String) {
        authRemoteDataSource.signUp(email, password )
    }

    suspend fun signIn(email: String, password: String) {
        authRemoteDataSource.signIn(email, password)
    }

    fun signOut() {
        authRemoteDataSource.signOut()
    }
}