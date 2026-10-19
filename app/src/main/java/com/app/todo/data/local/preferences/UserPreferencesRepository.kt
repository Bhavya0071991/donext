package com.app.todo.data.local.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

@Singleton
class UserPreferencesRepository @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    private object Keys {
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val REMINDERS_ENABLED = booleanPreferencesKey("reminders_enabled")
        val DEFAULT_REMINDER_TIME = stringPreferencesKey("default_reminder_time")
        val SORT_TASKS_BY = stringPreferencesKey("sort_tasks_by")
        val SHOW_COMPLETED_TASKS = booleanPreferencesKey("show_completed_tasks")
        val LAST_BACKUP_DATE = stringPreferencesKey("last_backup_date")
    }

    val themeMode: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[Keys.THEME_MODE] ?: "System default"
    }

    val remindersEnabled: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[Keys.REMINDERS_ENABLED] ?: true
    }

    val defaultReminderTime: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[Keys.DEFAULT_REMINDER_TIME] ?: "30 minutes before"
    }

    val sortTasksBy: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[Keys.SORT_TASKS_BY] ?: "Due date"
    }

    val showCompletedTasks: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[Keys.SHOW_COMPLETED_TASKS] ?: false
    }

    val lastBackupDate: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[Keys.LAST_BACKUP_DATE] ?: "Never"
    }

    suspend fun setThemeMode(mode: String) {
        context.dataStore.edit { prefs -> prefs[Keys.THEME_MODE] = mode }
    }

    suspend fun setRemindersEnabled(enabled: Boolean) {
        context.dataStore.edit { prefs -> prefs[Keys.REMINDERS_ENABLED] = enabled }
    }

    suspend fun setDefaultReminderTime(time: String) {
        context.dataStore.edit { prefs -> prefs[Keys.DEFAULT_REMINDER_TIME] = time }
    }

    suspend fun setSortTasksBy(sortBy: String) {
        context.dataStore.edit { prefs -> prefs[Keys.SORT_TASKS_BY] = sortBy }
    }

    suspend fun setShowCompletedTasks(show: Boolean) {
        context.dataStore.edit { prefs -> prefs[Keys.SHOW_COMPLETED_TASKS] = show }
    }

    suspend fun setLastBackupDate(dateText: String) {
        context.dataStore.edit { prefs -> prefs[Keys.LAST_BACKUP_DATE] = dateText }
    }
}
