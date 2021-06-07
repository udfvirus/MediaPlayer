package com.javavirys.mediaplayer.core.entity

import androidx.annotation.RawRes

data class ResourceTrack(
    override val name: String,
    @RawRes val rawRes: Int
) : Track