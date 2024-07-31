package com.crm.edu.data.markAttendance

import com.crm.edu.data.markAttendance.remote.AttendanceTypeDTO
import com.crm.edu.data.markAttendance.remote.CheckAttendanceDTO

fun CheckAttendanceDTO.asData() = CheckAttendanceData(
    attendanceType = attendanceTypeList.map { it.asData() },
    buttonColor = buttonColor.orEmpty(),
    buttonText = buttonText.orEmpty()

)

fun AttendanceTypeDTO.asData() = AttendanceTypeData(
    id = id.orEmpty(),
    name = name.orEmpty(),
    status = status.orEmpty(),
    approvalStatus = approvalStatus.orEmpty(),
    short = short.orEmpty()
)