package com.capstone.nutricipe.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.capstone.nutricipe.data.local.Session
import kotlinx.coroutines.launch

class DetailViewModel(private val pref: Session) : ViewModel(){

    private val _acceptance = MutableLiveData<Boolean>()
    val acceptance: LiveData<Boolean> = _acceptance

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    init {
        _acceptance.value = false
        if (pref.getToken().asLiveData().value != null) {
            _acceptance.value = true
        }
    }

    fun getToken(): LiveData<String> {
        return pref.getToken().asLiveData()
    }

    fun saveToken(token: String) {
        viewModelScope.launch {
            pref.saveToken(token)
        }
    }
}