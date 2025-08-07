package com.dealmate.shared

import io.ktor.client.engine.*
import io.ktor.client.engine.darwin.*

internal actual fun getEngine(): HttpClientEngine = Darwin.create()
