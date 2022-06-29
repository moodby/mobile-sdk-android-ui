package com.ecommpay.msdk.ui.presentation.main.views

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ecommpay.msdk.ui.R
import com.ecommpay.msdk.ui.theme.SDKTheme

@Composable
internal fun ExpandablePaymentMethodItem(
    iconUrl: String? = null,
    name: String,
    isExpanded: Boolean = false,
    content: @Composable ColumnScope.() -> Unit,
) {
    var expandState by remember { mutableStateOf(isExpanded) }
    val rotationState by animateFloatAsState(if (expandState) 180f else 0f)
    Box(
        modifier = Modifier
            .background(
                color = SDKTheme.colors.backgroundPaymentMethodItem,
                shape = SDKTheme.shapes.radius6
            )
            .border(width = 1.dp, color = SDKTheme.colors.borderPaymentMethodItem)
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 400,
                    easing = LinearOutSlowInEasing
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(SDKTheme.dimensions.paddingDp15)
        ) {
            Row {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(iconUrl)
                        .crossfade(true)
                        .build(),
                    fallback = painterResource(
                        id = if (SDKTheme.colors.isLight)
                            R.drawable.sdk_card_logo_light
                        else R.drawable.sdk_card_logo_dark
                    ),
                    contentDescription = "",
                    contentScale = ContentScale.Inside,
                    placeholder = painterResource(
                        id = if (SDKTheme.colors.isLight)
                            R.drawable.sdk_card_logo_light
                        else R.drawable.sdk_card_logo_dark
                    ),
                    modifier = Modifier.size(height = 20.dp, width = 50.dp),
                    alignment = Alignment.CenterStart
                )
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = name,
                        textAlign = TextAlign.Center,
                        style = SDKTheme.typography.s14Normal
                    )
                    Spacer(modifier = Modifier.size(SDKTheme.dimensions.paddingDp10))
                    Image(
                        modifier = Modifier
                            .clickable(
                                indication = null, //отключаем анимацию при клике
                                interactionSource = remember { MutableInteractionSource() },
                                onClick = { expandState = !expandState }
                            )
                            .rotate(rotationState),
                        imageVector = Icons.Default.KeyboardArrowDown,
                        colorFilter = ColorFilter.tint(SDKTheme.colors.topBarCloseButton),
                        contentDescription = "",
                    )
                }
            }
            if (expandState) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    content = content
                )
            }
        }
    }
}

@Composable
@Preview
fun ExpandablePaymentMethodItemPreview() {
    ExpandablePaymentMethodItem(
        name = "Bank card"
    ) {
        Text(text = "sdfsdfsdf") // testing content (delete later)
    }
}