package com.app.todo.presentation.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.todo.domain.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val repository: TaskRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(CalendarUiState())
    val uiState: StateFlow<CalendarUiState> = _uiState.asStateFlow()
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    init {
        observeTasks()
    }
    private fun observeTasks() {
        viewModelScope.launch {
            repository.observeTasks().collect { allTasks ->
                val datesWithTasks = allTasks.mapNotNull { task ->
                    task.dueAt?.let { dateFormat.format(Date(it)) }
                }.toSet()


                val selectedDateStr = dateFormat.format(_uiState.value.selectedDate.time)
                val filteredTasks = allTasks.filter { task ->
                    task.dueAt != null && dateFormat.format(Date(task.dueAt)) == selectedDateStr
                }

                _uiState.update {
                    it.copy(
                        tasksForSelectedDate = filteredTasks,
                        datesWithTasks = datesWithTasks
                    )
                }
            }
        }
    }

    fun onEvent(event: CalendarUiEvent){
        when (event) {
            is CalendarUiEvent.OnDateSelected -> {
                _uiState.update { it.copy(selectedDate = event.date) }
                observeTasks()
            }
            is CalendarUiEvent.OnTaskCheckedChanged -> {
                viewModelScope.launch {
                    repository.upsert(event.task.copy(isDone = event.isDone))
                }
            }
        }
    }

}