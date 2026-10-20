package com.app.todo.presentation.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.todo.core.util.DateTimeUtils
import com.app.todo.domain.model.Task
import com.app.todo.domain.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val repository: TaskRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(TasksUiState())
    val uiState: StateFlow<TasksUiState> = _uiState.asStateFlow()

    private val _selectedFilter = MutableStateFlow(TaskFilter.All)

    init {
        observeTasks()
    }

    private fun observeTasks() {
        viewModelScope.launch {
            combine(
                repository.observeTasks(),
                _selectedFilter
            ) { allTasks, filter ->
                val todayStart = DateTimeUtils.getTodayStartMillis()
                val todayEnd = DateTimeUtils.getTodayEndMillis()

                val todayTasks = allTasks.filter {
                    it.dueAt != null && it.dueAt in todayStart..todayEnd
                }

                val filteredTasks = when (filter) {
                    TaskFilter.All -> allTasks
                    TaskFilter.TODAY -> todayTasks
                    TaskFilter.UPCOMING -> allTasks.filter {
                        it.dueAt != null && it.dueAt > todayEnd && !it.isDone
                    }
                    TaskFilter.DONE -> allTasks.filter {
                        it.isDone
                    }
                }

                TasksUiState(
                    tasks = filteredTasks,
                    selectedFilter = filter,
                    totalTodayCount = todayTasks.size,
                    completedTodayCount = todayTasks.count { it.isDone },
                    isLoading = false,
                    isAddTaskSheetOpen = _uiState.value.isAddTaskSheetOpen
                )
            }.collect { newState ->
                _uiState.value = newState
            }
        }
    }

    fun onEvent(event: TasksUiEvent) {
        when (event) {
            is TasksUiEvent.OnFilterSelected -> {
                _selectedFilter.value = event.filter
            }
            is TasksUiEvent.OnTaskCheckedChanged -> {
                viewModelScope.launch {
                    repository.upsert(event.task.copy(isDone = event.isDone))
                }
            }
            is TasksUiEvent.OnDeleteTask -> {
                viewModelScope.launch {
                    repository.delete(event.taskId)
                }
            }
            is TasksUiEvent.OnSaveNewTask -> {
                viewModelScope.launch {
                    repository.upsert(
                        Task(
                            title = event.title,
                            notes = event.notes,
                            dueAt = event.dueAt,
                            priority = event.priority,
                            listId = event.listId,
                            isDone = false
                        )
                    )
                    _uiState.update { it.copy(isAddTaskSheetOpen = false) }
                }
            }
            is TasksUiEvent.OnToggleAddTaskSheet -> {
                _uiState.update { it.copy(isAddTaskSheetOpen = event.isOpen) }
            }
        }
    }
}
