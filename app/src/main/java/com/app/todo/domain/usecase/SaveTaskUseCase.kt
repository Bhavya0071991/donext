package com.app.todo.domain.usecase

import com.app.todo.domain.model.Task
import com.app.todo.domain.repository.TaskRepository
import javax.inject.Inject

class SaveTaskUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(task: Task): Long = repository.upsert(task)
}
