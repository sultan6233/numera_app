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
            text = "Как вас зовут?",
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
            placeholder = { Text("Полное имя") }
        )
        Spacer(modifier = Modifier.height(24.dp))
        DefaultNumeraButton(
            text = "Далее",
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
            text = "Дата рождения",
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
                placeholder = { Text("ДД") }
            )
            OutlinedTextField(
                modifier = Modifier.width(80.dp),
                value = state.month,
                onValueChange = { onAction(OnboardingAction.OnMonthChanged(it)) },
                placeholder = { Text("ММ") }
            )
            OutlinedTextField(
                modifier = Modifier.width(110.dp),
                value = state.year,
                onValueChange = { onAction(OnboardingAction.OnYearChanged(it)) },
                placeholder = { Text("ГГГГ") }
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        DefaultNumeraButton(
            text = "Рассчитать",
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
            text = "Рассчитываем ваш профиль…",
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
            text = "Ваше число жизненного пути",
            fontSize = 15.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(28.dp))
        DefaultNumeraButton(
            text = "Узнать полную картину",
            onClick = { onAction(OnboardingAction.OnContinueClick) }
        )
    }
}
