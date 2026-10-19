package com.app.todo.presentation.lists

data class TaskListUiModel(
    val id: Long,
    val name: String,
    val colorHex: String,
    val totalTasks: Int,
    val completedTasks: Int
)

data class ListsUiState(
    val lists: List<TaskListUiModel> = emptyList(),
    val isCreateListSheetOpen: Boolean = false,
    val isLoading: Boolean = false
)

sealed interface ListsUiEvent {
    data class OnCreateNewList(val name: String, val colorHex: String) : ListsUiEvent
    data class OnToggleCreateListSheet(val isOpen: Boolean) : ListsUiEvent
}