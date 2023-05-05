package com.internship.filemanager.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.internship.filemanager.data.FileNote
import kotlinx.coroutines.flow.Flow
import java.io.File

@Dao
interface FileDao {
    @Query("SELECT * FROM file ORDER BY " +
            "CASE WHEN :isAsc = 1 THEN name END ASC, " +
            "CASE WHEN :isAsc = 2 THEN name END DESC ")
    fun getFilesSortedByName(isAsc: Int?): Flow<List<FileNote>>

    @Query("SELECT * FROM file ORDER BY " +
            "CASE WHEN :isAsc = 1 THEN space END ASC, " +
            "CASE WHEN :isAsc = 2 THEN space END DESC ")
    fun getFilesSortedBySpace(isAsc: Int?): Flow<List<FileNote>>

    @Query("SELECT * FROM file ORDER BY " +
            "CASE WHEN :isAsc = 1 THEN date END ASC, " +
            "CASE WHEN :isAsc = 2 THEN date END DESC ")
    fun getFilesSortedByDate(isAsc: Int?): Flow<List<FileNote>>

    @Query("SELECT * FROM file ORDER BY " +
            "CASE WHEN :isAsc = 1 THEN extension END ASC, " +
            "CASE WHEN :isAsc = 2 THEN extension END DESC ")
    fun getFilesSortedByExtension(isAsc: Int?): Flow<List<FileNote>>

    @Insert
    suspend fun insertAll(vararg files: FileNote)
}