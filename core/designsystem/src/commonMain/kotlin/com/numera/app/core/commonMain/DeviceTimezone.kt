package com.numera.app.core.commonMain

/**
 * IANA zone id ("Asia/Tashkent") or fixed offset ("+05:00") - matches what the backend's
 * `SaveProfileRequest.timezone` expects (see numerology-backend's ProfileModels.kt). Drives
 * when the nightly daily-insight/push jobs fire for this user server-side.
 */
expect fun deviceTimezoneId(): String
