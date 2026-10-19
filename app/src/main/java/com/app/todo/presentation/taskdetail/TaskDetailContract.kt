package com.app.todo.presentation.taskdetail

import com.app.todo.domain.model.Subtask
import com.app.todo.domain.model.Task

data class TaskDetailUiState(
    val task: Task? =null,
    val listName:String = "Learning",
    val subtasks:List<Subtask> =emptyList(),
    val isLoading: Boolean =true,
    val isTaskDeleted: Boolean =false
    )

sealed interface TaskDetailUiEvent{
    data class OnToggleTaskDone(val isDone: Boolean) : TaskDetailUiEvent
    data class OnToggleSubtaskDone(val subtask: Subtask, val isDone: Boolean) : TaskDetailUiEvent
    data class OnAddSubtask(val title: String) : TaskDetailUiEvent
    object OnDeleteTask : TaskDetailUiEvent
}