package com.crm.edu.data.leaves.remote

import retrofit2.http.GET

interface LeavesApi {
    @GET("leave_apply_list")
    suspend fun getLeavesData(): LeavesResponseDTO
}
