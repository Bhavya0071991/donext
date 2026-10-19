package com.app.todo.presentation.calendar

import com.app.todo.domain.model.Task
import java.util.Calendar

data class CalendarUiState(
    val selectedDate: Calendar = Calendar.getInstance(),
    val tasksForSelectedDate: List<Task> = emptyList(),
    val datesWithTasks: Set<String> = emptySet(), // Format: "yyyy-MM-dd"
    val isLoading: Boolean = false
)

sealed interface CalendarUiEvent {
    data class OnDateSelected(val date: Calendar) : CalendarUiEvent
    data class OnTaskCheckedChanged(val task: Task, val isDone: Boolean) : CalendarUiEvent
}