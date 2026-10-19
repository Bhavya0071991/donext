package com.app.todo.presentation.tasks

import com.app.todo.domain.model.Priority
import com.app.todo.domain.model.Task

enum class TaskFilter{
    All, TODAY, UPCOMING, DONE
}

data class TasksUiState(
    val tasks: List<Task> = emptyList(),
    val selectedFilter: TaskFilter = TaskFilter.All,
    val totalTodayCount: Int = 0,
    val completedTodayCount: Int = 0,
    val isLoading: Boolean = false,
    val isAddTaskSheetOpen: Boolean =false
    )

sealed interface TasksUiEvent{
    data class OnFilterSelected(val filter: TaskFilter): TasksUiEvent
    data class OnTaskCheckedChanged(val task: Task, val isDone: Boolean): TasksUiEvent
    data class OnDeleteTask(val taskId: Long) : TasksUiEvent
    data class OnSaveNewTask(
        val title: String,
        val notes: String,
        val dueAt: Long?,
        val priority: Priority,
        val listId: Long? = null
    ) : TasksUiEvent
    data class OnToggleAddTaskSheet(val isOpen: Boolean) : TasksUiEvent
}