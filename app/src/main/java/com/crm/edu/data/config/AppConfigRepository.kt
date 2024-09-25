package com.crm.edu.data.config

import com.crm.edu.core.EResult
import com.crm.edu.data.config.local.LocalDataSource
import com.crm.edu.data.config.local.pref.ConfigPreferencesKeys
import com.crm.edu.data.config.remote.api.ConfigApi
import com.crm.edu.data.login.remote.model.ConfigResponseDTO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class AppConfigRepository @Inject constructor(
    private val LocalDataSource: LocalDataSource,
    private val configApi: ConfigApi
) {

    suspend fun getAppConfig(): Flow<EResult<ConfigResponseDTO>> = flow {
        emit(EResult.Loading)
        try {
            val remoteData = configApi.getAppConfig()
            LocalDataSource.saveUserData(getAppConfigMap(remoteData))
            emit(EResult.Success(remoteData))
        } catch (ex: Exception) {
            emit(EResult.Error(ex))
        }
    }.onStart {
        emit(EResult.Loading)
    }.catch { e ->
        emit(EResult.Error(e))
    }


    private fun getAppConfigMap(configResponse: ConfigResponseDTO): Map<String, String> {
        return mapOf(
            ConfigPreferencesKeys.ID.name to configResponse.configData?.id.toString(),
            ConfigPreferencesKeys.COMPANY_NAME.name to configResponse.configData?.companyName.toString(),
            ConfigPreferencesKeys.LOGO.name to configResponse.configData?.logo.toString(),
            ConfigPreferencesKeys.BASE_URL.name to configResponse.configData?.baseUrl.toString(),
            ConfigPreferencesKeys.VERSION.name to configResponse.configData?.version.toString(),
            ConfigPreferencesKeys.CREATED_DATE.name to configResponse.configData?.createdDate.toString(),
            ConfigPreferencesKeys.CREATED_BY.name to configResponse.configData?.createdBy.toString(),
            ConfigPreferencesKeys.UPDATED_AT.name to configResponse.configData?.updatedAt.toString(),
            ConfigPreferencesKeys.UPDATED_BY.name to configResponse.configData?.updatedBy.toString(),
        )
    }
}