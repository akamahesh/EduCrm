package com.crm.edu.ui.compose.screens.myteam

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crm.edu.core.EResult
import com.crm.edu.data.myteam.MyTeamRepository
import com.crm.edu.data.myteam.StaffAttendanceData
import com.crm.edu.data.myteam.remote.StaffDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyTeamScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: MyTeamRepository,
) : ViewModel() {


    private val _uiState = MutableStateFlow<UIState>(UIState.Loading)
    internal val uiState: StateFlow<UIState> get() = _uiState

    init {
        fetchStaffAttendance()
    }

    internal fun fetchStaffAttendance() {
        viewModelScope.launch {
            val month = "2"
            val year = "2024"
            val teamStatus = "1"
            repository.getTeamAttendance(month, year, teamStatus).collect { result ->
                Log.d("EduLogs", "fetchStaffAttendance: $result")
                when (result) {
                    is EResult.Loading -> {
                        _uiState.value = UIState.Loading
                    }

                    is EResult.Success -> {
                        _uiState.value = UIState.Success(StaffAttendanceState(result.data))
                    }

                    is EResult.Error -> {
                        _uiState.value = UIState.Error(result.exception.message.toString())
                    }

                    else -> {}
                }
            }

        }
    }

}


internal sealed class UIState {
    object Loading : UIState()
    data class Success(val data: StaffAttendanceState) : UIState()
    data class Error(val message: String) : UIState()
}

data class StaffAttendanceState(
    val staffList: List<StaffAttendanceData>
)