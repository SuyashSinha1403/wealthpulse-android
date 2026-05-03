package com.suyash.wealthpulse.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.suyash.wealthpulse.ui.theme.*

@Composable
fun GoalPlannerPreviewScreen(
    onOpenFullPlanner: () -> Unit,
    onEnableInsights: () -> Unit
) {
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(BackgroundDark, SurfaceDark, BackgroundDark)
                )
            )
    ) {
        Box(
            modifier = Modifier
                .size(220.dp)
                .align(Alignment.TopEnd)
                .offset(x = 86.dp, y = (-70).dp)
                .background(PrimaryEmerald.copy(alpha = 0.10f), CircleShape)
        )
        Box(
            modifier = Modifier
                .size(190.dp)
                .align(Alignment.CenterStart)
                .offset(x = (-96).dp)
                .background(SecondaryPurple.copy(alpha = 0.08f), CircleShape)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = Spacing.Large, vertical = Spacing.ExtraLarge)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(Spacing.Large)
        ) {
            HeroPlannerCard()

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
                        Text(text = "Split across safe and growth buckets", style = AppTypography.bodyMedium, color = TextSecondary)
                    }
                    PlanScoreBadge(score = "72")
                }
            }

            WealthPulseCard {
                SectionTitle(icon = { Icon(Icons.Default.Security, contentDescription = null, tint = PrimaryEmerald) }, title = "Exact monthly split")
                Spacer(modifier = Modifier.height(Spacing.Medium))
                AllocationRow(
                    name = "FD / RD ladder",
                    note = "Principal-safe base for the first 12 months.",
                    percent = "45%",
                    amount = "Rs. 5,380"
                )
                AllocationRow(
                    name = "Liquid or ultra-short debt fund",
                    note = "Flexible buffer for goal timing changes.",
                    percent = "25%",
                    amount = "Rs. 2,988"
                )
                AllocationRow(
                    name = "Conservative hybrid fund",
                    note = "Measured upside without making the goal a trading bet.",
                    percent = "20%",
                    amount = "Rs. 2,390"
                )
                AllocationRow(
                    name = "Gold ETF / SGB style hedge",
                    note = "Small diversifier for inflation and currency risk.",
                    percent = "10%",
                    amount = "Rs. 1,192",
                    isLast = true
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(Spacing.Medium)) {
                PlanMetricCard(
                    title = "Safe bucket",
                    value = "70%",
                    subtitle = "FD, RD, liquid debt",
                    icon = { Icon(Icons.Default.Security, contentDescription = null, tint = PrimaryEmerald) },
                    modifier = Modifier.weight(1f)
                )
                PlanMetricCard(
                    title = "Growth bucket",
                    value = "30%",
                    subtitle = "Hybrid, gold hedge",
                    icon = { Icon(Icons.Default.TrendingUp, contentDescription = null, tint = SecondaryBlue) },
                    modifier = Modifier.weight(1f)
                )
            }

            WealthPulseCard {
                SectionTitle(icon = { Icon(Icons.Default.Description, contentDescription = null, tint = PrimaryEmerald) }, title = "Why this plan?")
                Spacer(modifier = Modifier.height(Spacing.Medium))
                InsightLine("18 months is intermediate term, so the plan protects the goal before chasing returns.")
                InsightLine("Emergency money stays separate before any aggressive investment.")
                InsightLine("The answer comes from income, expenses, savings, liabilities, risk comfort, and goal timing.")
            }

            WealthPulseCard {
                SectionTitle(icon = { Icon(Icons.Default.AccountBalanceWallet, contentDescription = null, tint = SecondaryOrange) }, title = "AI-native edge")
                Spacer(modifier = Modifier.height(Spacing.Medium))
                Text(
                    text = "With permission, WealthPulse can read spending signals from SMS, UPI, and bank alerts to update the plan when cash flow changes.",
                    style = AppTypography.bodyMedium,
                    color = TextSecondary
                )
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
}

@Composable
private fun InsightLine(text: String) {
    Row(horizontalArrangement = Arrangement.spacedBy(Spacing.Medium)) {
        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = PrimaryEmerald, modifier = Modifier.size(20.dp))
        Text(text = text, style = AppTypography.bodyMedium, color = TextSecondary, modifier = Modifier.weight(1f))
    }
}

@Composable
private fun HeroPlannerCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark.copy(alpha = 0.90f)),
        border = BorderStroke(1.dp, PrimaryEmerald.copy(alpha = 0.20f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        colors = listOf(PrimaryEmerald.copy(alpha = 0.12f), SurfaceDark.copy(alpha = 0.0f))
                    )
                )
                .padding(Spacing.Large),
            verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(Spacing.Small),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = PrimaryEmerald, modifier = Modifier.size(18.dp))
                Text(text = "AI goal planner", style = AppTypography.labelSmall, color = PrimaryEmerald)
            }
            Text(
                text = "Invest for a goal, not an app screen.",
                style = AppTypography.headlineLarge,
                color = TextPrimary
            )
            Text(
                text = "Demo profile: 24 years, Rs. 75k salary, Rs. 45k expenses, Rs. 80k savings, 18-month home goal.",
                style = AppTypography.bodyMedium,
                color = TextSecondary
            )
        }
    }
}

@Composable
private fun PlanScoreBadge(score: String) {
    Box(
        modifier = Modifier
            .size(58.dp)
            .clip(CircleShape)
            .background(PrimaryEmerald.copy(alpha = 0.12f))
            .border(BorderStroke(1.dp, PrimaryEmerald.copy(alpha = 0.35f)), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = score, style = AppTypography.titleLarge, color = TextPrimary, fontWeight = FontWeight.Bold)
            Text(text = "/100", style = AppTypography.labelSmall, color = TextMuted, textAlign = TextAlign.Center)
        }
    }
}

@Composable
private fun SectionTitle(icon: @Composable () -> Unit, title: String) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(Spacing.Small),
        verticalAlignment = Alignment.CenterVertically
    ) {
        icon()
        Text(text = title, style = AppTypography.titleLarge, color = TextPrimary)
    }
}

@Composable
private fun AllocationRow(
    name: String,
    note: String,
    percent: String,
    amount: String,
    isLast: Boolean = false
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Spacing.Medium),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(PrimaryEmerald.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Check, contentDescription = null, tint = PrimaryEmerald, modifier = Modifier.size(16.dp))
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(text = name, style = AppTypography.titleMedium, color = TextPrimary)
                Spacer(modifier = Modifier.height(Spacing.ExtraSmall))
                Text(text = note, style = AppTypography.bodyMedium, color = TextSecondary)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(text = percent, style = AppTypography.titleMedium, color = TextPrimary, fontWeight = FontWeight.Bold)
                Text(text = amount, style = AppTypography.labelSmall, color = PrimaryEmerald)
            }
        }
        if (!isLast) {
            Spacer(modifier = Modifier.height(Spacing.Medium))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(BorderDark.copy(alpha = 0.70f))
            )
            Spacer(modifier = Modifier.height(Spacing.Medium))
        }
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
