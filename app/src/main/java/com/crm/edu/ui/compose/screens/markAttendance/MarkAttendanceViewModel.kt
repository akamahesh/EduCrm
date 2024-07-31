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
import com.crm.edu.data.markAttendance.remote.MarkAttendanceData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
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

    private val _markAttendanceState =
        MutableStateFlow<EResult<MarkAttendanceData>>(EResult.Loading)
    val markAttendanceState: StateFlow<EResult<MarkAttendanceData>> get() = _markAttendanceState


    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    private val _locationData = MutableStateFlow<Location?>(null)
    val locationData: StateFlow<Location?> get() = _locationData


    init {
        fetchAttendanceDetail()
        fetchLocation()
    }

    @SuppressLint("MissingPermission")
    fun fetchLocation() {
        viewModelScope.launch {
            val locationRequest = LocationRequest.Builder(
                Priority.PRIORITY_BALANCED_POWER_ACCURACY,
                5000L
            ).build()

            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    locationResult.lastLocation?.let { location ->
                        _locationData.value = location
                        fusedLocationClient.removeLocationUpdates(this)
                    }
                }
            }

            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
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
                _markAttendanceState.value = it
            }
        }
    }


}