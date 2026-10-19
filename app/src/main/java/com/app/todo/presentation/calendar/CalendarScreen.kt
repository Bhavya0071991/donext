package com.app.todo.presentation.calendar
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.todo.domain.model.Priority
import com.app.todo.domain.model.Task
import com.app.todo.ui.theme.AppBackground
import com.app.todo.ui.theme.CardBackground
import com.app.todo.ui.theme.PrimaryBlue
import com.app.todo.ui.theme.PriorityHigh
import com.app.todo.ui.theme.PriorityLow
import com.app.todo.ui.theme.PriorityMedium
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import androidx.compose.ui.platform.LocalLocale

@Composable
fun CalendarScreen(
    viewModel: CalendarViewModel,
    onTaskClick: (Long) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val selectedCalendar = uiState.selectedDate

    val monthYearFormat = SimpleDateFormat("MMMM yyyy", LocalLocale.current.platformLocale)
    val monthTitle = monthYearFormat.format(selectedCalendar.time)

    val selectedDateHeaderFormat = SimpleDateFormat("E, d MMM", LocalLocale.current.platformLocale)
    val selectedDateHeader = selectedDateHeaderFormat.format(selectedCalendar.time)

    Scaffold(
        containerColor = AppBackground
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Month Title
            Text(
                text = monthTitle,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Calendar Month Grid View Component
            CalendarGridComponent(
                selectedDate = selectedCalendar,
                datesWithTasks = uiState.datesWithTasks,
                onDateSelected = { newDate ->
                    viewModel.onEvent(CalendarUiEvent.OnDateSelected(newDate))
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Selected Date Tasks Header
            Text(
                text = selectedDateHeader,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Tasks List for Selected Date
            if (uiState.tasksForSelectedDate.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No tasks scheduled for this day",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.tasksForSelectedDate, key = { it.id }) { task ->
                        CalendarTaskCard(
                            task = task,
                            onCheckedChanged = { isDone ->
                                viewModel.onEvent(CalendarUiEvent.OnTaskCheckedChanged(task, isDone))
                            },
                            onClick = { onTaskClick(task.id) }
                        )
                    }
                    item { Spacer(modifier = Modifier.height(80.dp)) }
                }
            }
        }
    }
}

@Composable
fun CalendarGridComponent(
    selectedDate: Calendar,
    datesWithTasks: Set<String>,
    onDateSelected: (Calendar) -> Unit
) {
    val dayHeaders = listOf("M", "T", "W", "T", "F", "S", "S")

    // Generate Calendar Days for Current Month
    val daysInMonth = getDaysForMonthGrid(selectedDate)
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", LocalLocale.current.platformLocale)

    Column {
        // Day Headers Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            dayHeaders.forEach { header ->
                Text(
                    text = header,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.width(36.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Days Grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier.height(260.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            items(daysInMonth) { dateCal ->
                if (dateCal == null) {
                    Spacer(modifier = Modifier.size(36.dp))
                } else {
                    val dateStr = dateFormat.format(dateCal.time)
                    val isSelected = dateCal.get(Calendar.DAY_OF_MONTH) == selectedDate.get(Calendar.DAY_OF_MONTH) &&
                            dateCal.get(Calendar.MONTH) == selectedDate.get(Calendar.MONTH)
                    val hasTasks = datesWithTasks.contains(dateStr)

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clip(CircleShape)
                            .clickable { onDateSelected(dateCal) }
                            .padding(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(if (isSelected) PrimaryBlue else Color.Transparent),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = dateCal.get(Calendar.DAY_OF_MONTH).toString(),
                                fontSize = 15.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) Color.White else Color.Black
                            )
                        }

                        // Blue task dot indicator
                        if (hasTasks) {
                            Box(
                                modifier = Modifier
                                    .padding(top = 2.dp)
                                    .size(4.dp)
                                    .clip(CircleShape)
                                    .background(if (isSelected) PrimaryBlue else PrimaryBlue)
                            )
                        } else {
                            Spacer(modifier = Modifier.height(6.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CalendarTaskCard(
    task: Task,
    onCheckedChanged: (Boolean) -> Unit,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(CardBackground)
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
                colors = CheckboxDefaults.colors(checkedColor = PrimaryBlue)
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (task.isDone) Color.Gray else Color.Black,
                    textDecoration = if (task.isDone) TextDecoration.LineThrough else TextDecoration.None
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (task.dueAt != null) {
                        val timeText = SimpleDateFormat("h:mm a", LocalLocale.current.platformLocale).format(Date(task.dueAt))
                        Text(text = timeText, fontSize = 12.sp, color = Color.Gray)
                    }

                    val (pColor, pText) = when (task.priority) {
                        Priority.HIGH -> PriorityHigh to "High"
                        Priority.MEDIUM -> PriorityMedium to "Medium"
                        Priority.LOW -> PriorityLow to "Low"
                    }

                    Text(
                        text = pText,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = pColor
                    )
                }
            }
        }
    }
}

fun getDaysForMonthGrid(calendar: Calendar): List<Calendar?> {
    val days = mutableListOf<Calendar?>()
    val cal = calendar.clone() as Calendar
    cal.set(Calendar.DAY_OF_MONTH, 1)

    val firstDayOfWeek = (cal.get(Calendar.DAY_OF_WEEK) + 5) % 7 // Convert Sunday start to Monday start
    for (i in 0 until firstDayOfWeek) {
        days.add(null)
    }

    val maxDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
    for (day in 1..maxDays) {
        val dayCal = cal.clone() as Calendar
        dayCal.set(Calendar.DAY_OF_MONTH, day)
        days.add(dayCal)
    }

    return days
}