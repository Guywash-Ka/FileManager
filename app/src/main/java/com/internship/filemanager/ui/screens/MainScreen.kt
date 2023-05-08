package com.internship.filemanager.ui.screens

import android.annotation.SuppressLint
import android.os.Environment
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.internship.filemanager.data.FileNote
import com.internship.filemanager.ui.components.FileCard
import com.internship.filemanager.ui.components.TopAppBar
import com.internship.filemanager.ui.theme.FileManagerTheme
import com.internship.filemanager.viewmodel.FileViewModel
import kotlinx.coroutines.launch
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.attribute.BasicFileAttributes
import java.util.*

@SuppressLint("UsableSpace")
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    allFiles: State<List<FileNote>>,
    newFiles: List<FileNote>
) {
    val filesToShow = remember { mutableStateOf(allFiles.value) }

    val coroutineScope = rememberCoroutineScope()
    val currentPath = remember { mutableStateOf(Environment.getExternalStorageDirectory().path) }
    Box(modifier = modifier.fillMaxHeight(1f)) {
        Column() {
            TopAppBar(
                modifier = modifier,
                filesToShow = filesToShow,
                currentPath = currentPath,
            )

            Text(
                text = styleCurrentPath(currentPath.value),
                modifier = modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                fontSize = 18.sp,
                color = Color.Gray
            )

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
        FloatingActionButton(
            onClick = {
                      filesToShow.value = newFiles
            },
            modifier = modifier
                .align(Alignment.BottomEnd)
                .padding(8.dp)
        ) {
            Text("Show new files", modifier = Modifier.padding(4.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    FileManagerTheme {
//        MainScreen(fileViewModel = FileViewModel())
    }
}

fun getCreationTime(path: String): Date {
    return Date(Files.readAttributes(Paths.get(path), BasicFileAttributes::class.java).creationTime().toMillis())
}

fun styleCurrentPath(path: String): String {
    return path.replaceFirst(Environment.getExternalStorageDirectory().toString(), "home")
}