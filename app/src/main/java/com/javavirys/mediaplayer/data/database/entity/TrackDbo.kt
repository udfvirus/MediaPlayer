package com.javavirys.mediaplayer.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.javavirys.mediaplayer.data.database.entity.TrackDbo.Companion.COLUMN_FILE_NAME
import com.javavirys.mediaplayer.data.database.entity.TrackDbo.Companion.TABLE_NAME

@Entity(
    tableName = TABLE_NAME,
    indices = [Index(value = [COLUMN_FILE_NAME], unique = true)]
)
data class TrackDbo(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    @ColumnInfo(name = COLUMN_FILE_NAME) val filePath: String,
    val name: String,
    val favorite: Boolean = false,
    val bitrate: Int = 0
) {

    companion object {
        const val TABLE_NAME = "tracks"
        const val COLUMN_FILE_NAME = "file_path"
    }
}