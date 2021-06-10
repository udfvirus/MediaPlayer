package com.javavirys.mediaplayer.presentation.navigation

import android.content.Context
import android.content.Intent
import androidx.navigation.ActivityNavigator
import com.javavirys.mediaplayer.presentation.screen.MainActivity

class SplashRouter(private val context: Context) {

    private val activityNavigator = ActivityNavigator(context)

    fun navigateToMainScreen() {
        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        activityNavigator.navigate(
            activityNavigator.createDestination().setIntent(intent),
            null,
            null,
            null
        )
    }
}