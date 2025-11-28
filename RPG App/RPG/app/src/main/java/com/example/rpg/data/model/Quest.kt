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
    val repeatType: RepeatType = RepeatType.NONE,
    val repeatInterval: Int = 1,
    val allDay: Boolean = false,
    val assignee: String = User().id,
    val assignedTo: String = User().id,
    val status: Status = Status.INPROGRESS,
    val imageUri : String? = null,
    var imageURL : String = "",
    var submittedImageUrl : String = ""
)

/** Representation of Statuses a quest can have:
 * AVAILABLE - A quest is set by parent where any child in the family can accept the quest.
 * INPROGESS - A quest is assigned to a child, the quest is currently being worked on by the child.
 * PENDING - A quest is submitted by a child and is pending review by the parent for completion.
 * COMPLETED - A quest is seen as completed by the parent.
 * INCOMPLETED - A quest is incomplete due to child unable to complete quest in time or parent believes it a quest was not done correctly.
 */
enum class Status {
    AVAILABLE,
    INPROGRESS,
    PENDING,
    COMPLETED,
    INCOMPLETE
}

// representation of Repeat Days
enum class RepeatType {
    NONE,
    DAY,
    WEEK,
    MONTH,
    YEAR
}

// representation of Reward
enum class Reward {
    //    INGAME,
//    CASH,
    OTHER,
    NONE
}
