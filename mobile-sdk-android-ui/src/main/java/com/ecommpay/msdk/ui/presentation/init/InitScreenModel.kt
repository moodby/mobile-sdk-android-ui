package com.ecommpay.msdk.ui.presentation.init

import androidx.compose.runtime.Immutable
import com.ecommpay.msdk.ui.base.mvi.UiEvent
import com.ecommpay.msdk.ui.base.mvi.UiState
import com.ecommpay.msdk.ui.model.common.ErrorResult
import com.ecommpay.msdk.ui.model.init.UIPaymentMethod

@Immutable
internal sealed class InitScreenUiEvent : UiEvent {
    object ShowLoading : InitScreenUiEvent()
    class InitLoaded(val data: List<UIPaymentMethod>) : InitScreenUiEvent()
    class ShowError(val error: ErrorResult) : InitScreenUiEvent()
}

@Immutable
internal data class InitScreenState(
    val isInitLoaded: Boolean = false,
    val data: List<UIPaymentMethod> = emptyList(),
    val error: ErrorResult? = null
) : UiState {

    companion object {
        fun initial() = InitScreenState()
    }

    override fun toString(): String {
        return "isInitLoaded: $isInitLoaded, data.size: ${data.size}, error: $error"
    }
}
