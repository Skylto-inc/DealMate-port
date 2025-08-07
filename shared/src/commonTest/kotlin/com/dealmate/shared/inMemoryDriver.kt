package com.dealmate.shared

import app.cash.sqldelight.db.SqlDriver

internal expect fun inMemoryDriver(): SqlDriver
