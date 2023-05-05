package com.internship.filemanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.internship.filemanager.ui.screens.MainScreen
import com.internship.filemanager.ui.theme.FileManagerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FileManagerTheme {
                    MainScreen()
            }
        }
    }
}