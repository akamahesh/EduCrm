package com.crm.edu.domain.login


import com.crm.edu.core.EResult
import com.crm.edu.data.login.LoginRepository
import com.crm.edu.data.login.remote.model.LoginResponseDTO
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val loginRepository: LoginRepository) {
    suspend fun execute(username: String, password: String): Flow<EResult<LoginResponseDTO>> {
        return loginRepository.login(username, password)
    }
}