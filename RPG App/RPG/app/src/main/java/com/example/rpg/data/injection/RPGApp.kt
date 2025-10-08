package com.example.rpg.data.injection

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Sets up Dagger Hilt to handle dependency injection throughout the app.
 * Marking the Application class with '@HiltAndroidApp' annotation, ensures all components can use injected dependencies/
 */
@HiltAndroidApp
class RPGApp: Application() {}