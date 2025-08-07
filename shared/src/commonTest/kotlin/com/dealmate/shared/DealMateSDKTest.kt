package com.dealmate.shared

import com.dealmate.shared.db.DealMateDatabase
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class DealMateSDKTest {

    @Test
    fun `getFeatureToggles success`() = runTest {
        val mockEngine = MockEngine { request ->
            respond(
                content = """{"toggles":[{"name":"test","enabled":true}]}""",
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }
        val apiClient = ApiClient(mockEngine)
        val database = DealMateDatabase(inMemoryDriver())
        val sdk = DealMateSDK(apiClient, database)

        val toggles = sdk.getFeatureToggles(true)

        assertEquals(1, toggles.size)
        assertEquals("test", toggles[0].name)
        assertEquals(true, toggles[0].enabled)

        val cachedToggles = database.dealMateDatabaseQueries.selectAll().executeAsList()
        assertEquals(1, cachedToggles.size)
        assertEquals("test", cachedToggles[0].name)
        assertEquals(true, cachedToggles[0].enabled)
    }
}
