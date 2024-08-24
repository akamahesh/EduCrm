package com.crm.edu.data.myteam.remote

import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val myTeamApi: MyTeamApi
) {

    suspend fun getTeamAttendance(month: String, year: String, teamStatus: String): StaffAttendanceDTO {
        val requestBody = mapOf(
            "month" to month,
            "year" to year,
            "team_status" to teamStatus
        )
        return myTeamApi.getTeamAttendance(requestBody)
    }

    suspend fun getMyAttendanceCalendar(month: String, year: String): StaffAttendanceDTO {
        val requestBody = mapOf(
            "month" to month,
            "year" to year,
        )
        return myTeamApi.getTeamAttendance(requestBody)
    }

    suspend fun getTeamAttendanceCalendar(
        month: String,
        year: String,
        staffId: String
    ): StaffAttendanceDTO {
        val requestBody = mapOf(
            "month" to month,
            "year" to year,
            "team_staff_id" to staffId
        )
        return myTeamApi.getTeamAttendance(requestBody)
    }
}