package com.paymentpage.msdk.ui.presentation.main.screens.result.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.paymentpage.msdk.core.base.ErrorCode
import com.paymentpage.msdk.core.domain.entities.init.PaymentMethodType
import com.paymentpage.msdk.ui.LocalMainViewModel
import com.paymentpage.msdk.ui.PaymentActivity
import com.paymentpage.msdk.ui.base.ErrorResult
import com.paymentpage.msdk.ui.presentation.main.screens.paymentMethods.models.UIPaymentMethod
import com.paymentpage.msdk.ui.theme.SDKTheme
import com.paymentpage.msdk.ui.utils.extensions.paymentDateToPatternDate

@Composable
internal fun ResultTableInfo(
    onError: (ErrorResult, Boolean) -> Unit,
) {
    val viewModel = LocalMainViewModel.current
    val payment = viewModel.lastState.payment
    if (payment != null) {
        val valueTitleCardWallet = when (val method = viewModel.lastState.currentMethod) {
            is UIPaymentMethod.UICardPayPaymentMethod, is UIPaymentMethod.UISavedCardPayPaymentMethod -> {
                "${payment.account?.type?.uppercase() ?: ""} ${payment.account?.number}"
            }
            is UIPaymentMethod.UIApsPaymentMethod -> {
                method.paymentMethod.name ?: PaymentActivity.stringResourceManager.getStringByKey(
                    method.paymentMethod.translations["title"] ?: "")
            }
            is UIPaymentMethod.UIGooglePayPaymentMethod -> {
                method.paymentMethod.name
                    ?: PaymentActivity.stringResourceManager.getStringByKey("google_pay_host_title")
            }
            else -> {
                if (payment.methodType == PaymentMethodType.CARD)
                    "${payment.account?.type?.uppercase() ?: ""} ${payment.account?.number}"
                else
                    "${payment.methodName}"
            }
        }
        val completeFields = payment.completeFields?.associate { field ->
            val translation =
                field.name?.let { PaymentActivity.stringResourceManager.getStringByKey(it) }
                    ?: field.defaultLabel
            translation to field.value
        } ?: emptyMap()
        val titleKeyWithValueMap = mutableMapOf(
            PaymentActivity.stringResourceManager.getStringByKey("title_card_wallet") to
                    valueTitleCardWallet,
            PaymentActivity.stringResourceManager.getStringByKey("title_payment_id") to
                    "${payment.id}",
            PaymentActivity.stringResourceManager.getStringByKey("title_payment_date") to
                    payment.date?.paymentDateToPatternDate("dd.MM.yyyy HH:mm"),
        ) + completeFields
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(color = SDKTheme.colors.panelBackgroundColor)
                .padding(top = 15.dp, start = 15.dp, end = 15.dp)
        ) {
            titleKeyWithValueMap
                .filterValues { !it.isNullOrEmpty() }
                .mapValues { it.value as String }
                .filterKeys { !it.isNullOrEmpty() }
                .mapKeys { it.key as String }
                .forEach { (key, value) ->
                    Row {
                        Column {
                            Text(
                                text = key,
                                style = SDKTheme.typography.s14Light.copy(color = SDKTheme.colors.secondaryTextColor),
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1
                            )
                        }
                        Text(text = " ")
                        Column(modifier = Modifier.weight(1f), horizontalAlignment = End) {
                            Text(
                                text = value,
                                style = SDKTheme.typography.s14Normal,
                                textAlign = TextAlign.End
                            )
                        }
                    }
                    Spacer(modifier = Modifier.size(15.dp))
                }
        }
    } else {
        onError(ErrorResult(code = ErrorCode.PAYMENT_NOT_FOUND, message = "Not found payment in State"), true)
    }
}