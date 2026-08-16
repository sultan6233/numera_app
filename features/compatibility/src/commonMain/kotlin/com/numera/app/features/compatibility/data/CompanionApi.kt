package com.numera.app.features.compatibility.data

import dev.zacsweers.metro.Inject
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.serialization.Serializable

@Serializable
data class CompanionResponse(val id: String, val name: String, val birthDate: String)

@Serializable
private data class CreateCompanionRequest(val name: String, val birthDate: String)

@Inject
class CompanionApi(private val httpClient: HttpClient) {
    suspend fun getCompanions(): List<CompanionResponse> =
        httpClient.get("profile/companions").body()

    /** relationLabel is accepted by the response shape but 500s the backend if sent - name + birthDate only. */
    suspend fun addCompanion(name: String, birthDate: String): CompanionResponse =
        httpClient.post("profile/companions") {
            setBody(CreateCompanionRequest(name, birthDate))
        }.body()

    suspend fun removeCompanion(id: String) {
        httpClient.delete("profile/companions/$id")
    }
}
