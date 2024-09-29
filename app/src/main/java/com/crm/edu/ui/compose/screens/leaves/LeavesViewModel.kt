package com.crm.edu.ui.compose.screens.leaves

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crm.edu.core.EResult
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

    private val _LeavesDialogState = MutableStateFlow<LeavesDialogState?>(null)
    internal val leavesDialogState: StateFlow<LeavesDialogState?> get() = _LeavesDialogState

    val selectedMonth = MutableStateFlow(getCurrentMonthYear().first)
    val selectedYear = MutableStateFlow(getCurrentMonthYear().second)

    init {
        Log.d("EduLogs", "LeavesViewModel teamStatus : $teamStatus")
        fetchLeaveRequests(teamStatus)
    }

    private fun fetchLeaveRequests(teamStatus: String) {
        viewModelScope.launch {
            repository.getLeaveData(teamStatus, selectedMonth.value + 1, selectedYear.value)
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
                when (result) {
                    is EResult.Loading -> {
                        _LeavesDialogState.value = LeavesDialogState.Loading
                    }

                    is EResult.Success -> {
                        _LeavesDialogState.value = LeavesDialogState.Success(result.data.message, true)
                    }

                    is EResult.Error -> {
                        _LeavesDialogState.value =
                            LeavesDialogState.Error(result.exception.message.toString(), false)
                    }

                    else -> {}
                }
            }
        }
    }

    fun dismissDialog() {
        _LeavesDialogState.value = null
    }

    fun onLeaveDeleted(id: String, status: String) {
        viewModelScope.launch {
            repository.deleteLeave(id, status).collect { result ->
                when (result) {
                    is EResult.Loading -> {
                        _LeavesDialogState.value = LeavesDialogState.Loading
                    }

                    is EResult.Success -> {
                        _LeavesDialogState.value = LeavesDialogState.Success(result.data.message, true)
                    }

                    is EResult.Error -> {
                        _LeavesDialogState.value =
                            LeavesDialogState.Error(result.exception.message.toString(), false)
                    }

                    else -> {}
                }
            }
        }
    }

    fun refreshData() {
        fetchLeaveRequests(teamStatus)
    }

}

internal sealed class UIState {
    data object Loading : UIState()
    data class Success(val data: LeavesDataState) : UIState()
    data class Error(val message: String) : UIState()

}

sealed class LeavesDialogState {
    data object Loading : LeavesDialogState()
    data class Success(val message: String, val shouldRefresh: Boolean) : LeavesDialogState()
    data class Error(val message: String, val shouldRefresh: Boolean) : LeavesDialogState()

}

data class LeavesDataState(
    val leaveDataList: List<LeaveData>
)