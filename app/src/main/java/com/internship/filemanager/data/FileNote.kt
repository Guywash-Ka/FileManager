package com.internship.filemanager.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "file")
data class FileNote(
    @PrimaryKey val id: Int,
    val name: String,
    val space: Int,
    val path: String,
    val extension: String,
    val date: Long,
    var fileState: Int // 0 - to delete; 1 - exist, but old; 2 - exist, new
)
