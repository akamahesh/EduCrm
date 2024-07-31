package com.crm.edu.data.markAttendance.local

import com.crm.edu.data.login.local.pref.UserPreferences
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val userPreferences: UserPreferences
) {

    suspend fun getDepartmentId(): String? {
        return userPreferences.getDepartmentId()
    }

    suspend fun getReportingId(): String? {
        return userPreferences.getReportingId()
    }
}