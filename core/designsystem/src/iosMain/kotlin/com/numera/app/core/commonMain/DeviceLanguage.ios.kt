package com.numera.app.core.commonMain

import platform.Foundation.NSLocale
import platform.Foundation.preferredLanguages

// Unverified on-device - no Mac/Xcode toolchain available in this environment to compile or run
// the iOS target (same limitation noted for DeviceTimezone.ios.kt). NSLocale.preferredLanguages
// returns BCP-47 tags like "en-US" or "pt-BR"; only Portuguese needs the region kept to
// disambiguate from pt-PT, matching the Android side's logic.
actual fun deviceLanguageCode(): String {
    val preferred = (NSLocale.preferredLanguages.firstOrNull() as? String) ?: "ru"
    val parts = preferred.split("-")
    val language = parts.getOrElse(0) { "ru" }
    val region = parts.getOrNull(1)
    return if (language == "pt" && region == "BR") "pt-BR" else language
}
