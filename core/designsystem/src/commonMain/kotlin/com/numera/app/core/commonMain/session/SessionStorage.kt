package com.numera.app.core.commonMain.session

import com.russhwolf.settings.Settings
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * Anonymous, account-free session: identity is a locally generated device id, exchanged for a
 * long-lived backend JWT via POST /auth/anonymous (see NetworkBindings) - there's no
 * email/password flow, so the only "auth" state to keep is that one token.
 */
@Inject
@SingleIn(AppScope::class)
class SessionStorage {
    private var settings: Settings? = null
    private var cachedDeviceId: String? = null

    fun attach(settings: Settings) {
        this.settings = settings
    }

    @OptIn(ExperimentalUuidApi::class)
    fun deviceId(): String {
        cachedDeviceId?.let { return it }
        val existing = settings?.getStringOrNull(KEY_DEVICE_ID)
        val id = existing ?: Uuid.random().toString()
        if (existing == null) settings?.putString(KEY_DEVICE_ID, id)
        cachedDeviceId = id
        return id
    }

    fun hasCompletedOnboarding(): Boolean = settings?.getBoolean(KEY_ONBOARDING_DONE, false) ?: false

    fun saveProfile(fullName: String, birthYear: Int, birthMonth: Int, birthDay: Int) {
        settings?.putString(KEY_FULL_NAME, fullName)
        settings?.putInt(KEY_BIRTH_YEAR, birthYear)
        settings?.putInt(KEY_BIRTH_MONTH, birthMonth)
        settings?.putInt(KEY_BIRTH_DAY, birthDay)
        settings?.putBoolean(KEY_ONBOARDING_DONE, true)
    }

    fun fullName(): String = settings?.getString(KEY_FULL_NAME, "") ?: ""
    fun birthYear(): Int = settings?.getInt(KEY_BIRTH_YEAR, 2000) ?: 2000
    fun birthMonth(): Int = settings?.getInt(KEY_BIRTH_MONTH, 1) ?: 1
    fun birthDay(): Int = settings?.getInt(KEY_BIRTH_DAY, 1) ?: 1

    /**
     * Local-only records (companion profiles, habits, journal entries) share this class purely
     * because it's the one place that already holds the attached [Settings] instance from
     * App() startup - there's no backend for this app-local data, so a full repository layer
     * would be pure ceremony over a handful of delimited strings.
     */
    fun companionRecords(): List<String> =
        settings?.getString(KEY_COMPANIONS, "")?.split(RECORD_SEPARATOR)?.filter { it.isNotBlank() } ?: emptyList()

    fun saveCompanionRecords(records: List<String>) {
        settings?.putString(KEY_COMPANIONS, records.joinToString(RECORD_SEPARATOR))
    }

    fun habitRecords(): List<String> =
        settings?.getString(KEY_HABITS, "")?.split(RECORD_SEPARATOR)?.filter { it.isNotBlank() } ?: emptyList()

    fun saveHabitRecords(records: List<String>) {
        settings?.putString(KEY_HABITS, records.joinToString(RECORD_SEPARATOR))
    }

    /** One record per day a habit was marked done - lets the Growth feature compute streaks
     *  instead of storing a single done/not-done flag that never resets. */
    fun habitCompletionRecords(): List<String> =
        settings?.getString(KEY_HABIT_COMPLETIONS, "")?.split(RECORD_SEPARATOR)?.filter { it.isNotBlank() } ?: emptyList()

    fun saveHabitCompletionRecords(records: List<String>) {
        settings?.putString(KEY_HABIT_COMPLETIONS, records.joinToString(RECORD_SEPARATOR))
    }

    fun moodRecords(): List<String> =
        settings?.getString(KEY_MOODS, "")?.split(RECORD_SEPARATOR)?.filter { it.isNotBlank() } ?: emptyList()

    fun saveMoodRecords(records: List<String>) {
        settings?.putString(KEY_MOODS, records.joinToString(RECORD_SEPARATOR))
    }

    fun notificationsEnabled(): Boolean = settings?.getBoolean(KEY_NOTIFICATIONS, true) ?: true

    fun setNotificationsEnabled(enabled: Boolean) {
        settings?.putBoolean(KEY_NOTIFICATIONS, enabled)
    }

    fun authToken(): String? = settings?.getStringOrNull(KEY_AUTH_TOKEN)

    fun saveAuthToken(token: String) {
        settings?.putString(KEY_AUTH_TOKEN, token)
    }

    private companion object {
        const val KEY_DEVICE_ID = "device_id"
        const val KEY_ONBOARDING_DONE = "onboarding_done"
        const val KEY_FULL_NAME = "full_name"
        const val KEY_BIRTH_YEAR = "birth_year"
        const val KEY_BIRTH_MONTH = "birth_month"
        const val KEY_BIRTH_DAY = "birth_day"
        const val KEY_COMPANIONS = "companions"
        const val KEY_HABITS = "habits"
        const val KEY_HABIT_COMPLETIONS = "habit_completions"
        const val KEY_MOODS = "moods"
        const val KEY_NOTIFICATIONS = "notifications_enabled"
        const val KEY_AUTH_TOKEN = "auth_token"
        const val RECORD_SEPARATOR = "|~|"
    }
}
