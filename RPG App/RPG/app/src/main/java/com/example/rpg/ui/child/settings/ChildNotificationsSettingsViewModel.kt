package com.example.rpg.ui.child.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rpg.data.local.SettingsDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChildNotificationsSettingsViewModel @Inject constructor(
    private val settingsDataStore: SettingsDataStore

) : ViewModel()  {
    val remindersEnabled = settingsDataStore.questRemindersEnabled
    fun toggleReminders(enabled: Boolean) = viewModelScope.launch {
        settingsDataStore.setQuestRemindersEnabled(enabled)
    }
}