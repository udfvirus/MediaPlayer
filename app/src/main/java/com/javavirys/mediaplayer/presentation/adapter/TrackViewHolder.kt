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