package com.suyash.wealthpulse.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.suyash.wealthpulse.ui.theme.*

@Composable
fun EntryScreen(
    onStartPlan: () -> Unit,
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
                .offset(x = 170.dp, y = (-64).dp)
                .background(PrimaryEmerald.copy(alpha = 0.10f), CircleShape)
        )
        Box(
            modifier = Modifier
                .size(180.dp)
                .align(Alignment.BottomStart)
                .offset(x = (-72).dp, y = 48.dp)
                .background(SecondaryBlue.copy(alpha = 0.08f), CircleShape)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .verticalScroll(scrollState)
                .padding(horizontal = Spacing.Huge, vertical = Spacing.ExtraLarge),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(Spacing.Large))
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(PrimaryEmerald.copy(alpha = 0.12f))
                    .border(BorderStroke(1.dp, PrimaryEmerald.copy(alpha = 0.35f)), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = PrimaryEmerald, modifier = Modifier.size(32.dp))
            }
            Spacer(modifier = Modifier.height(Spacing.Large))
            Text(
                text = "WealthPulse",
                style = AppTypography.headlineLarge,
                color = TextPrimary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(Spacing.Small))
            Text(
                text = "AI money coach for upcoming Indian goals.",
                style = AppTypography.bodyLarge,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(Spacing.Huge))

            Text(
                text = "Tell us your salary, spending, savings, risk comfort, and goal. WealthPulse turns it into a monthly plan with specific buckets.",
                style = AppTypography.bodyMedium,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(Spacing.ExtraLarge))

            Column(verticalArrangement = Arrangement.spacedBy(Spacing.Medium)) {
                EntryFeatureCard(
                    title = "Goal-first planning",
                    text = "Home, emergency fund, laptop, bike, travel, or education.",
                    icon = { Icon(Icons.Default.TrackChanges, contentDescription = null, tint = PrimaryEmerald) }
                )
                EntryFeatureCard(
                    title = "Concrete monthly action",
                    text = "Know how much goes to FD, debt, index, gold, or liquid buckets.",
                    icon = { Icon(Icons.Default.Savings, contentDescription = null, tint = SecondaryBlue) }
                )
                EntryFeatureCard(
                    title = "Cash-flow intelligence",
                    text = "Optional notification insights help the plan react to real spending.",
                    icon = { Icon(Icons.Default.Lock, contentDescription = null, tint = SecondaryOrange) }
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
            Spacer(modifier = Modifier.height(Spacing.Large))
        }
    }
}

@Composable
private fun EntryFeatureCard(
    title: String,
    text: String,
    icon: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark.copy(alpha = 0.88f)),
        border = BorderStroke(1.dp, BorderDark),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier.padding(Spacing.Large),
            horizontalArrangement = Arrangement.spacedBy(Spacing.Medium),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(PrimaryEmerald.copy(alpha = 0.10f)),
                contentAlignment = Alignment.Center
            ) {
                icon()
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, style = AppTypography.titleMedium, color = TextPrimary)
                Spacer(modifier = Modifier.height(Spacing.ExtraSmall))
                Text(text = text, style = AppTypography.bodyMedium, color = TextSecondary)
            }
        }
    }
}
