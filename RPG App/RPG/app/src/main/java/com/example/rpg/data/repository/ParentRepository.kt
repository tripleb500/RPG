package com.example.rpg.data.repository

import com.example.rpg.data.datasource.UserRemoteDataSource
import com.example.rpg.data.model.User
import javax.inject.Inject


class ParentRepository @Inject constructor(
    private val dataSource: UserRemoteDataSource
){
    suspend fun getChildren(parentId: String): List<User> {
        return dataSource.getChildren(parentId) // suspend function
    }
}