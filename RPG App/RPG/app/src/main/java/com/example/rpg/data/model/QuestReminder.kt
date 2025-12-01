package com.example.rpg.data.model

import com.google.firebase.firestore.DocumentId
import java.util.Date

// represent what a Quest Reminder is
data class QuestReminder(
    @DocumentId val id: String = "",
    val questId: String = "",
    val childId: String = "",
    val parentId: String = "",
    val notificationMessage: String = "",
    val createdAt: Date? = null,
)