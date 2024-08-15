package com.crm.edu.core.network

import com.crm.edu.data.login.local.pref.UserPreferences
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val userPreferences: UserPreferences
): Interceptor {

    private var jwtToken: String? = null
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest: Request = chain.request()
        if(jwtToken == null){
            jwtToken = runBlocking {
                userPreferences.getJWTtoken()
            }
        }
        val authenticatedRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer $jwtToken")
            .build()
        return chain.proceed(authenticatedRequest)
    }
}