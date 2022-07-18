package com.paymentpage.ui.test.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.paymentpage.ui.test.BuildConfig
import com.paymentpage.ui.test.MainActivity

@Composable
fun MainScreen(
    navController: NavController,
    activity: MainActivity
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {

        val apiHost = remember { mutableStateOf(MainActivity.apiHost) }
        val wsApiHost = remember { mutableStateOf(MainActivity.wsApiHost) }

        val projectId =
            remember { mutableStateOf(MainActivity.projectId.toString()) }
        val secretKey =
            remember { mutableStateOf(MainActivity.secretKey) }

        val paymentId =
            remember { mutableStateOf(MainActivity.paymentId) }
        val paymentAmount =
            remember { mutableStateOf(MainActivity.paymentAmount.toString()) }
        val paymentCurrency =
            remember { mutableStateOf(MainActivity.paymentCurrency) }
        val paymentDescription =
            remember { mutableStateOf(MainActivity.paymentDescription) }
        val customerId =
            remember { mutableStateOf(MainActivity.customerId) }

        Row() {
            Text(text = "Brand")
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = BuildConfig.BRAND, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(
            value = apiHost.value,
            onValueChange = { apiHost.value = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = "Api Host") }
        )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = wsApiHost.value,
            onValueChange = { wsApiHost.value = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = "Ws Api Host") }
        )

        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(
            value = projectId.value,
            onValueChange = { projectId.value = it.filter { it.isDigit() } },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = "Project Id") }
        )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = secretKey.value,
            onValueChange = { secretKey.value = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = "Secret Key") }
        )



        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(
            value = paymentId.value,
            onValueChange = { paymentId.value = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = "Payment Id") }
        )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = paymentAmount.value,
            onValueChange = { paymentAmount.value = it.filter { it.isDigit() } },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = { Text(text = "Payment Amount") }
        )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = paymentCurrency.value,
            onValueChange = { paymentCurrency.value = it.uppercase() },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = "Payment Currency") }
        )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = paymentDescription.value,
            onValueChange = { paymentDescription.value = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = "Payment Description") }
        )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = customerId.value,
            onValueChange = { customerId.value = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = "Customer Id") }
        )


        Spacer(modifier = Modifier.height(10.dp))
        Button(modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
            onClick = {
                MainActivity.apiHost = apiHost.value
                MainActivity.wsApiHost = wsApiHost.value

                MainActivity.projectId = projectId.value.toInt()
                MainActivity.secretKey = secretKey.value

                MainActivity.paymentId = paymentId.value
                MainActivity.paymentAmount = paymentAmount.value.toLong()
                MainActivity.paymentCurrency = paymentCurrency.value
                MainActivity.paymentDescription = paymentDescription.value
                MainActivity.customerId = customerId.value

                activity.startPaymentPage()
            }) {
            Text(text = "Sale", color = Color.White, fontSize = 18.sp)
        }

    }
}