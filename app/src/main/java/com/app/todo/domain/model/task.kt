package com.app.todo.domain.model

enum class Priority { LOW, MEDIUM, HIGH }

data class Task(
    val id: Long = 0,
    val title: String,
    val notes: String = "",
    val dueAt: Long? = null,
    val priority: Priority = Priority.MEDIUM,
    val isDone: Boolean = false,
    val listId: Long? = null,
    val subtasks: List<Subtask> = emptyList()
)

data class Subtask(
    val id: Long = 0,
    val taskId: Long,
    val title: String,
    val isDone: Boolean = false
)

data class TaskList(
    val id: Long = 0,
    val name: String,
    val colorHex: String
)
