package com.internship.filemanager.repository

import android.content.Context
import androidx.room.Room
import com.internship.filemanager.data.FileNote
import com.internship.filemanager.data.SortField
import com.internship.filemanager.database.FileDatabase
import com.internship.filemanager.database.migration_1_2
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import java.io.File

private const val DATABASE_NAME = "file-database"

class FileRepository(
    context: Context,
    private val coroutineScope: CoroutineScope = GlobalScope
) {
    private val database: FileDatabase = Room
        .databaseBuilder(
            context.applicationContext,
            FileDatabase::class.java,
            DATABASE_NAME
        )
        .addMigrations(migration_1_2)
        .build()

    fun getFilesSortedByKey(key: SortField = SortField.NAME, isAsc: Int = 1): Flow<List<FileNote>> {
        return when (key) {
            SortField.NAME -> database.fileDao().getFilesSortedByName(isAsc)
            SortField.SPACE -> database.fileDao().getFilesSortedBySpace(isAsc)
            SortField.DATE -> database.fileDao().getFilesSortedByDate(isAsc)
            SortField.EXTENSION -> database.fileDao().getFilesSortedByExtension(isAsc)
        }
    }

    suspend fun deleteAllFiles() {
        database.fileDao().deleteAll()
    }

    suspend fun insertAllFiles(vararg files: FileNote) {
        database.fileDao().insertAll(*files)
    }

    suspend fun isFileExist(id: Int): Boolean {
        return database.fileDao().filesAmountWithId(id) > 0
    }

    suspend fun updateFileState(id: Int, fileState: Int) {
        database.fileDao().updateFileState(id = id, fileState = fileState)
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