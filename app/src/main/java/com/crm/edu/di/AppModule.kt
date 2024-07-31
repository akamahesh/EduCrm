package com.crm.edu.di

import android.content.Context
import androidx.room.Room
import com.crm.edu.core.network.AuthInterceptor
import com.crm.edu.data.AppDatabase
import com.crm.edu.data.holiday.HolidayApi
import com.crm.edu.data.holiday.local.HolidayDao
import com.crm.edu.data.login.local.pref.UserPreferences
import com.crm.edu.data.login.remote.api.LoginApi
import com.crm.edu.data.markAttendance.remote.MarkAttendanceApi
import com.crm.edu.utils.Constants
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private val json = Json {
        ignoreUnknownKeys = true
    }

    @Provides
    @Singleton
    fun provideBaseOkHttpClient(
        authInterceptor: AuthInterceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor,
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthInterceptor(): AuthInterceptor {
        return AuthInterceptor("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJsb2dpbl90b2tlbiI6IjFmYjZiODE4MmQ0NDgwZWRkODc0OGRkMjA1YTM3ZGQwIiwiaWF0IjoxNzIwOTYwNDQ0LCJleHAiOjE3MjYxNDQ0NDR9._u40fM2tsSLWNMYpd65FmduhF-PXJqfLevDrPKXZ9fM")
    }

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY // Choose the level of logging you want
        }
    }


    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(Constants.Endpoints.STAGING_ENDPOINT)
        .client(okHttpClient)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    @Provides
    @Singleton
    fun provideHolidayApi(retrofit: Retrofit): HolidayApi = retrofit.create(HolidayApi::class.java)


    @Provides
    @Singleton
    fun provideMarkAttendanceApi(retrofit: Retrofit): MarkAttendanceApi =
        retrofit.create(MarkAttendanceApi::class.java)

    @Provides
    @Singleton
    fun provideLoginApi(retrofit: Retrofit): LoginApi =
        retrofit.create(LoginApi::class.java)

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "app_db").build()

    @Provides
    @Singleton
    fun provideUserPreference(@ApplicationContext context: Context): UserPreferences =
        UserPreferences(context)

    @Provides
    @Singleton
    fun provideNewsDao(db: AppDatabase): HolidayDao = db.holidayDao()
}
