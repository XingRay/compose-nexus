package io.github.xingray.compose_nexus.controls

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import io.github.xingray.compose_nexus.theme.NexusTheme

/**
 * Calendar state holder.
 */
@Stable
class CalendarState(
    initialYear: Int = 2026,
    initialMonth: Int = 1,
) {
    var viewYear by mutableIntStateOf(initialYear)
        private set
    var viewMonth by mutableIntStateOf(initialMonth)
        private set
    var selectedDate by mutableStateOf<NexusDate?>(null)

    fun prevMonth() {
        if (viewMonth == 1) { viewMonth = 12; viewYear-- }
        else viewMonth--
    }

    fun nextMonth() {
        if (viewMonth == 12) { viewMonth = 1; viewYear++ }
        else viewMonth++
    }

    fun prevYear() { viewYear-- }
    fun nextYear() { viewYear++ }

    fun select(date: NexusDate) {
        selectedDate = date
    }

    fun today() {
        // Simplified: no actual platform date API in common
        viewYear = 2026
        viewMonth = 3
    }
}

@Composable
fun rememberCalendarState(
    initialYear: Int = 2026,
    initialMonth: Int = 1,
): CalendarState = remember { CalendarState(initialYear, initialMonth) }

/**
 * Element Plus Calendar — a full-size inline calendar panel.
 *
 * @param state Calendar state.
 * @param modifier Modifier.
 * @param onDateSelect Callback when a date is selected.
 * @param dateCell Optional custom content for each date cell.
 */
@Composable
fun NexusCalendar(
    state: CalendarState = rememberCalendarState(),
    modifier: Modifier = Modifier,
    onDateSelect: ((NexusDate) -> Unit)? = null,
    dateCell: (@Composable (NexusDate) -> Unit)? = null,
) {
    val colorScheme = NexusTheme.colorScheme
    val typography = NexusTheme.typography
    val shapes = NexusTheme.shapes

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(shapes.base)
            .border(1.dp, colorScheme.border.lighter, shapes.base)
            .background(colorScheme.fill.blank),
    ) {
        // Header with navigation
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(colorScheme.fill.blank)
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                CalendarNavButton("«") { state.prevYear() }
                CalendarNavButton("‹") { state.prevMonth() }
            }

            NexusText(
                text = "${state.viewYear} - ${monthName(state.viewMonth)}",
                color = colorScheme.text.primary,
                style = typography.large,
            )

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                CalendarNavButton("›") { state.nextMonth() }
                CalendarNavButton("»") { state.nextYear() }
            }
        }

        NexusDivider()

        // Weekday headers
        val weekdays = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            weekdays.forEach { day ->
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center,
                ) {
                    NexusText(
                        text = day,
                        color = colorScheme.text.regular,
                        style = typography.small,
                    )
                }
            }
        }

        // Days grid
        val daysInMonth = daysInMonthCalendar(state.viewYear, state.viewMonth)
        val firstDayOfWeek = dayOfWeekCalendar(state.viewYear, state.viewMonth, 1)
        val totalCells = firstDayOfWeek + daysInMonth
        val rows = (totalCells + 6) / 7

        Column(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)) {
            for (row in 0 until rows) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                ) {
                    for (col in 0..6) {
                        val cellIndex = row * 7 + col
                        val dayNum = cellIndex - firstDayOfWeek + 1

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(2.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            if (dayNum in 1..daysInMonth) {
                                val date = NexusDate(state.viewYear, state.viewMonth, dayNum)
                                val isSelected = state.selectedDate == date

                                if (dateCell != null) {
                                    Box(
                                        modifier = Modifier
                                            .clickable {
                                                state.select(date)
                                                onDateSelect?.invoke(date)
                                            }
                                            .pointerHoverIcon(PointerIcon.Hand),
                                    ) {
                                        dateCell(date)
                                    }
                                } else {
                                    CalendarDayCell(
                                        day = dayNum,
                                        isSelected = isSelected,
                                        onClick = {
                                            state.select(date)
                                            onDateSelect?.invoke(date)
                                        },
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CalendarNavButton(text: String, onClick: () -> Unit) {
    val colorScheme = NexusTheme.colorScheme
    val typography = NexusTheme.typography

    NexusText(
        text = text,
        color = colorScheme.text.secondary,
        style = typography.large,
        modifier = Modifier
            .clickable(onClick = onClick)
            .pointerHoverIcon(PointerIcon.Hand)
            .padding(4.dp),
    )
}

@Composable
private fun CalendarDayCell(
    day: Int,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val colorScheme = NexusTheme.colorScheme
    val typography = NexusTheme.typography

    val bgColor = if (isSelected) colorScheme.primary.base else Color.Transparent
    val textColor = if (isSelected) colorScheme.white else colorScheme.text.regular

    Box(
        modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(bgColor)
            .clickable(onClick = onClick)
            .pointerHoverIcon(PointerIcon.Hand),
        contentAlignment = Alignment.Center,
    ) {
        NexusText(
            text = day.toString(),
            color = textColor,
            style = typography.base,
        )
    }
}

private fun monthName(month: Int): String = when (month) {
    1 -> "January"; 2 -> "February"; 3 -> "March"; 4 -> "April"
    5 -> "May"; 6 -> "June"; 7 -> "July"; 8 -> "August"
    9 -> "September"; 10 -> "October"; 11 -> "November"; 12 -> "December"
    else -> ""
}

// Same logic as DatePicker, private to avoid clashes
private fun daysInMonthCalendar(year: Int, month: Int): Int = when (month) {
    1, 3, 5, 7, 8, 10, 12 -> 31
    4, 6, 9, 11 -> 30
    2 -> if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) 29 else 28
    else -> 30
}

private fun dayOfWeekCalendar(year: Int, month: Int, day: Int): Int {
    var y = year; var m = month
    if (m < 3) { m += 12; y-- }
    val q = day; val k = y % 100; val j = y / 100
    val h = (q + (13 * (m + 1)) / 5 + k + k / 4 + j / 4 + 5 * j) % 7
    return ((h + 5) % 7)
}
