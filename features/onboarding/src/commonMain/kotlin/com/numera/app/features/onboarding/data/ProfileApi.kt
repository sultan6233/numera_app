package com.numera.app.features.onboarding.data

import dev.zacsweers.metro.Inject
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.serialization.Serializable

@Serializable
private data class UpdateProfileRequest(val name: String, val birthDate: String)

@Inject
class ProfileApi(private val httpClient: HttpClient) {
    /**
     * birthDate must be "YYYY-MM-DD" - the backend needs it to compute daily insights.
     *
     * The request field is `name`, not `fullName` - matches numerology-backend's
     * SaveProfileRequest exactly (mismatched field names here would silently no-op instead of
     * failing, since every field on that DTO is nullable).
     *
     * No timezone field here on purpose: the X-Timezone header (set on every request in
     * NetworkBindings.kt) is what the backend actually reads to keep the profile's timezone in
     * sync - this call doesn't need to carry it too.
     */
    suspend fun updateProfile(fullName: String, birthDate: String) {
        httpClient.post("profile") {
            setBody(UpdateProfileRequest(fullName, birthDate))
        }
    }
}
