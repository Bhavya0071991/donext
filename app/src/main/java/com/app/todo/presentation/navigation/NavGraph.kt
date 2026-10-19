package com.app.todo.presentation.navigation
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.app.todo.presentation.calendar.CalendarScreen
import com.app.todo.presentation.calendar.CalendarViewModel
import com.app.todo.presentation.lists.ListsScreen
import com.app.todo.presentation.lists.ListsViewModel
import com.app.todo.presentation.onboarding.OnboardingScreen
import com.app.todo.presentation.settings.SettingsScreen
import com.app.todo.presentation.settings.SettingsViewModel
import com.app.todo.presentation.taskdetail.TaskDetailScreen
import com.app.todo.presentation.taskdetail.TaskDetailViewModel
import com.app.todo.presentation.tasks.TasksScreen
import com.app.todo.presentation.tasks.TasksViewModel

@Composable
fun AppNavGraph(
    startDestination: String = Screen.Onboarding.route
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = currentRoute in listOf(
        Screen.Tasks.route,
        Screen.Calendar.route,
        Screen.Lists.route,
        Screen.Settings.route
    )

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                TodoBottomBar(
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
        ) {
            // Screen 1: Onboarding
            composable(Screen.Onboarding.route) {
                OnboardingScreen(
                    onGetStartedClick = {
                        navController.navigate(Screen.Tasks.route) {
                            popUpTo(Screen.Onboarding.route) { inclusive = true }
                        }
                    }
                )
            }

            // Screen 2: Tasks Screen
            composable(Screen.Tasks.route) {
                val viewModel: TasksViewModel = hiltViewModel()
                TasksScreen(
                    viewModel = viewModel,
                    onTaskClick = { taskId ->
                        navController.navigate(Screen.TaskDetails.createRoute(taskId))
                    }
                )
            }

            // Screen 3: Calendar (Placeholder for next step)
            composable(Screen.Calendar.route) {
                // Screen 3: Calendar

                    val viewModel: CalendarViewModel = hiltViewModel()
                    CalendarScreen(
                        viewModel = viewModel,
                        onTaskClick = { taskId ->
                            navController.navigate(Screen.TaskDetails.createRoute(taskId))
                        }
                    )

            }

            // Screen 4: Lists (Placeholder for next step)
            composable(Screen.Lists.route) {
                val viewModel: ListsViewModel = hiltViewModel()
                ListsScreen(
                    viewModel = viewModel,
                    onListClick = { listId -> }
                )
            }

            // Screen 5: Settings
            composable(Screen.Settings.route) {
                val viewModel: SettingsViewModel = hiltViewModel()
                SettingsScreen(viewModel = viewModel)
            }

            // Task Detail Route
            composable(Screen.TaskDetails.route) {
                val viewModel: TaskDetailViewModel = hiltViewModel()
                TaskDetailScreen(viewModel = viewModel,
                    onBackClick = {navController.popBackStack()})
            }
        }
    }
}