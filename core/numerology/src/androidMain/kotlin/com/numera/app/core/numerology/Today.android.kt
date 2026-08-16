package com.numera.app.core.numerology

import com.numera.app.core.numerology.model.SimpleDate
import java.time.LocalDate

actual fun todaySimpleDate(): SimpleDate {
    val today = LocalDate.now()
    return SimpleDate(today.year, today.monthValue, today.dayOfMonth)
}
