package com.app.todo.presentation.taskdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.todo.domain.model.Subtask
import com.app.todo.domain.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskDetailViewModel @Inject constructor(
    private val repository: TaskRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val taskId: Long = checkNotNull(savedStateHandle.get<String>("taskId")).toLong()
    private val _uiState = MutableStateFlow(TaskDetailUiState())
    val uiState: StateFlow<TaskDetailUiState> = _uiState.asStateFlow()

    init {
        loadTaskDetails()
    }

    private fun loadTaskDetails() {
        viewModelScope.launch {
            val task = repository.getTask(taskId)
            _uiState.update { it.copy(task = task, isLoading = false) }

            // Observe lists to find matching list name for task.listId
            repository.observeTaskLists().collect { taskLists ->
                val matchingList = taskLists.find { it.id == task?.listId }
                if (matchingList != null) {
                    _uiState.update { it.copy(listName = matchingList.name) }
                }
            }
        }

        viewModelScope.launch {
            repository.observeSubtasks(taskId).collect { subtasks ->
                _uiState.update { it.copy(subtasks = subtasks) }
            }
        }
    }

    fun onEvent(event: TaskDetailUiEvent) {
        when (event) {
            is TaskDetailUiEvent.OnToggleTaskDone -> {
                viewModelScope.launch {
                    val currentTask = _uiState.value.task ?: return@launch
                    val updatedTask = currentTask.copy(isDone = event.isDone)
                    repository.upsert(updatedTask)
                    _uiState.update { it.copy(task = updatedTask) }
                }
            }

            is TaskDetailUiEvent.OnToggleSubtaskDone -> {
                viewModelScope.launch {
                    repository.upsertSubtask(event.subtask.copy(isDone = event.isDone))
                }
            }

            is TaskDetailUiEvent.OnAddSubtask -> {
                viewModelScope.launch {
                    repository.upsertSubtask(
                        Subtask(
                            taskId = taskId,
                            title = event.title,
                            isDone = false
                        )
                    )
                }
            }

            TaskDetailUiEvent.OnDeleteTask -> {
                viewModelScope.launch {
                    repository.delete(taskId)
                    _uiState.update { it.copy(isTaskDeleted = true) }
                }
            }
        }
    }

}


