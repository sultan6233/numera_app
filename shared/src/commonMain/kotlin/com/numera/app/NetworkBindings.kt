package com.numera.app

import com.numera.app.core.commonMain.session.SessionStorage
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.expectSuccess
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.http.encodedPath
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

/**
 * No login/JWT here - identity is a device id generated locally and sent as a header on every
 * request (see [SessionStorage] and the backend spec's `POST /auth/anonymous`), so there's no
 * bearer/refresh-token plugin to install, unlike a typical authenticated Ktor client.
 */
@ContributesTo(AppScope::class)
interface NetworkBindings {
    @Provides
    @SingleIn(AppScope::class)
    fun provideHttpClient(sessionStorage: SessionStorage): HttpClient = HttpClient {
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
                protocol = URLProtocol.HTTPS
                host = "api.numera.app"
                encodedPath = "/v1/"
            }
            contentType(ContentType.Application.Json)
            header("X-Device-Id", sessionStorage.deviceId())
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
}
