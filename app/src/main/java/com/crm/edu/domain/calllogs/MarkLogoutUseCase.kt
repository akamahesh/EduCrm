package com.crm.edu.domain.calllogs

import com.crm.edu.data.login.LoginRepository
import javax.inject.Inject

class MarkLogoutUseCase  @Inject constructor(private val loginRepository: LoginRepository) {
    suspend fun execute(): Unit {
        loginRepository.markUserLogout()
    }
}