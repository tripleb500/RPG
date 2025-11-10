package com.example.rpg.data.repository

import com.example.rpg.data.datasource.StatsRemoteDataSource
import com.example.rpg.data.model.Stats
import jakarta.inject.Inject

class StatsRepository @Inject constructor(
    private val statsRemoteDataSource: StatsRemoteDataSource
) {

    suspend fun createStats(statItem: Stats) = statsRemoteDataSource.createStats(statItem)
    suspend fun getStats(statItem: Stats) = statsRemoteDataSource.getStats(statItems)


}