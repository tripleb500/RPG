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
    val rewardType: Reward = Reward.NONE, // TODO
    val repeat: Boolean = false,
    val allDay: Boolean = false,
    val completed: Boolean = false,
    val assignee: String = User().id,
    val assignedTo: String = User().id,
//    val userFirstName: String = User().firstname,
    val status: Status = Status.INPROGRESS
)

// representation of Status
enum class Status {
    INPROGRESS,
    PENDING,
    COMPLETED,
    INCOMPLETED
}

// representation of Reward
enum class Reward {
    //    INGAME,
//    CASH,
    OTHER,
    NONE
}