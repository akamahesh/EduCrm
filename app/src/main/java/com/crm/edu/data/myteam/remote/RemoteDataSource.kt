package com.crm.edu.data.myteam.remote

import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val myTeamApi: MyTeamApi
) {

    suspend fun getTeamAttendance(month: String, year: String, teamStatus: String): StaffAttendanceDTO {
        val requestBody = mapOf<String, String?>(
            "month" to month,
            "year" to year,
            "team_status" to teamStatus
        )
        return myTeamApi.getTeamAttendance(requestBody)
    }
}