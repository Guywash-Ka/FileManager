package com.internship.filemanager.ui.screens

import android.annotation.SuppressLint
import android.os.Environment
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.internship.filemanager.ui.components.FileCard
import com.internship.filemanager.ui.components.TopAppBar
import com.internship.filemanager.ui.theme.FileManagerTheme
import com.internship.filemanager.viewmodel.FileViewModel
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.attribute.BasicFileAttributes
import java.util.*

@SuppressLint("UsableSpace")
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    fileViewModel: FileViewModel = FileViewModel()
) {
    val allFiles = fileViewModel.files.collectAsState()
    val filesToShow = remember { mutableStateOf(allFiles.value) }
    
    val currentPath = remember { mutableStateOf(Environment.getExternalStorageDirectory().path) }

    Column() {
        TopAppBar(
            modifier = modifier,
            filesToShow = filesToShow,
            currentPath = currentPath,
        )

        Text(text = currentPath.value)
        
        LazyColumn(modifier) {
            items(items = filesToShow.value) { fileElem ->
                FileCard(
                    extension = fileElem.extension,
                    name = fileElem.name,
                    space = fileElem.space,
                    date = Date(fileElem.date),
                    path = fileElem.path,
                    hash = fileElem.id,
                    fileState = fileElem.fileState,
                    filesToShow = filesToShow,
                    currentPath = currentPath,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    FileManagerTheme {
        MainScreen(fileViewModel = FileViewModel())
    }
}

fun getCreationTime(path: String): Date {
    return Date(Files.readAttributes(Paths.get(path), BasicFileAttributes::class.java).creationTime().toMillis())
}