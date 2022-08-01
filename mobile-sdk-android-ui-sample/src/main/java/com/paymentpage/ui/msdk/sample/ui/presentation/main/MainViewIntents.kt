package com.paymentpage.ui.msdk.sample.ui.presentation.main

import android.net.Uri
import com.paymentpage.ui.msdk.sample.ui.presentation.base.ViewIntents
import com.paymentpage.ui.msdk.sample.ui.presentation.main.models.PaymentData

sealed interface MainViewIntents: ViewIntents {
    //Init
    object Init: MainViewIntents
    //Changing PaymentData fields
    data class ChangeField(val paymentData: PaymentData): MainViewIntents
    //Checkboxes
    object ChangeGooglePayCheckBox: MainViewIntents
    object ChangeApiHostCheckBox: MainViewIntents
    //Customization brand color and logo
    object ChangeCustomizationCheckbox: MainViewIntents
    data class SelectResourceImage(val id: Int, val paymentData: PaymentData): MainViewIntents
    data class SelectLocalImage(val uri: Uri, val paymentData: PaymentData): MainViewIntents
    //Custom force payment method
    object ChangeForcePaymentMethodCheckbox: MainViewIntents
    data class SelectForcePaymentMethod(val id: Int, val paymentData: PaymentData): MainViewIntents
    //Sale button
    object Sale: MainViewIntents
}