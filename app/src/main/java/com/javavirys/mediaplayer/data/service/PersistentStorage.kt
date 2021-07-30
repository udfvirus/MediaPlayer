/*
 * Copyright 2021 Vitaliy Sychov
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

package com.javavirys.mediaplayer.data.service

import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PersistentStorage(private val preferences: SharedPreferences) {

    fun loadRecentSong(): MediaBrowserCompat.MediaItem? {
        val mediaId = preferences.getString(RECENT_SONG_MEDIA_ID_KEY, null)
        return if (mediaId == null) {
            null
        } else {
            val extras = Bundle().also {
                val position = preferences.getLong(RECENT_SONG_POSITION_KEY, 0L)
                it.putLong(MEDIA_DESCRIPTION_EXTRAS_START_PLAYBACK_POSITION_MS, position)
            }

            MediaBrowserCompat.MediaItem(
                MediaDescriptionCompat.Builder()
                    .setMediaId(mediaId.toString())
                    .setTitle(preferences.getString(RECENT_SONG_TITLE_KEY, ""))
                    .setSubtitle(preferences.getString(RECENT_SONG_SUBTITLE_KEY, ""))
                    .setExtras(extras)
                    .build(), MediaBrowserCompat.MediaItem.FLAG_PLAYABLE
            )
        }
    }

    suspend fun saveRecentSong(description: MediaDescriptionCompat, position: Long) =
        withContext(Dispatchers.IO) {
            preferences.edit()
                .putString(RECENT_SONG_MEDIA_ID_KEY, description.mediaId)
                .putString(RECENT_SONG_TITLE_KEY, description.title.toString())
                .putString(RECENT_SONG_SUBTITLE_KEY, description.subtitle.toString())
                .putLong(RECENT_SONG_POSITION_KEY, position)
                .apply()
        }

    companion object {

        const val MEDIA_DESCRIPTION_EXTRAS_START_PLAYBACK_POSITION_MS = "playback_start_position_ms"

        private const val RECENT_SONG_MEDIA_ID_KEY = "recent_song_media_id"

        private const val RECENT_SONG_TITLE_KEY = "recent_song_title"

        private const val RECENT_SONG_SUBTITLE_KEY = "recent_song_subtitle"

        private const val RECENT_SONG_ICON_URI_KEY = "recent_song_icon_uri"

        private const val RECENT_SONG_POSITION_KEY = "recent_song_position"
    }
}