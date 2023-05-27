package com.capstone.nutricipe.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.capstone.nutricipe.data.local.Session
import com.capstone.nutricipe.data.paging.PhotoRepository
import com.capstone.nutricipe.data.remote.model.ResultItem
import kotlinx.coroutines.launch

class MainViewModel(private val pref: Session, private val photoRepository: PhotoRepository) :
    ViewModel() {

    fun getToken(): LiveData<String> {
        return pref.getToken().asLiveData()
    }

    fun saveToken(token: String) {
        viewModelScope.launch {
            pref.saveToken(token)
        }
    }

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
//    private val _listStory = MutableLiveData<List<ListStoryItem>>()
//    val listStory: LiveData<List<ListStoryItem>> = _listStory
    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message
    private val _acceptance = MutableLiveData<Boolean>()
    val acceptance: LiveData<Boolean> = _acceptance

    fun getPhoto(token: String): LiveData<PagingData<ResultItem>> {
        return photoRepository.getPhoto(token).cachedIn(viewModelScope)
    }

}