package com.internship.filemanager.viewmodel

import android.os.Environment
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.internship.filemanager.data.FileNote
import com.internship.filemanager.data.SortField
import com.internship.filemanager.repository.FileRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.attribute.BasicFileAttributes

class FileViewModel: ViewModel() {
    private val fileRepository = FileRepository.get()

    private val coroutineScope: CoroutineScope = GlobalScope

    private val _files: MutableStateFlow<List<FileNote>> = MutableStateFlow(emptyList())
    val files: StateFlow<List<FileNote>>
        get() = _files.asStateFlow()

    fun getFilesSortedByKey(key: SortField, isAsc: Int = 1): List<FileNote> {
        return fileRepository.getFilesSortedByKey(key, isAsc)
    }

    fun getFilesByExtension(extension: String): Flow<List<FileNote>> {
        return fileRepository.getFilesByExtension(extension)
    }

    fun getNewFiles(): Flow<List<FileNote>> {
        return fileRepository.getNewFiles()
    }

    suspend fun updateFileStateBeforeClose() {
        fileRepository.updateFileStateBeforeClose()
    }

    init {
        viewModelScope.launch {
            Log.d("VIEW MODEL TAG", "VM launched")
            _files.value = fileRepository.getFilesSortedByKey()

//            coroutineScope.launch {
            Environment.getExternalStorageDirectory().walkTopDown().forEach {
                if (fileRepository.rowIsExist(it.hashCode())) {
//                    it.fileState = 1
                    fileRepository.updateFileState(id = it.hashCode(), fileState = 1)
                } else {
//                    it.fileState = 2
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

//                    fileRepository.updateFileState(id = it.hashCode(), fileState = 2)
                }
            }
//            }
//            fileRepository.getNewFiles().collect {
//                if (fileRepository.rowIsExist(it.id)) {
//                    it.fileState = 2
//                    fileRepository.updateFileState(id = it.id, fileState = 2)
//                } else {
//                    it.fileState = 2
//                    fileRepository.insert(it)
//                    fileRepository.updateFileState(id = it.id, fileState = 2)
//                }
//                _files.value = it
//            }

            fileRepository.deleteOldFiles()
//            fileRepository.deleteAllFiles()
//            fileRepository.getFilesSortedByKey(SortField.NAME, 1).collect {
//                _files.value = it
//            }
        }
    }
}

fun getCreationTime(path: String): Long {
    return Files.readAttributes(Paths.get(path), BasicFileAttributes::class.java).creationTime().toMillis()
}