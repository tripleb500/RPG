@file:OptIn(ExperimentalCoroutinesApi::class)
package com.example.rpg.data.local

import kotlinx.coroutines.ExperimentalCoroutinesApi
import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore("settings")

@Singleton
class SettingsDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val QUEST_REMINDERS_ENABLED = booleanPreferencesKey("quest_reminders_enabled")

    val questRemindersEnabled = context.dataStore.data.map { prefs ->
        prefs[QUEST_REMINDERS_ENABLED] ?: true
    }

    suspend fun setQuestRemindersEnabled(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[QUEST_REMINDERS_ENABLED] = enabled
        }
    }

}