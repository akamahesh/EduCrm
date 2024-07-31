package com.crm.edu.data.leaverequest.remote

import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface LeaveRequestApi {

    @GET("check_leave")
    suspend fun fetchLeaveDetail(): LeaveRequestDetailDTO

    @FormUrlEncoded
    @POST("leave_apply")
    suspend fun leaveRequest(@FieldMap(encoded = false) data: Map<String, String?>): LeaveRequestResponseDTO
}
