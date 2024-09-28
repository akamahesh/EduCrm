package com.crm.edu.domain.login


import com.crm.edu.core.EResult
import com.crm.edu.data.login.LoginRepository
import com.crm.edu.data.login.remote.model.LoginResponseDTO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val loginRepository: LoginRepository) {
    suspend fun execute(username: String, password: String): Flow<EResult<LoginResponseDTO>> {
        return loginRepository.login(username, password).map {
            when (it) {
                is EResult.Error -> it
                EResult.Loading -> it
                is EResult.Success -> {
                    if (it.data.status == 0) {
                        EResult.Error(
                            IllegalArgumentException(
                                it.data.message ?: "Incorrect Details"
                            ), message = it.data.message ?: "Incorrect Details"
                        )
                    } else {
                        EResult.Success(it.data)
                    }
                }

                is EResult.SuccessAndLoading -> it
            }
        }

    }
}