package com.numera.app.features.home.data

import com.numera.app.features.home.domain.HomeRepository
import com.numera.app.features.home.presentation.model.DailyInsightUi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class HomeRepositoryImpl(private val homeApi: HomeApi) : HomeRepository {
    override suspend fun getDailyInsight(date: String): Result<DailyInsightUi> = withContext(Dispatchers.IO) {
        try {
            Result.success(homeApi.getDailyInsight(date).toUi())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
