package com.numera.app.features.compatibility.domain

import com.numera.app.core.numerology.model.SimpleDate
import com.numera.app.features.compatibility.presentation.model.CompanionUi

interface CompanionRepository {
    /** Backend is the source of truth when reachable; falls back to the local cache offline. */
    suspend fun getCompanions(): List<CompanionUi>
    suspend fun addCompanion(name: String, birthDate: SimpleDate): CompanionUi
    suspend fun removeCompanion(id: String)
    fun findCompanion(id: String): CompanionUi?
}
