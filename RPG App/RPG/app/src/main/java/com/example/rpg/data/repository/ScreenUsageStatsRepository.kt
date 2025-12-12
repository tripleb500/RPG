package com.example.rpg.data.repository

import android.app.usage.UsageStatsManager
import android.util.Log
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
    /**
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
    */

    fun syncToday(childId: String): Flow<Boolean> = flow {
        val success = try {
            val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            val screenTimeMs = localUsage.getDailyScreenTime()
            if(screenTimeMs > 0) {
                Log.d("ScreenUsageRepository", "Uploading screen time for $childId: $screenTimeMs")
                firestoreUsage.uploadDailyUsage(childId, today, screenTimeMs)
                true
            }else{
                Log.w("ScreenUsageRepository", "No screen time recorded for today.")
                false
            }
            //firestoreUsage.uploadDailyUsage(childId, today, screenTimeMs)
            //true
        } catch(e: Exception) {
            Log.e("ScreenUsageRepository", "Failed to upload screentime: ${e.message}", e)
            false
        }
        emit(success)
    }

    fun observeChildUsage(childId: String): Flow<List<ScreenTimeRecord>> {
        return firestoreUsage.observeDailyUsage(childId)
    }

    suspend fun getCurrentDay(childId: String): ScreenTimeRecord? {
        return firestoreUsage.getCurrentDay(childId)
    }
}