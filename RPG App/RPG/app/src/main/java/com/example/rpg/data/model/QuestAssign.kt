package com.example.rpg.data.model

data class QuestAssign (
    val title : String = "",
    //val assigned : DateTime,
    //val startTime : DateTime,
    //val endTime : DateTime,
    val reward : Int = 0,
    val repeat : Boolean = false,
    val allDay : Boolean = false
) {
}