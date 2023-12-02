package com.example.tshare

import android.content.Context
import android.content.SharedPreferences

class preferenceHelper(private val context: Context) {

    private val sharedPref: SharedPreferences by lazy {
        context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    }
    //to save string data
    fun saveString(key: String, value: String) {
        sharedPref.edit().putString(key, value).apply()
    }
    //to save integer data
    fun saveInt(key: String, value: Int) {
        sharedPref.edit().putInt(key, value).apply()
    }
    //to get string data
    fun getString(key: String, defaultValue: String): String {
        return sharedPref.getString(key, defaultValue) ?: defaultValue
    }
    //to get integer data
    fun getInt(key: String, defaultValue: Int): Int {
        return sharedPref.getInt(key, defaultValue)
    }
    //to clear shared preference
    fun clearSharedPreferences() {
        sharedPref.edit().clear().apply()
    }
    //to remove a perticular data from the shared preference
    fun removeString(key: String) {
        sharedPref.edit().remove(key).apply()
    }


}