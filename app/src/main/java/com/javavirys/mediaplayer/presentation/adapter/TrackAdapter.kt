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