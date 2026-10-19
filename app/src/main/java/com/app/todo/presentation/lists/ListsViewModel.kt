package com.app.todo.presentation.lists
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.todo.domain.model.TaskList
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
class ListsViewModel @Inject constructor(
    private val repository: TaskRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ListsUiState())
    val uiState: StateFlow<ListsUiState> = _uiState.asStateFlow()

    init {
        loadListsAndCounts()
    }

    private fun loadListsAndCounts() {
        viewModelScope.launch {
            combine(
                repository.observeTaskLists(),
                repository.observeTasks()
            ) { taskLists, allTasks ->
                val defaultLists = if (taskLists.isEmpty()) {
                    listOf(
                        TaskList(1, "Personal", "#E2E4FF"),
                        TaskList(2, "Work", "#FFE4CB"),
                        TaskList(3, "Learning", "#CFF2DE"),
                        TaskList(4, "Shopping", "#FFE2EA")
                    ).also { initialList ->
                        initialList.forEach { repository.upsertTaskList(it) }
                    }
                } else taskLists

                defaultLists.map { list ->
                    val tasksInList = allTasks.filter { it.listId == list.id }
                    TaskListUiModel(
                        id = list.id,
                        name = list.name,
                        colorHex = list.colorHex,
                        totalTasks = tasksInList.size,
                        completedTasks = tasksInList.count { it.isDone }
                    )
                }
            }.collect { uiLists ->
                _uiState.update { it.copy(lists = uiLists) }
            }
        }
    }

    fun onEvent(event: ListsUiEvent) {
        when (event) {
            is ListsUiEvent.OnCreateNewList -> {
                viewModelScope.launch {
                    repository.upsertTaskList(
                        TaskList(name = event.name, colorHex = event.colorHex)
                    )
                    _uiState.update { it.copy(isCreateListSheetOpen = false) }
                }
            }
            is ListsUiEvent.OnToggleCreateListSheet -> {
                _uiState.update { it.copy(isCreateListSheetOpen = event.isOpen) }
            }
        }
    }
}