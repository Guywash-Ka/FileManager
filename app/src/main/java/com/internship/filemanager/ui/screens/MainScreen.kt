package com.internship.filemanager.ui.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.internship.filemanager.data.FilterState
import com.internship.filemanager.data.SortField
import com.internship.filemanager.ui.components.FileCard
import com.internship.filemanager.ui.theme.FileManagerTheme
import com.internship.filemanager.viewmodel.FileViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
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
    val coroutineScope = rememberCoroutineScope()
    val nameState = remember { mutableStateOf(FilterState.NONE) }
    val spaceState = remember { mutableStateOf(FilterState.NONE) }
    val dateState = remember { mutableStateOf(FilterState.NONE) }

    Column() {
        Row(modifier = modifier.fillMaxWidth(1f), horizontalArrangement = Arrangement.SpaceEvenly) {
            TextButton(onClick = {
                if (nameState.value == FilterState.DESCENDING) {
                    filesToShow.value = filesToShow.value.sortedBy { it.name }
                    nameState.value = FilterState.ASCENDING
                } else {
                    filesToShow.value = filesToShow.value.sortedByDescending { it.name }
                    nameState.value = FilterState.DESCENDING
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
                filesToShow.value = filesToShow.value.sortedByDescending { it.extension }
            }) {
                Text("Extension")
            }
        }
        LazyColumn(modifier) {
            items(items = filesToShow.value) { fileElem ->
//                Log.d("File size = ", "${fileElem.space}")
                FileCard(
                    extension = fileElem.extension,
                    name = fileElem.name,
                    space = fileElem.space,
                    date = Date(fileElem.date),
                    path = fileElem.path,
                    hash = fileElem.id,
                    fileState = fileElem.fileState
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