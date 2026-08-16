package com.numera.app.features.home.data

import dev.zacsweers.metro.Inject
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

@Inject
class HomeApi(private val httpClient: HttpClient) {
    suspend fun getDailyInsight(date: String): DailyInsightResponse =
        httpClient.get("daily-insight") { parameter("date", date) }.body()
}
