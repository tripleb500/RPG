package com.example.rpg.data.repository

import com.example.rpg.data.datasource.StatsRemoteDataSource
import com.example.rpg.data.model.Stats
import com.google.firebase.firestore.FieldValue
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class StatsRepository @Inject constructor(
    private val statsRemoteDataSource: StatsRemoteDataSource
) {
    suspend fun createStats(userId: String, stats: Stats) =
        statsRemoteDataSource.createStats(userId, stats)

    suspend fun incrementStats(
        userId: String,
        questsCompletedInc: Int = 0,
        questsStreakInc: Int = 0,
        totalXPInc: Int = 0
    ) {
        val updates = mutableMapOf<String, Any>()
        if (questsCompletedInc != 0) updates["questsCompleted"] = FieldValue.increment(questsCompletedInc.toLong())
        if (questsStreakInc != 0) updates["questsStreak"] = FieldValue.increment(questsStreakInc.toLong())
        if (totalXPInc != 0) updates["totalXP"] = FieldValue.increment(totalXPInc.toLong())

        updateStats(userId, updates)
    }


    suspend fun updateStats(userId: String, updates: Map<String, Any>) =
        statsRemoteDataSource.updateStats(userId, updates)

    suspend fun getStats(userId: String) =
        statsRemoteDataSource.getStats(userId)

    fun getStatsFlow(userIdFlow: Flow<String?>): Flow<Stats> =
        statsRemoteDataSource.getStatsFlow(userIdFlow)
}