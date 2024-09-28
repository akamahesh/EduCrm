package com.crm.edu.network

import android.util.Log
import com.crm.edu.data.config.local.pref.ConfigPreferences
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import java.net.URL


class HostSelectionInterceptor(private val configPreferences: ConfigPreferences):Interceptor {
    @Volatile
    private var host: String? = null

    fun getHost():String? {
        if(host == null) {
            host =
                runBlocking { configPreferences.getBaseUrl() }
            if(host?.isNotEmpty() == true){
                host = URL(host).host
                Log.d("HostSelectionInterceptor","host $host") //also setting in provideBaseUrl but for next session
            }
        }
        return host
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val currentHost = getHost()
        if (currentHost != null) {
            try {
                val newUrl = request.url.newBuilder()
                    .host(currentHost)
                    .build()
                request = request.newBuilder().url(newUrl).build()
            }catch (excpetion:IllegalArgumentException){
                Log.e("HostSelectionInterceptor", "intercept: ", excpetion)
            }
        }
        return chain.proceed(request)
    }
}
