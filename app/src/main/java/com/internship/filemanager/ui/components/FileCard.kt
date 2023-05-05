package com.internship.filemanager.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.internship.filemanager.R
import com.internship.filemanager.data.FileState
import java.time.Instant
import java.util.*

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
        elevation = 4.dp
    ) {
        Row(modifier = modifier.background(backgroundColor)) {
            if (!popularExtensions.contains(extension)) {
                Box(contentAlignment = Alignment.Center) {
                    Image(
                        painter = painterResource(selectIcon(extension)),
                        contentDescription = "File icon",
                        modifier = Modifier.width(50.dp).height(100.dp)
                    )
                    Text(text = extension, fontWeight = FontWeight.Bold)
                }
            } else {
                Image(
                    painter = painterResource(selectIcon(extension)),
                    contentDescription = "File icon",
                    modifier = Modifier.width(50.dp).height(100.dp)
                )
            }
            Text(text = "Extension: $extension\nName: $name\nSpace: $space KB\nDate: $date\nPath: $path\nHash: $hash")

        }
    }
}

@Preview(showBackground = true)
@Composable
fun FileCardPreview() {
    FileCard(extension = "zip", name = "picture", space = 2200, date = Date(Instant.now().toEpochMilli()), path = "/system", hash = 235347612, fileState = FileState.NEW.value)
}

fun selectIcon(extension: String): Int {
    return when (extension) {
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