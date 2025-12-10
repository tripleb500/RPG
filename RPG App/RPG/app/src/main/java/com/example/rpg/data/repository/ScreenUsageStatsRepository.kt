package com.example.rpg.data.repository

import android.app.usage.UsageStatsManager
import com.example.rpg.data.datasource.FirestoreScreenUsageRemoteDataSource
import com.example.rpg.data.datasource.ScreenUsageStatsRemoteDataSource
import com.example.rpg.data.model.ScreenTimeRecord
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class ScreenUsageStatsRepository @Inject constructor (
    private val localUsage: ScreenUsageStatsRemoteDataSource,
    private val firestoreUsage: FirestoreScreenUsageRemoteDataSource
) {
    fun syncToday(childId: String): Flow<Boolean> = flow {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val screenTimeMs = localUsage.getDailyScreenTime()

        try {
            firestoreUsage.uploadDailyUsage(childId, today, screenTimeMs)
            emit(true)
        } catch (e: Exception) {
            emit(false)
        }
    }

    fun observeChildUsage(childId: String): Flow<List<ScreenTimeRecord>> {
        return firestoreUsage.observeDailyUsage(childId)
    }
}