package com.ecommpay.ui.msdk.sample.domain.ui.main

import android.net.Uri
import com.ecommpay.msdk.ui.EcmpPaymentSDK
import com.ecommpay.msdk.ui.EcmpScreenDisplayMode
import com.ecommpay.ui.msdk.sample.domain.ui.base.ViewState
import com.ecommpay.ui.msdk.sample.domain.entities.PaymentData

data class MainViewState(
    val paymentData: PaymentData = PaymentData(),
    val isVisibleForcePaymentMethodFields: Boolean = false,
    val isVisibleApiHostFields: Boolean = false,
    val isVisibleGooglePayFields: Boolean = false,
    val isVisibleCustomizationFields: Boolean = false,
    val isVisibleMockModeType: Boolean = false,
    val isVisibleScreenDisplayMode: Boolean = false,
    val selectedResourceImageId: Int = -1,
    val selectedForcePaymentMethodId: Int = -1,
    val selectedMockModeType: EcmpPaymentSDK.EcmpMockModeType = EcmpPaymentSDK.EcmpMockModeType.DISABLED,
    val selectedScreenDisplayModes: List<EcmpScreenDisplayMode> = listOf(EcmpScreenDisplayMode.DEFAULT),
    val localImageUri: Uri? = null,
) : ViewState