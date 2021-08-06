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

package com.javavirys.mediaplayer.presentation.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.javavirys.mediaplayer.R
import com.javavirys.mediaplayer.core.entity.PlayingStatus
import com.javavirys.mediaplayer.core.entity.Track

class TrackViewHolder(
    view: View,
    private val onItemClick: (item: Track) -> Unit,
    private val onItemLongClick: (item: Track) -> Unit
) : RecyclerView.ViewHolder(view) {

    private var currPlayingStatus: PlayingStatus? = null

    private val nameTextView by lazy {
        ViewCompat.requireViewById<TextView>(
            itemView,
            R.id.nameTextView
        )
    }

    private val logoImageView by lazy {
        ViewCompat.requireViewById<ImageView>(
            itemView,
            R.id.logoImageView
        )
    }

    private val circleCheckBox by lazy {
        ViewCompat.requireViewById<AppCompatCheckBox>(
            itemView,
            R.id.circleCheckBox
        )
    }

    private val playVisualization by lazy {
        ViewCompat.requireViewById<LottieAnimationView>(
            itemView,
            R.id.playVisualization
        )
    }

    fun bind(item: Track) {
        itemView.setOnClickListener { onItemClick(item) }
        itemView.setOnLongClickListener {
            onItemLongClick(item)
            true
        }
        nameTextView.text = item.name
        circleCheckBox.isChecked = item.selected

        processPlayingStatus(item.playingStatus)
    }

    private fun processPlayingStatus(playingStatus: PlayingStatus) {
        if (currPlayingStatus == playingStatus) return
        currPlayingStatus = playingStatus

        playVisualization.isVisible = playingStatus != PlayingStatus.STATE_STOP
        when (playingStatus) {
            PlayingStatus.STATE_PLAYING -> playVisualization.playAnimation()
            PlayingStatus.STATE_PAUSED -> playVisualization.pauseAnimation()
            PlayingStatus.STATE_STOP -> playVisualization.cancelAnimation()
        }
    }
}