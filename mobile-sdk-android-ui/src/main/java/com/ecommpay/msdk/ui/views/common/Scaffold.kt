package com.ecommpay.msdk.ui.views.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ecommpay.msdk.ui.PaymentActivity
import com.ecommpay.msdk.ui.R
import com.ecommpay.msdk.ui.theme.SDKTheme
import com.ecommpay.msdk.ui.utils.extensions.core.annotatedString


@Composable
internal fun Scaffold(
    title: String = "",
    notScrollableContent: @Composable () -> Unit = {},
    scrollableContent: @Composable () -> Unit = {},
    footerContent: @Composable () -> Unit = {},
    onClose: () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .background(SDKTheme.colors.backgroundColor)
            .height(LocalConfiguration.current.screenHeightDp.dp * 0.9f) //Height of bottom sheet
            .fillMaxWidth(),
        content = {
            Column(
                modifier = Modifier.padding(SDKTheme.dimensions.paddingDp20),
                horizontalAlignment = Alignment.Start
            ) {
                TopBar(
                    title = title,
                    onClose = onClose
                )
                notScrollableContent()
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                ) {
                    scrollableContent()
                    Spacer(modifier = Modifier.size(SDKTheme.dimensions.paddingDp15))
                    footerContent()
                }
            }
        }
    )
}