package com.internship.filemanager.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.internship.filemanager.data.FileNote
import com.internship.filemanager.data.FileState
import kotlinx.coroutines.flow.Flow

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

    @Query("SELECT * FROM file WHERE extension = :extension")
    fun getFilesByExtension(extension: String): Flow<List<FileNote>>

    @Query("SELECT * FROM file WHERE fileState = 2")
    fun getNewFiles(): Flow<List<FileNote>>

    @Insert
    suspend fun insertAll(vararg files: FileNote)

    @Insert
    suspend fun insert(file: FileNote)

    @Query("DELETE FROM file")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM file WHERE id = :id")
    suspend fun filesAmountWithId(id: Int): Int

    @Query("SELECT EXISTS(SELECT * FROM file WHERE id = :id)")
    suspend fun rowIsExist(id : Int) : Boolean

    @Query("UPDATE file SET fileState=:fileState WHERE id = :id")
    suspend fun updateFileState(id: Int, fileState: Int)

    @Query("UPDATE file SET fileState=0")
    suspend fun updateFileStateBeforeClose()

    @Query("DELETE FROM file WHERE fileState=0")
    suspend fun deleteOldFiles()
}