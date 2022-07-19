package com.paymentpage.msdk.ui.views.card

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.paymentpage.msdk.core.domain.entities.SdkExpiry
import com.paymentpage.msdk.ui.PaymentActivity
import com.paymentpage.msdk.ui.utils.MaskVisualTransformation
import com.paymentpage.msdk.ui.views.common.CustomTextField

@Composable
internal fun ExpiryField(
    modifier: Modifier,
    value: String? = null,
    isDisabled: Boolean = false,
    onValueChanged: (String, Boolean) -> Unit,
) {
    CustomTextField(
        initialValue = value?.replace("/", ""),
        modifier = modifier,
        onFilterValueBefore = { text -> text.filter { it.isDigit() } },
        onRequestValidatorMessage = {
            val expiryDate = SdkExpiry(it)
            when {
                !expiryDate.isValid() ->
                    PaymentActivity.stringResourceManager.getStringByKey("message_about_expiry")
                else -> null
            }
        },
        onValueChanged = { value, isValid ->
            val expiryDate = SdkExpiry(value)
            //if (expiryDate.month != null && expiryDate.year != null)
            onValueChanged(
                expiryDate.stringValue,
                expiryDate.month != null && expiryDate.year != null && isValid
            )
        },
        visualTransformation = MaskVisualTransformation("##/##"),
        label = PaymentActivity.stringResourceManager.getStringByKey("title_expiry"),
        isDisabled = isDisabled,
        keyboardType = KeyboardType.Number,
        maxLength = 4,
        isRequired = true
    )
}

@Composable
@Preview(showBackground = true)
private fun ExpiryFieldPreview() {
    ExpiryField(
        modifier = Modifier,
        value = "02/30",
        onValueChanged = { _, _ -> }
    )
}

@Composable
@Preview(showBackground = true)
private fun ExpiryFieldPreviewDisabled() {
    ExpiryField(
        isDisabled = true,
        modifier = Modifier,
        value = "02/30",
        onValueChanged = { _, _ -> }
    )
}