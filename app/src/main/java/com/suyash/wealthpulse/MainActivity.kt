package com.suyash.wealthpulse

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.suyash.wealthpulse.ui.EntryScreen
import com.suyash.wealthpulse.ui.GoalPlannerPreviewScreen
import com.suyash.wealthpulse.ui.WebViewScreen
import com.suyash.wealthpulse.ui.theme.WealthPulseTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WealthPulseTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var showWebView by remember { mutableStateOf(false) }
                    var showPlanner by remember { mutableStateOf(false) }

                    if (showWebView) {
                        WebViewScreen(url = "https://wealthpulse-ai.lovable.app/goal-planner")
                    } else if (showPlanner) {
                        GoalPlannerPreviewScreen(
                            onOpenFullPlanner = { showWebView = true },
                            onEnableInsights = {
                                val intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
                                startActivity(intent)
                            }
                        )
                    } else {
                        EntryScreen(
                            onStartPlan = { showPlanner = true },
                            onEnableInsights = {
                                val intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
                                startActivity(intent)
                            }
                        )
                    }
                }
            }
        }
    }
}
