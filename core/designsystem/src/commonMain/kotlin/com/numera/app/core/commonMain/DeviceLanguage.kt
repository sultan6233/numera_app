package com.numera.app.core.commonMain

/**
 * App-content language code as the backend's SupportedLanguages expects: "en", "es", "pt-BR",
 * "uk", "tr", "de", "fr", "pl", "it", or "ru" - the same set of locales the client itself ships
 * string resources for (core/designsystem's composeResources values-* folders), so the daily
 * insight the backend writes in matches what the rest of the UI is already showing. Any other
 * system language (not in that list) is returned as-is; the backend's X-Language handling
 * ignores unsupported codes and leaves the stored language untouched, defaulting new users to
 * Russian.
 */
expect fun deviceLanguageCode(): String
