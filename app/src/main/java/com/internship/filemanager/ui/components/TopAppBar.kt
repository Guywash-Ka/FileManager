package com.internship.filemanager.ui.components

import android.os.Environment
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.internship.filemanager.data.FileNote
import com.internship.filemanager.data.FilterState
import com.internship.filemanager.viewmodel.getCreationTime
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun TopAppBar(
    modifier: Modifier,
    filesToShow: MutableState<List<FileNote>>,
    currentPath: MutableState<String>,
) {
    val coroutineScope = rememberCoroutineScope()
    val nameState = remember { mutableStateOf(FilterState.NONE) }
    val spaceState = remember { mutableStateOf(FilterState.NONE) }
    val dateState = remember { mutableStateOf(FilterState.NONE) }
    val extensionRowState = remember { mutableStateOf(false)}

    Column() {
        Row(modifier = modifier.fillMaxWidth(1f), horizontalArrangement = Arrangement.SpaceEvenly) {
            IconButton(onClick = {
                currentPath.value = getParentPath(currentPath.value)
                filesToShow.value = getFiles(currentPath.value)
            }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Go back")
            }
            TextButton(onClick = {
                coroutineScope.launch {
                    if (nameState.value == FilterState.DESCENDING) {
                        filesToShow.value = filesToShow.value.sortedBy { it.name }
                        nameState.value = FilterState.ASCENDING
                    } else {
                        filesToShow.value = filesToShow.value.sortedByDescending { it.name }
                        nameState.value = FilterState.DESCENDING
                    }
                }

            }) {
                Row() {
                    if (nameState.value == FilterState.ASCENDING) {
                        Icon(Icons.Default.KeyboardArrowUp, "Arrow up")
                    } else if (nameState.value == FilterState.DESCENDING) {
                        Icon(Icons.Default.KeyboardArrowDown, "Arrow down")
                    }
                    Text("Name")
                }
            }
            TextButton(onClick = {
                if (spaceState.value == FilterState.DESCENDING) {
                    filesToShow.value = filesToShow.value.sortedBy { it.space }
                    spaceState.value = FilterState.ASCENDING
                } else {
                    filesToShow.value = filesToShow.value.sortedByDescending { it.space }
                    spaceState.value = FilterState.DESCENDING
                }
            }) {
                Row() {
                    if (spaceState.value == FilterState.ASCENDING) {
                        Icon(Icons.Default.KeyboardArrowUp, "Arrow up")
                    } else if (spaceState.value == FilterState.DESCENDING){
                        Icon(Icons.Default.KeyboardArrowDown, "Arrow down")
                    }
                    Text("Space")
                }
            }
            TextButton(onClick = {
                if (dateState.value == FilterState.DESCENDING) {
                    filesToShow.value = filesToShow.value.sortedBy { it.date }
                    dateState.value = FilterState.ASCENDING
                } else {
                    filesToShow.value = filesToShow.value.sortedByDescending { it.date }
                    dateState.value = FilterState.DESCENDING
                }
            }) {
                Row() {
                    if (dateState.value == FilterState.ASCENDING) {
                        Icon(Icons.Default.KeyboardArrowUp, "Arrow up")
                    } else if (dateState.value == FilterState.DESCENDING) {
                        Icon(Icons.Default.KeyboardArrowDown, "Arrow down")
                    }
                    Text("Date")
                }
            }
            TextButton(onClick = {
                extensionRowState.value = !extensionRowState.value
            }) {
                Text("Extension")
            }
        }
        if (extensionRowState.value) {
            Row(modifier = modifier.fillMaxWidth(1f), horizontalArrangement = Arrangement.SpaceEvenly) {
                TextButton(onClick = {
                    filesToShow.value = filesToShow.value.filter { it.extension == "png" }
                }) {
                    Text("PNG")
                }
                TextButton(onClick = {
                    filesToShow.value = filesToShow.value.filter { it.extension == "pdf" }
                }) {
                    Text("PDF")
                }
                TextButton(onClick = {
                    filesToShow.value = filesToShow.value.filter { it.extension == "doc" }
                }) {
                    Text("DOC")
                }
            }
        }
    }
}

fun getParentPath(path: String): String {
    if (path != Environment.getExternalStorageDirectory().path) {
        return path.substringBeforeLast("/")
    }
    return path
}

fun getFiles(path: String): List<FileNote> {
    return File(path)
        .listFiles()
        ?.map {
            FileNote(
                id = it.hashCode(),
                name = it.name,
                space = it.length().toInt(),
                date = getCreationTime(it.absolutePath),
                extension = it.extension,
                path = it.path,
                fileState = 1,
            )
        } ?: emptyList()
}