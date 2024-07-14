package com.crm.edu.data.login.remote.api

import com.crm.edu.data.login.remote.model.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import javax.inject.Singleton

@Singleton
interface LoginService {
    @FormUrlEncoded
    @POST("https://leadsimulator.com/triangle/external/login")
    suspend fun doLoginWithPassword(
        @FieldMap(encoded = false) data: Map<String, String>): Response<LoginResponse>
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
}


data class LoginRequest(val userName: String, val password: String)