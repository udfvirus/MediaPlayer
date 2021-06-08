package com.javavirys.mediaplayer.presentation.navigation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ActivityNavigator
import com.javavirys.mediaplayer.presentation.screen.MainActivity

class SplashRouter(private val activity: AppCompatActivity) {

    private val activityNavigator = ActivityNavigator(activity)

    fun navigateToMainScreen() {
        val intent = Intent(activity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        activityNavigator.navigate(
            activityNavigator.createDestination().setIntent(intent),
            null,
            null,
            null
        )
    }
}