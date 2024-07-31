package com.crm.edu.data.markAttendance.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MarkAttendanceRequestDTO(
    @SerialName("latitude") val latitude: String,
    @SerialName("longitude") val longitude: String,
    @SerialName("attendance_type") val attendanceType: String,
    @SerialName("department_id") val departmentId: String,
    @SerialName("reporting_id") val reportingId: String
)
