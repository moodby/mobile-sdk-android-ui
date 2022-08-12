@file:OptIn(ExperimentalAnimationApi::class)

package com.paymentpage.msdk.ui.navigation


import androidx.activity.compose.BackHandler
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalFocusManager
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.paymentpage.msdk.ui.LocalMainViewModel
import com.paymentpage.msdk.ui.PaymentDelegate
import com.paymentpage.msdk.ui.ActionType
import com.paymentpage.msdk.ui.base.ErrorResult
import com.paymentpage.msdk.ui.presentation.aps.ApsScreen
import com.paymentpage.msdk.ui.presentation.clarificationFields.ClarificationFieldsScreen
import com.paymentpage.msdk.ui.presentation.customerFields.CustomerFieldsScreen
import com.paymentpage.msdk.ui.presentation.init.InitScreen
import com.paymentpage.msdk.ui.presentation.loading.LoadingScreen
import com.paymentpage.msdk.ui.presentation.main.FinalPaymentState
import com.paymentpage.msdk.ui.presentation.main.MainScreen
import com.paymentpage.msdk.ui.presentation.result.ResultDeclineScreen
import com.paymentpage.msdk.ui.presentation.result.ResultSuccessScreen
import com.paymentpage.msdk.ui.presentation.threeDSecure.ThreeDSecureScreen
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


@OptIn(ExperimentalAnimationApi::class)
@Composable
internal fun NavigationComponent(
    actionType: ActionType,
    navigator: Navigator,
    delegate: PaymentDelegate,
    onCancel: () -> Unit,
    onError: (ErrorResult, Boolean) -> Unit
) {
    val navController = rememberAnimatedNavController()
    val focusManager = LocalFocusManager.current

    LaunchedEffect("navigation") {
        navigator.sharedFlow.onEach {
            focusManager.clearFocus()
            navController.navigateUp()
            navController.navigate(it.getPath())
        }.launchIn(this)
    }

    AnimatedNavHost(
        navController = navController,
        startDestination = Route.Init.getPath(),
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None }
    ) {
        composable(route = Route.Init.getPath()) {
            InitScreen(navigator = navigator, onCancel = onCancel, onError = onError)
        }
        composable(route = Route.Main.getPath()) {
            MainScreen(
                navigator = navigator,
                delegate = delegate,
                onCancel = onCancel,
                onError = onError
            )
        }
        composable(route = Route.CustomerFields.getPath()) {
            CustomerFieldsScreen(
                onBack = {
                    navController.navigateUp()
                },
                onCancel = onCancel
            )
        }
        composable(route = Route.ClarificationFields.getPath()) {
            ClarificationFieldsScreen(
                onCancel = onCancel
            )
        }
        composable(route = Route.AcsPage.getPath()) {
            ThreeDSecureScreen(
                onCancel = onCancel
            )
        }
        composable(route = Route.ApsPage.getPath()) {
            ApsScreen(
                onCancel = onCancel
            )
        }
        composable(route = Route.SuccessResult.getPath()) {
            ResultSuccessScreen(onClose = { delegate.onCompleteWithSuccess(it) })
        }

        composable(route = Route.DeclineResult.getPath()) {
            ResultDeclineScreen(onClose = { delegate.onCompleteWithDecline(it) })
        }

        composable(route = Route.Loading.getPath()) {
            BackHandler(true) { }
            LoadingScreen(onCancel = onCancel)
        }
    }
}