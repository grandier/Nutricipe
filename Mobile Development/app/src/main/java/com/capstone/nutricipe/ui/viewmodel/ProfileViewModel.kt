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
import com.capstone.nutricipe.data.remote.model.Profile
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _acceptance = MutableLiveData<Boolean>()
    val acceptance: LiveData<Boolean> = _acceptance

    private val _profile = MutableLiveData<Data?>()
    val profile: LiveData<Data?> = _profile

    init {
        _acceptance.value = false
        if (pref.getToken().asLiveData().value != null) {
            _acceptance.value = true
        }
    }

    fun getProfile(token: String) {
        _isLoading.value = true

        ApiConfig.getApiService().getProfile("Bearer $token")
            .enqueue(object : Callback<Profile> {
                override fun onResponse(call: Call<Profile>, response: Response<Profile>) {
                    _isLoading.value = false

                    if (response.isSuccessful) {
                        if (response.body()?.message.equals("Success")) {
                            val profileData = response.body()?.data
                            if (profileData != null) {
                                val name = profileData.name
                                val email = profileData.email
                                _message.value = response.body()?.message.toString()
                                _acceptance.value = true
                                _profile.value = profileData
                            } else {
                                _message.value = "Invalid profile data"
                                _acceptance.value = false
                            }
                        }
                    } else {
                        val errorBody = response.errorBody()?.string()
                        val errorMessage =
                            if (errorBody != null) JSONObject(errorBody).getString("message") else response.message()
                        _message.value = errorMessage
                        _acceptance.value = false
                    }
                }

                override fun onFailure(call: Call<Profile>, t: Throwable) {
                    _isLoading.value = false
                    _message.value = t.message
                }
            })
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