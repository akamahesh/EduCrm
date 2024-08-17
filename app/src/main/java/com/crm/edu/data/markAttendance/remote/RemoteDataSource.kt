package com.crm.edu.data.markAttendance.remote

import android.util.Log
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val markAttendanceApi: MarkAttendanceApi
) {

    suspend fun getCheckAttendanceData() = markAttendanceApi.fetchAttendanceDetail()

    suspend fun markCheckInOut(
        attendanceTypeId: String,
        departmentId: String?,
        reportingId: String?,
        lat: String,
        long: String
    ): MarkAttendanceResponseDTO {
        val requestBody = mapOf(
            "latitude" to lat,
            "longitude" to long,
            "attendance_type" to attendanceTypeId,
            "department_id" to departmentId,
            "reporting_id" to reportingId,
            "image" to "NA"
        )
        Log.e("EduLog", "markCheckInOut request body : $requestBody")
        return markAttendanceApi.markCheckInOut(requestBody)
    }
}