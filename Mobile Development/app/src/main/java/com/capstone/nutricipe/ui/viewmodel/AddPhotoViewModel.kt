package com.capstone.nutricipe.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.capstone.nutricipe.data.local.Session

class AddPhotoViewModel(private val pref: Session) : ViewModel()  {

    fun getToken(): LiveData<String> {
        return pref.getToken().asLiveData()
    }
}