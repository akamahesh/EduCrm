package com.crm.edu.ui.compose.screens.markAttendance

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crm.edu.core.EResult
import com.crm.edu.data.markAttendance.CheckAttendanceData
import com.crm.edu.data.markAttendance.MarkAttendanceRepository
import com.crm.edu.utils.getCurrentLocation
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MarkAttendanceViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: MarkAttendanceRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _state = MutableStateFlow<EResult<CheckAttendanceData>>(EResult.Loading)
    val state: StateFlow<EResult<CheckAttendanceData>> get() = _state

    private val _dialogState = MutableStateFlow<AttendanceDialogState?>(null)
    internal val dialogState: StateFlow<AttendanceDialogState?> get() = _dialogState


    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    private val _locationData = MutableStateFlow<Location?>(null)
    val locationData: StateFlow<Location?> get() = _locationData


    init {
        fetchAttendanceDetail()
//        fetchLocation()
    }

    @SuppressLint("MissingPermission")
    fun fetchLocation() {
        getCurrentLocation(fusedLocationClient) {
            //location
            setLocationData(it)
        }
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

    fun checkInOut(attendanceTypeId: String, lat: String, long: String) {
        viewModelScope.launch {
            repository.markCheckInOut(attendanceTypeId, lat, long).collect {
                when (it) {
                    is EResult.Loading -> {
                        _dialogState.value = AttendanceDialogState.Loading
                    }

                    is EResult.Success -> {
                        _dialogState.value = AttendanceDialogState.Success(it.data.message, true)
                    }

                    is EResult.Error -> {
                        _dialogState.value = AttendanceDialogState.Error(it.message, true)
                    }

                    else -> {

                    }
                }
            }
        }
    }

    private fun setLocationData(location: Location?) {
        _locationData.value = location
    }

    fun dismissDialog() {
        _dialogState.value = null
    }
}

sealed class AttendanceDialogState {
    data object Loading : AttendanceDialogState()
    data class Success(val message: String, val shouldRefresh: Boolean) : AttendanceDialogState()
    data class Error(val message: String, val shouldRefresh: Boolean) : AttendanceDialogState()

}