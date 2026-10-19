package com.app.todo.domain.repository

import com.app.todo.domain.model.Subtask
import com.app.todo.domain.model.Task
import com.app.todo.domain.model.TaskList
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun observeTasks(): Flow<List<Task>>
    fun observeTasksByList(listId: Long): Flow<List<Task>>
    suspend fun getTask(id: Long): Task?
    suspend fun upsert(task: Task): Long
    suspend fun delete(id: Long)
    suspend fun clearCompletedTasks()

    // Subtasks
    fun observeSubtasks(taskId: Long): Flow<List<Subtask>>
    suspend fun upsertSubtask(subtask: Subtask)
    suspend fun deleteSubtask(id: Long)

    // Task Lists
    fun observeTaskLists(): Flow<List<TaskList>>
    suspend fun upsertTaskList(list: TaskList)
}