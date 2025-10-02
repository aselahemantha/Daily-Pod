package com.example.dailypod.view.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.dailypod.data.entity.HabitRecordEntity
import com.example.dailypod.data.entity.HabitTemplateEntity
import com.example.dailypod.view.components.HabitStatsCard
import com.example.dailypod.view.components.StatsCard
import kotlinx.coroutines.runBlocking

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsScreen(
    habitTemplateEntities: List<HabitTemplateEntity>,
    todayEntries: List<HabitRecordEntity>,
    getHabitProgressEntity: suspend (String) -> com.example.dailypod.data.entity.HabitProgressEntity,
    modifier: Modifier = Modifier,
) {
    val completedToday = todayEntries.count { it.completed }
    val todayProgress =
        if (habitTemplateEntities.isNotEmpty()) (completedToday.toFloat() / habitTemplateEntities.size * 100).toInt() else 0

    LazyColumn(
        modifier =
            modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
    ) {
        // Header
        item {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = "Statistics",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = "Track your progress over time",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        // Today's Overview
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                StatsCard(
                    title = "Today",
                    subtitle = "Today",
                    value = "$completedToday/${habitTemplateEntities.size}",
                    icon = Icons.Default.Home,
                    progress = todayProgress.toFloat(),
                    showProgress = true,
                    modifier = Modifier.weight(1f),
                )

                StatsCard(
                    title = "Average Rate",
                    subtitle = "Avg Rate",
                    value = "${
                        calculateAverageCompletionRate(
                            habitTemplateEntities,
                            getHabitProgressEntity,
                        )
                    }%",
                    icon = Icons.Default.Home,
                    modifier = Modifier.weight(1f),
                )
            }
        }

        // Habit Statistics
        item {
            Text(
                text = "Habit Progress",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }

        if (habitTemplateEntities.isEmpty()) {
            item {
                EmptyStatsCard()
            }
        } else {
            items(habitTemplateEntities) { habit ->
                val stats by remember(habit.id) {
                    derivedStateOf {
                        runBlocking { getHabitProgressEntity(habit.id) }
                    }
                }

                HabitStatsCard(
                    habitTemplateEntity = habit,
                    stats = stats,
                )
            }
        }

        // Achievement Summary
        if (habitTemplateEntities.isNotEmpty()) {
            item {
                AchievementCard(
                    habitTemplateEntities = habitTemplateEntities,
                    getHabitProgressEntity = getHabitProgressEntity,
                )
            }
        }
    }
}

@Composable
private fun EmptyStatsCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                imageVector = Icons.Default.Home,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(48.dp),
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "No habits created yet",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Create your first habit to start tracking progress",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AchievementCard(
    habitTemplateEntities: List<HabitTemplateEntity>,
    getHabitProgressEntity: suspend (String) -> com.example.dailypod.data.entity.HabitProgressEntity,
    modifier: Modifier = Modifier,
) {
    val totalStreaks =
        remember {
            derivedStateOf {
                runBlocking {
                    habitTemplateEntities.sumOf { habit ->
                        getHabitProgressEntity(habit.id).currentStreak
                    }
                }
            }
        }

    Card(
        modifier = modifier.fillMaxWidth(),
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier =
                    Modifier
                        .size(48.dp)
                        .background(
                            Color(0xFF10B981),
                            RoundedCornerShape(24.dp),
                        ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp),
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = "Total Active Streaks",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = "${totalStreaks.value} days",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}

@Composable
private fun calculateAverageCompletionRate(
    habitTemplateEntities: List<HabitTemplateEntity>,
    getHabitProgressEntity: suspend (String) -> com.example.dailypod.data.entity.HabitProgressEntity,
): Int =
    if (habitTemplateEntities.isEmpty()) {
        0
    } else {
        runBlocking {
            val totalRate =
                habitTemplateEntities.sumOf { habit ->
                    getHabitProgressEntity(habit.id).completionRate
                }
            totalRate / habitTemplateEntities.size
        }
    }
