package com.internship.filemanager

import android.app.Application
import com.internship.filemanager.repository.FileRepository

class FileManagerApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        FileRepository.initialize(this)
    }
}