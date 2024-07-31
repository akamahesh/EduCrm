package com.crm.edu.ui.compose.screens.leaveRequest

import android.content.Context
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crm.edu.core.EResult
import com.crm.edu.data.leaverequest.LeaveRequestRepository
import com.crm.edu.data.leaverequest.remote.LeaveType
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LeaveRequestViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: LeaveRequestRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {


    private val _uiState = MutableStateFlow<UIState>(UIState.Loading)
    val uiState: StateFlow<UIState> get() = _uiState

    var leaveCount = 0
    var isSelectingFromDate = true
    var applyDate = ""


    init {
        getLeaveDetails()
    }

    private fun getLeaveDetails() {
        viewModelScope.launch {
            repository.getLeaveDetails().collect { result ->
                Log.d("EduLogs", "getLeaveDetails: $result")
                when (result) {
                    is EResult.Loading -> {
                        _uiState.value = UIState.Loading
                    }

                    is EResult.Success -> {
                        _uiState.value =
                            UIState.Success(LeaveRequestState().copy(leaveTypes = result.data.leaveType))
                    }

                    is EResult.Error -> {
                        _uiState.value = UIState.Error(result.exception.message.toString())
                    }

                    else -> {

                    }
                }
            }
        }
    }

    fun onFromDateChange(date: String) {
        if (uiState.value is UIState.Success) {
            val currentState = (uiState.value as UIState.Success).data
            val updatedState = currentState.copy(fromDate = date)
            _uiState.value = UIState.Success(updatedState)
        }
    }

    fun onToDateChange(date: String) {
        if (uiState.value is UIState.Success) {
            val currentState = (uiState.value as UIState.Success).data
            val updatedState = currentState.copy(toDate = date)
            _uiState.value = UIState.Success(updatedState)
        }
    }

    fun onLeaveTypeChange(leaveType: String) {
        if (uiState.value is UIState.Success) {
            val currentState = (uiState.value as UIState.Success).data
            val updatedState = currentState.copy(selectedLeaveType = leaveType)
            _uiState.value = UIState.Success(updatedState)
        }
    }

    fun onHalfDayChange(isHalfDay: Boolean) {
        if (uiState.value is UIState.Success) {
            val currentState = (uiState.value as UIState.Success).data
            val updatedState = currentState.copy(halfDay = isHalfDay)
            _uiState.value = UIState.Success(updatedState)
        }
    }

    fun onHalfDayPeriodChange(period: String) {
        if (uiState.value is UIState.Success) {
            val currentState = (uiState.value as UIState.Success).data
            val updatedState = currentState.copy(halfDayPeriod = period)
            _uiState.value = UIState.Success(updatedState)
        }
    }

    fun onReasonChange(reason: String) {
        if (uiState.value is UIState.Success) {
            val currentState = (uiState.value as UIState.Success).data
            val updatedState = currentState.copy(reason = reason)
            _uiState.value = UIState.Success(updatedState)
        }
    }

    fun applyLeave() {
        viewModelScope.launch {
            val leaveTypes: List<LeaveType> = (_uiState.value as UIState.Success).data.leaveTypes
            val selectedLeaveType: String =
                (_uiState.value as UIState.Success).data.selectedLeaveType
            val leaveTypeID = leaveTypes.find { it.name == selectedLeaveType }?.id.toString()

            repository.applyLeaveRequest(leaveTypeID, leaveCount.toString(), applyDate)
                .collect { result ->
                    Log.d("EduLogs", "applyLeave: $result")
                    when (result) {
                        is EResult.Loading -> {
                            _uiState.value = UIState.Loading
                        }

                        is EResult.Success -> {
                            _uiState.value = UIState.Exit(result.data.message.toString())
                        }

                        is EResult.Error -> {
                            _uiState.value = UIState.Error(result.exception.message.toString())
                        }

                        else -> {

                        }
                    }

                }
        }
    }

    fun retry() {
        getLeaveDetails()
    }

}

sealed class UIState {
    object Loading : UIState()
    data class Success(val data: LeaveRequestState) : UIState()
    data class Error(val message: String) : UIState()
    data class Exit(val message: String) : UIState()
}

data class LeaveRequestState(
    val fromDate: String = "",
    val toDate: String = "",
    val leaveTypes: List<LeaveType> = emptyList(),
    val selectedLeaveType: String = "",
    val halfDay: Boolean = false,
    val halfDayPeriod: String = "First Half",
    val reason: String = ""
)