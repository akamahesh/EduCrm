package com.crm.edu.ui.compose.screens.splash

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crm.edu.data.login.local.pref.UserPreferences
import com.crm.edu.data.login.local.pref.UserPreferencesKeys
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.log

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _isUserLoggedIn = MutableStateFlow<Boolean?>(null)
    val isUserLoggedIn: StateFlow<Boolean?> get() = _isUserLoggedIn


    init {
        viewModelScope.launch {
            val isUserLoggedIn = userPreferences.isUserLoggedIn()
            Log.d("EduLogs", "User Logged in :  $isUserLoggedIn")
            _isUserLoggedIn.value = isUserLoggedIn
        }
    }

}