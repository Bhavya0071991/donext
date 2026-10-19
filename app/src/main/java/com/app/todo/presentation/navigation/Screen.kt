package com.app.todo.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String) {
    object Onboarding : Screen("onboarding")
    object Tasks : Screen("tasks")
    object Calendar : Screen("calendar")
    object Lists : Screen("lists")
    object Settings : Screen("settings")
    object Search : Screen("search")
    object TaskDetails : Screen("task_details/{taskId}") {
        fun createRoute(taskId: Long) = "task_details/$taskId"
    }
}

// Bottom Navigation Items
sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Tasks : BottomNavItem(Screen.Tasks.route, "Tasks", Icons.AutoMirrored.Filled.List)
    object Calendar : BottomNavItem(Screen.Calendar.route, "Calendar", Icons.Default.DateRange)
    object Lists : BottomNavItem(Screen.Lists.route, "Lists", Icons.Default.GridView)
    object Settings : BottomNavItem(Screen.Settings.route, "Settings", Icons.Default.Settings)
}

val bottomNavItems = listOf(
    BottomNavItem.Tasks,
    BottomNavItem.Calendar,
    BottomNavItem.Lists,
    BottomNavItem.Settings
)