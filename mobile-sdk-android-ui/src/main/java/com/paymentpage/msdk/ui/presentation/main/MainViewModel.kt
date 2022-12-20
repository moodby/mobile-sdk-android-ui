package com.paymentpage.msdk.ui.presentation.main

import com.paymentpage.msdk.core.base.ErrorCode
import com.paymentpage.msdk.core.domain.entities.clarification.ClarificationField
import com.paymentpage.msdk.core.domain.entities.customer.CustomerField
import com.paymentpage.msdk.core.domain.entities.payment.Payment
import com.paymentpage.msdk.core.domain.entities.payment.PaymentStatus
import com.paymentpage.msdk.core.domain.entities.threeDSecure.AcsPage
import com.paymentpage.msdk.core.domain.interactors.card.remove.CardRemoveDelegate
import com.paymentpage.msdk.core.domain.interactors.pay.PayDelegate
import com.paymentpage.msdk.ui.base.ErrorResult
import com.paymentpage.msdk.ui.base.mvi.TimeMachine
import com.paymentpage.msdk.ui.base.mvvm.BaseViewModel
import com.paymentpage.msdk.ui.core.CardRemoveInteractorProxy
import com.paymentpage.msdk.ui.core.PayInteractorProxy
import kotlinx.coroutines.flow.StateFlow

internal class MainViewModel(
    val payInteractor: PayInteractorProxy,
    val cardRemoveInteractor: CardRemoveInteractorProxy,
) : BaseViewModel<MainScreenState, MainScreenUiEvent>(), PayDelegate, CardRemoveDelegate {

    init {
        payInteractor.delegate = this
        cardRemoveInteractor.delegate = this
    }

    override val reducer = MainReducer(MainScreenState.initial())

    override val state: StateFlow<MainScreenState>
        get() = reducer.state

    override val timeMachine: TimeMachine<MainScreenState>
        get() = reducer.timeMachine


    override fun onCleared() {
        super.onCleared()
        payInteractor.cancel()
        cardRemoveInteractor.cancel()
    }


    override fun onClarificationFields(
        clarificationFields: List<ClarificationField>,
        payment: Payment
    ) {
        sendEvent(MainScreenUiEvent.ShowClarificationFields(clarificationFields = clarificationFields))
    }

    override fun onCompleteWithDecline(paymentMessage: String?, payment: Payment) {
        sendEvent(MainScreenUiEvent.SetPayment(payment))
        sendEvent(
            MainScreenUiEvent.ShowDeclinePage(
                paymentMessage = paymentMessage,
                isTryAgain = false
            )
        )
    }

    override fun onCompleteWithFail(
        isTryAgain: Boolean,
        paymentMessage: String?,
        payment: Payment
    ) {
        sendEvent(MainScreenUiEvent.SetPayment(payment))
        sendEvent(
            MainScreenUiEvent.ShowDeclinePage(
                paymentMessage = paymentMessage,
                isTryAgain = isTryAgain
            )
        )
    }


    override fun onCompleteWithSuccess(payment: Payment) {
        sendEvent(MainScreenUiEvent.SetPayment(payment))
        sendEvent(MainScreenUiEvent.ShowSuccessPage)
    }

    override fun onCustomerFields(customerFields: List<CustomerField>) {
        val visibleFields = customerFields.filter { !it.isHidden }
        if (visibleFields.isNotEmpty())
            sendEvent(MainScreenUiEvent.ShowCustomerFields(customerFields = visibleFields))
    }

    override fun onError(code: ErrorCode, message: String) {
        sendEvent(MainScreenUiEvent.ShowError(ErrorResult(code = code, message = message)))
    }

    override fun onSuccess(result: Boolean) {
        sendEvent(MainScreenUiEvent.ShowDeleteCardLoading(isLoading = false))
        setCurrentMethod(null)
    }

    override fun onPaymentCreated() {
    }

    override fun onStatusChanged(status: PaymentStatus, payment: Payment) {
        sendEvent(MainScreenUiEvent.SetPayment(payment))
    }

    override fun onThreeDSecure(acsPage: AcsPage, isCascading: Boolean, payment: Payment) {
        sendEvent(MainScreenUiEvent.ShowAcsPage(acsPage = acsPage, isCascading = isCascading))
    }
}
