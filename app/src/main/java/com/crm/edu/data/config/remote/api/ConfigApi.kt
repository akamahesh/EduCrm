package com.crm.edu.data.config.remote.api

import com.crm.edu.data.login.remote.model.ConfigResponseDTO
import retrofit2.http.GET
import retrofit2.http.Query

interface ConfigApi {

    @GET("app_config")
    suspend fun getAppConfig(@Query("version") version: Int=CONFIG_API_REQUEST_VERSION): ConfigResponseDTO

}

const val CONFIG_API_REQUEST_VERSION = 1