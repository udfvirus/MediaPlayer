package com.javavirys.mediaplayer.data.database.dao

import androidx.room.*
import com.javavirys.mediaplayer.data.database.entity.TrackDbo
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg trackDbo: TrackDbo)

    @Delete
    suspend fun delete(vararg trackDbo: TrackDbo)

    @Query("SELECT * FROM ${TrackDbo.TABLE_NAME} ORDER BY name")
    fun getAllTracks(): Flow<TrackDbo>

    @Query("SELECT COUNT(*) FROM ${TrackDbo.TABLE_NAME}")
    suspend fun getTracksCount(): Int
}