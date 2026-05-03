package com.suyash.wealthpulse.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.suyash.wealthpulse.ui.theme.*

@Composable
fun GoalPlannerPreviewScreen(
    onOpenFullPlanner: () -> Unit,
    onEnableInsights: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .padding(horizontal = Spacing.Large, vertical = Spacing.ExtraLarge)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(Spacing.Large)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(Spacing.Small)) {
            Text(text = "AI Goal Plan", style = AppTypography.headlineLarge, color = TextPrimary)
            Text(
                text = "Demo profile: 24 years, Rs. 75k salary, Rs. 45k expenses, Rs. 80k savings, 18-month goal.",
                style = AppTypography.bodyMedium,
                color = TextSecondary
            )
        }

        WealthPulseCard {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "Recommended monthly action", style = AppTypography.labelSmall, color = TextMuted)
                    Spacer(modifier = Modifier.height(Spacing.ExtraSmall))
                    Text(text = "Rs. 11,950", style = AppTypography.headlineMedium, color = TextPrimary)
                    Text(text = "Tight but possible", style = AppTypography.bodyMedium, color = TextSecondary)
                }
                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = PrimaryEmerald)
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(Spacing.Medium)) {
            PlanMetricCard(
                title = "Safe bucket",
                value = "70%",
                subtitle = "FD, RD, liquid style",
                icon = { Icon(Icons.Default.Security, contentDescription = null, tint = PrimaryEmerald) },
                modifier = Modifier.weight(1f)
            )
            PlanMetricCard(
                title = "Growth bucket",
                value = "30%",
                subtitle = "Measured upside",
                icon = { Icon(Icons.Default.TrendingUp, contentDescription = null, tint = SecondaryBlue) },
                modifier = Modifier.weight(1f)
            )
        }

        WealthPulseCard {
            Text(text = "Why this plan?", style = AppTypography.titleLarge, color = TextPrimary)
            Spacer(modifier = Modifier.height(Spacing.Medium))
            InsightLine("18 months is intermediate term, so the plan balances stability with controlled growth.")
            InsightLine("Emergency money stays separate before any aggressive investment.")
            InsightLine("The plan uses real cash-flow context instead of asking users to pick asset classes first.")
        }

        WealthPulseCard {
            Text(text = "Native moat", style = AppTypography.titleLarge, color = TextPrimary)
            Spacer(modifier = Modifier.height(Spacing.Medium))
            Row(horizontalArrangement = Arrangement.spacedBy(Spacing.Medium)) {
                Icon(Icons.Default.AccountBalanceWallet, contentDescription = null, tint = SecondaryOrange)
                Text(
                    text = "With permission, WealthPulse can understand UPI and bank transaction signals and update goals when spending changes.",
                    style = AppTypography.bodyMedium,
                    color = TextSecondary,
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(Spacing.Large))
            WealthPulseButtonOutlined(
                text = "Enable transaction insights",
                onClick = onEnableInsights
            )
        }

        WealthPulseButtonFilled(
            text = "Open full planner",
            onClick = onOpenFullPlanner
        )
    }
}

@Composable
private fun InsightLine(text: String) {
    Row(horizontalArrangement = Arrangement.spacedBy(Spacing.Medium)) {
        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = PrimaryEmerald, modifier = Modifier.size(20.dp))
        Text(text = text, style = AppTypography.bodyMedium, color = TextSecondary, modifier = Modifier.weight(1f))
    }
}

@Composable
private fun PlanMetricCard(
    title: String,
    value: String,
    subtitle: String,
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    WealthPulseCard(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column {
                Text(text = title, color = TextSecondary, style = AppTypography.labelSmall)
                Spacer(modifier = Modifier.height(Spacing.ExtraSmall))
                Text(text = value, color = TextPrimary, style = AppTypography.headlineMedium, fontWeight = FontWeight.Bold)
                Text(text = subtitle, color = TextMuted, style = AppTypography.labelSmall)
            }
            icon()
        }
    }
}
