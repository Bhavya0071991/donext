package com.app.todo.domain.usecase

import com.app.todo.domain.repository.TaskRepository
import javax.inject.Inject

class DeleteTaskUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(taskId: Long) = repository.delete(taskId)
}
