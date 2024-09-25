package com.crm.edu.domain.appConfig


import com.crm.edu.core.EResult
import com.crm.edu.data.config.AppConfigRepository
import com.crm.edu.data.login.remote.model.ConfigResponseDTO
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAppConfigUsecase @Inject constructor(private val appConfigRepository: AppConfigRepository) {
    suspend fun execute(): Flow<EResult<ConfigResponseDTO>> {
        return appConfigRepository.getAppConfig()
    }
}