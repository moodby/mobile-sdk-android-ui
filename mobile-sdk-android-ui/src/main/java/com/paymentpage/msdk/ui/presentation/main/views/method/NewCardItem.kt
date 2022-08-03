package com.paymentpage.msdk.ui.presentation.main.views.method

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.paymentpage.msdk.core.domain.entities.init.PaymentMethodCardType

import com.paymentpage.msdk.ui.LocalMainViewModel
import com.paymentpage.msdk.ui.LocalPaymentOptions
import com.paymentpage.msdk.ui.PaymentActivity
import com.paymentpage.msdk.ui.presentation.main.models.UIPaymentMethod
import com.paymentpage.msdk.ui.presentation.main.saleCard
import com.paymentpage.msdk.ui.presentation.main.views.COUNT_OF_VISIBLE_CUSTOMER_FIELDS
import com.paymentpage.msdk.ui.presentation.main.views.method.expandable.ExpandablePaymentMethodItem
import com.paymentpage.msdk.ui.theme.SDKTheme
import com.paymentpage.msdk.ui.utils.extensions.core.annotatedString
import com.paymentpage.msdk.ui.utils.extensions.core.hasVisibleCustomerFields
import com.paymentpage.msdk.ui.utils.extensions.core.visibleCustomerFields
import com.paymentpage.msdk.ui.views.button.PayOrConfirmButton
import com.paymentpage.msdk.ui.views.card.CardHolderField
import com.paymentpage.msdk.ui.views.card.CvvField
import com.paymentpage.msdk.ui.views.card.ExpiryField
import com.paymentpage.msdk.ui.views.card.panField.PanField
import com.paymentpage.msdk.ui.views.customerFields.CustomerFields

@Composable
internal fun NewCardItem(
    method: UIPaymentMethod.UICardPayPaymentMethod,
) {
    val viewModel = LocalMainViewModel.current
    val customerFields = remember { method.paymentMethod.customerFields }
    val additionalFields = LocalPaymentOptions.current.additionalFields
    val savedState = remember { mutableStateOf(false) }
    var isCustomerFieldsValid by remember { mutableStateOf(method.isCustomerFieldsValid) }
    var isCvvValid by remember { mutableStateOf(method.isValidCvv) }
    var isPanValid by remember { mutableStateOf(method.isValidPan) }
    var isCardHolderValid by remember { mutableStateOf(method.isValidCardHolder) }
    var isExpiryValid by remember { mutableStateOf(method.isValidExpiry) }
    var cardType by remember { mutableStateOf<PaymentMethodCardType?>(null) }

    ExpandablePaymentMethodItem(
        method = method,
        headerBackgroundColor = SDKTheme.colors.backgroundColor,
        fallbackIcon = painterResource(id = SDKTheme.images.cardLogoResId),
    ) {
        Spacer(modifier = Modifier.size(10.dp))
        Column(Modifier.fillMaxWidth()) {
            PanField(
                initialValue = method.pan,
                modifier = Modifier.fillMaxWidth(),
                paymentMethod = method.paymentMethod,
                onValueChanged = { value, isValid ->
                    isPanValid = isValid
                    method.pan = value
                    method.isValidPan = isValid
                },
                onPaymentMethodCardTypeChange = {
                    cardType = it
                }
            )
            Spacer(modifier = Modifier.size(10.dp))
            CardHolderField(
                initialValue = method.cardHolder,
                modifier = Modifier.fillMaxWidth(),
                onValueChanged = { value, isValid ->
                    isCardHolderValid = isValid
                    method.cardHolder = value
                    method.isValidCardHolder = isValid
                }
            )
            Spacer(modifier = Modifier.size(10.dp))
            Row {
                ExpiryField(
                    modifier = Modifier.weight(1f),
                    initialValue = method.expiry,
                    onValueChanged = { value, isValid ->
                        isExpiryValid = isValid
                        method.expiry = value
                        method.isValidExpiry = isValid
                    }
                )
                Spacer(modifier = Modifier.size(10.dp))
                CvvField(
                    initialValue = method.cvv,
                    modifier = Modifier.weight(1f),
                    cardType = cardType,
                    onValueChanged = { value, isValid ->
                        isCvvValid = isValid
                        method.cvv = value
                        method.isValidCvv = isValid
                    }
                )
            }

            if (customerFields.hasVisibleCustomerFields() && customerFields.visibleCustomerFields().size <= COUNT_OF_VISIBLE_CUSTOMER_FIELDS) {
                CustomerFields(
                    visibleCustomerFields = customerFields.visibleCustomerFields(),
                    additionalFields = additionalFields,
                    customerFieldValues = method.customerFieldValues,
                    onCustomerFieldsChanged = { fields, isValid ->
                        method.customerFieldValues = fields
                        isCustomerFieldsValid = isValid
                        method.isCustomerFieldsValid = isCustomerFieldsValid
                    }
                )
            }
            Spacer(modifier = Modifier.size(22.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                    ) {
                        savedState.value = !savedState.value
                    },
                verticalAlignment = Alignment.Top
            ) {
                Checkbox(
                    modifier = Modifier.size(25.dp),
                    checked = savedState.value,
                    onCheckedChange = {
                        savedState.value = it
                        method.saveCard = it
                    },
                    colors = CheckboxDefaults.colors(checkedColor = SDKTheme.colors.brand)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        PaymentActivity.stringResourceManager.getStringByKey("title_saved_cards"),
                        color = SDKTheme.colors.primaryTextColor,
                        fontSize = 16.sp,
                    )
                    val uriHandler = LocalUriHandler.current
                    val linkedString = PaymentActivity
                        .stringResourceManager
                        .getLinkMessageByKey("cof_agreements")
                        .annotatedString()
                    ClickableText(
                        style = SDKTheme.typography.s12Light,
                        text = linkedString,
                        onClick = {
                            linkedString
                                .getStringAnnotations("URL", it, it)
                                .firstOrNull()?.let { stringAnnotation ->
                                    uriHandler.openUri(stringAnnotation.item)
                                }
                        }
                    )

                }
            }
            Spacer(modifier = Modifier.size(15.dp))
            PayOrConfirmButton(
                method = method,
                customerFields = customerFields,
                isValidCvv = isCvvValid,
                isValidCustomerFields = isCustomerFieldsValid,
                isValidPan = isPanValid,
                isValidCardHolder = isCardHolderValid,
                isValidExpiry = isExpiryValid,
                onClickButton = {
                    viewModel.saleCard(
                        method = method,
                        allCustomerFields = customerFields,
                        additionalFields = additionalFields
                    )
                }
            )
        }
    }
}