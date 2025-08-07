package com.dealmate.shared

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver

internal actual fun inMemoryDriver(): SqlDriver {
    return NativeSqliteDriver(DealMateDatabase.Schema, "test.db")
}
