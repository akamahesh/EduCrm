package com.crm.edu.data.myteam.remote

import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface MyTeamApi {

    @FormUrlEncoded
    @POST("staff_attendance_list")
    suspend fun getTeamAttendance(@FieldMap(encoded = false) data: Map<String, String?>): StaffAttendanceDTO
}
