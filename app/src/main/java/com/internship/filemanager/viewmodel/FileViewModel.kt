package com.internship.filemanager.viewmodel

import android.os.Environment
import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.internship.filemanager.data.FileNote
import com.internship.filemanager.data.SortField
import com.internship.filemanager.repository.FileRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.attribute.BasicFileAttributes
import java.util.*

class FileViewModel: ViewModel() {
    private val fileRepository = FileRepository.get()

    private val _files: MutableStateFlow<List<FileNote>> = MutableStateFlow(emptyList())
    val files: StateFlow<List<FileNote>>
        get() = _files.asStateFlow()

    fun getFilesSortedByKey(key: SortField, isAsc: Int = 1): Flow<List<FileNote>> {
        return fileRepository.getFilesSortedByKey(key, isAsc)
    }

    fun getFilesByExtension(extension: String): Flow<List<FileNote>> {
        return fileRepository.getFilesByExtension(extension)
    }

    init {
        viewModelScope.launch {
            val path = Environment.getExternalStorageDirectory().absolutePath
            val files = File(path)
                .listFiles()
//                .filter { it.isFile }
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
            _files.value = files!!
            files.forEach {
                if (fileRepository.isFileExist(it.id)) {
                    fileRepository.updateFileState(id = it.id, fileState = 1)
                } else {
                    fileRepository.insert(it)
                    fileRepository.updateFileState(id = it.id, fileState = 2)
                }
            }
            fileRepository.deleteOldFiles()
//            fileRepository.getFilesSortedByKey(SortField.NAME, 1).collect {
//                _files.value = it
//            }
        }
    }
}

fun getCreationTime(path: String): Long {
    return Files.readAttributes(Paths.get(path), BasicFileAttributes::class.java).creationTime().toMillis()
}