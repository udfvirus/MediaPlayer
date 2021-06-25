/*
 * Copyright 2021 Vitaliy Sychov. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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