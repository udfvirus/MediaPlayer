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
package com.javavirys.mediaplayer.data.mapper

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import com.javavirys.mediaplayer.core.entity.Track
import com.javavirys.mediaplayer.data.database.entity.TrackDbo
import com.javavirys.mediaplayer.util.extension.*

class TrackMapper {

    fun toTrack(trackDbo: TrackDbo): Track = Track(
        trackDbo.id,
        trackDbo.name,
        trackDbo.filePath,
        trackDbo.artist
    )

    fun toTrackDbo(track: Track) = TrackDbo(track.id, track.name, track.path, track.artist)

    fun toMediaMetadataCompat(trackDbo: TrackDbo): MediaMetadataCompat =
        MediaMetadataCompat.Builder().apply {
            id = trackDbo.id.toString()
            title = trackDbo.name
            artist = trackDbo.artist
            mediaUri = trackDbo.filePath
//        albumArtUri = MediaPlaybackService.RESOURCE_ROOT_URI +
//                resources.getResourceEntryName(R.drawable.ic_recommended)
            flag = MediaBrowserCompat.MediaItem.FLAG_PLAYABLE
        }.build()
}