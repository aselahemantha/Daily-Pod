package com.example.dailypod.view.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.dailypod.data.entity.HabitTemplateEntity
import com.example.dailypod.view.components.HabitCard
import com.example.dailypod.view.util.scale.scaleHeight
import com.example.dailypod.view.util.scale.scaleWidth
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.*
import com.example.dailypod.R
import com.example.dailypod.view.util.scale.scaleElevation
import com.example.dailypod.view.util.scale.scaleRadius

@Suppress("ktlint:standard:function-naming")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodayScreen(
    habitTemplateEntities: List<HabitTemplateEntity>,
    todayEntries: List<com.example.dailypod.data.entity.HabitRecordEntity>,
    onToggleHabit: (String) -> Unit,
    onShowHabitForm: () -> Unit,
    getHabitProgressEntity: suspend (String) -> com.example.dailypod.data.entity.HabitProgressEntity,
    modifier: Modifier = Modifier,
) {
    val completedToday = todayEntries.count { it.completed }
    val todayProgress =
        if (habitTemplateEntities.isNotEmpty()) (completedToday.toFloat() / habitTemplateEntities.size * 100).toInt() else 0

    val dateFormatter = SimpleDateFormat("EEEE, MMMM d", Locale.getDefault())
    val todayDate = dateFormatter.format(Date())

    LazyColumn(
        modifier =
            modifier
                .fillMaxSize()
                .padding(horizontal = 16f.scaleWidth()),
        verticalArrangement = Arrangement.spacedBy(16f.scaleHeight()),
        contentPadding = PaddingValues(vertical = 16f.scaleHeight()),
    ) {
        // Header
        item {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = stringResource(R.string.APPNAME),
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                )
                Text(
                    text = todayDate,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        // Progress Summary
        if (habitTemplateEntities.isNotEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors =
                        CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                        ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    shape = RoundedCornerShape(12f.scaleRadius()),
                ) {
                    Row(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(16f.scaleWidth()),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column {
                            Text(
                                text = "Today's Progress",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                            Text(
                                text = "$completedToday / ${habitTemplateEntities.size}",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                        }

                        Box(
                            modifier =
                                Modifier
                                    .size(64.dp)
                                    .background(
                                        MaterialTheme.colorScheme.primary,
                                        RoundedCornerShape(32.dp),
                                    ),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = "$todayProgress%",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimary,
                            )
                        }
                    }
                }
            }
        }

        // Habits List
        if (habitTemplateEntities.isEmpty()) {
            item {
                EmptyStateCard(
                    onShowHabitForm = onShowHabitForm,
                )
            }
        } else {
            items(habitTemplateEntities) { habit ->
                val isCompleted = todayEntries.any { it.habitId == habit.id && it.completed }

                // Use remember to cache stats for each habit
                val stats by remember(habit.id) {
                    derivedStateOf {
                        runBlocking { getHabitProgressEntity(habit.id) }
                    }
                }

                HabitCard(
                    habitTemplateEntity = habit,
                    isCompleted = isCompleted,
                    streak = stats.currentStreak,
                    onToggle = { onToggleHabit(habit.id) },
                )
            }
        }
    }
}

@Composable
private fun EmptyStateCard(
    onShowHabitForm: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4f.scaleElevation()),
        shape = RoundedCornerShape(12f.scaleRadius()),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(32f.scaleWidth()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier =
                    Modifier
                        .size(64f.scaleWidth())
                        .background(
                            MaterialTheme.colorScheme.primary,
                            RoundedCornerShape(32f.scaleRadius()),
                        ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(R.drawable.home_icon),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(32.dp),
                )
            }

            Spacer(modifier = Modifier.height(16f.scaleHeight()))

            Text(
                text = "Start Your Journey",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
            )

            Spacer(modifier = Modifier.height(8f.scaleHeight()))

            Text(
                text = "Create your first habit to begin tracking your daily progress.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(24f.scaleHeight()))

            Button(
                onClick = onShowHabitForm,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Create First Habit")
            }
        }
    }
}
