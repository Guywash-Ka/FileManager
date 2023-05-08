package com.internship.filemanager.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.internship.filemanager.data.FileNote
import kotlinx.coroutines.flow.Flow

@Dao
interface FileDao {
    @Query("SELECT * FROM file WHERE fileState = 2")
    fun getNewFiles(): Flow<List<FileNote>>

    @Insert
    suspend fun insert(file: FileNote)

    @Query("SELECT EXISTS(SELECT * FROM file WHERE id = :id)")
    suspend fun rowIsExist(id : Int) : Boolean

    @Query("UPDATE file SET fileState=:fileState WHERE id = :id")
    suspend fun updateFileState(id: Int, fileState: Int)

    @Query("UPDATE file SET fileState=0")
    suspend fun updateFileStateBeforeClose()

    @Query("DELETE FROM file WHERE fileState=0")
    suspend fun deleteOldFiles()
}