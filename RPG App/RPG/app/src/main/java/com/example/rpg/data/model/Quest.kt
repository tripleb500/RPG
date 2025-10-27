package com.example.rpg.data.model

import com.google.firebase.firestore.DocumentId
import java.util.Date

// represent what a Quest is
data class Quest(
    @DocumentId val id: String = "",
    val title: String = "",
    val description: String = "",
    val assignDate: Date? = null,
    val deadlineDate: Date? = null,
    val completionDate: Date? = null,
    val rewardAmount: Int = 0,
    val rewardType: Quest_Reward = Quest_Reward.NONE, // TODO
    val repeat: Boolean = false,
    val allDay: Boolean = false,
    val completed: Boolean = false,
    val assignee: String = User().id,
    val assignedTo: String = User().id,
    val status: Quest_Status = Quest_Status.INPROGRESS
)