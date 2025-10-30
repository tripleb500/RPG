package com.example.rpg.data.repository

import com.example.rpg.data.datasource.StatsRemoteDataSource
import com.example.rpg.data.model.Quest
import com.example.rpg.data.model.Statistics
import jakarta.inject.Inject

class StatsRepository @Inject constructor(
    private val statsRemoteDataSource: StatsRemoteDataSource
) {

    suspend fun create(statItem: Statistics) = statsRemoteDataSource.create(statItem)

}