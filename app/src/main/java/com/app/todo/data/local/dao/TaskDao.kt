package com.app.todo.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.app.todo.data.local.entity.SubtaskEntity
import com.app.todo.data.local.entity.TaskEntity
import com.app.todo.data.local.entity.TaskListEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Query("SELECT * FROM tasks ORDER BY isDone ASC, dueAt ASC")
    fun observeTasks(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE listId = :listId ORDER BY isDone ASC")
    fun observeTasksInList(listId: Long): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE id = :id")
    suspend fun getTaskById(id: Long): TaskEntity?

    @Upsert
    suspend fun upsertTask(task: TaskEntity): Long

    @Query("DELETE FROM tasks WHERE id = :id")
    suspend fun deleteTask(id: Long)

    @Query("DELETE FROM tasks WHERE isDone = 1")
    suspend fun clearCompletedTasks()

    // Subtasks
    @Query("SELECT * FROM subtasks WHERE taskId = :taskId")
    fun observeSubtasks(taskId: Long): Flow<List<SubtaskEntity>>

    @Upsert
    suspend fun upsertSubtask(subtask: SubtaskEntity)

    @Query("DELETE FROM subtasks WHERE id = :id")
    suspend fun deleteSubtask(id: Long)

    // Task Lists
    @Query("SELECT * FROM task_lists")
    fun observeTaskLists(): Flow<List<TaskListEntity>>

    @Upsert
    suspend fun upsertTaskList(list: TaskListEntity)
}