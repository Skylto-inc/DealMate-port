package com.dealmate.shared

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class FeatureToggles(
    val toggles: List<FeatureToggle>
)

@Serializable
data class FeatureToggle(
    val name: String,
    val enabled: Boolean
)

@Serializable
data class AuthRequest(
    val email: String,
    val pass: String
)

@Serializable
data class AuthResponse(
    val token: String
)

@Serializable
data class Transaction(
    val id: String,
    val amount: Double,
    val vendor: String,
    val date: String
)

@Serializable
data class Reward(
    val id: String,
    val amount: Double,
    val description: String
)

class ApiClient(engine: HttpClientEngine, private var token: String?) {
    private val client = HttpClient(engine) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
        install(Auth) {
            bearer {
                loadTokens {
                    if (token != null) {
                        BearerTokens(token!!, "")
                    } else {
                        null
                    }
                }
            }
        }
    }

    private val backendUrl = "http://127.0.0.1:8000"
    private val authUrl = "http://12.0.0.1:3001"

    suspend fun getFeatureToggles(): FeatureToggles {
        return client.get("$backendUrl/api/feature-toggles").body()
    }

    suspend fun login(request: AuthRequest): AuthResponse {
        val response: AuthResponse = client.post("$authUrl/auth/login") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
        this.token = response.token
        return response
    }

    suspend fun signup(request: AuthRequest): AuthResponse {
        val response: AuthResponse = client.post("$authUrl/auth/signup") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
        this.token = response.token
        return response
    }

    suspend fun getTransactions(): List<Transaction> {
        return client.get("$backendUrl/wallet/transactions").body()
    }

    suspend fun getRewards(): List<Reward> {
        return client.get("$backendUrl/wallet/rewards").body()
    }
}
