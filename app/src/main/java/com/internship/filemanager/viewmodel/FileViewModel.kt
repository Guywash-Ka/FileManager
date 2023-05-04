package com.internship.filemanager.viewmodel

import android.os.Environment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File

class FileViewModel: ViewModel() {
    private val _files: MutableStateFlow<List<File>> = MutableStateFlow(emptyList())
    val files: StateFlow<List<File>>
        get() = _files.asStateFlow()

    private fun getAllFiles(): List<File> {
        val path = Environment.getRootDirectory().toString()
        return File(path).walkTopDown().filter { it.isFile }.toList()
    }

    init {
        viewModelScope.launch {
            _files.value = getAllFiles()
        }
    }
}