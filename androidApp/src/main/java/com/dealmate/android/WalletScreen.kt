package com.dealmate.android

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dealmate.shared.DealMateSDK
import com.dealmate.shared.Reward
import com.dealmate.shared.Transaction
import kotlinx.coroutines.launch

@Composable
fun WalletScreen(sdk: DealMateSDK) {
    var transactions by remember { mutableStateOf<List<Transaction>>(emptyList()) }
    var rewards by remember { mutableStateOf<List<Reward>>(emptyList()) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(true) {
        scope.launch {
            try {
                transactions = sdk.getTransactions()
                rewards = sdk.getRewards()
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Transactions")
        LazyColumn {
            items(transactions) { transaction ->
                Text("${transaction.vendor}: $${transaction.amount}")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text("Rewards")
        LazyColumn {
            items(rewards) { reward ->
                Text("${reward.description}: $${reward.amount}")
            }
        }
    }
}
