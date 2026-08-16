package com.numera.app.features.compatibility.data

import com.numera.app.core.commonMain.session.SessionStorage
import com.numera.app.core.numerology.model.SimpleDate
import com.numera.app.features.compatibility.domain.CompanionRepository
import com.numera.app.features.compatibility.presentation.model.CompanionUi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

private const val FIELD_SEPARATOR = ";;"

/**
 * Backend (/profile/companions) is the source of truth when reachable. The local cache exists
 * purely so the compatibility screen still shows something offline - it's overwritten by every
 * successful fetch, not merged with it. A companion added while offline is saved locally with a
 * local-only id and simply never makes it to the backend; there's no outbox/retry queue here,
 * which is an acceptable gap for how rarely this list changes.
 */
class CompanionRepositoryImpl(
    private val companionApi: CompanionApi,
    private val sessionStorage: SessionStorage
) : CompanionRepository {

    override suspend fun getCompanions(): List<CompanionUi> = withContext(Dispatchers.IO) {
        try {
            val remote = companionApi.getCompanions().map { it.toCompanionUi() }
            sessionStorage.saveCompanionRecords(remote.map { it.toRecord() })
            remote
        } catch (e: Exception) {
            localCompanions()
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun addCompanion(name: String, birthDate: SimpleDate): CompanionUi = withContext(Dispatchers.IO) {
        val isoDate = birthDate.toIso()
        val companion = try {
            companionApi.addCompanion(name, isoDate).toCompanionUi()
        } catch (e: Exception) {
            CompanionUi(id = Uuid.random().toString(), name = name, birthDate = birthDate)
        }
        sessionStorage.saveCompanionRecords(localCompanions().plus(companion).map { it.toRecord() })
        companion
    }

    override suspend fun removeCompanion(id: String) = withContext(Dispatchers.IO) {
        try {
            companionApi.removeCompanion(id)
        } catch (e: Exception) {
            // Local removal below still gives immediate feedback even if the backend is unreachable.
        }
        sessionStorage.saveCompanionRecords(localCompanions().filter { it.id != id }.map { it.toRecord() })
        Unit
    }

    override fun findCompanion(id: String): CompanionUi? = localCompanions().find { it.id == id }

    private fun localCompanions(): List<CompanionUi> =
        sessionStorage.companionRecords().mapNotNull { it.toCompanionOrNull() }

    private fun CompanionResponse.toCompanionUi(): CompanionUi =
        CompanionUi(id = id, name = name, birthDate = birthDate.toSimpleDate())

    private fun SimpleDate.toIso(): String =
        "$year-${month.toString().padStart(2, '0')}-${day.toString().padStart(2, '0')}"

    private fun String.toSimpleDate(): SimpleDate {
        val parts = split("-")
        return SimpleDate(
            year = parts.getOrNull(0)?.toIntOrNull() ?: 2000,
            month = parts.getOrNull(1)?.toIntOrNull() ?: 1,
            day = parts.getOrNull(2)?.toIntOrNull() ?: 1
        )
    }

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
