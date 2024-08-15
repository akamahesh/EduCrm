package com.crm.edu.data.leaves.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HolidayListDTO(
    @SerialName("holiday_name") val holidayName: String? = null,
    @SerialName("colour") val colour: String? = null,
    @SerialName("week_day") val weekDay: String? = null,
    @SerialName("holiday_date") val holidayDate: String? = null,
    @SerialName("holiday_type") val holidayType: String? = null
)

@Serializable
data class HolidayTypeDTO(
    @SerialName("name") val name: String? = null,
    @SerialName("colour") val colour: String? = null
)