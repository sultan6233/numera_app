package com.numera.app.features.paywall.presentation

import androidx.compose.runtime.Stable
import com.numera.app.core.billing.SubscriptionProducts
import com.numera.app.core.commonMain.components.NumeraGlyph
import com.numera.app.features.paywall.presentation.model.PlanUi
import com.numera.app.features.paywall.presentation.model.ValueItemUi
import numera.features.paywall.generated.resources.Res
import numera.features.paywall.generated.resources.plan_weekly_title
import numera.features.paywall.generated.resources.plan_weekly_price
import numera.features.paywall.generated.resources.plan_weekly_period
import numera.features.paywall.generated.resources.plan_monthly_title
import numera.features.paywall.generated.resources.plan_monthly_price
import numera.features.paywall.generated.resources.plan_monthly_period
import numera.features.paywall.generated.resources.plan_yearly_title
import numera.features.paywall.generated.resources.plan_yearly_price
import numera.features.paywall.generated.resources.plan_yearly_period
import numera.features.paywall.generated.resources.plan_yearly_badge
import numera.features.paywall.generated.resources.plan_yearly_per_day
import numera.features.paywall.generated.resources.paywall_value_1
import numera.features.paywall.generated.resources.paywall_value_2
import numera.features.paywall.generated.resources.paywall_value_3
import numera.features.paywall.generated.resources.paywall_value_4

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
    PlanUi(SubscriptionProducts.WEEKLY, Res.string.plan_weekly_title, Res.string.plan_weekly_price, Res.string.plan_weekly_period),
    PlanUi(SubscriptionProducts.MONTHLY, Res.string.plan_monthly_title, Res.string.plan_monthly_price, Res.string.plan_monthly_period),
    PlanUi(
        SubscriptionProducts.YEARLY, Res.string.plan_yearly_title, Res.string.plan_yearly_price, Res.string.plan_yearly_period,
        badgeRes = Res.string.plan_yearly_badge, perDayNoteRes = Res.string.plan_yearly_per_day
    )
)

private val DEFAULT_VALUE_ITEMS = listOf(
    ValueItemUi(NumeraGlyph.Spiral, Res.string.paywall_value_1),
    ValueItemUi(NumeraGlyph.Sun, Res.string.paywall_value_2),
    ValueItemUi(NumeraGlyph.Duality, Res.string.paywall_value_3),
    ValueItemUi(NumeraGlyph.Calendar, Res.string.paywall_value_4)
)
