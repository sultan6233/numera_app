package com.numera.app.features.onboarding.data

import dev.zacsweers.metro.Inject
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.serialization.Serializable

@Serializable
private data class UpdateProfileRequest(val fullName: String, val birthDate: String)

@Inject
class ProfileApi(private val httpClient: HttpClient) {
    /** birthDate must be "YYYY-MM-DD" - the backend needs it to compute daily insights. */
    suspend fun updateProfile(fullName: String, birthDate: String) {
        httpClient.post("profile") {
            setBody(UpdateProfileRequest(fullName, birthDate))
        }
    }
}
