package com.numera.app.core.numerology.model

/**
 * Deliberately not kotlinx-datetime.LocalDate - this module has zero external dependencies
 * so it stays trivially portable and testable; callers convert their platform date type to
 * this at the boundary.
 */
data class SimpleDate(val year: Int, val month: Int, val day: Int)
