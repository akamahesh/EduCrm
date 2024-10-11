package com.crm.edu.ui.compose.screens.calllogs


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crm.edu.core.EResult
import com.crm.edu.data.leaves.ApproveLeaveData
import com.crm.edu.domain.calllogs.GetUserNameDesignation
import com.crm.edu.domain.calllogs.MarkLogoutUseCase
import com.crm.edu.domain.calllogs.NameDesignation
import com.crm.edu.domain.login.GetLogoFromAppConfigUseCase
import com.crm.edu.ui.compose.screens.calendar.tryouts.CalendarUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CallLogsViewModel @Inject constructor(
    private val markLogoutUseCase: MarkLogoutUseCase,
    private val getUserNameDesignation : GetUserNameDesignation,
    private val getLogoFromAppConfigUseCase: GetLogoFromAppConfigUseCase
) : ViewModel() {

    private val _userNameDesignationState =
        MutableStateFlow<NameDesignation>(NameDesignation("", null))
    val userNameDesignationState: StateFlow<NameDesignation> get() = _userNameDesignationState

    private val _callLogsUiState = MutableStateFlow<CallLogsUIState?>(null)
    internal val callLogsUiState: StateFlow<CallLogsUIState?> get() = _callLogsUiState

    private val _updateLogoImage = MutableStateFlow<String?>(null)
    val updateLogoImage: StateFlow<String?> = _updateLogoImage.asStateFlow()

    init {
        getUserNameDesignation()
        getLogoFromAppConfig()
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

    private fun getLogoFromAppConfig() {
        viewModelScope.launch {
            _updateLogoImage.value = getLogoFromAppConfigUseCase.execute()
        }
    }
}

internal sealed class CallLogsUIState {
    data object moveToLogin : CallLogsUIState()
}