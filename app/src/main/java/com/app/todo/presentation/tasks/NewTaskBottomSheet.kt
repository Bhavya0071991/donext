package com.app.todo.presentation.tasks

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.todo.domain.model.Priority
import com.app.todo.ui.theme.PrimaryBlue
import com.app.todo.ui.theme.PrimaryBlueLight
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewTaskBottomSheet(
    sheetState: SheetState,
    onDismiss: () -> Unit,
    onSaveTask: (title: String, notes: String, dueAt: Long?, priority: Priority, listId: Long?) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var selectedDueOption by remember { mutableStateOf("Today") } // Today, Tomorrow, Pick date
    var selectedPriority by remember { mutableStateOf(Priority.MEDIUM) }
    var selectedListId by remember { mutableStateOf<Long?>(1L) } // Default: Personal (id = 1)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "New task",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            // Title Field
            Column {
                Text("Title", fontSize = 12.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    placeholder = { Text("What needs doing?") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryBlue,
                        unfocusedBorderColor = Color.LightGray
                    )
                )
            }

            // Category List Selection Chips
            Column {
                Text("List", fontSize = 12.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf(
                        1L to "Personal",
                        2L to "Work",
                        3L to "Learning",
                        4L to "Shopping"
                    ).forEach { (id, name) ->
                        val isSelected = selectedListId == id
                        ChipItem(
                            label = name,
                            isSelected = isSelected,
                            onClick = { selectedListId = id },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Notes Field
            Column {
                Text("Notes (optional)", fontSize = 12.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    placeholder = { Text("Add details") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryBlue,
                        unfocusedBorderColor = Color.LightGray
                    )
                )
            }

            // Due Selection Chips
            Column {
                Text("Due", fontSize = 12.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("Today", "Tomorrow", "Pick date").forEach { dueOpt ->
                        val isSelected = selectedDueOption == dueOpt
                        ChipItem(
                            label = dueOpt,
                            isSelected = isSelected,
                            onClick = { selectedDueOption = dueOpt },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Priority Selection Chips
            Column {
                Text("Priority", fontSize = 12.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Priority.entries.forEach { priority ->
                        val isSelected = selectedPriority == priority
                        ChipItem(
                            label = priority.name.lowercase().replaceFirstChar { it.uppercase() },
                            isSelected = isSelected,
                            onClick = { selectedPriority = priority },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Save Task Button
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        val dueMillis = when (selectedDueOption) {
                            "Today" -> System.currentTimeMillis()
                            "Tomorrow" -> System.currentTimeMillis() + 86400000L
                            else -> System.currentTimeMillis()
                        }
                        onSaveTask(title, notes, dueMillis, selectedPriority, selectedListId)
                    }
                },
                enabled = title.isNotBlank(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(26.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
            ) {
                Text("Save task", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}

@Composable
fun ChipItem(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(44.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) PrimaryBlueLight else Color(0xFFF2F2F7))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) PrimaryBlue else Color.Black
        )
    }
}