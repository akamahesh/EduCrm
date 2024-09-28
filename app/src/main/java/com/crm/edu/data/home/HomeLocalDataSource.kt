package com.crm.edu.data.home

import com.crm.edu.data.login.local.pref.UserPreferences
import javax.inject.Inject

class HomeLocalDataSource @Inject constructor(
    private val userPreferences: UserPreferences
) {

    suspend fun getCallStatus(): String? {
        return userPreferences.getCallStatus()
    }

}