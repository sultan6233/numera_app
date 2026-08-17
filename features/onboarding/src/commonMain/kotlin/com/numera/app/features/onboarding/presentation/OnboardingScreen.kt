package com.numera.app.features.onboarding.presentation

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.numera.app.core.commonMain.components.DefaultNumeraButton
import com.numera.app.core.commonMain.components.NumberWheel
import com.numera.app.core.commonMain.displayFontFamily
import org.jetbrains.compose.resources.stringResource
import numera.features.onboarding.generated.resources.Res
import numera.features.onboarding.generated.resources.onboarding_name_question
import numera.features.onboarding.generated.resources.onboarding_full_name
import numera.features.onboarding.generated.resources.next
import numera.features.onboarding.generated.resources.onboarding_birth_date_title
import numera.features.onboarding.generated.resources.day_placeholder
import numera.features.onboarding.generated.resources.month_placeholder
import numera.features.onboarding.generated.resources.year_placeholder
import numera.features.onboarding.generated.resources.calculate
import numera.features.onboarding.generated.resources.calculating_profile
import numera.features.onboarding.generated.resources.life_path_number_label
import numera.features.onboarding.generated.resources.see_full_picture

@Composable
fun OnboardingScreen(state: OnboardingState, onAction: (OnboardingAction) -> Unit) {
    Scaffold { innerPadding ->
        when (state) {
            is OnboardingState.Content -> Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
                Crossfade(targetState = state.step, label = "onboarding_step") { step ->
                    when (step) {
                        OnboardingStep.NAME -> NameStep(state.name, onAction)
                        OnboardingStep.BIRTH_DATE -> BirthDateStep(state, onAction)
                        OnboardingStep.CALCULATING -> CalculatingStep()
                        OnboardingStep.TEASER -> TeaserStep(state.lifePathPreview, onAction)
                    }
                }
            }
        }
    }
}

@Composable
private fun NameStep(name: String, onAction: (OnboardingAction) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(Res.string.onboarding_name_question),
            fontFamily = displayFontFamily(),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(24.dp))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = name,
            onValueChange = { onAction(OnboardingAction.OnNameChanged(it)) },
            placeholder = { Text(stringResource(Res.string.onboarding_full_name)) }
        )
        Spacer(modifier = Modifier.height(24.dp))
        DefaultNumeraButton(
            text = stringResource(Res.string.next),
            enabled = name.isNotBlank(),
            onClick = { onAction(OnboardingAction.OnNameNext) }
        )
    }
}

@Composable
private fun BirthDateStep(state: OnboardingState.Content, onAction: (OnboardingAction) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(Res.string.onboarding_birth_date_title),
            fontFamily = displayFontFamily(),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(24.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedTextField(
                modifier = Modifier.width(80.dp),
                value = state.day,
                onValueChange = { onAction(OnboardingAction.OnDayChanged(it)) },
                placeholder = { Text(stringResource(Res.string.day_placeholder)) }
            )
            OutlinedTextField(
                modifier = Modifier.width(80.dp),
                value = state.month,
                onValueChange = { onAction(OnboardingAction.OnMonthChanged(it)) },
                placeholder = { Text(stringResource(Res.string.month_placeholder)) }
            )
            OutlinedTextField(
                modifier = Modifier.width(110.dp),
                value = state.year,
                onValueChange = { onAction(OnboardingAction.OnYearChanged(it)) },
                placeholder = { Text(stringResource(Res.string.year_placeholder)) }
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        DefaultNumeraButton(
            text = stringResource(Res.string.calculate),
            onClick = { onAction(OnboardingAction.OnBirthDateNext) }
        )
    }
}

@Composable
private fun CalculatingStep() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(Res.string.calculating_profile),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 14.sp
        )
    }
}

@Composable
private fun TeaserStep(lifePath: Int?, onAction: (OnboardingAction) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        NumberWheel(litNumbers = setOfNotNull(lifePath), diameter = 220.dp) {
            Text(
                text = lifePath?.toString() ?: "",
                fontFamily = displayFontFamily(),
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = stringResource(Res.string.life_path_number_label),
            fontSize = 15.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(28.dp))
        DefaultNumeraButton(
            text = stringResource(Res.string.see_full_picture),
            onClick = { onAction(OnboardingAction.OnContinueClick) }
        )
    }
}
