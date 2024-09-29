package com.crm.edu.ui.compose.screens.login

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crm.edu.core.EResult
import com.crm.edu.domain.login.GetLogoFromAppConfigUseCase
import com.crm.edu.domain.login.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val loginUseCase: LoginUseCase,
    private val getLogoFromAppConfigUseCase: GetLogoFromAppConfigUseCase
) : ViewModel() {
    private val _loginState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val loginState: StateFlow<LoginUiState> = _loginState.asStateFlow()

    private val _updateLogoImage = MutableStateFlow<String?>(null)
    val updateLogoImage: StateFlow<String?> = _updateLogoImage.asStateFlow()

    private val _showDialogEvent = MutableStateFlow(false)
    val showDialogEvent: StateFlow<Boolean> = _showDialogEvent

    init {
        getLogoFromAppConfig()
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginUiState.Loading
            val result = loginUseCase.execute(username, password)
            result.collect {
                Log.d("EduLogs", " Login result : $it")
                when (it) {
                    is EResult.Loading -> {
                        _loginState.value = LoginUiState.Loading
                    }

                    is EResult.Success -> {
                        _loginState.value = LoginUiState.Success(it.data.message)
                    }

                    is EResult.Error -> {
                        _loginState.value =
                            LoginUiState.Error(it.exception.message ?: "Unknown error")
                        triggerDialog()
                    }

                    else -> {}
                }
            }
        }
    }

    fun getLogoFromAppConfig() {
        viewModelScope.launch {
            _updateLogoImage.value = getLogoFromAppConfigUseCase.execute()
        }
    }

    fun triggerDialog() {
        _showDialogEvent.value = true
    }

    fun onDialogDismissed() {
        _showDialogEvent.value = false
    }
}

sealed interface LoginUiState {
    object Idle : LoginUiState
    object Loading : LoginUiState
    data class Success(val message: String?) : LoginUiState
    data class Error(val message: String) : LoginUiState
}