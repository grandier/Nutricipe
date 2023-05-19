package com.capstone.nutricipe.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.capstone.nutricipe.data.local.Session
import kotlinx.coroutines.launch

class ProfileViewModel(private val pref: Session) :
    ViewModel() {

    fun getToken(): LiveData<String> {
        return pref.getToken().asLiveData()
    }

    fun saveToken(token: String) {
        viewModelScope.launch {
            pref.saveToken(token)
        }
    }

    private fun clearToken() {
        viewModelScope.launch {
            pref.clearToken()
        }
    }

    fun logout() {
        clearToken()
    }
}