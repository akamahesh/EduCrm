package com.crm.edu.data.markAttendance.remote

import android.util.Log
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val markAttendanceApi: MarkAttendanceApi
) {

    suspend fun getCheckAttendanceData() = markAttendanceApi.fetchAttendanceDetail()

    suspend fun markCheckInOut(attendanceTypeId: String): MarkAttendanceResponseDTO {
        val requestBody = mapOf(
            "latitude" to "23.56765432",
            "longitude" to "24.234564433",
            "attendance_type" to attendanceTypeId,
            "department_id" to "1",
            "reporting_id" to "79"
        )
        Log.e("EduLog", "request body : $requestBody")
        return markAttendanceApi.markCheckInOut(requestBody)
    }
}