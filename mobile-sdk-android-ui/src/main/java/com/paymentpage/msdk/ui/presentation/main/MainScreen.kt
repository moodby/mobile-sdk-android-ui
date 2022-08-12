package com.paymentpage.msdk.ui.presentation.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.paymentpage.msdk.ui.LocalMainViewModel
import com.paymentpage.msdk.ui.base.ErrorResult
import com.paymentpage.msdk.ui.navigation.Navigator
import com.paymentpage.msdk.ui.navigation.Route
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach


@Suppress("UNUSED_PARAMETER")
@Composable
internal fun MainScreen(
    navigator: Navigator,
    onError: (ErrorResult, Boolean) -> Unit,
    content: @Composable () -> Unit
) {
    val mainViewModel = LocalMainViewModel.current

    LaunchedEffect(Unit) {
        mainViewModel.state.onEach {
            when {
                it.error != null -> onError(it.error, true)
                it.isLoading == true && navigator.lastRoute != Route.Loading ->
                    navigator.navigateTo(Route.Loading)
                it.finalPaymentState != null -> {
                    when (it.finalPaymentState) {
                        is FinalPaymentState.Success -> navigator.navigateTo(Route.SuccessResult)
                        is FinalPaymentState.Decline -> navigator.navigateTo(Route.DeclineResult)
                    }
                }
                it.customerFields.isNotEmpty() -> navigator.navigateTo(Route.CustomerFields)
                it.clarificationFields.isNotEmpty() -> navigator.navigateTo(Route.ClarificationFields)
                it.acsPageState != null -> navigator.navigateTo(Route.AcsPage)
                it.apsPageState != null -> navigator.navigateTo(Route.ApsPage)
            }
        }.collect()
    }


}