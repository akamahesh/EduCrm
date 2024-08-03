package com.crm.edu.ui.compose.screens.myteam

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.crm.edu.data.leaverequest.LeaveRequestRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyTeamScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: LeaveRequestRepository,
) : ViewModel() {


}