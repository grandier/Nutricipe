package com.capstone.nutricipe.ui.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPreference(context: Context) {
    private val PREF_NAME = "sharedprefstarted"
    private var sharedPref : SharedPreferences
    private val editor: SharedPreferences.Editor

    init {
        sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        editor = sharedPref.edit()
    }

    fun getString(key: String): String?{
        return sharedPref.getString(key, null)
    }

    fun put (key: String, value: Boolean){
        editor.putBoolean(key, value).apply()
    }

    fun getBoolean(key: String): Boolean{
        return sharedPref.getBoolean(key, false)
    }

}