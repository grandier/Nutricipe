package com.capstone.nutricipe.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.capstone.nutricipe.data.local.Session
import com.capstone.nutricipe.data.remote.api.ApiConfig
import com.capstone.nutricipe.data.remote.model.Data
import com.capstone.nutricipe.data.remote.model.GetUploaded
import com.capstone.nutricipe.data.remote.model.Temp
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecommendedViewModel(private val pref: Session) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _acceptance = MutableLiveData<Boolean>()
    val acceptance: LiveData<Boolean> = _acceptance

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _uploaded = MutableLiveData<Temp?>()
    val uploaded: LiveData<Temp?> = _uploaded

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

    fun getUploaded(token: String, idHistory: String) {
        _isLoading.value = true

        // Example: Making an API call using Retrofit
        val service = ApiConfig.getApiService().getUploaded("Bearer $token", idHistory)
        service.enqueue(object : Callback<GetUploaded> {
            override fun onResponse(call: Call<GetUploaded>, response: Response<GetUploaded>) {
                _isLoading.value = false
                Log.e("TAG", "onResponse: ${response.body()?.temp}")

                if (response.isSuccessful) {
                    if (response.body()?.message.equals("success")){
                        // Handle the successful response
                        val uploadedData = response.body()?.temp
                        if (uploadedData != null) {
                            _message.value = response.body()?.message.toString()
                            _acceptance.value = true
                            _uploaded.value = uploadedData
                            Log.e("TAG", "nyoba: $uploadedData")
                        } else {
                            _message.value = "Invalid data"
                            _acceptance.value = false
                        }
                        _message.value = "Data retrieved successfully"
                    }
                } else {
                    // Handle the error response
                    val errorMessage = response.message()

                    _message.value = "Failed to retrieve data: $errorMessage"
                }
            }

            override fun onFailure(call: Call<GetUploaded>, t: Throwable) {
                _isLoading.value = false
                // Handle the failure
                val error = t.message
                _message.value = "Failed to retrieve data: $error"
            }
        })
    }
}
