package com.internship.filemanager.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.internship.filemanager.data.FileNote

@Database(entities = [ FileNote::class ], version = 1)
abstract class FileDatabase: RoomDatabase() {
    abstract fun fileDao(): FileDao
}