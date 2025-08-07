package com.dealmate.shared

import io.ktor.client.engine.*

internal expect fun getEngine(): HttpClientEngine
