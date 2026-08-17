package com.numera.app.features.paywall.presentation.model

import org.jetbrains.compose.resources.StringResource

data class PlanUi(
    val productId: String,
    val titleRes: StringResource,
    val priceRes: StringResource,
    val periodRes: StringResource,
    val badgeRes: StringResource? = null,
    val perDayNoteRes: StringResource? = null
)
