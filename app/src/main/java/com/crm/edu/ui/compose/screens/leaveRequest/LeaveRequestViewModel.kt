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
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class LeaveRequestViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: LeaveRequestRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {


    private val _uiState = MutableStateFlow<UIState>(UIState.Loading)
    internal val uiState: StateFlow<UIState> get() = _uiState

    var isSelectingFromDate = true

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
                        val leaveTypes = result.data.leaveType
                        val selectedLeaveType = leaveTypes.first()
                        _uiState.value =
                            UIState.Success(
                                LeaveRequestState().copy(
                                    leaveTypes = leaveTypes,
                                    selectedLeaveType = selectedLeaveType
                                )
                            )
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

    fun onLeaveTypeChange(leaveType: LeaveType) {
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

    fun onHalfDayPeriodChange(period: Int) {
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
            val selectedLeaveType: LeaveType? =
                (_uiState.value as UIState.Success).data.selectedLeaveType
            val leaveTypeID = selectedLeaveType?.id.toString()
            val fromDate = (uiState.value as UIState.Success).data.fromDate
            val toDate = (uiState.value as UIState.Success).data.toDate
            val isHalfDay: Int = if ((uiState.value as UIState.Success).data.halfDay) 1 else 0
            val halfDayType: Int = ((uiState.value as UIState.Success).data.halfDayPeriod)
            val leaveCount = getDayCount(fromDate, toDate)
            Log.d(
                "EduLogs",
                "applyLeave: $leaveTypeID, $leaveCount, $fromDate, $toDate, $isHalfDay, $halfDayType"
            )
            repository.applyLeaveRequest(leaveTypeID, fromDate, toDate, isHalfDay, halfDayType)
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

    fun getDayCount(fromDate: String, toDate: String): Long {
        // Define the date format
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        // Parse the dates
        val startDate = dateFormat.parse(fromDate)
        val endDate = dateFormat.parse(toDate)

        // Calculate the difference in time (milliseconds)
        val diffInMillis = endDate.time - startDate.time

        // Convert the difference in time to days
        return diffInMillis / (1000 * 60 * 60 * 24)
    }

}

internal sealed class UIState {
    object Loading : UIState()
    data class Success(val data: LeaveRequestState) : UIState()
    data class Error(val message: String) : UIState()
    data class Exit(val message: String) : UIState()
}

data class LeaveRequestState(
    val fromDate: String = "",
    val toDate: String = "",
    val leaveTypes: List<LeaveType> = emptyList(),
    val selectedLeaveType: LeaveType? = null,
    val halfDay: Boolean = false,
    val halfDayPeriod: Int = 1,
    val reason: String = ""
)