package com.example.rpg.data.injection

import android.app.Application
import dagger.hilt.android.HiltAndroidApp


/**
 * Set up Dagger Hilt to handle dependency injection throughout the app.
 * Marking the Application Class with '@HiltAndroidApp' annotation, ensures all components can use injected dependencies.
 */

@HiltAndroidApp
class RPGApp : Application()