package com.internship.filemanager.viewmodel

import android.os.Environment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.internship.filemanager.data.FileNote
import com.internship.filemanager.repository.FileRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.attribute.BasicFileAttributes

class FileViewModel: ViewModel() {
    private val fileRepository = FileRepository.get()

    private val _files: MutableStateFlow<List<FileNote>> = MutableStateFlow(emptyList())
    val files: StateFlow<List<FileNote>>
        get() = _files.asStateFlow()

    fun getNewFiles(): Flow<List<FileNote>> {
        return fileRepository.getNewFiles()
    }

    suspend fun updateFileStateBeforeClose() {
        fileRepository.updateFileStateBeforeClose()
    }

    init {
        viewModelScope.launch {
            _files.value = fileRepository.getFilesSortedByKey()

            Environment.getExternalStorageDirectory().walkTopDown().forEach {
                if (fileRepository.rowIsExist(it.hashCode())) {
                    fileRepository.updateFileState(id = it.hashCode(), fileState = 1)
                } else {
                    fileRepository.insert(
                        FileNote (
                            id = it.hashCode(),
                            name = it.name,
                            space = it.length().toInt(),
                            date = getCreationTime(it.absolutePath),
                            extension = it.extension,
                            path = it.path,
                            fileState = 2,
                        )
                    )
                }
            }
            fileRepository.deleteOldFiles()
        }
    }
}

fun getCreationTime(path: String): Long {
    return Files.readAttributes(Paths.get(path), BasicFileAttributes::class.java).creationTime().toMillis()
}