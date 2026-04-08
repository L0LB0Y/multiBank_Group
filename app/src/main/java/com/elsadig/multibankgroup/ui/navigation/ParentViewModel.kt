package com.elsadig.multibankgroup.ui.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elsadig.multibankgroup.domain.repository.IStockRepository
import com.elsadig.multibankgroup.domain.usecase.ManagePriceUpdateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ParentViewModel @Inject constructor(
    private val managePriceFeedUseCase: ManagePriceUpdateUseCase,
    repository: IStockRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ParentScreenUIState())
    val uiState = _uiState.combine(repository.connectionState) { uiState, connectionState ->
        uiState.copy(connectionState = connectionState)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = ParentScreenUIState()
    )

    fun toggleFeed() {
        if (_uiState.value.isFeedRunning) {
            managePriceFeedUseCase.stop()
            _uiState.update { it.copy(isFeedRunning = false) }
        } else {
            managePriceFeedUseCase.start()
            _uiState.update { it.copy(isFeedRunning = true) }
        }
    }
}

data class ParentScreenUIState(
    val connectionState: Boolean = false,
    val isFeedRunning: Boolean = false
)