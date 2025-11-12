package com.example.rpg.data.repository

import com.example.rpg.data.datasource.StatsRemoteDataSource
import com.example.rpg.data.model.Quest
import com.example.rpg.data.model.Stats
import com.example.rpg.data.model.Status
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class StatsRepository @Inject constructor(
    private val remoteDataSource: StatsRemoteDataSource
) {
    suspend fun createStatsForUser(userId: String) {
        remoteDataSource.createStatsForUser(userId)
    }

    suspend fun incrementQuestsInProgress(userId: String) {
        remoteDataSource.incrementQuestsInProgress(userId)
    }

    suspend fun completeQuest(userId: String) {
        remoteDataSource.completeQuest(userId)
    }

    suspend fun getStats(userId: String): Stats? {
        return remoteDataSource.getStats(userId)
    }

    suspend fun recalculateStats(userId: String, userQuests: List<Quest>) {
        // Count quests by status
        val completed = userQuests.count { it.status == Status.COMPLETED }
        val inProgress = userQuests.count { it.status != Status.COMPLETED }

        // Update Firestore
        remoteDataSource.updateStats(userId, completed, inProgress)
    }
}