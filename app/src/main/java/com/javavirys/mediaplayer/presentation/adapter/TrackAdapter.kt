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

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.javavirys.mediaplayer.R
import com.javavirys.mediaplayer.core.entity.Track
import com.javavirys.mediaplayer.util.extension.inflate

class TrackAdapter(
    private val onItemClick: (item: Track) -> Unit
) :
    RecyclerView.Adapter<TrackViewHolder>() {

    private val items = mutableListOf<Track>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = TrackViewHolder(
        parent.inflate(R.layout.view_track_item),
        onItemClick
    )

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    fun addItem(track: Track) {
        val foundItem = items.find { track.id == it.id }
        if (foundItem == null) {
            items.add(track)
            notifyItemInserted(items.lastIndex)
        }
    }
}