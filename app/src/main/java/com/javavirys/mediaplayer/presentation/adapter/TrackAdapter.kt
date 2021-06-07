package com.javavirys.mediaplayer.presentation.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.javavirys.mediaplayer.R
import com.javavirys.mediaplayer.core.entity.Track
import com.javavirys.mediaplayer.util.extension.inflate

class TrackAdapter(private val onItemClick: (item: Track) -> Unit) :
    RecyclerView.Adapter<TrackViewHolder>() {

    var items: List<Track>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = TrackViewHolder(
        parent.inflate(R.layout.view_track_item),
        onItemClick
    )

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        items?.let {
            holder.bind(it[position])
        }
    }

    override fun getItemCount() = items?.size ?: 0
}