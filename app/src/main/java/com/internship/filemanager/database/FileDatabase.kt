package com.internship.filemanager.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.internship.filemanager.data.FileNote

@Database(entities = [ FileNote::class ], version = 2)
abstract class FileDatabase: RoomDatabase() {
    abstract fun fileDao(): FileDao
}

val migration_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE file ADD COLUMN fileState INTEGER NOT NULL DEFAULT 2")
    }
}