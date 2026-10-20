package com.app.todo.presentation.tasks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.todo.domain.model.Priority
import com.app.todo.presentation.common.ChipItem
import com.app.todo.presentation.common.CustomTextField
import com.app.todo.presentation.common.PrimaryButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewTaskBottomSheet(
    sheetState: SheetState,
    onDismiss: () -> Unit,
    onSaveTask: (title: String, notes: String, dueAt: Long?, priority: Priority, listId: Long?) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var selectedDueOption by remember { mutableStateOf("Today") }
    var selectedPriority by remember { mutableStateOf(Priority.MEDIUM) }
    var selectedListId by remember { mutableStateOf<Long?>(1L) }

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

            // Title Field using Common CustomTextField
            CustomTextField(
                value = title,
                onValueChange = { title = it },
                label = "Title",
                placeholder = "What needs doing?"
            )

            // Category List Selection Chips using Common ChipItem
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

            // Notes Field using Common CustomTextField
            CustomTextField(
                value = notes,
                onValueChange = { notes = it },
                label = "Notes (optional)",
                placeholder = "Add details",
                singleLine = false
            )

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

            // Save Task Button using Common PrimaryButton
            PrimaryButton(
                text = "Save task",
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
                enabled = title.isNotBlank()
            )
        }
    }
}
