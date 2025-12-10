package com.example.rpg.data.datasource

import android.app.usage.UsageStatsManager
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ScreenUsageStatsRemoteDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val usageStatsManager =
        context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager

    fun getDailyScreenTime(): Long {
        val end = System.currentTimeMillis()
        val start = end - TimeUnit.DAYS.toMillis(1)

        val stats = usageStatsManager.queryUsageStats(
            UsageStatsManager.INTERVAL_DAILY,
            start,
            end
        )

        var totalTime = 0L
        for (usage in stats) {
            totalTime += usage.totalTimeInForeground
        }
        return totalTime
    }
}