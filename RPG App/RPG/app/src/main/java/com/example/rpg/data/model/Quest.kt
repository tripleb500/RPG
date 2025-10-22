package com.example.rpg.data.model
import com.google.firebase.firestore.DocumentId
import java.util.Date

data class Quest(
    @DocumentId val id: String = "",
    val title: String = "",
    val description: String = "",
    val assignedAt: com.google.firebase.Timestamp? = null,
    val dueDate: com.google.firebase.Timestamp? = null,
    val completedAt: com.google.firebase.Timestamp? = null,
    val rewardAmount : Int = 0,
    val rewardType : Reward = Reward.NONE,
    val repeat : Boolean = false,
    val allDay : Boolean = false,
    val completed : Boolean = false,
    val assignee : String = Child().id,
)