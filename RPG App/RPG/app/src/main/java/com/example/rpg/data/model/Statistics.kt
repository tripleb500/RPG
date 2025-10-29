package com.example.rpg.data.model

import com.google.firebase.firestore.DocumentId

data class Statistics(
    @DocumentId val id: String = "",
    val questsCompleted: Int = 0,
    val questsAccepted: Int = 0,
    val questsStreak: Int = 0,
    val totalXP: Int = 0,
    val rewardsEarned: List<String> = emptyList()
)