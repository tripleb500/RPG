package com.example.rpg.ui.child.home

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import com.example.rpg.ui.child.quest.Quest

class ChildHomeViewModel : ViewModel() {

    private val _quests = mutableStateListOf(
        Quest("Dishes", "Game Time", 25),
        Quest("HW", "5 Dollars", 25),
        Quest("Trash", "50 xp", 19)
    )

    val quests: SnapshotStateList<Quest> = _quests
}