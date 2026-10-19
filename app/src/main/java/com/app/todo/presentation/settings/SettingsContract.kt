package com.app.todo.presentation.settings

enum class SettingsDialogType {
    THEME, REMINDER_TIME, SORT_BY, BACKUP, NONE
}

data class SettingsUiState(
    val themeMode: String = "System default",
    val remindersEnabled: Boolean = true,
    val defaultReminderTime: String = "30 minutes before",
    val sortTasksBy: String = "Due date",
    val showCompletedTasks: Boolean = false,
    val lastBackupDate: String = "Never",
    val activeDialog: SettingsDialogType = SettingsDialogType.NONE
)

sealed interface SettingsUiEvent {
    data class OnThemeChanged(val theme: String) : SettingsUiEvent
    data class OnRemindersToggled(val enabled: Boolean) : SettingsUiEvent
    data class OnDefaultReminderTimeChanged(val time: String) : SettingsUiEvent
    data class OnSortByChanged(val sortBy: String) : SettingsUiEvent
    data class OnShowCompletedToggled(val show: Boolean) : SettingsUiEvent
    object OnPerformBackup : SettingsUiEvent
    data class OnOpenDialog(val dialogType: SettingsDialogType) : SettingsUiEvent
    object OnDismissDialog : SettingsUiEvent
}