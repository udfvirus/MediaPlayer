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
package com.javavirys.mediaplayer.data.service

import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.content.ContextCompat
import androidx.media.MediaBrowserServiceCompat
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.ext.cast.CastPlayer
import com.google.android.exoplayer2.ext.cast.SessionAvailabilityListener
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.ext.mediasession.TimelineQueueNavigator
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.google.android.gms.cast.MediaQueueItem
import com.google.android.gms.cast.framework.CastContext
import com.javavirys.mediaplayer.R
import com.javavirys.mediaplayer.data.mapper.MediaMetadataMapper
import com.javavirys.mediaplayer.data.service.listener.MediaPlaybackPreparer
import com.javavirys.mediaplayer.data.service.listener.MediaPlayerEventListener
import com.javavirys.mediaplayer.di.DatabaseFactory
import com.javavirys.mediaplayer.di.getPreferences
import com.javavirys.mediaplayer.util.extension.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import timber.log.Timber

class MediaPlaybackService : MediaBrowserServiceCompat() {

    private lateinit var notificationManager: UampNotificationManager

    private lateinit var currentPlayer: Player

    private var isForegroundService = false

    private lateinit var mediaSessionConnector: MediaSessionConnector

    private var currentPlaylistItems: List<MediaMetadataCompat> = emptyList()

    private val uAmpAudioAttributes = AudioAttributes.Builder()
        .setContentType(C.CONTENT_TYPE_MUSIC)
        .setUsage(C.USAGE_MEDIA)
        .build()

    private val playerListener = MediaPlayerEventListener(
        onShowNotificationForPlayer = { notificationManager.showNotificationForPlayer(currentPlayer) },
        onHideNotificationForPlayer = { notificationManager.hideNotification() },
        ::stopForegroundService,
        ::saveRecentSongToStorage
    )

    private val exoPlayer: ExoPlayer by lazy {
        SimpleExoPlayer.Builder(this).build().apply {
            setAudioAttributes(uAmpAudioAttributes, true)
            setHandleAudioBecomingNoisy(true)
            addListener(playerListener)
        }
    }

    private val castPlayer: CastPlayer? by lazy {
        try {
            val castContext = CastContext.getSharedInstance(this)
            CastPlayer(castContext).apply {
                setSessionAvailabilityListener(MediaCastSessionAvailabilityListener())
                addListener(playerListener)
            }
        } catch (e: Exception) {
            Timber.e(
                """Cast is not available on this device. Exception thrown when attempting to obtain CastContext. ${e.message}"""
            )
            null
        }
    }

    private val dataSourceFactory: DefaultDataSourceFactory by lazy {
        DefaultDataSourceFactory(
            this,
            Util.getUserAgent(this, getString(R.string.app_name)),
            null
        )
    }

    private val serviceJob = SupervisorJob()

    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

    private val mediaStorage by lazy {
        MediaStorage(DatabaseFactory.getDatabaseInstance(applicationContext))
    }

    private val persistentStorage by lazy { PersistentStorage(getPreferences(baseContext)) }

    override fun onCreate() {
        super.onCreate()

        val sessionActivityPendingIntent =
            packageManager?.getLaunchIntentForPackage(packageName)?.let { sessionIntent ->
                PendingIntent.getActivity(this, 0, sessionIntent, 0)
            }

        val mediaSession = MediaSessionCompat(
            this,
            MediaPlaybackService::class.java.simpleName
        ).apply {
            setSessionActivity(sessionActivityPendingIntent)
            isActive = true
        }

        sessionToken = mediaSession.sessionToken

        notificationManager = UampNotificationManager(
            this,
            mediaSession.sessionToken,
            PlayerNotificationListener()
        )

        // Load tracks
        serviceScope.launch {
            mediaStorage.load()
        }

        mediaSessionConnector = MediaSessionConnector(mediaSession)
        mediaSessionConnector.setPlaybackPreparer(
            MediaPlaybackPreparer(
                mediaStorage,
                ::preparePlaylist
            )
        )
        mediaSessionConnector.setQueueNavigator(UampQueueNavigator(mediaSession))

        switchToPlayer(
            previousPlayer = null,
            newPlayer = if (castPlayer?.isCastSessionAvailable == true) castPlayer!! else exoPlayer
        )

        notificationManager.showNotificationForPlayer(currentPlayer)
    }

