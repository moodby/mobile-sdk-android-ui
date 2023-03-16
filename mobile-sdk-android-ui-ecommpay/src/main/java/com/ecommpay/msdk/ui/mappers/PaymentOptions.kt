package com.ecommpay.msdk.ui.mappers

import com.ecommpay.msdk.ui.EcmpAdditionalFieldType
import com.ecommpay.msdk.ui.EcmpPaymentOptions
import com.ecommpay.msdk.ui.EcmpRecipientInfo
import com.ecommpay.msdk.ui.EcmpScreenDisplayMode
import com.paymentpage.msdk.core.domain.entities.RecipientInfo
import com.paymentpage.msdk.core.domain.interactors.pay.googlePay.GooglePayEnvironment
import com.paymentpage.msdk.ui.*

internal fun EcmpPaymentOptions.map(): SDKPaymentOptions =
    SDKPaymentOptions(
        //payment configuration
        paymentInfo = paymentInfo,
        recurrentInfo = recurrentData,
        recipientInfo = recipientInfo?.map(),
        actionType = SDKActionType.valueOf(actionType.name),
        screenDisplayModes = screenDisplayModes.map(),
        additionalFields = additionalFields.map { additionalField ->
            SDKAdditionalField(
                type = additionalField.type?.map(),
                value = additionalField.value
            )
        },
        hideScanningCards = hideScanningCards,
        //google pay configuration
        merchantId = merchantId,
        merchantName = merchantName,
        merchantEnvironment = if (isTestEnvironment) GooglePayEnvironment.TEST else GooglePayEnvironment.PROD,
        //theme customization
        isDarkTheme = isDarkTheme,
        logoImage = logoImage,
        brandColor = brandColor,
    )


internal fun EcmpAdditionalFieldType.map(): SDKAdditionalFieldType? =
    SDKAdditionalFieldType.values().find { it.value == value }

internal fun EcmpScreenDisplayMode.map(): SDKScreenDisplayMode? =
    SDKScreenDisplayMode.values().find { it.name == name }

internal fun List<EcmpScreenDisplayMode>.map(): List<SDKScreenDisplayMode> =
    mapNotNull { it.map() }

internal fun EcmpRecipientInfo.map(): RecipientInfo =
    RecipientInfo(
        walletOwner = walletOwner,
        walletId = walletId,
        country = country,
        pan = pan,
        cardHolder = cardHolder,
        address = address,
        city = city,
        stateCode = stateCode,
        firstName = null,
        lastName = null
    )