package com.crm.edu.data.myteam

import com.crm.edu.data.myteam.local.StaffAttendanceEntity
import com.crm.edu.data.myteam.remote.StaffDTO

fun StaffDTO.asEntity() = StaffAttendanceEntity(
    time = time,
    title = title,
    start = start,
    colour = colour,
    designation = designation,
    staff_name = staffName
)