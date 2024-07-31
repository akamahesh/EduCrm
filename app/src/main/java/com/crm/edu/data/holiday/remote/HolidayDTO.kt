package com.crm.edu.data.holiday.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HolidayDTO(
    @SerialName("status") val status: Int? = null,
    @SerialName("message") val message: String? = null,
    @SerialName("holiday_type") val holidayTypeDTO: List<HolidayTypeDTO> = emptyList(),
    @SerialName("holiday_list") val holidayListDTO: List<HolidayListDTO> = emptyList()

)

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