package com.crm.edu.ui.compose.screens.splash

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crm.edu.core.EResult
import com.crm.edu.data.login.local.pref.UserPreferences
import com.crm.edu.data.login.local.pref.UserPreferencesKeys
import com.crm.edu.domain.appConfig.GetAppConfigUsecase
import com.crm.edu.ui.compose.screens.login.LoginUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.log

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val userPreferences: UserPreferences,
    private val getAppConfigUsecase: GetAppConfigUsecase
) : ViewModel() {


    private val _appConfigDataFetchState = MutableStateFlow<SplashUiState>(SplashUiState.Idle)
    val appConfigDataFetchState: StateFlow<SplashUiState> = _appConfigDataFetchState.asStateFlow()

    private val _isUserLoggedIn = MutableStateFlow<Boolean?>(null)
    val isUserLoggedIn: StateFlow<Boolean?> get() = _isUserLoggedIn


    init {
        viewModelScope.launch {
            Log.d("SplashVMinit", "SplashViewModel getAppConfigUsecase before ")
            val result = getAppConfigUsecase.execute()
            Log.d("SplashVMinit", "SplashViewModel getAppConfigUsecase After ")

            result.collect {
                Log.d("SplashVMinit", " Login result : $it")
                when (it) {
                    is EResult.Loading -> {
                        _appConfigDataFetchState.value = SplashUiState.Loading
                    }

                    is EResult.Success -> {
                        _appConfigDataFetchState.value = SplashUiState.Success(it.data.message)
                        Log.d("SplashVMinit", "SplashViewModel EResult.Success ")
                        moveToNextScreen()
                    }

                    is EResult.Error -> {
                        _appConfigDataFetchState.value =
                            SplashUiState.Error(it.exception.message ?: "Unknown error")
                        Log.d("SplashVMinit", "SplashViewModel EResult.Error ")
                        moveToNextScreen()
                    }

                    else -> {
                        Log.d("SplashVMinit", "SplashViewModel EResult.else ")
                        moveToNextScreen()
                    }
                }
            }
        }
    }


    suspend fun moveToNextScreen() {
        val isUserLoggedIn = userPreferences.isUserLoggedIn()
        Log.d("EduLogs", "User Logged in :  $isUserLoggedIn")
        _isUserLoggedIn.value = isUserLoggedIn
    }
}

sealed interface SplashUiState {
    object Idle : SplashUiState
    object Loading : SplashUiState
    data class Success(val message: String?) : SplashUiState
    data class Error(val message: String) : SplashUiState
}