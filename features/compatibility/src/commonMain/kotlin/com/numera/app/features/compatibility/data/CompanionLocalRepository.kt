package com.numera.app.features.compatibility.data

import com.numera.app.core.commonMain.session.SessionStorage
import com.numera.app.core.numerology.model.SimpleDate
import com.numera.app.features.compatibility.domain.CompanionRepository
import com.numera.app.features.compatibility.presentation.model.CompanionUi
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

private const val FIELD_SEPARATOR = ";;"

class CompanionLocalRepository(private val sessionStorage: SessionStorage) : CompanionRepository {

    override fun getCompanions(): List<CompanionUi> =
        sessionStorage.companionRecords().mapNotNull { it.toCompanionOrNull() }

    @OptIn(ExperimentalUuidApi::class)
    override fun addCompanion(name: String, birthDate: SimpleDate): CompanionUi {
        val companion = CompanionUi(Uuid.random().toString(), name, birthDate)
        val records = sessionStorage.companionRecords() + companion.toRecord()
        sessionStorage.saveCompanionRecords(records)
        return companion
    }

    override fun findCompanion(id: String): CompanionUi? = getCompanions().find { it.id == id }

    private fun CompanionUi.toRecord(): String =
        listOf(id, name, birthDate.year, birthDate.month, birthDate.day).joinToString(FIELD_SEPARATOR)

    private fun String.toCompanionOrNull(): CompanionUi? {
        val parts = split(FIELD_SEPARATOR)
        if (parts.size != 5) return null
        val (id, name, year, month, day) = parts
        return CompanionUi(
            id = id,
            name = name,
            birthDate = SimpleDate(year.toIntOrNull() ?: return null, month.toIntOrNull() ?: return null, day.toIntOrNull() ?: return null)
        )
    }
}
