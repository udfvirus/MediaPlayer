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
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.javavirys.mediaplayer.R
import com.javavirys.mediaplayer.core.entity.Track

class TrackViewHolder(
    view: View,
    private val onItemClick: (item: Track) -> Unit
) : RecyclerView.ViewHolder(view) {

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

    fun bind(item: Track) {
        itemView.setOnClickListener {
            onItemClick(item)
        }
        nameTextView.text = item.name
    }
}