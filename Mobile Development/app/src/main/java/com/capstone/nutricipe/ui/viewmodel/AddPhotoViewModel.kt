package com.capstone.nutricipe.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.capstone.nutricipe.data.local.Session

class AddPhotoViewModel(private val pref: Session) : ViewModel()  {

    private var file: String? = null

    fun getFile(): String? {
        return file
    }

    fun setFile(file: String?) {
        this.file = file
    }

    fun getToken(): LiveData<String> {
        return pref.getToken().asLiveData()
    }
}