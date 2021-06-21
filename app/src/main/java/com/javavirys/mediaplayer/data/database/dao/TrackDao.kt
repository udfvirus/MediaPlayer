package com.javavirys.mediaplayer.data.database.dao

import androidx.room.*
import com.javavirys.mediaplayer.data.database.entity.TrackDbo
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(trackDbo: TrackDbo): Long

    @Delete
    suspend fun delete(vararg trackDbo: TrackDbo)

    @Query("SELECT * FROM ${TrackDbo.TABLE_NAME} ORDER BY name")
    fun getAllTracks(): Flow<List<TrackDbo>>

    @Query("SELECT * FROM ${TrackDbo.TABLE_NAME} ORDER BY name")
    suspend fun getTrackList(): List<TrackDbo>

    @Query("SELECT COUNT(*) FROM ${TrackDbo.TABLE_NAME}")
    suspend fun getTracksCount(): Int

    @Query("SELECT * FROM ${TrackDbo.TABLE_NAME} WHERE id = :id")
    suspend fun getTrackById(id: Long): TrackDbo

    @Query("SELECT * FROM ${TrackDbo.TABLE_NAME} WHERE id = (select min(id) from ${TrackDbo.TABLE_NAME} where id > :currentId)")
    suspend fun getNextTrack(currentId: Long): TrackDbo?

    @Query("SELECT * FROM ${TrackDbo.TABLE_NAME} WHERE id = (select max(id) from ${TrackDbo.TABLE_NAME} where id < :currentId)")
    suspend fun getPreviousTrack(currentId: Long): TrackDbo?
}