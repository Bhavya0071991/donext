package com.app.todo.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.app.todo.data.local.dao.TaskDao
import com.app.todo.data.local.entity.SubtaskEntity
import com.app.todo.data.local.entity.TaskEntity
import com.app.todo.data.local.entity.TaskListEntity

@Database(
    entities = [TaskEntity::class, SubtaskEntity::class, TaskListEntity::class],
    version = 1,
    exportSchema = false
)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}