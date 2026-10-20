package com.app.todo.data.repositoryimpl

import com.app.todo.data.local.dao.TaskDao
import com.app.todo.data.mapper.toDomain
import com.app.todo.data.mapper.toEntity
import com.app.todo.domain.model.Subtask
import com.app.todo.domain.model.Task
import com.app.todo.domain.model.TaskList
import com.app.todo.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val dao: TaskDao
): TaskRepository {

    override fun observeTasks(): Flow<List<Task>> {
        return dao.observeTasks().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun observeTasksByList(listId: Long): Flow<List<Task>> {
        return dao.observeTasksInList(listId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getTask(id: Long): Task? {
        return dao.getTaskById(id)?.toDomain()
    }

    override suspend fun upsert(task: Task): Long {
        return dao.upsertTask(task.toEntity())
    }

    override suspend fun delete(id: Long) {
        dao.deleteTask(id)
    }

    override suspend fun clearCompletedTasks() {
        dao.clearCompletedTasks()
    }

    override fun observeSubtasks(taskId: Long): Flow<List<Subtask>> {
        return dao.observeSubtasks(taskId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun upsertSubtask(subtask: Subtask) {
        dao.upsertSubtask(subtask.toEntity())
    }

    override suspend fun deleteSubtask(id: Long) {
        dao.deleteSubtask(id)
    }

    override fun observeTaskLists(): Flow<List<TaskList>> {
        return dao.observeTaskLists().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun upsertTaskList(list: TaskList) {
        dao.upsertTaskList(list.toEntity())
    }
}
