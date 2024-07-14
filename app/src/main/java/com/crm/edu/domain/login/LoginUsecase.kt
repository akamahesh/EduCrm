package com.crm.edu.domain.login


import com.crm.edu.data.login.LoginRepository
import com.crm.edu.data.login.remote.model.LoginResponse
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val loginRepository: LoginRepository) {
    suspend fun execute(username: String, password: String): Result<LoginResponse> {
        return loginRepository.login(username, password)
    }
}