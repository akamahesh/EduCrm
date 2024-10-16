package com.crm.edu.ui.compose.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crm.edu.domain.calllogs.GetUserNameDesignation
import com.crm.edu.domain.calllogs.MarkLogoutUseCase
import com.crm.edu.domain.calllogs.NameDesignation
import com.crm.edu.ui.compose.screens.calllogs.CallLogsUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val markLogoutUseCase: MarkLogoutUseCase,
    private val getUserNameDesignation: GetUserNameDesignation,
) : ViewModel() {

    private val _userNameDesignationState =
        MutableStateFlow(NameDesignation("", null))
    val userNameDesignationState: StateFlow<NameDesignation> get() = _userNameDesignationState

    private val _callLogsUiState = MutableStateFlow<CallLogsUIState?>(null)
    internal val callLogsUiState: StateFlow<CallLogsUIState?> get() = _callLogsUiState

    init {
        getUserNameDesignation()
    }

    private fun getUserNameDesignation() {
        viewModelScope.launch {
            _userNameDesignationState.value = getUserNameDesignation.execute()
        }
    }

    fun markLogout() {
        viewModelScope.launch {
            markLogoutUseCase.execute()
            _callLogsUiState.value = CallLogsUIState.moveToLogin
        }
    }

}