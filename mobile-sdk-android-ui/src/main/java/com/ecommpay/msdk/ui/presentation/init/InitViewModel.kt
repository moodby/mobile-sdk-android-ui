package com.ecommpay.msdk.ui.presentation.init

import com.ecommpay.msdk.core.base.ErrorCode
import com.ecommpay.msdk.core.domain.entities.PaymentInfo
import com.ecommpay.msdk.core.domain.entities.init.PaymentMethod
import com.ecommpay.msdk.core.domain.entities.init.SavedAccount
import com.ecommpay.msdk.core.domain.entities.payment.Payment
import com.ecommpay.msdk.core.domain.interactors.init.InitDelegate
import com.ecommpay.msdk.core.domain.interactors.init.InitInteractor
import com.ecommpay.msdk.core.domain.interactors.init.InitRequest
import com.ecommpay.msdk.ui.base.mvi.Reducer
import com.ecommpay.msdk.ui.base.mvi.TimeMachine
import com.ecommpay.msdk.ui.base.mvvm.BaseViewModel
import com.ecommpay.msdk.ui.base.ErrorResult
import kotlinx.coroutines.flow.StateFlow

internal class InitViewModel(
    private val initInteractor: InitInteractor,
    private val paymentInfo: PaymentInfo
) :
    BaseViewModel<InitScreenState, InitScreenUiEvent>() {
    override val reducer = InitReducer(InitScreenState.initial())

    override val state: StateFlow<InitScreenState>
        get() = reducer.state

    override val timeMachine: TimeMachine<InitScreenState>
        get() = reducer.timeMachine

    init {
        loadInit()
    }

    private fun loadInit() {
        initInteractor.execute(
            request = InitRequest(
                paymentInfo = paymentInfo,
                recurrentInfo = null,
                threeDSecureInfo = null
            ),
            callback = object : InitDelegate {
                override fun onInitReceived(
                    paymentMethods: List<PaymentMethod>,
                    savedAccounts: List<SavedAccount>,
                ) = sendEvent(InitScreenUiEvent.InitLoaded)

                override fun onError(code: ErrorCode, message: String) =
                    sendEvent(InitScreenUiEvent.ShowError(ErrorResult(code, message)))

                //Restore payment
                override fun onPaymentRestored(payment: Payment) {
                }
            }
        )
    }

    class InitReducer(initial: InitScreenState) :
        Reducer<InitScreenState, InitScreenUiEvent>(initial) {
        override fun reduce(oldState: InitScreenState, event: InitScreenUiEvent) {
            when (event) {
                is InitScreenUiEvent.InitLoaded -> setState(
                    oldState.copy(isInitLoaded = true)
                )
                is InitScreenUiEvent.ShowLoading -> setState(
                    oldState.copy(isInitLoaded = false)
                )
                is InitScreenUiEvent.ShowError -> setState(
                    oldState.copy(
                        isInitLoaded = false,
                        error = event.error
                    )
                )
            }
        }
    }
}