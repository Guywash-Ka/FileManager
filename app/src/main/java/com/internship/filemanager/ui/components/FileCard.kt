package com.internship.filemanager.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.internship.filemanager.R
import java.time.Instant
import java.util.*

@Composable
fun FileCard(
    extension: String,
    name: String,
    space: Int,
    path: String,
    date: Date,
    hash: Int
) {
    Card(modifier = Modifier.padding(bottom = 4.dp).fillMaxWidth(1f), elevation = 4.dp) {
        Row {
            Image(
                painter = painterResource(selectIcon(extension)),
                contentDescription = "File icon",
                modifier = Modifier.width(50.dp).height(100.dp)
            )
            Text(text = "Extension: $extension\nName: $name\nSpace: $space KB\nDate: $date\nPath: $path\nHash: $hash")

        }
    }
}

@Preview(showBackground = true)
@Composable
fun FileCardPreview() {
    FileCard(extension = "mp3", name = "picture", space = 2200, date = Date(Instant.now().toEpochMilli()), path = "/system", hash = 235347612)
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