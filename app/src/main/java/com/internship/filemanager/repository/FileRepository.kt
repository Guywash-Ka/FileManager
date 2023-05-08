package com.internship.filemanager.repository

import android.content.Context
import android.os.Environment
import androidx.room.Room
import com.internship.filemanager.data.FileNote
import com.internship.filemanager.data.SortField
import com.internship.filemanager.database.FileDatabase
import com.internship.filemanager.database.migration_1_2
import com.internship.filemanager.viewmodel.getCreationTime
import kotlinx.coroutines.flow.Flow
import java.io.File

private const val DATABASE_NAME = "file-database"

class FileRepository(
    context: Context,
) {
    private val database: FileDatabase = Room
        .databaseBuilder(
            context.applicationContext,
            FileDatabase::class.java,
            DATABASE_NAME
        )
        .addMigrations(migration_1_2)
        .build()

    fun getFilesSortedByKey(key: SortField = SortField.NAME, isAsc: Int = 1): List<FileNote> {
        val path = Environment.getExternalStorageDirectory().absolutePath
        val files = File(path)
            .listFiles()
            ?.map { FileNote(
                id = it.hashCode(),
                name = it.name,
                space = it.length().toInt(),
                date = getCreationTime(it.absolutePath),
                extension = it.extension,
                path = it.path,
                fileState = 1,
            )
            }
            ?: emptyList()
        val resFiles = if (isAsc > 0) {
            when (key) {
                SortField.NAME -> files.sortedBy { it.name }
                SortField.SPACE -> files.sortedBy { it.space }
                else -> files.sortedBy { it.date }
            }
        } else {
            when (key) {
                SortField.NAME -> files.sortedByDescending { it.name }
                SortField.SPACE -> files.sortedByDescending { it.space }
                else -> files.sortedByDescending { it.date }
            }
        }
        return resFiles
    }

    fun getNewFiles(): Flow<List<FileNote>> {
        return database.fileDao().getNewFiles()
    }

    suspend fun rowIsExist(id: Int): Boolean {
        return database.fileDao().rowIsExist(id)
    }

    suspend fun updateFileState(id: Int, fileState: Int) {
        database.fileDao().updateFileState(id = id, fileState = fileState)
    }

    suspend fun updateFileStateBeforeClose() {
        database.fileDao().updateFileStateBeforeClose()
    }

    suspend fun insert(file: FileNote) {
        database.fileDao().insert(file)
    }

    suspend fun deleteOldFiles(){
        database.fileDao().deleteOldFiles()
    }

    companion object {
        private var INSTANCE: FileRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = FileRepository(context)
            }
        }

        fun get(): FileRepository {
            return INSTANCE ?: throw IllegalStateException("FileRepository must be initialized")
        }
    }
}