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
    val date: Long
)
