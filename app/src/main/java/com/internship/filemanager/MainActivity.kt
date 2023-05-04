package com.internship.filemanager

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.internship.filemanager.ui.components.FileCard
import com.internship.filemanager.ui.theme.FileManagerTheme
import com.internship.filemanager.viewmodel.FileViewModel
import java.nio.file.Files
import java.nio.file.attribute.BasicFileAttributes
import java.util.*


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FileManagerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@SuppressLint("UsableSpace")
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
) {
    val fileViewModel = FileViewModel()
    val allFiles = fileViewModel.files.collectAsState().value
    LazyColumn(modifier) {
        items(allFiles) { fileElem ->
            FileCard(
                extension = fileElem.extension,
                name = fileElem.name,
                space = fileElem.usableSpace.toInt() / 1024,
                date = Date(fileElem.lastModified()),
                path = fileElem.absolutePath,
                hash = fileElem.hashCode()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    FileManagerTheme {
        MainScreen()
    }
}