package com.internship.filemanager.viewmodel

import android.os.Environment
import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.internship.filemanager.data.FileNote
import com.internship.filemanager.data.SortField
import com.internship.filemanager.repository.FileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
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

    private fun getFilesSortedByKey(key: SortField) {

    }

    init {
        viewModelScope.launch {
            val path = Environment.getRootDirectory().toString()
            val files = File(path)
                .walkTopDown()
                .filter { it.isFile }
                .map { FileNote(
                    id = it.hashCode(),
                    name = it.name,
                    space = it.usableSpace.toInt(),
                    date = getCreationTime(it.absolutePath),
                    extension = it.extension,
                    path = it.path,
                    fileState = 1,
                )
                }
            _files.value = files.toList().sortedBy { it.name }
            files.forEach {
                if (fileRepository.isFileExist(it.id)) {
                    fileRepository.updateFileState(id = it.id, fileState = 1)
                } else {
                    fileRepository.insert(it)
                    fileRepository.updateFileState(id = it.id, fileState = 2)
                }
            }
            fileRepository.deleteOldFiles()
            fileRepository.getFilesSortedByKey(SortField.NAME).collect {
                _files.value = it
            }
        }
    }
}

fun getCreationTime(path: String): Long {
    return Files.readAttributes(Paths.get(path), BasicFileAttributes::class.java).creationTime().toMillis()
}