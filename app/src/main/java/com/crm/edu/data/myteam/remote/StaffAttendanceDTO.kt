package com.crm.edu.data.myteam.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StaffAttendanceDTO(
    @SerialName("status") val status: Int? = null,
    @SerialName("message") val message: String? = null,
    @SerialName("data") val data: List<StaffDTO> = emptyList(),
)

/**
 * {"time":"","title":"Absent","start":"2024-07-01","colour":"#F4DDE5","designation":"","staffid":"153","staff_name":null}
 */
@Serializable
data class StaffDTO(
    @SerialName("time") val time: String,
    @SerialName("title") val title: String,
    @SerialName("start") val start: String,
    @SerialName("colour") val colour: String,
    @SerialName("designation") val designation: String,
    @SerialName("staff_name") val staffName: String? = null,
    @SerialName("staffid") val staffId: String ,
)

