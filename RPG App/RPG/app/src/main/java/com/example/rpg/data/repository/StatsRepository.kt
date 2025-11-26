package com.example.rpg.data.repository

import com.example.rpg.data.datasource.StatsRemoteDataSource
import com.example.rpg.data.model.Stats
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class StatsRepository @Inject constructor(
    private val statsRemoteDataSource: StatsRemoteDataSource
) {
    suspend fun createStats(userId: String, stats: Stats) =
        statsRemoteDataSource.createStats(userId, stats)

    suspend fun updateStats(userId: String, updates: Map<String, Any>) =
        statsRemoteDataSource.updateStats(userId, updates)

    suspend fun getStats(userId: String) =
        statsRemoteDataSource.getStats(userId)
}