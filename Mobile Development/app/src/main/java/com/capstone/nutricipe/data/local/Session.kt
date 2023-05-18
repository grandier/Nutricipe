package com.capstone.nutricipe.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class Session private constructor(private val dataStore: DataStore<Preferences>) {

    private val userTokenKey = stringPreferencesKey("user_token")

    fun getToken(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[userTokenKey] ?: ""
        }
    }

    suspend fun saveToken(token: String) {
        dataStore.edit { preferences ->
            preferences[userTokenKey] = token
        }
    }

    suspend fun clearToken() {
        dataStore.edit { preferences ->
            preferences.remove(userTokenKey)
        }
    }

    companion object {
        @Volatile
        private var instance: Session? = null

        fun getInstance(dataStore: DataStore<Preferences>): Session {
            return instance ?: synchronized(this) {
                instance ?: Session(dataStore).also { instance = it }
            }
        }
    }
}