package com.capstone.nutricipe.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.capstone.nutricipe.data.local.Session
import com.capstone.nutricipe.data.remote.api.ApiConfig
import com.capstone.nutricipe.data.remote.model.Register
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel(private val pref: Session) : ViewModel() {

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
    private val _acceptance = MutableLiveData<Boolean>()
    val acceptance: LiveData<Boolean> = _acceptance
    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    init {
        _acceptance.value = false
        if (pref.getToken().asLiveData().value != null) {
            _acceptance.value = true
        }
    }

    fun register(name: String, email: String, password: String) {
        _isLoading.value = true

        ApiConfig.getApiService().register(name, email, password)
            .enqueue(object : Callback<Register> {
                override fun onResponse(call: Call<Register>, response: Response<Register>) {
                    _isLoading.value = false

                    if (response.isSuccessful) {
                        _message.value = response.message()
                        _acceptance.value = true
                    } else {
                        _message.value = response.message()
                        _acceptance.value = false
                    }
                }

                override fun onFailure(call: Call<Register>, t: Throwable) {
                    _isLoading.value = false
                    _message.value = t.message
                }
            })
    }

}