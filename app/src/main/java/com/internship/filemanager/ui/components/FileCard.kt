package com.internship.filemanager.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.internship.filemanager.R
import com.internship.filemanager.data.FileNote
import com.internship.filemanager.data.FileState
import com.internship.filemanager.viewmodel.getCreationTime
import java.io.File
import java.time.Instant
import java.util.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FileCard(
    modifier: Modifier = Modifier,
    extension: String,
    name: String,
    space: Int,
    path: String,
    date: Date,
    hash: Int,
    fileState: Int,
    filesToShow: MutableState<List<FileNote>>,
    currentPath: MutableState<String>,
) {
    val popularExtensions = listOf("doc", "jpg", "mp3", "pdf", "png", "ppt", "txt", "xls", "xml", "zip")
    val backgroundColor = when (fileState) {
        FileState.NEW.value -> Color.Green.copy(alpha = 0.2f)
        FileState.OLD.value -> Color.White
        else -> Color.Gray.copy(alpha = 0.2f)
    }
    Card(
        modifier = modifier
            .padding(bottom = 4.dp)
            .fillMaxWidth(1f),
        elevation = 4.dp,
        onClick = {
            val file = File(path)
            if (file.isDirectory) {
                currentPath.value = file.path
                filesToShow.value = file
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
                    }?: emptyList()
            }
        }
    ) {
        Row(modifier = modifier.background(backgroundColor)) {
            if (!popularExtensions.contains(extension)) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = modifier.width(50.dp).height(100.dp)
                ) {
                    Image(
                        painter = painterResource(selectIcon(extension)),
                        contentDescription = "File icon",
                        modifier = modifier
                    )
                    Text(
                        modifier = modifier.padding(horizontal = 4.dp),
                        text = extension,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            } else {
                Image(
                    painter = painterResource(selectIcon(extension)),
                    contentDescription = "File icon",
                    modifier = Modifier
                        .width(50.dp)
                        .height(100.dp)
                )
            }
            Text(text = "Extension: $extension\n" +
                    "Name: $name\n" +
                    "Space: ${ styleSpace(space) }\n" +
                    "Date: $date\n" +
                    "Path: $path\n" +
                    "Hash: $hash")
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Preview(showBackground = true)
@Composable
fun FileCardPreview() {
    FileCard(
        extension = "zip",
        name = "picture",
        space = 2200,
        date = Date(Instant.now().toEpochMilli()),
        path = "/system",
        hash = 235347612,
        fileState = FileState.NEW.value,
        filesToShow = mutableStateOf(listOf()),
        currentPath = mutableStateOf("current/path")
    )
}

fun selectIcon(extension: String): Int {
    return when (extension) {
        "" -> R.drawable.folder
        "doc" -> R.drawable.doc
        "jpg" -> R.drawable.jpg
        "mp3" -> R.drawable.mp3
        "pdf" -> R.drawable.pdf
        "png" -> R.drawable.png
        "ppt" -> R.drawable.ppt
        "txt" -> R.drawable.txt
        "xls" -> R.drawable.xls
        "xml" -> R.drawable.xml
        "zip" -> R.drawable.zip
        else -> R.drawable.file
    }
}

fun styleSpace(space: Int): String {
    return when (space) {
        in 0..1023 -> "$space Bytes"
        in 1024..1_048_575 -> "${ space / 1024 } KB"
        in 1_048_576..1_073_741_823 -> "${ space / 1048576 } MB"
        else -> "${space / 1_073_741_824} GB"
    }
}