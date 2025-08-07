package com.dealmate.shared

import io.ktor.client.engine.*
import io.ktor.client.engine.android.*

internal actual fun getEngine(): HttpClientEngine = Android.create()
