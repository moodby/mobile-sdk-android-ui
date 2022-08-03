package com.paymentpage.ui.msdk.sample

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.ecommpay.msdk.ui.EcmpPaymentInfo
import com.ecommpay.msdk.ui.EcmpPaymentMethodType
import com.ecommpay.msdk.ui.PaymentSDK
import com.ecommpay.msdk.ui.paymentOptions
import com.paymentpage.msdk.ui.base.Constants
import com.paymentpage.ui.msdk.sample.data.ProcessRepository
import com.paymentpage.ui.msdk.sample.ui.navigation.NavigationComponent
import com.paymentpage.ui.msdk.sample.utils.SignatureGenerator

class SampleActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Scaffold() {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.White)
                                .verticalScroll(rememberScrollState())
                                .padding(it),
                        ) {
                            NavigationComponent {
                                startPaymentPage()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun startPaymentPage() {
        val repositoryPaymentData = ProcessRepository.paymentData
        val additionalFieldsToSend = ProcessRepository.additionalFields
        val payment = EcmpPaymentInfo(
            forcePaymentMethod = EcmpPaymentMethodType.values().find { it.value == repositoryPaymentData.forcePaymentMethod },
            hideSavedWallets = repositoryPaymentData.hideSavedWallets,
            projectId = repositoryPaymentData.projectId ?: -1,
            paymentId = repositoryPaymentData.paymentId,
            paymentAmount = repositoryPaymentData.paymentAmount ?: -1,
            paymentCurrency = repositoryPaymentData.paymentCurrency,
            customerId = repositoryPaymentData.customerId,
            paymentDescription = repositoryPaymentData.paymentDescription
        )
        payment.signature =
            SignatureGenerator.generateSignature(
                payment.getParamsForSignature(), repositoryPaymentData.secretKey
            )
        val paymentOptions = paymentOptions {
            logoImage = repositoryPaymentData.bitmap
            brandColor = repositoryPaymentData.brandColor
            paymentInfo = payment
            merchantId = repositoryPaymentData.merchantId
            merchantName = repositoryPaymentData.merchantName
            additionalFields = additionalFieldsToSend?.toMutableList() ?: mutableListOf()
        }
        val sdk = PaymentSDK(context = this.applicationContext, paymentOptions = paymentOptions)

        val intent = sdk.intent
        intent.putExtra(Constants.EXTRA_API_HOST, repositoryPaymentData.apiHost)
        intent.putExtra(
            Constants.EXTRA_WS_API_HOST,
            repositoryPaymentData.wsApiHost
        )
        if (repositoryPaymentData.mockModeEnabled)
            intent.putExtra(
                Constants.EXTRA_MOCK_MODE_ENABLED,
                repositoryPaymentData.mockModeEnabled
            )

        startActivityForResult(intent, 2405)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (resultCode) {
            PaymentSDK.RESULT_SUCCESS -> {}
            PaymentSDK.RESULT_CANCELLED -> {}
            PaymentSDK.RESULT_DECLINE -> {}
            PaymentSDK.RESULT_ERROR -> {
                val errorCode = data?.getStringExtra(PaymentSDK.EXTRA_ERROR_CODE)
                val message = data?.getStringExtra(PaymentSDK.EXTRA_ERROR_MESSAGE)
            }
        }
    }
}