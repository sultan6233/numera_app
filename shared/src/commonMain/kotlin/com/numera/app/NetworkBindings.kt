package com.numera.app

import com.numera.app.core.commonMain.deviceTimezoneId
import com.numera.app.core.commonMain.session.SessionStorage
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.expectSuccess
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
private data class AnonymousAuthRequest(val deviceId: String)

@Serializable
private data class AnonymousAuthResponse(val userId: String, val token: String)

/**
 * Identity here is anonymous (no email/password), but the backend still hands back a real JWT
 * for it - POST /auth/anonymous exchanges the locally generated device id for a bearer token
 * (roughly a year-long expiry, no separate refresh token). Ktor's bearer plugin calls
 * refreshTokens() both the first time (loadTokens returns null) and after any 401, so
 * "re-authenticate anonymously" doubles as the refresh path - there's nothing else to refresh.
 *
 * Backend host is a bare IP over plain HTTP (no TLS yet) - see the cleartext exception scoped to
 * this host in androidApp's network_security_config.xml.
 */
@ContributesTo(AppScope::class)
interface NetworkBindings {
    @Provides
    @SingleIn(AppScope::class)
    fun provideHttpClient(sessionStorage: SessionStorage): HttpClient {
        val httpClient = HttpClient {
            expectSuccess = true

            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                    explicitNulls = false
                    encodeDefaults = true
                })
            }

            defaultRequest {
                url {
                    protocol = URLProtocol.HTTP
                    host = "92.42.99.211"
                    port = 8080
                }
                contentType(ContentType.Application.Json)
                // Sent on every request, not just profile writes: the backend's `authenticated`
                // route wrapper reads this header and keeps the stored profile timezone in sync
                // on every authenticated call (see Security.kt), so a traveling user's nightly
                // insight/push schedule follows their current timezone without a dedicated
                // "update my timezone" endpoint.
                header("X-Timezone", deviceTimezoneId())
            }

            install(Auth) {
                bearer {
                    loadTokens {
                        val token = sessionStorage.authToken()
                        if (token != null) BearerTokens(token, token) else null
                    }
                    refreshTokens {
                        val response = client.post("auth/anonymous") {
                            markAsRefreshTokenRequest()
                            setBody(AnonymousAuthRequest(sessionStorage.deviceId()))
                            // Branches on status itself (this call must succeed even the very
                            // first time, before any token exists) - expectSuccess=true would
                            // throw before that check runs.
                            expectSuccess = false
                        }
                        if (response.status.isSuccess()) {
                            val auth = response.body<AnonymousAuthResponse>()
                            sessionStorage.saveAuthToken(auth.token)
                            BearerTokens(auth.token, auth.token)
                        } else {
                            null
                        }
                    }
                }
            }

            install(Logging) {
                level = LogLevel.ALL
            }
            install(HttpTimeout) {
                requestTimeoutMillis = 30_000
                connectTimeoutMillis = 30_000
                socketTimeoutMillis = 30_000
            }
        }

        return httpClient
    }
}
