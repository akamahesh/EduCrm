package com.crm.edu.data.config.local


import com.crm.edu.data.config.local.pref.ConfigPreferences
import javax.inject.Inject

class LocalDataSource @Inject constructor( private val configPreferences: ConfigPreferences) {

    suspend fun  saveUserData(map: Map<String, String>) {
        configPreferences.saveUserData(map)
    }

    suspend fun getVersion(): String? {
        return configPreferences.getVersion()
    }

}