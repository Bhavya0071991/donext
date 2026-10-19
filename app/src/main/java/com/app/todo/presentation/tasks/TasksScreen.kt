package com.app.todo.presentation.tasks

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.todo.domain.model.Priority
import com.app.todo.domain.model.Task
import com.app.todo.ui.theme.AppBackground
import com.app.todo.ui.theme.CardBackground
import com.app.todo.ui.theme.PrimaryBlue
import com.app.todo.ui.theme.PrimaryBlueLight
import com.app.todo.ui.theme.PriorityHigh
import com.app.todo.ui.theme.PriorityLow
import com.app.todo.ui.theme.PriorityMedium
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.ui.platform.LocalLocale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreen(
    viewModel: TasksViewModel,
    onTaskClick: (Long) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val dateHeader = SimpleDateFormat("EEEE, d MMMM", LocalLocale.current.platformLocale).format(Date())

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onEvent(TasksUiEvent.OnToggleAddTaskSheet(true)) },
                containerColor = PrimaryBlue,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.padding(bottom = 80.dp) // Avoid overlap with bottom nav bar
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Task", modifier = Modifier.size(28.dp))
            }
        },
        containerColor = androidx.compose.material3.MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Subtitle Date Header
            Text(
                text = dateHeader,
                fontSize = 14.sp,
                color = Color.Gray
            )

            // Large Title
            Text(
                text = "My Tasks",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = androidx.compose.material3.MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Daily Progress Bar Card
            if (uiState.totalTodayCount > 0) {
                DailyProgressCard(
                    completed = uiState.completedTodayCount,
                    total = uiState.totalTodayCount
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Filter Chips Row
            FilterChipsRow(
                selectedFilter = uiState.selectedFilter,
                onFilterSelected = { viewModel.onEvent(TasksUiEvent.OnFilterSelected(it)) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Task List or Empty State
            if (uiState.tasks.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    EmptyTasksView(
                        onAddTaskClick = {
                            viewModel.onEvent(TasksUiEvent.OnToggleAddTaskSheet(true))
                        }
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(uiState.tasks, key = { it.id }) { task ->
                        TaskItemCard(
                            task = task,
                            onCheckedChanged = { isDone ->
                                viewModel.onEvent(TasksUiEvent.OnTaskCheckedChanged(task, isDone))
                            },
                            onClick = { onTaskClick(task.id) }
                        )
                    }
                    item { Spacer(modifier = Modifier.height(100.dp)) }
                }
            }
        }
    }

    // Modal Bottom Sheet for Adding New Task
    if (uiState.isAddTaskSheetOpen) {
        NewTaskBottomSheet(
            sheetState = sheetState,
            onDismiss = { viewModel.onEvent(TasksUiEvent.OnToggleAddTaskSheet(false)) },
            onSaveTask = { title, notes, dueAt, priority, listId ->
                viewModel.onEvent(TasksUiEvent.OnSaveNewTask(title, notes, dueAt, priority, listId))
            }
        )
    }
}

@Composable
fun DailyProgressCard(completed: Int, total: Int) {
    val progress = if (total > 0) completed.toFloat() / total else 0f

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(PrimaryBlueLight)
            .padding(16.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "$completed of $total done today",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = PrimaryBlue,
                trackColor = Color.White
            )
        }
    }
}

@Composable
fun FilterChipsRow(
    selectedFilter: TaskFilter,
    onFilterSelected: (TaskFilter) -> Unit
) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(TaskFilter.entries) { filter ->
            val isSelected = selectedFilter == filter
            val label = filter.name.lowercase().replaceFirstChar { it.uppercase() }

            Box(
                modifier = Modifier
                    .height(38.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (isSelected) PrimaryBlueLight else Color.Transparent)
                    .clickable { onFilterSelected(filter) }
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = label,
                    fontSize = 14.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    color = if (isSelected) PrimaryBlue else Color.Gray
                )
            }
        }
    }
}

@Composable
fun TaskItemCard(
    task: Task,
    onCheckedChanged: (Boolean) -> Unit,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(androidx.compose.material3.MaterialTheme.colorScheme.surface)
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Checkbox(
                checked = task.isDone,
                onCheckedChange = onCheckedChanged,
                colors = CheckboxDefaults.colors(
                    checkedColor = PrimaryBlue,
                    uncheckedColor = Color.LightGray
                )
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (task.isDone) Color.Gray else androidx.compose.material3.MaterialTheme.colorScheme.onSurface,
                    textDecoration = if (task.isDone) TextDecoration.LineThrough else TextDecoration.None
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (task.dueAt != null) {
                        val dueText = SimpleDateFormat("E, d MMM h:mm a", LocalLocale.current.platformLocale)
                            .format(Date(task.dueAt))
                        Text(text = dueText, fontSize = 12.sp, color = Color.Gray)
                    }

                    // Priority Badge matching screenshot design
                    val (priorityColor, priorityText) = when (task.priority) {
                        Priority.HIGH -> PriorityHigh to "High"
                        Priority.MEDIUM -> PriorityMedium to "Medium"
                        Priority.LOW -> PriorityLow to "Low"
                    }

                    Text(
                        text = priorityText,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = priorityColor
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyTasksView(onAddTaskClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Large Checkmark Graphic Card
        Box(
            modifier = Modifier
                .size(140.dp)
                .clip(RoundedCornerShape(40.dp))
                .background(PrimaryBlueLight),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = PrimaryBlue,
                modifier = Modifier.size(64.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "All clear",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = androidx.compose.material3.MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "You have no tasks right now. Add one to get started.",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(horizontal = 32.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onAddTaskClick,
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
            modifier = Modifier.height(48.dp)
        ) {
            Text("Add task", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}