    private fun switchToPlayer(previousPlayer: Player?, newPlayer: Player) {
        if (previousPlayer == newPlayer) {
            return
        }
        currentPlayer = newPlayer
        if (previousPlayer != null) {
            val playbackState = previousPlayer.playbackState
            if (currentPlaylistItems.isEmpty()) {
                currentPlayer.stop(true)
            } else if (playbackState != Player.STATE_IDLE && playbackState != Player.STATE_ENDED) {
                preparePlaylist(
                    metadataList = currentPlaylistItems,
                    itemToPlay = currentPlaylistItems[previousPlayer.currentWindowIndex],
                    playWhenReady = previousPlayer.playWhenReady,
                    playbackStartPositionMs = previousPlayer.currentPosition
                )
            }
        }
        mediaSessionConnector.setPlayer(newPlayer)
        previousPlayer?.stop(true)
    }

    private fun preparePlaylist(
        metadataList: List<MediaMetadataCompat>,
        itemToPlay: MediaMetadataCompat?,
        playWhenReady: Boolean,
        playbackStartPositionMs: Long
    ) {
        val initialWindowIndex = if (itemToPlay == null) 0 else metadataList.indexOf(itemToPlay)
        currentPlaylistItems = metadataList

        currentPlayer.playWhenReady = playWhenReady
        currentPlayer.stop(/* reset= */ true)
        if (currentPlayer == exoPlayer) {

            val mediaSource = metadataList.toMediaSource(dataSourceFactory)
            exoPlayer.prepare(mediaSource)

            exoPlayer.seekTo(initialWindowIndex, playbackStartPositionMs)
        } else /* currentPlayer == castPlayer */ {
            val items: Array<MediaQueueItem> = metadataList.map {
                it.toMediaQueueItem()
            }.toTypedArray()
            castPlayer!!.loadItems(
                items,
                initialWindowIndex,
                playbackStartPositionMs,
                Player.REPEAT_MODE_OFF
            )
        }
    }

    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot {
        return BrowserRoot(
            getString(R.string.app_name),
            null
        )
    }

    override fun onLoadChildren(
        parentId: String,
        result: Result<List<MediaBrowserCompat.MediaItem>>
    ) {
        when (parentId) {
            ROOT_ID -> sendResultForRootId(result)
            RECENT_ID -> {
                result.sendResult(persistentStorage.loadRecentSong()?.let { song -> listOf(song) })
            }
            else -> result.sendResult(null)
        }
    }

    private fun sendResultForRootId(result: Result<List<MediaBrowserCompat.MediaItem>>) {
        val resultsSent = mediaStorage.whenReady {
            val mediaItems = mutableListOf<MediaBrowserCompat.MediaItem>()

            mediaStorage.getTrackList().forEach { item ->
                mediaItems.add(MediaMetadataMapper.toMediaItem(item))
            }
            result.sendResult(mediaItems)
        }
        if (!resultsSent) {
            result.detach()
        }
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        saveRecentSongToStorage()
        super.onTaskRemoved(rootIntent)
        currentPlayer.stop(true)
    }

    override fun onDestroy() {
        serviceJob.cancel()

        exoPlayer.removeListener(playerListener)
        exoPlayer.release()
    }

    private fun stopForegroundService(removeNotification: Boolean) {
        stopForeground(removeNotification)
        isForegroundService = false
    }

    private fun saveRecentSongToStorage() {
        val description = currentPlaylistItems[currentPlayer.currentWindowIndex].description
        val position = currentPlayer.currentPosition

        serviceScope.launch {
            persistentStorage.saveRecentSong(
                description,
                position
            )
        }
    }


    private inner class PlayerNotificationListener :
        PlayerNotificationManager.NotificationListener {

        override fun onNotificationPosted(
            notificationId: Int,
            notification: Notification,
            ongoing: Boolean
        ) {
            if (ongoing && !isForegroundService) {
                ContextCompat.startForegroundService(
                    applicationContext,
                    Intent(applicationContext, this@MediaPlaybackService.javaClass)
                )

                startForeground(notificationId, notification)
                isForegroundService = true
            }
        }

        override fun onNotificationCancelled(notificationId: Int, dismissedByUser: Boolean) {
            stopForegroundService(true)
            stopSelf()
        }
    }

    private inner class UampQueueNavigator(
        mediaSession: MediaSessionCompat
    ) : TimelineQueueNavigator(mediaSession) {

        override fun getMediaDescription(
            player: Player,
            windowIndex: Int
        ): MediaDescriptionCompat {
            Timber.d("UampQueueNavigator.getMediaDescription.currentPlaylistItems[$windowIndex].description = ${currentPlaylistItems[windowIndex].description}")
            return currentPlaylistItems[windowIndex].description
        }
    }

    private inner class MediaCastSessionAvailabilityListener : SessionAvailabilityListener {

        override fun onCastSessionAvailable() {
            switchToPlayer(currentPlayer, castPlayer!!)
        }

        override fun onCastSessionUnavailable() {
            switchToPlayer(currentPlayer, exoPlayer)
        }
    }

    companion object {

        const val ROOT_ID = "/"

        const val RECENT_ID = "/RECENT_ID"
    }
}