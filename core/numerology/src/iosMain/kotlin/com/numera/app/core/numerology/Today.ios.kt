package com.numera.app.core.numerology

import com.numera.app.core.numerology.model.SimpleDate
import platform.Foundation.NSCalendar
import platform.Foundation.NSCalendarUnitDay
import platform.Foundation.NSCalendarUnitMonth
import platform.Foundation.NSCalendarUnitYear
import platform.Foundation.NSDate

actual fun todaySimpleDate(): SimpleDate {
    val calendar = NSCalendar.currentCalendar
    val components = calendar.components(
        NSCalendarUnitYear or NSCalendarUnitMonth or NSCalendarUnitDay,
        fromDate = NSDate()
    )
    return SimpleDate(
        year = components.year.toInt(),
        month = components.month.toInt(),
        day = components.day.toInt()
    )
}
