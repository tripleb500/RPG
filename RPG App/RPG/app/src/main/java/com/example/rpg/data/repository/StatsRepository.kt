package com.example.rpg.data.repository

import com.example.rpg.data.datasource.StatsRemoteDataSource
import com.example.rpg.data.model.Stats
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class StatsRepository @Inject constructor(
    private val statsRemoteDataSource: StatsRemoteDataSource
) {

    suspend fun createStats(Stats: Stats) = statsRemoteDataSource.createStats(Stats)
    //suspend fun updateStats(statItem: String, newCount: Stats) = statsRemoteDataSource.updateStats(statItem, newCount)


}