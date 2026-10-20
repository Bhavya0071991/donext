package com.app.todo.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.app.todo.domain.model.Priority
import com.app.todo.domain.model.Task

@Entity(
    tableName = "tasks",
    foreignKeys = [
        ForeignKey(
            entity = TaskListEntity::class,
            parentColumns = ["id"],
            childColumns = ["listId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index(value = ["listId"])]
)
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val notes: String = "",
    val dueAt: Long? = null,
    val priority: String = Priority.MEDIUM.name,
    val isDone: Boolean = false,
    val listId: Long? = null,
    val createdAt: Long = System.currentTimeMillis()
)
