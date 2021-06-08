package com.javavirys.mediaplayer.di

import androidx.appcompat.app.AppCompatActivity
import com.javavirys.mediaplayer.presentation.navigation.MainRouter
import com.javavirys.mediaplayer.presentation.navigation.SplashRouter

object RouterFactory {

    fun getSplashRouter(activity: AppCompatActivity) = SplashRouter(activity)

    fun getMainRouter(activity: AppCompatActivity) = MainRouter(activity)
}