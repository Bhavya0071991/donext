package com.app.todo.presentation.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.todo.ui.theme.AppBackground
import com.app.todo.ui.theme.PrimaryBlue

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = AppBackground
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Settings",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(24.dp))

            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                // Theme Setting
                SettingActionRow(
                    title = "Theme",
                    subtitle = uiState.themeMode,
                    actionText = "Change",
                    onActionClick = {
                        viewModel.onEvent(SettingsUiEvent.OnOpenDialog(SettingsDialogType.THEME))
                    }
                )
                HorizontalDivider(color = Color(0xFFE5E5EA))

                // Reminders Toggle Setting
                SettingToggleRow(
                    title = "Reminders",
                    subtitle = "Notify me when tasks are due",
                    checked = uiState.remindersEnabled,
                    onCheckedChange = { enabled ->
                        viewModel.onEvent(SettingsUiEvent.OnRemindersToggled(enabled))
                    }
                )
                HorizontalDivider(color = Color(0xFFE5E5EA))

                // Default Reminder Setting
                SettingActionRow(
                    title = "Default reminder",
                    subtitle = uiState.defaultReminderTime,
                    actionText = "Change",
                    onActionClick = {
                        viewModel.onEvent(SettingsUiEvent.OnOpenDialog(SettingsDialogType.REMINDER_TIME))
                    }
                )
                HorizontalDivider(color = Color(0xFFE5E5EA))

                // Sort Tasks Setting
                SettingActionRow(
                    title = "Sort tasks by",
                    subtitle = uiState.sortTasksBy,
                    actionText = "Change",
                    onActionClick = {
                        viewModel.onEvent(SettingsUiEvent.OnOpenDialog(SettingsDialogType.SORT_BY))
                    }
                )
                HorizontalDivider(color = Color(0xFFE5E5EA))

                // Show Completed Tasks Toggle Setting
                SettingToggleRow(
                    title = "Show completed tasks",
                    subtitle = "On the home screen",
                    checked = uiState.showCompletedTasks,
                    onCheckedChange = { show ->
                        viewModel.onEvent(SettingsUiEvent.OnShowCompletedToggled(show))
                    }
                )
                HorizontalDivider(color = Color(0xFFE5E5EA))

                // Backup and Restore Setting
                SettingActionRow(
                    title = "Backup and restore",
                    subtitle = "Last backup: ${uiState.lastBackupDate}",
                    actionText = "Change",
                    onActionClick = {
                        viewModel.onEvent(SettingsUiEvent.OnOpenDialog(SettingsDialogType.BACKUP))
                    }
                )
                HorizontalDivider(color = Color(0xFFE5E5EA))

                // About Setting
                Column {
                    Text("About", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Version 1.0.0", fontSize = 14.sp, color = Color.Gray)
                }
            }
        }
    }

    // Dialog Pickers
    when (uiState.activeDialog) {
        SettingsDialogType.THEME -> {
            RadioSelectionDialog(
                title = "Select Theme",
                options = listOf("System default", "Light", "Dark"),
                selectedOption = uiState.themeMode,
                onOptionSelected = { viewModel.onEvent(SettingsUiEvent.OnThemeChanged(it)) },
                onDismiss = { viewModel.onEvent(SettingsUiEvent.OnDismissDialog) }
            )
        }
        SettingsDialogType.REMINDER_TIME -> {
            RadioSelectionDialog(
                title = "Default Reminder",
                options = listOf("15 minutes before", "30 minutes before", "1 hour before", "1 day before"),
                selectedOption = uiState.defaultReminderTime,
                onOptionSelected = { viewModel.onEvent(SettingsUiEvent.OnDefaultReminderTimeChanged(it)) },
                onDismiss = { viewModel.onEvent(SettingsUiEvent.OnDismissDialog) }
            )
        }
        SettingsDialogType.SORT_BY -> {
            RadioSelectionDialog(
                title = "Sort Tasks By",
                options = listOf("Due date", "Priority", "Title", "Creation date"),
                selectedOption = uiState.sortTasksBy,
                onOptionSelected = { viewModel.onEvent(SettingsUiEvent.OnSortByChanged(it)) },
                onDismiss = { viewModel.onEvent(SettingsUiEvent.OnDismissDialog) }
            )
        }
        SettingsDialogType.BACKUP -> {
            AlertDialog(
                onDismissRequest = { viewModel.onEvent(SettingsUiEvent.OnDismissDialog) },
                title = { Text("Backup Data", fontWeight = FontWeight.Bold) },
                text = { Text("Would you like to backup your current database state?") },
                confirmButton = {
                    TextButton(onClick = { viewModel.onEvent(SettingsUiEvent.OnPerformBackup) }) {
                        Text("Backup Now", fontWeight = FontWeight.Bold, color = PrimaryBlue)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { viewModel.onEvent(SettingsUiEvent.OnDismissDialog) }) {
                        Text("Cancel", color = Color.Gray)
                    }
                }
            )
        }
        SettingsDialogType.NONE -> {}
    }
}

@Composable
fun RadioSelectionDialog(
    title: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = title, fontWeight = FontWeight.Bold) },
        text = {
            Column {
                options.forEach { option ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onOptionSelected(option) }
                            .padding(vertical = 8.dp)
                    ) {
                        RadioButton(
                            selected = (option == selectedOption),
                            onClick = { onOptionSelected(option) },
                            colors = RadioButtonDefaults.colors(selectedColor = PrimaryBlue)
                        )
                        Text(text = option, fontSize = 16.sp, modifier = Modifier.padding(start = 8.dp))
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = Color.Gray)
            }
        }
    )
}

@Composable
fun SettingActionRow(
    title: String,
    subtitle: String,
    actionText: String,
    onActionClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Spacer(modifier = Modifier.height(2.dp))
            Text(subtitle, fontSize = 14.sp, color = Color.Gray)
        }

        TextButton(onClick = onActionClick) {
            Text(actionText, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = PrimaryBlue)
        }
    }
}

@Composable
fun SettingToggleRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Spacer(modifier = Modifier.height(2.dp))
            Text(subtitle, fontSize = 14.sp, color = Color.Gray)
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = PrimaryBlue)
        )
    }
}