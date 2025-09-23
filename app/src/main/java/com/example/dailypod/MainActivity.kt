package com.example.dailypod

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dailypod.habits.ui.theme.DailyPodTheme
import com.example.dailypod.data.db.HabitDatabase
import com.example.dailypod.repository.HabitRepository
import com.example.dailypod.view.components.BottomNavigationBar
import com.example.dailypod.view.components.HabitForm
import com.example.dailypod.view.navigation.HabitNavigation
import com.example.dailypod.viewmodel.HabitViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DailyPodTheme {
                val database = HabitDatabase.getDatabase(this)
                val repository = HabitRepository(database.habitDao())
                val viewModel: HabitViewModel = viewModel {
                    HabitViewModel(repository)
                }

                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route ?: "today"

                var showHabitForm by remember { mutableStateOf(false) }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        BottomNavigationBar(
                            currentRoute = currentRoute,
                            onNavigate = { route ->
                                if (route == "add") {
                                    showHabitForm = true
                                } else {
                                    navController.navigate(route) {
                                        popUpTo(navController.graph.startDestinationId) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            }
                        )
                    }
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        HabitNavigation(
                            navController = navController,
                            viewModel = viewModel,
                            onShowHabitForm = { showHabitForm = true }
                        )
                    }
                }

                if (showHabitForm) {
                    HabitForm(
                        onSubmit = { habitData ->
                            viewModel.addHabit(
                                name = habitData.name,
                                description = habitData.description,
                                color = habitData.color,
                                icon = habitData.icon,
                                targetFrequency = habitData.targetFrequency,
                                targetCount = habitData.targetCount
                            )
                            showHabitForm = false
                        },
                        onCancel = { showHabitForm = false }
                    )
                }
            }
        }
    }
}
