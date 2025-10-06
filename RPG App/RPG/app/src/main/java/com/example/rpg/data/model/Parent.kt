package com.example.rpg.data.model

data class Parent(
    val id : String = "",
    val username: String = "",
    val firstname: String = "",
    val lastname: String = "",
    val age: Int = 0,
    val children: List<Child> = emptyList()
)
