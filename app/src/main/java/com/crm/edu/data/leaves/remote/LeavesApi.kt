package com.crm.edu.data.leaves.remote

import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface LeavesApi {
    @GET("leave_apply_list")
    suspend fun getLeavesData(): LeavesResponseDTO

    @FormUrlEncoded
    @POST("leave_apply_list")
    suspend fun approveLeave(@FieldMap(encoded = false) data: Map<String, String?>): ApproveLeaveResponseDTO
}
