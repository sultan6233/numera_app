package com.numera.app.features.paywall.presentation.model

data class PlanUi(
    val productId: String,
    val title: String,
    val price: String,
    val period: String,
    val badge: String? = null,
    val perDayNote: String? = null
)
