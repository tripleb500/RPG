package com.example.rpg.data.injection

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Sets up Firebase dependencies for dependency injection in Android app using Dagger Hilt
 *
 * Provides instances of Firebase services that app needs, specifically:
 * FirebaseAuth - Handles user authentication (sign-up, sign-in, logout, etc).
 * Firebase Firestore - Reads and writes data to Firestore cloud database
 *
 * Allows for Firebase services to be injected wherever we need them.
 */

@Module  // @Module in Dagger, is responsible for providing objects that can be injected into other classes.
@InstallIn(SingletonComponent::class)  // Creates the Firebase services once and shares throughout the entire app.
object FireBaseHiltModule {  // Object Declaration, only one instance of this object exists.
    @Provides fun auth(): FirebaseAuth = Firebase.auth

    @Provides fun firestore(): FirebaseFirestore = Firebase.firestore
}