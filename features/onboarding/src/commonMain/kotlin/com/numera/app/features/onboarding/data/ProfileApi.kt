package com.numera.app.features.onboarding.data

import dev.zacsweers.metro.Inject
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.serialization.Serializable

@Serializable
private data class UpdateProfileRequest(val name: String, val birthDate: String, val timezone: String)

@Inject
class ProfileApi(private val httpClient: HttpClient) {
    /**
     * birthDate must be "YYYY-MM-DD" - the backend needs it to compute daily insights.
     * timezone is an IANA id ("Asia/Tashkent") or fixed offset ("+05:00") - it drives when the
     * backend's nightly insight/push jobs fire for this user, see [deviceTimezoneId].
     *
     * The request field is `name`, not `fullName` - matches numerology-backend's
     * SaveProfileRequest exactly (mismatched field names here would silently no-op instead of
     * failing, since every field on that DTO is nullable).
     */
    suspend fun updateProfile(fullName: String, birthDate: String, timezone: String) {
        httpClient.post("profile") {
            setBody(UpdateProfileRequest(fullName, birthDate, timezone))
        }
    }
}
