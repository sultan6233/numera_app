package com.numera.app.features.compatibility.domain

import com.numera.app.core.numerology.model.SimpleDate
import com.numera.app.features.compatibility.presentation.model.CompanionUi

interface CompanionRepository {
    fun getCompanions(): List<CompanionUi>
    fun addCompanion(name: String, birthDate: SimpleDate): CompanionUi
    fun findCompanion(id: String): CompanionUi?
}
