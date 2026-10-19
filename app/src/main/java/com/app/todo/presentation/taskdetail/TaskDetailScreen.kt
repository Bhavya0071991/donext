package com.app.todo.presentation.taskdetail
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.todo.domain.model.Priority
import com.app.todo.ui.theme.AppBackground
import com.app.todo.ui.theme.PrimaryBlue
import com.app.todo.ui.theme.PriorityHigh
import com.app.todo.ui.theme.PriorityLow
import com.app.todo.ui.theme.PriorityMedium
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.ui.platform.LocalLocale

@Composable
fun TaskDetailScreen(
    viewModel: TaskDetailViewModel,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val task = uiState.task

    LaunchedEffect(uiState.isTaskDeleted) {
        if (uiState.isTaskDeleted) {
            onBackClick()
        }
    }

    Scaffold(
        containerColor = AppBackground
    ) { padding ->
        if (task != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 20.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                // Back Button
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Task Title
                Text(
                    text = task.title,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Metadata Table Rows (Due, Priority, List)
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Due", fontSize = 15.sp, color = Color.Gray)
                        val dueText = if (task.dueAt != null) {
                            SimpleDateFormat("E, d MMM h:mm a", LocalLocale.current.platformLocale).format(Date(task.dueAt))
                        } else "No due date"
                        Text(dueText, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = Color.Black)
                    }
                    HorizontalDivider(color = Color(0xFFE5E5EA))

                    // Priority Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Priority", fontSize = 15.sp, color = Color.Gray)
                        val (pColor, pText) = when (task.priority) {
                            Priority.HIGH -> PriorityHigh to "High"
                            Priority.MEDIUM -> PriorityMedium to "Medium"
                            Priority.LOW -> PriorityLow to "Low"
                        }
                        Text(pText, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = pColor)
                    }
                    HorizontalDivider(color = Color(0xFFE5E5EA))

                    // List Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("List", fontSize = 15.sp, color = Color.Gray)
                        Text(uiState.listName, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = Color.Black)
                    }
                    HorizontalDivider(color = Color(0xFFE5E5EA))
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Notes Section
                if (task.notes.isNotBlank()) {
                    Text("Notes", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(task.notes, fontSize = 15.sp, color = Color.DarkGray, lineHeight = 22.sp)
                    Spacer(modifier = Modifier.height(24.dp))
                }

                // Subtasks Section
                Text("Subtasks", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.subtasks, key = { it.id }) { subtask ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Checkbox(
                                checked = subtask.isDone,
                                onCheckedChange = { isChecked ->
                                    viewModel.onEvent(
                                        TaskDetailUiEvent.OnToggleSubtaskDone(subtask, isChecked)
                                    )
                                },
                                colors = CheckboxDefaults.colors(checkedColor = PrimaryBlue)
                            )
                            Text(
                                text = subtask.title,
                                fontSize = 15.sp,
                                color = if (subtask.isDone) Color.Gray else Color.Black,
                                textDecoration = if (subtask.isDone) TextDecoration.LineThrough else TextDecoration.None
                            )
                        }
                        HorizontalDivider(color = Color(0xFFE5E5EA))
                    }
                }

                // Bottom Action Buttons (Delete & Mark Done)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedButton(
                        onClick = { viewModel.onEvent(TaskDetailUiEvent.OnDeleteTask) },
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp),
                        shape = RoundedCornerShape(26.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
                    ) {
                        Text("Delete", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = {
                            viewModel.onEvent(TaskDetailUiEvent.OnToggleTaskDone(!task.isDone))
                        },
                        modifier = Modifier
                            .weight(1.5f)
                            .height(52.dp),
                        shape = RoundedCornerShape(26.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                    ) {
                        Text(
                            text = if (task.isDone) "Mark pending" else "Mark done",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}