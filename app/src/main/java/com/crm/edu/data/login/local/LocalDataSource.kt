package com.crm.edu.data.login.local

import com.crm.edu.data.login.local.pref.UserPreferences
import javax.inject.Inject

class LocalDataSource @Inject constructor(private val userPreferences: UserPreferences) {

    suspend fun saveUserData(map: Map<String, String>) {
        userPreferences.saveUserData(map)
    }

    suspend fun setUserLoggedIn(isLoggedIn: Boolean) {
        userPreferences.setUserLoggedIn(isLoggedIn)
    }
    suspend fun isUserLoggedIn(): Boolean {
        return userPreferences.isUserLoggedIn()
    }
}