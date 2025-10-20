package com.example.rpg.data.repository

import com.example.rpg.data.datasource.UserRemoteDataSource
import com.example.rpg.data.model.User
import javax.inject.Inject


class UserRepository @Inject constructor(
    private val remoteDataSource: UserRemoteDataSource
 ) {

    suspend fun createProfile(user: User) {
        return remoteDataSource.createProfile(user)
    }

    suspend fun getProfile(id: String): User? {
        return remoteDataSource.getProfile(id)
    }
}