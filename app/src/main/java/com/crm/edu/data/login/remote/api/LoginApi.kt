package com.crm.edu.data.login.remote.api

import com.crm.edu.data.login.remote.model.LoginResponseDTO
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface LoginApi {

    @FormUrlEncoded
    @POST("login")
    suspend fun doLoginWithPassword(
        @FieldMap(encoded = false) data: Map<String, String>): LoginResponseDTO

}