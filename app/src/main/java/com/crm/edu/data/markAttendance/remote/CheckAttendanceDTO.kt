package com.crm.edu.data.markAttendance.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CheckAttendanceDTO(
    @SerialName("attendance_type") val attendanceTypeList: List<AttendanceTypeDTO> = emptyList(),
    @SerialName("button_color") val buttonColor: String? = null,
    @SerialName("button_text") val buttonText: String? = null
)

@Serializable
data class AttendanceTypeDTO(
    @SerialName("id") var id: String? = null,
    @SerialName("name") var name: String? = null,
    @SerialName("status") var status: String? = null,
    @SerialName("approval_status") var approvalStatus: String? = null,
    @SerialName("short") var short: String? = null
)