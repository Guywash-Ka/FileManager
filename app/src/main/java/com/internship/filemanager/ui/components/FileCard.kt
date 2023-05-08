package com.internship.filemanager.ui.components

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import com.internship.filemanager.BuildConfig
import com.internship.filemanager.R
import com.internship.filemanager.data.FileNote
import com.internship.filemanager.data.FileState
import com.internship.filemanager.viewmodel.getCreationTime
import kotlinx.coroutines.Dispatchers
import java.io.File
import java.text.DateFormat
import java.text.DateFormat.getDateInstance
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*


@OptIn(ExperimentalMaterialApi::class, ExperimentalTextApi::class)
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
    val context = LocalContext.current
    val popularExtensions = listOf("doc", "jpg", "mp3", "pdf", "png", "ppt", "txt", "xls", "xml",
        "zip", "docx", "exe", "gif", "xlsx")
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
                    }
                    ?.sortedBy { it.name }
                    ?: emptyList()
            } else {
                try {
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        FileProvider.getUriForFile(
                            context,
                            context.applicationContext.packageName + ".provider",
                            file))
                    intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                    context.startActivity(intent)
                } catch (e: java.lang.Error) {
                    println(e.message)
                }
            }
        }
    ) {
        Row(modifier = modifier.fillMaxWidth(1f), horizontalArrangement = Arrangement.SpaceBetween) {
            Row() {
                if (!popularExtensions.contains(extension)) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = modifier
                            .width(50.dp)
                            .height(60.dp)
                    ) {
                        Image(
                            painter = painterResource(selectIcon(extension)),
                            contentDescription = "File icon",
                            modifier = modifier
                        )
                        Text(
                            modifier = modifier.padding(horizontal = 8.dp),
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
                            .height(60.dp)
                    )
                }
                Column(modifier = modifier.height(60.dp).width(250.dp), verticalArrangement = Arrangement.SpaceBetween) {
                    Text(
                        text = name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(text = SimpleDateFormat("dd.MM.yyyy|hh:mm").format(date), color = Color.Gray)
                }
            }
            IconButton(onClick = {
                val file = File(path)
                try {
                    val uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file)
                    val intent = Intent(Intent.ACTION_SEND)
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    intent.type = "*/*"
                    intent.putExtra(Intent.EXTRA_STREAM, uri)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK;
                    context.startActivity(intent)
                } catch (e: java.lang.Error) {
                    e.printStackTrace()
                }
            }) {
                Icon(
                    Icons.Default.Share,
                    "Share",
                    modifier = modifier
                        .width(40.dp)
                        .height(50.dp)
                        .align(Alignment.CenterVertically),
                    tint = Color.LightGray,
                )
            }
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Preview(showBackground = true)
@Composable
fun FileCardPreview() {
    FileCard(
        extension = "zip",
        name = "PicturePicturePicturePicturePicturePicturePicturePicture",
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
        "docx" -> R.drawable.docx
        "jpg" -> R.drawable.jpg
        "mp3" -> R.drawable.mp3
        "pdf" -> R.drawable.pdf
        "png" -> R.drawable.png
        "ppt" -> R.drawable.ppt
        "txt" -> R.drawable.txt
        "xls" -> R.drawable.xls
        "xml" -> R.drawable.xml
        "zip" -> R.drawable.zip
        "exe" -> R.drawable.exe
        "gif" -> R.drawable.gif
        "xlsx" -> R.drawable.xlsx
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