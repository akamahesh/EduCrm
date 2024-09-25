package com.crm.edu.domain.login


import com.crm.edu.core.EResult
import com.crm.edu.data.config.AppConfigRepository
import com.crm.edu.data.login.LoginRepository
import com.crm.edu.data.login.remote.model.LoginResponseDTO
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLogoFromAppConfigUseCase @Inject constructor(private val appConfigRepository: AppConfigRepository) {
    suspend fun execute():String? {
        return appConfigRepository.getLogo()
    }
}