package com.app.todo.presentation.common

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.app.todo.domain.model.Priority
import com.app.todo.ui.theme.PriorityHigh
import com.app.todo.ui.theme.PriorityLow
import com.app.todo.ui.theme.PriorityMedium

@Composable
fun PriorityBadge(
    priority: Priority,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 12.sp
) {
    val (priorityColor, priorityText) = when (priority) {
        Priority.HIGH -> PriorityHigh to "High"
        Priority.MEDIUM -> PriorityMedium to "Medium"
        Priority.LOW -> PriorityLow to "Low"
    }

    Text(
        text = priorityText,
        fontSize = fontSize,
        fontWeight = FontWeight.Bold,
        color = priorityColor,
        modifier = modifier
    )
}
