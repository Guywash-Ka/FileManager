package com.internship.filemanager

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.internship.filemanager.data.FileNote
import com.internship.filemanager.ui.screens.MainScreen
import com.internship.filemanager.ui.theme.FileManagerTheme
import com.internship.filemanager.viewmodel.FileViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FileManagerTheme {
                val fileViewModel: FileViewModel by viewModels()
                val allFiles = fileViewModel.files.collectAsState()
                val newFiles = fileViewModel.getNewFiles().collectAsState(emptyList()).value

                FileManagerApp(allFiles = allFiles, newFiles = newFiles)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        val fileViewModel: FileViewModel by viewModels()
        val scope = CoroutineScope(Dispatchers.Default)
        scope.launch {
            fileViewModel.updateFileStateBeforeClose()
        }
    }
}

@Composable
fun FileManagerApp(allFiles: State<List<FileNote>>, newFiles: List<FileNote>) {
    val isPermitted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        Environment.isExternalStorageManager()
    } else
        ContextCompat.checkSelfPermission(LocalContext.current, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED

    if (isPermitted) {
        MainScreen(allFiles = allFiles, newFiles = newFiles)
    } else {
        val context = LocalContext.current
        AlertDialog(
            onDismissRequest = {},
            title = {
                Text("Разрешите доступ к файлам")
                    },
            text = {
                Text("Приложению необходимо иметь доступ ко всем файлам системы. После разрешения перезапустите приложение")
                   },
            buttons = {
                Button(modifier = Modifier.padding(horizontal = 24.dp), onClick = {
                    context.startActivity(Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION))
                })
                { Text("OK")}
            }
        )
    }
}