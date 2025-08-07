package com.dealmate.shared

import com.dealmate.shared.db.DatabaseDriverFactory
import com.dealmate.shared.db.DealMateDatabase

class DealMateSDK(private val apiClient: ApiClient, private val database: DealMateDatabase) {

    constructor(databaseDriverFactory: DatabaseDriverFactory) : this(
        ApiClient(getEngine(), null),
        DealMateDatabase(databaseDriverFactory.createDriver())
    )

    private val dbQuery = database.dealMateDatabaseQueries
    private var authToken: String? = null

    @Throws(Exception::class)
    suspend fun getFeatureToggles(forceReload: Boolean): List<com.dealmate.shared.db.FeatureToggle> {
        val cachedToggles = dbQuery.selectAll().executeAsList()
        if (cachedToggles.isNotEmpty() && !forceReload) {
            return cachedToggles
        }

        val apiToggles = apiClient.getFeatureToggles().toggles
        dbQuery.transaction {
            apiToggles.forEach { toggle ->
                dbQuery.insert(toggle.name, toggle.enabled)
            }
        }
        return dbQuery.selectAll().executeAsList()
    }

    @Throws(Exception::class)
    suspend fun login(email: String, pass: String) {
        val response = apiClient.login(AuthRequest(email, pass))
        authToken = response.token
    }

    @Throws(Exception::class)
    suspend fun signup(email: String, pass: String) {
        val response = apiClient.signup(AuthRequest(email, pass))
        authToken = response.token
    }

    @Throws(Exception::class)
    suspend fun getTransactions(): List<Transaction> {
        return apiClient.getTransactions()
    }

    @Throws(Exception::class)
    suspend fun getRewards(): List<Reward> {
        return apiClient.getRewards()
    }
}
