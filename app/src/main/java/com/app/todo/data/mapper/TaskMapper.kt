package com.app.todo.data.mapper

import com.app.todo.data.local.entity.SubtaskEntity
import com.app.todo.data.local.entity.TaskEntity
import com.app.todo.data.local.entity.TaskListEntity
import com.app.todo.domain.model.Priority
import com.app.todo.domain.model.Subtask
import com.app.todo.domain.model.Task
import com.app.todo.domain.model.TaskList

fun TaskEntity.toDomain(subtasks: List<Subtask> = emptyList()): Task = Task(
    id = id,
    title = title,
    notes = notes,
    dueAt = dueAt,
    priority = runCatching { Priority.valueOf(priority) }.getOrDefault(Priority.MEDIUM),
    isDone = isDone,
    listId = listId,
    subtasks = subtasks
)

fun Task.toEntity(): TaskEntity = TaskEntity(
    id = id,
    title = title,
    notes = notes,
    dueAt = dueAt,
    priority = priority.name,
    isDone = isDone,
    listId = listId
)

fun SubtaskEntity.toDomain(): Subtask = Subtask(
    id = id,
    taskId = taskId,
    title = title,
    isDone = isDone
)

fun Subtask.toEntity(): SubtaskEntity = SubtaskEntity(
    id = id,
    taskId = taskId,
    title = title,
    isDone = isDone
)

fun TaskListEntity.toDomain(): TaskList = TaskList(
    id = id,
    name = name,
    colorHex = colorHex
)

fun TaskList.toEntity(): TaskListEntity = TaskListEntity(
    id = id,
    name = name,
    colorHex = colorHex
)
