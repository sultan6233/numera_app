package com.numera.app.features.paywall.presentation

import androidx.compose.runtime.Stable
import com.numera.app.core.billing.SubscriptionProducts
import com.numera.app.features.paywall.presentation.model.PlanUi
import com.numera.app.features.paywall.presentation.model.ValueItemUi

sealed interface PaywallState {
    @Stable
    data class Content(
        val lifePathNumber: Int? = null,
        val expressionNumber: Int? = null,
        val plans: List<PlanUi> = DEFAULT_PLANS,
        val valueItems: List<ValueItemUi> = DEFAULT_VALUE_ITEMS,
        val selectedProductId: String = SubscriptionProducts.YEARLY,
        val isPurchasing: Boolean = false
    ) : PaywallState
}

private val DEFAULT_PLANS = listOf(
    PlanUi(SubscriptionProducts.WEEKLY, "Неделя", "699 ₽", "в неделю"),
    PlanUi(SubscriptionProducts.MONTHLY, "Месяц", "1 190 ₽", "в месяц"),
    PlanUi(
        SubscriptionProducts.YEARLY, "Год", "4 990 ₽", "в год",
        badge = "Выгоднее всего", perDayNote = "≈ 14 ₽ в день"
    )
)

private val DEFAULT_VALUE_ITEMS = listOf(
    ValueItemUi("💫", "Число души, личности и ещё 4 расчёта"),
    ValueItemUi("✨", "Новый персональный разбор каждый день"),
    ValueItemUi("💞", "Совместимость с близкими"),
    ValueItemUi("📅", "Личный календарь энергии на каждый день")
)
