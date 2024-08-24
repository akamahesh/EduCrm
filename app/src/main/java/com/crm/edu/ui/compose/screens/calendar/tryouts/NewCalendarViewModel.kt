package com.crm.edu.ui.compose.screens.calendar.tryouts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crm.edu.core.EResult
import com.crm.edu.data.attendanceCalendar.StaffAttendanceCalendarRepository
import com.crm.edu.data.myteam.StaffAttendanceData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewCalendarViewModel @Inject constructor(
    private val repository: StaffAttendanceCalendarRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<CalendarUIState>(CalendarUIState.Loading)
    internal val uiState: StateFlow<CalendarUIState> get() = _uiState

    fun getStaffAttendanceCalendarData(
        month: String,
        year: String,
        staffId: String? = null
    ) {
        viewModelScope.launch {
            repository.getStaffAttendanceCalendarData(month, year, staffId)
                .collect { result ->
                    when (result) {
                        is EResult.Loading -> {
                            _uiState.value = CalendarUIState.Loading
                        }

                        is EResult.Success -> {
                            _uiState.value =
                                CalendarUIState.Success(result.data)
                        }

                        is EResult.Error -> {
                            _uiState.value =
                                CalendarUIState.Error(result.exception.message.toString())
                        }

                        else -> {}

                    }
                }
        }
    }

}

internal sealed class CalendarUIState {
    data object Loading : CalendarUIState()
    data class Success(val data: List<StaffAttendanceData>) : CalendarUIState()
    data class Error(val message: String) : CalendarUIState()
}