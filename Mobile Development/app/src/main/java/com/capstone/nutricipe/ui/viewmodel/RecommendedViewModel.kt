package com.capstone.nutricipe.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.capstone.nutricipe.data.local.Session
import com.capstone.nutricipe.data.remote.api.ApiConfig
import com.capstone.nutricipe.data.remote.model.DeleteHistory
import com.capstone.nutricipe.data.remote.model.ResultItem
import com.capstone.nutricipe.data.remote.model.UploadedHistory
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
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

    private val _messageDeleted = MutableLiveData<String>()
    val messageDeleted: LiveData<String> = _messageDeleted

    private val _isDeleted = MutableLiveData<Boolean>()
    val isDeleted: LiveData<Boolean> = _isDeleted

    private val _uploaded = MutableLiveData<ResultItem?>()
    val uploaded: LiveData<ResultItem?> = _uploaded

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
        service.enqueue(object : Callback<UploadedHistory> {
            override fun onResponse(call: Call<UploadedHistory>, response: Response<UploadedHistory>) {

                if (response.isSuccessful) {
                    if (response.body()?.message.equals("success")){
                        // Handle the successful response
                        val uploadedData = response.body()?.result
                        if (uploadedData != null) {
                            _message.value = response.body()?.message.toString()
                            _acceptance.value = true
                            _uploaded.value = uploadedData[0]
                            _isLoading.value = false
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

            override fun onFailure(call: Call<UploadedHistory>, t: Throwable) {
                _isLoading.value = false
                // Handle the failure
                val error = t.message
                _message.value = "Failed to retrieve data: $error"
            }
        })
    }

    fun deleteHistory(token: String, idHistory: String) {
        _isDeleted.value = false
        _isLoading.value = false

        val requestBody = RequestBody.create("application/x-www-form-urlencoded".toMediaTypeOrNull(), "idHistory=$idHistory")

        // Example: Making an API call using Retrofit
        ApiConfig.getApiService().deleteHistory("Bearer $token", requestBody)
            .enqueue(object : Callback<DeleteHistory> {
                override fun onResponse(call: Call<DeleteHistory>, response: Response<DeleteHistory>) {
                    if (response.isSuccessful) {

                        Log.e("TAG", "onResponse: ${response.body()?.message}")
                        if (response.body()?.message.equals("Delete Success")) {
                            // Handle the successful response
                            val uploadedData = response.body()
                            if (uploadedData != null) {
                                _messageDeleted.value = response.body()?.message.toString()
                                Log.e("TAG", "onResponse: ${response.body()?.message.toString()}")
                                _acceptance.value = true
                                _isLoading.value = false
                                _isDeleted.value = true
                            } else {
                                _messageDeleted.value = "Invalid data"
                                _acceptance.value = false
                                _isDeleted.value = false
                            }
                            return  // Exit the function after handling the response
                        }
                    }

                    // Handle the error response
                    val errorMessage = response.message()
                    _messageDeleted.value = "Failed to retrieve data: $errorMessage"
                }

                override fun onFailure(call: Call<DeleteHistory>, t: Throwable) {
                    _isLoading.value = false
                    // Handle the failure
                    val error = t.message
                    _messageDeleted.value = "Failed to retrieve data: $error"
                }
            })
    }
}
