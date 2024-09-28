package com.crm.edu.di

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.crm.edu.core.network.AuthInterceptor
import com.crm.edu.data.AppDatabase
import com.crm.edu.data.config.local.pref.ConfigPreferences
import com.crm.edu.data.config.remote.api.ConfigApi
import com.crm.edu.data.holiday.HolidayApi
import com.crm.edu.data.holiday.local.HolidayDao
import com.crm.edu.data.leaverequest.remote.LeaveRequestApi
import com.crm.edu.data.leaves.local.LeavesDao
import com.crm.edu.data.leaves.remote.LeavesApi
import com.crm.edu.data.login.local.pref.UserPreferences
import com.crm.edu.data.login.remote.api.LoginApi
import com.crm.edu.data.markAttendance.remote.MarkAttendanceApi
import com.crm.edu.data.myteam.local.MyTeamDao
import com.crm.edu.data.myteam.remote.MyTeamApi
import com.crm.edu.network.HostSelectionInterceptor
import com.crm.edu.utils.Constants
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Named
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
        hostSelectionInterceptor: HostSelectionInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(hostSelectionInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthInterceptor(userPreferences: UserPreferences): AuthInterceptor {
        return AuthInterceptor(userPreferences)
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
    fun provideHostSelectionInterceptor(configPreferences:ConfigPreferences): HostSelectionInterceptor {
        return HostSelectionInterceptor(configPreferences)
    }


    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, @Named("baseUrl") baseUrl: String): Retrofit {

        Log.d("AppModule", "provideRetrofit:: base url $baseUrl") //HostSelectionInterceptor
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    @Provides
    @Named("baseUrl")
    fun provideBaseUrl(configPreferences:ConfigPreferences): String {
        val baseUrl = runBlocking { configPreferences.getBaseUrl() ?: Constants.Endpoints.STAGING_ENDPOINT }
        Log.d("AppModule", " :provideBaseUrl :: base url $baseUrl")
        return baseUrl
    }

    @Provides
    @Singleton
    fun provideHolidayApi(retrofit: Retrofit): HolidayApi = retrofit.create(HolidayApi::class.java)


    @Provides
    @Singleton
    fun provideLeaveRequestApi(retrofit: Retrofit): LeaveRequestApi =
        retrofit.create(LeaveRequestApi::class.java)


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
    fun provideLeavesApi(retrofit: Retrofit): LeavesApi =
        retrofit.create(LeavesApi::class.java)

    @Provides
    @Singleton
    fun provideMyTeamApi(retrofit: Retrofit): MyTeamApi =
        retrofit.create(MyTeamApi::class.java)

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
    fun provideHolidayDayDao(db: AppDatabase): HolidayDao = db.holidayDao()

    @Provides
    @Singleton
    fun provideMyTeamNewsDao(db: AppDatabase): MyTeamDao = db.myTeamDao()

    @Provides
    @Singleton
    fun provideLeavesDao(db: AppDatabase): LeavesDao = db.leaveDao()

    @Provides
    @Singleton
    fun provideAppConfigApi(retrofit: Retrofit): ConfigApi =
        retrofit.create(ConfigApi::class.java)

    @Provides
    @Singleton
    fun provideConfigPreference(@ApplicationContext context: Context): ConfigPreferences =
        ConfigPreferences(context)

}
