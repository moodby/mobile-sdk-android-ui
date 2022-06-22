package com.ecommpay.msdk.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ecommpay.msdk.ui.theme.SDKLightTypography
import com.ecommpay.msdk.ui.theme.SDKTheme

@Composable
fun SDKButton(
    modifier: Modifier = Modifier,
    payLabel: String,
    amount: String,
    currency: String,
    isEnabled: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .wrapContentSize()
            .background(SDKTheme.colors.backgroundPaymentMethods)
    ){
        Button(
            onClick = onClick,
            content = {
                Text(
                    text = payLabel,
                    style = SDKLightTypography.s16Normal.copy(color = Color.White)
                )
                Text(text = " ")
                Text(
                    text = amount,
                    style = SDKLightTypography.s16Normal.copy(color = Color.White, fontWeight = FontWeight.Bold)
                )
                Text(text = " ")
                Text(
                    text = currency,
                    style = SDKLightTypography.s16Normal.copy(color = Color.White, fontWeight = FontWeight.Bold)
                )
            },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = SDKTheme.colors.brand,
                disabledBackgroundColor = SDKTheme.colors.brand.copy(alpha = 0.3f)
            ),
            shape = SDKTheme.shapes.radius6,
            modifier = modifier
                .height(45.dp)
                .fillMaxWidth(),
            enabled = isEnabled,
        )
    }
}

@Composable
@Preview
private fun SDKButtonDefaultPreview() {
    SDKTheme {
        SDKButton(
            payLabel = "Pay",
            amount = "100.00",
            currency = "USD",
            isEnabled = true) {
        }
    }
}

@Composable
@Preview
private fun SDKButtonDisabledPreview() {
    SDKTheme {
        SDKButton(
            payLabel = "Pay",
            amount = "100.00",
            currency = "USD",
            isEnabled = false) {
        }
    }
}