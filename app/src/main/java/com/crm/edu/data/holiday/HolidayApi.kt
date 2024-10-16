package com.crm.edu.data.holiday

import com.crm.edu.data.holiday.remote.HolidayDTO
import retrofit2.http.GET

interface HolidayApi {
    //    @GET("https://95fe-103-119-199-35.ngrok-free.app/holiday.json")
    @GET("holiday_list")
    suspend fun getHolidayData(): HolidayDTO
}
