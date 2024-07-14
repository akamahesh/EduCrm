package com.crm.edu.data.login

import com.crm.edu.data.login.remote.api.LoginService
import com.crm.edu.data.login.remote.model.LoginResponse
import javax.inject.Inject

class LoginRepository @Inject constructor(private val loginService: LoginService) {
    suspend fun login(username: String, password: String): Result<LoginResponse> {
        return try {
            val formData = HashMap<String, String>()
            formData["email"] = username
            formData["password"] = password
            val response = loginService.doLoginWithPassword(formData)
            if (response.isSuccessful) Result.success(response.body()!!)
            else Result.failure(Exception(response.message()))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}