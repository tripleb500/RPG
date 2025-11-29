package com.example.rpg.data.repository

import com.example.rpg.data.datasource.UserRemoteDataSource
import com.example.rpg.data.model.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val remoteDataSource: UserRemoteDataSource
) {
    suspend fun createProfile(user: User) =
        remoteDataSource.createProfile(user)

    suspend fun getProfile(id: String): User? =
        remoteDataSource.getProfile(id)

    fun getProfileFlow(id: String): Flow<User?> =
        remoteDataSource.getProfileFlow(id)

    suspend fun getUserByUsername(username: String): User? =
        remoteDataSource.getUserByUsername(username)

    fun getUserByUsernameFlow(username: String): Flow<User?> =
        remoteDataSource.getUserByUsernameFlow(username)


    suspend fun addChild(parentId: String, childId: String) =
        remoteDataSource.addChild(parentId, childId)

    suspend fun getChildren(parentId: String): List<User> =
        remoteDataSource.getChildren(parentId)

    fun getChildrenFlow(parentId: String): Flow<List<User>> =
        remoteDataSource.getChildrenFlow(parentId)


    suspend fun getUserByUid(uid: String): User? =
        remoteDataSource.getUserByUid(uid)

    fun getUserByUidFlow(uid: String): Flow<User?> =
        remoteDataSource.getUserByUidFlow(uid)

    suspend fun updateUsername(uid: String, newUsername: String) {
        // Ensure username not taken
        val existingUser = remoteDataSource.getUserByUsername(newUsername)
        if (existingUser != null && existingUser.id != uid) {
            throw Exception("Username is already taken")
        }

        val currentUser = remoteDataSource.getUserByUid(uid)
            ?: throw Exception("User not found")

        val updated = currentUser.copy(username = newUsername)
        remoteDataSource.createProfile(updated)
    }

    suspend fun updateFCMToken(uid: String) =
        remoteDataSource.updateFCMToken(uid)
}
