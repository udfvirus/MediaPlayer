package com.javavirys.mediaplayer.data.mapper

import com.javavirys.mediaplayer.core.entity.FileSystemTrack
import com.javavirys.mediaplayer.core.entity.Track
import com.javavirys.mediaplayer.data.database.entity.TrackDbo

class TrackMapper {

    fun toTrack(trackDbo: TrackDbo): Track = FileSystemTrack(trackDbo.name, trackDbo.filePath)
}