package com.app.todo.presentation.settings
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.todo.data.local.preferences.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        observePreferences()
    }

    private fun observePreferences() {
        viewModelScope.launch {
            combine(
                combine(
                    preferencesRepository.themeMode,
                    preferencesRepository.remindersEnabled,
                    preferencesRepository.defaultReminderTime,
                    preferencesRepository.sortTasksBy,
                    preferencesRepository.showCompletedTasks
                ) { theme, reminders, reminderTime, sortBy, showCompleted ->
                    listOf(theme, reminders, reminderTime, sortBy, showCompleted)
                },
                preferencesRepository.lastBackupDate
            ) { prefsList, backupDate ->
                val theme = prefsList[0] as String
                val reminders = prefsList[1] as Boolean
                val reminderTime = prefsList[2] as String
                val sortBy = prefsList[3] as String
                val showCompleted = prefsList[4] as Boolean

                SettingsUiState(
                    themeMode = theme,
                    remindersEnabled = reminders,
                    defaultReminderTime = reminderTime,
                    sortTasksBy = sortBy,
                    showCompletedTasks = showCompleted,
                    lastBackupDate = backupDate,
                    activeDialog = _uiState.value.activeDialog
                )
            }.collect { newState ->
                _uiState.value = newState
            }
        }
    }

    fun onEvent(event: SettingsUiEvent) {
        when (event) {
            is SettingsUiEvent.OnThemeChanged -> {
                viewModelScope.launch {
                    preferencesRepository.setThemeMode(event.theme)
                    _uiState.update { it.copy(activeDialog = SettingsDialogType.NONE) }
                }
            }
            is SettingsUiEvent.OnRemindersToggled -> {
                viewModelScope.launch {
                    preferencesRepository.setRemindersEnabled(event.enabled)
                }
            }
            is SettingsUiEvent.OnDefaultReminderTimeChanged -> {
                viewModelScope.launch {
                    preferencesRepository.setDefaultReminderTime(event.time)
                    _uiState.update { it.copy(activeDialog = SettingsDialogType.NONE) }
                }
            }
            is SettingsUiEvent.OnSortByChanged -> {
                viewModelScope.launch {
                    preferencesRepository.setSortTasksBy(event.sortBy)
                    _uiState.update { it.copy(activeDialog = SettingsDialogType.NONE) }
                }
            }
            is SettingsUiEvent.OnShowCompletedToggled -> {
                viewModelScope.launch {
                    preferencesRepository.setShowCompletedTasks(event.show)
                }
            }
            SettingsUiEvent.OnPerformBackup -> {
                viewModelScope.launch {
                    val dateText = SimpleDateFormat("dd MMM yyyy, h:mm a", Locale.getDefault()).format(Date())
                    preferencesRepository.setLastBackupDate(dateText)
                    _uiState.update { it.copy(activeDialog = SettingsDialogType.NONE) }
                }
            }
            is SettingsUiEvent.OnOpenDialog -> {
                _uiState.update { it.copy(activeDialog = event.dialogType) }
            }
            SettingsUiEvent.OnDismissDialog -> {
                _uiState.update { it.copy(activeDialog = SettingsDialogType.NONE) }
            }
        }
    }
}