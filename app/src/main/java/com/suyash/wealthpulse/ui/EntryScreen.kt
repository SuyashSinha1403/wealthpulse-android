package com.suyash.wealthpulse.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.suyash.wealthpulse.ui.theme.*

@Composable
fun EntryScreen(
    onStartPlan: () -> Unit,
    onEnableInsights: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .padding(Spacing.Huge),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "WealthPulse",
            style = AppTypography.headlineLarge,
            color = PrimaryEmerald
        )
        Spacer(modifier = Modifier.height(Spacing.Large))
        Text(
            text = "AI money coach for short-term and medium-term goals.",
            style = AppTypography.bodyLarge,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(Spacing.ExtraLarge))
        
        WealthPulseCard {
            Text(
                text = "Plan before you invest. WealthPulse asks about age, salary, expenses, savings, liabilities, dependents, timeline, and risk before suggesting what should stay safe and what can grow.",
                style = AppTypography.bodyMedium,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )
        }
        
        Spacer(modifier = Modifier.height(Spacing.ExtraLarge))
        
        WealthPulseButtonFilled(
            text = "Start AI goal plan",
            onClick = onStartPlan
        )
        Spacer(modifier = Modifier.height(Spacing.Medium))
        WealthPulseButtonOutlined(
            text = "Enable transaction insights",
            onClick = onEnableInsights
        )
    }
}
