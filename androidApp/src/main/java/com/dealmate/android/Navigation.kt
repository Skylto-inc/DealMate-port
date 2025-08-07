package com.dealmate.android

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dealmate.shared.DealMateSDK

@Composable
fun Navigation(sdk: DealMateSDK) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "auth") {
        composable("auth") {
            AuthScreen(sdk = sdk, onLoginSuccess = { navController.navigate("wallet") })
        }
        composable("wallet") {
            WalletScreen(sdk = sdk)
        }
    }
}
