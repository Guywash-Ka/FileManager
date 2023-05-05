package com.internship.filemanager.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.internship.filemanager.ui.components.FileCard
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
    val allFiles = fileViewModel.files.collectAsState().value
    Column() {
        Row(modifier = modifier.fillMaxWidth(1f), horizontalArrangement = Arrangement.SpaceEvenly) {
            TextButton(onClick = { /*TODO*/ }) {
                Text("Name")
            }
            TextButton(onClick = { /*TODO*/ }) {
                Text("Space")
            }
            TextButton(onClick = { /*TODO*/ }) {
                Text("Date")
            }
            TextButton(onClick = { /*TODO*/ }) {
                Text("Extension")
            }
        }
        LazyColumn(modifier) {
            items(allFiles) { fileElem ->
                FileCard(
                    extension = fileElem.extension,
                    name = fileElem.name,
                    space = fileElem.space / 1024,
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