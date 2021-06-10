package com.javavirys.mediaplayer.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.javavirys.mediaplayer.data.database.dao.TrackDao
import com.javavirys.mediaplayer.data.database.entity.TrackDbo

@Database(
    entities = [
        TrackDbo::class
    ],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getTrackDao(): TrackDao
}