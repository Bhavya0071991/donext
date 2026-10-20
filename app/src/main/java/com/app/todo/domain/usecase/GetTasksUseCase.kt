package com.app.todo.domain.usecase

import com.app.todo.domain.model.Task
import com.app.todo.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTasksUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    operator fun invoke(): Flow<List<Task>> = repository.observeTasks()
}
