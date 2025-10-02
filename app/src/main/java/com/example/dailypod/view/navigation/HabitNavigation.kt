package com.example.dailypod.view.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.dailypod.view.screens.CalendarScreen
import com.example.dailypod.view.screens.SettingsScreen
import com.example.dailypod.view.screens.StatsScreen
import com.example.dailypod.view.screens.TodayScreen
import com.example.dailypod.viewmodel.HabitViewModel

@Suppress("ktlint:standard:function-naming")
@Composable
fun HabitNavigation(
    navController: NavHostController,
    viewModel: HabitViewModel,
    onShowHabitForm: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val habits by viewModel.habits.collectAsStateWithLifecycle()
    val todayEntries by viewModel.todayEntries.collectAsStateWithLifecycle()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: "today"

    NavHost(
        navController = navController,
        startDestination = "today",
        modifier = modifier,
    ) {
        composable("today") {
            TodayScreen(
                habitTemplateEntities = habits,
                todayEntries = todayEntries,
                onToggleHabit = { habitId ->
                    viewModel.toggleHabitCompletion(habitId)
                },
                onShowHabitForm = onShowHabitForm,
                getHabitProgressEntity = { habitId ->
                    viewModel.getHabitStats(habitId)
                },
            )
        }

        composable("stats") {
            StatsScreen(
                habitTemplateEntities = habits,
                todayEntries = todayEntries,
                getHabitProgressEntity = { habitId ->
                    viewModel.getHabitStats(habitId)
                },
            )
        }

        composable("calendar") {
            CalendarScreen()
        }

        composable("settings") {
            SettingsScreen()
        }
    }
}
