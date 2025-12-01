package com.example.rpg.data.model

import com.google.firebase.firestore.DocumentId

/**
 * Data class used to hold data associated with user
 * @DocumentId - annotation from Firebase Firestore, tells Firestore to map this field to document's ID in the Firestore database.
 * username - represents the inputted name by user amongst signing up.
 * familyRole - represents user's role in app; either parent or child.
 * email - represents users inputted email when signing up.
 */

data class User(
    @DocumentId val id: String = "",
    val username: String = "",
    val firstname: String = "",
    val lastname: String = "",
    val familyRole: String = "",
    val email: String = "",
    val childrenIds: List<String> = emptyList(),
    val fcmToken: String? = null,
    val profilePicture: String = ""
)