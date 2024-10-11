package com.crm.edu.domain.calllogs


import com.crm.edu.data.login.LoginRepository
import javax.inject.Inject

class GetUserNameDesignation  @Inject constructor(private val loginRepository: LoginRepository) {
    suspend fun execute(): NameDesignation {
        return NameDesignation(loginRepository.getUserFullName(), loginRepository.getDesignation())
    }
}

data class NameDesignation(val name: String, val designation: String?)