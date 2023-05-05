package com.internship.filemanager.repository

import android.content.Context
import androidx.room.Room
import com.internship.filemanager.data.FileNote
import com.internship.filemanager.data.SortField
import com.internship.filemanager.database.FileDatabase
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
        .build()

    fun getFilesSortedByKey(key: SortField, isAsc: Int = 1): Flow<List<FileNote>> {
        return when (key) {
            SortField.NAME -> database.fileDao().getFilesSortedByName(isAsc)
            SortField.SPACE -> database.fileDao().getFilesSortedBySpace(isAsc)
            SortField.DATE -> database.fileDao().getFilesSortedByDate(isAsc)
            SortField.EXTENSION -> database.fileDao().getFilesSortedByExtension(isAsc)
        }
    }

    suspend fun insertAllFiles(vararg files: FileNote) {
        database.fileDao().insertAll(*files)
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