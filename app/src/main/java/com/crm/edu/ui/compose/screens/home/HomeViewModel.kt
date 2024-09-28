package com.crm.edu.ui.compose.screens.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crm.edu.data.home.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: HomeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UIState>(UIState.Loading)
    internal val uiState: StateFlow<UIState> get() = _uiState

    init {
        getHomeConfiguration()
    }

    private fun getHomeConfiguration() {
        viewModelScope.launch {
            repository.getHomeConfiguration().let {
                _uiState.value = UIState.Success(HomeBottomData(it.showCallManager))
            }
        }
    }

}

internal sealed class UIState {
    data object Loading : UIState()
    data class Success(val homeBottomData: HomeBottomData) : UIState()
}

data class HomeBottomData(val showCallManager: Boolean = false)