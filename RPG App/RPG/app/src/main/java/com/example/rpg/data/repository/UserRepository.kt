package com.example.rpg.data.repository

import com.example.rpg.data.datasource.UserRemoteDataSource
import com.example.rpg.data.model.User
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val remoteDataSource: UserRemoteDataSource
) {
    suspend fun createProfile(user: User) = remoteDataSource.createProfile(user)
    suspend fun getProfile(id: String): User? = remoteDataSource.getProfile(id)
    suspend fun addChild(parentId: String, childId: String) =
        remoteDataSource.addChild(parentId, childId)

    suspend fun getUserByUsername(username: String): User? =
        remoteDataSource.getUserByUsername(username)

    suspend fun getChildren(parentId: String): List<User> = remoteDataSource.getChildren(parentId)
    suspend fun getUserByUid(uid: String): User? = remoteDataSource.getUserByUid(uid)

    suspend fun updateUsername(uid: String, newUsername: String) {
        val existingUser = remoteDataSource.getUserByUsername(newUsername)
        if (existingUser != null && existingUser.id != uid) {
            throw Exception("Username is already taken")
        }

        val currentUser = remoteDataSource.getUserByUid(uid) ?: throw Exception("User not found")

        val updateUser = currentUser.copy(username = newUsername)
        remoteDataSource.createProfile(updateUser)
    }

    suspend fun updateFCMToken(uid: String) =
        remoteDataSource.updateFCMToken(uid)
}