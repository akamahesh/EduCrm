package com.crm.edu.ui.compose.screens.leaves

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crm.edu.core.EResult
import com.crm.edu.data.leaves.ApproveLeaveData
import com.crm.edu.data.leaves.LeaveData
import com.crm.edu.data.leaves.LeaveRepository
import com.crm.edu.ui.compose.screens.calendar.utils.getCurrentMonthYear
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LeavesViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: LeaveRepository
) : ViewModel() {


    private val teamStatus: String = savedStateHandle["teamStatus"] ?: ""

    private val _uiState = MutableStateFlow<UIState>(UIState.Loading)
    internal val uiState: StateFlow<UIState> get() = _uiState

    val selectedMonth = MutableStateFlow(getCurrentMonthYear().first)
    val selectedYear = MutableStateFlow(getCurrentMonthYear().second)

    private val _leaveApprovalState =
        MutableStateFlow<EResult<ApproveLeaveData>?>(null)
    val leaveApprovalState: StateFlow<EResult<ApproveLeaveData>?> get() = _leaveApprovalState

    init {
        Log.d("EduLogs", "LeavesViewModel teamStatus : $teamStatus")
        fetchLeaveRequests(teamStatus)
    }

    fun fetchLeaveRequests(teamStatus: String) {
        viewModelScope.launch {
            repository.getLeaveData(teamStatus, selectedMonth.value, selectedYear.value)
                .collect { result ->
                Log.d("EduLogs", "fetchLeaveRequests: $result")
                when (result) {
                    is EResult.Loading -> {
                        _uiState.value = UIState.Loading
                    }

                    is EResult.Success -> {
                        _uiState.value = UIState.Success(LeavesDataState(result.data))
                    }

                    is EResult.SuccessAndLoading -> {
                        _uiState.value = UIState.Success(LeavesDataState(result.data))
                    }

                    is EResult.Error -> {
                        _uiState.value = UIState.Error(result.exception.message.toString())
                    }

                    else -> {}
                }
            }
        }
    }

    fun onLeaveApproved(id: String, approvalStatus: String, message: String) {
        viewModelScope.launch {
            repository.approveLeave(id, approvalStatus, message).collect { result ->
                Log.d("EduLogs", "onLeaveApproved: $result")
                _leaveApprovalState.value = result
            }
        }
    }

    fun refreshData() {
        fetchLeaveRequests(teamStatus)
    }

}

internal sealed class UIState {
    object Loading : UIState()
    data class Success(val data: LeavesDataState) : UIState()
    data class Error(val message: String) : UIState()
}

data class LeavesDataState(
    val leaveDataList: List<LeaveData>
)