package com.paymentpage.msdk.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.lifecycle.viewmodel.compose.viewModel
import com.paymentpage.msdk.core.MSDKCoreSession
import com.paymentpage.msdk.core.domain.entities.PaymentInfo
import com.paymentpage.msdk.core.domain.entities.RecipientInfo
import com.paymentpage.msdk.core.domain.entities.RecurrentInfo
import com.paymentpage.msdk.core.domain.entities.threeDSecure.ThreeDSecureInfo
import com.paymentpage.msdk.ui.presentation.init.InitViewModel
import com.paymentpage.msdk.ui.presentation.main.MainViewModel
import com.paymentpage.msdk.ui.utils.viewModelFactory

internal val LocalPaymentInfo = compositionLocalOf<PaymentInfo> { error("No PaymentInfo found!") }
internal val LocalRecurrentInfo = compositionLocalOf<RecurrentInfo?> { null }
internal val LocalThreeDSecureInfo = compositionLocalOf<ThreeDSecureInfo?> { null }
internal val LocalRecipientInfo = compositionLocalOf<RecipientInfo?> { null }
internal val LocalAdditionalFields = compositionLocalOf<List<AdditionalField>> { emptyList() }
internal val LocalMsdkSession =
    compositionLocalOf<MSDKCoreSession> { error("No MSDKCoreSession found!") }

internal val LocalMainViewModel =
    compositionLocalOf<MainViewModel> { error("No MainViewModel found!") }

internal val LocalInitViewModel =
    compositionLocalOf<InitViewModel> { error("No InitViewModel found!") }


@Composable
internal fun SDKCommonProvider(
    paymentOptions: PaymentOptions,
    msdkSession: MSDKCoreSession,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalPaymentInfo provides paymentOptions.paymentInfo!!,
        LocalRecurrentInfo provides paymentOptions.recurrentInfo,
        LocalThreeDSecureInfo provides paymentOptions.threeDSecureInfo,
        LocalRecipientInfo provides paymentOptions.recipientInfo,
        LocalAdditionalFields provides paymentOptions.additionalFields,
        LocalMsdkSession provides msdkSession,
        LocalMainViewModel provides viewModel(
            factory = viewModelFactory {
                MainViewModel(
                    payInteractor = msdkSession.getPayInteractor(),
                    paymentOptions = paymentOptions,
                )
            }
        ),
        LocalInitViewModel provides viewModel(
            factory = viewModelFactory {
                InitViewModel(
                    initInteractor = msdkSession.getInitInteractor(),
                    paymentOptions = paymentOptions,
                )
            }
        )

    ) {
        content()
    }
}