package com.javavirys.mediaplayer.di

import android.content.Context
import androidx.room.Room
import com.javavirys.mediaplayer.data.database.AppDatabase

object DatabaseFactory {

    private var appDatabase: AppDatabase? = null

    fun getDatabaseInstance(applicationContext: Context): AppDatabase {
        if (appDatabase == null) {
            appDatabase = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java, "media-player"
            ).build()
        }

        return appDatabase!!
    }
}