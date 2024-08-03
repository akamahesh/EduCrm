package com.crm.edu.data.myteam.remote

import com.crm.edu.data.holiday.HolidayApi
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val holidayApi: HolidayApi
) {

    suspend fun getHolidayData() = holidayApi.getHolidayData()
}