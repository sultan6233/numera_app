package com.numera.app.core.commonMain

import java.util.Locale

actual fun deviceLanguageCode(): String {
    val locale = Locale.getDefault()
    // Only Portuguese needs the region qualifier to disambiguate from pt-PT; every other
    // supported language is matched by its bare ISO 639-1 code (see SupportedLanguages.kt).
    return if (locale.language == "pt" && locale.country == "BR") "pt-BR" else locale.language
}
