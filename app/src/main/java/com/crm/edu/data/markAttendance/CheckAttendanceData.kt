package com.crm.edu.data.markAttendance

data class CheckAttendanceData(
    val attendanceType: List<AttendanceTypeData> = emptyList(),
    val buttonColor: String? = null,
    val buttonText: String? = null
)

data class AttendanceTypeData(
    val id: String? = null,
    val name: String? = null,
    val status: String? = null,
    val approvalStatus: String? = null,
    val short: String? = null
)