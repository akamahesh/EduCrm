package com.crm.edu.ui.compose.screens.holidayLeaves

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crm.edu.core.EResult
import com.crm.edu.data.holiday.HolidayData
import com.crm.edu.data.holiday.HolidayRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HolidayCalendarViewModel @Inject constructor(
    saveStateHandle: SavedStateHandle,
    private val repository: HolidayRepository
) : ViewModel() {

    private val _state = MutableStateFlow<EResult<List<HolidayData>>>(EResult.Loading)
    val state: StateFlow<EResult<List<HolidayData>>> get() = _state

    init {
        getHolidays()
    }

    private fun getHolidays() {
        viewModelScope.launch {
            repository.getHolidayList().collect {
                Log.v("EduLogs", "Result $it")
                _state.value = it
            }
        }
    }

    fun retry() {
        getHolidays()
    }
}