package com.capstone.nutricipe.ui.viewmodel

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.capstone.nutricipe.data.local.Session
import com.capstone.nutricipe.data.remote.api.ApiConfig
import com.capstone.nutricipe.data.remote.model.Login
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val pref: Session) : ViewModel() {

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

    fun login(email: String, password: String) {

        _isLoading.value = true

        ApiConfig.getApiService().login(email, password).enqueue(object : Callback<Login> {
            override fun onResponse(call: Call<Login>, response: Response<Login>) {
                _isLoading.value = false

                if (response.isSuccessful) {
                    if (response.body()?.message.equals("Success")) {
                        _message.value = response.message()
                        _acceptance.value = true
                        saveToken(response.body()?.loginResult?.token.toString())
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMessage =
                        if (errorBody != null) JSONObject(errorBody).getString("message") else response.message()
                    _message.value = response.message()
                }
            }

            override fun onFailure(call: Call<Login>, t: Throwable) {
                _isLoading.value = false
                _acceptance.value = false
                _message.value = "Failure"
                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
            }
        })

    }
}