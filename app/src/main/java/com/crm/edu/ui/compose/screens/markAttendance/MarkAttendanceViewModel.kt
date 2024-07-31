package com.crm.edu.ui.compose.screens.markAttendance

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crm.edu.core.EResult
import com.crm.edu.data.markAttendance.CheckAttendanceData
import com.crm.edu.data.markAttendance.MarkAttendanceRepository
import com.crm.edu.data.markAttendance.remote.MarkAttendanceData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MarkAttendanceViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: MarkAttendanceRepository
) : ViewModel() {

    private val _state = MutableStateFlow<EResult<CheckAttendanceData>>(EResult.Loading)
    val state: StateFlow<EResult<CheckAttendanceData>> get() = _state

    private val _markAttendanceState =
        MutableStateFlow<EResult<MarkAttendanceData>>(EResult.Loading)
    val markAttendanceState: StateFlow<EResult<MarkAttendanceData>> get() = _markAttendanceState


    init {
        fetchAttendanceDetail()
    }

    private fun fetchAttendanceDetail() {
        viewModelScope.launch {
            repository.getCheckAttendanceData().collect {
                _state.value = it
            }
        }
    }

    fun retry() {
        fetchAttendanceDetail()
    }

    fun checkInOut(attendanceTypeId: String) {
        viewModelScope.launch {
            repository.markCheckInOut(attendanceTypeId).collect {
                _markAttendanceState.value = it
            }
        }
    }


}