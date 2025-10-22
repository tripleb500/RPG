package com.example.rpg.data.model
import com.google.firebase.firestore.DocumentId
import java.util.Date

data class Quest(
    @DocumentId val id: String = "",
    val title: String = "",
    val description: String = "",
    val assignDate: Date? = null,
    val deadlineDate: Date? = null,
    val completionDate : Date? = null,
    val rewardAmount : Int = 0,
    val rewardType : Reward = Reward.NONE,
    val repeat : Boolean = false,
    val allDay : Boolean = false,
    val completed : Boolean = false,
    val assignee : String = User().id,
    val assignedTo : String = User().id,
    val status : String = "Pending"
)