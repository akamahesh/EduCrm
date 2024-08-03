package com.crm.edu.data.myteam

import com.crm.edu.data.holiday.local.HolidayEntity
import com.crm.edu.data.holiday.remote.HolidayListDTO

fun HolidayListDTO.asEntity() = HolidayEntity(
    holidayName = holidayName.orEmpty(),
    colour = colour.orEmpty(),
    holidayDate = holidayDate.orEmpty(),
    weekDay = weekDay.orEmpty(),
    holidayType = holidayType.orEmpty()
)