package com.crm.edu.data.markAttendance.remote

import retrofit2.http.Body
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface MarkAttendanceApi {
    //    @GET("https://95fe-103-119-199-35.ngrok-free.app/holiday.json")
    @GET("check_attendance")
    suspend fun fetchAttendanceDetail(): CheckAttendanceDTO

    @FormUrlEncoded
    @POST("attendance")
    suspend fun markCheckInOut(@FieldMap(encoded = false) data: Map<String, String>): MarkAttendanceResponseDTO
}
