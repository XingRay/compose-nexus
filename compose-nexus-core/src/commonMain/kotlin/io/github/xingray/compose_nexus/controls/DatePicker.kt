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
 * Simple date holder (year, month, day).
 */
data class NexusDate(
    val year: Int,
    val month: Int, // 1-12
    val day: Int,   // 1-31
)

/**
 * DatePicker state holder.
 */
@Stable
class DatePickerState(
    initialDate: NexusDate? = null,
) {
    var selectedDate by mutableStateOf(initialDate)
    var viewYear by mutableIntStateOf(initialDate?.year ?: 2026)
        private set
    var viewMonth by mutableIntStateOf(initialDate?.month ?: 1)
        private set
    var isOpen by mutableStateOf(false)
        internal set

    fun open() { isOpen = true }
    fun close() { isOpen = false }

    fun select(date: NexusDate) {
        selectedDate = date
        viewYear = date.year
        viewMonth = date.month
        isOpen = false
    }

    fun prevMonth() {
        if (viewMonth == 1) {
            viewMonth = 12
            viewYear--
        } else {
            viewMonth--
        }
    }

    fun nextMonth() {
        if (viewMonth == 12) {
            viewMonth = 1
            viewYear++
        } else {
            viewMonth++
        }
    }

    fun prevYear() { viewYear-- }
    fun nextYear() { viewYear++ }
}

@Composable
fun rememberDatePickerState(
    initialDate: NexusDate? = null,
): DatePickerState = remember { DatePickerState(initialDate) }

/**
 * Element Plus DatePicker — a calendar-based date picker.
 *
 * @param state DatePicker state.
 * @param modifier Modifier.
 * @param placeholder Input placeholder text.
 * @param onDateChange Callback when a date is selected.
 */
@Composable
fun NexusDatePicker(
    state: DatePickerState = rememberDatePickerState(),
    modifier: Modifier = Modifier,
    placeholder: String = "Select date",
    onDateChange: ((NexusDate) -> Unit)? = null,
) {
    val colorScheme = NexusTheme.colorScheme
    val typography = NexusTheme.typography
    val shapes = NexusTheme.shapes
    val shadows = NexusTheme.shadows

    val displayText = state.selectedDate?.let {
        "${it.year}-${it.month.toString().padStart(2, '0')}-${it.day.toString().padStart(2, '0')}"
    } ?: ""

    Column(modifier = modifier) {
        // Input trigger
        NexusInput(
            value = displayText,
            onValueChange = {},
            placeholder = placeholder,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { state.isOpen = !state.isOpen },
            suffix = { NexusText(text = "📅", style = typography.extraSmall) },
        )

        // Calendar popup
        if (state.isOpen) {
            androidx.compose.ui.window.Popup(
                alignment = Alignment.TopStart,
                properties = androidx.compose.ui.window.PopupProperties(focusable = true),
                onDismissRequest = { state.close() },
            ) {
                DateCalendarPanel(
                    state = state,
                    onDateChange = onDateChange,
                )
            }
        }
    }
}

@Composable
private fun DateCalendarPanel(
    state: DatePickerState,
    onDateChange: ((NexusDate) -> Unit)?,
) {
    val colorScheme = NexusTheme.colorScheme
    val typography = NexusTheme.typography
    val shapes = NexusTheme.shapes
    val shadows = NexusTheme.shadows

    Column(
        modifier = Modifier
            .size(width = 292.dp, height = 320.dp)
            .clip(shapes.base)
            .background(colorScheme.fill.blank)
            .border(1.dp, colorScheme.border.lighter, shapes.base)
            .padding(12.dp),
    ) {
        // Header: navigation
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                NavButton("«") { state.prevYear() }
                NavButton("‹") { state.prevMonth() }
            }
            NexusText(
                text = "${state.viewYear} / ${state.viewMonth.toString().padStart(2, '0')}",
                color = colorScheme.text.primary,
                style = typography.base,
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                NavButton("›") { state.nextMonth() }
                NavButton("»") { state.nextYear() }
            }
        }

        // Weekday header
        val weekdays = listOf("Mo", "Tu", "We", "Th", "Fr", "Sa", "Su")
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            weekdays.forEach { day ->
                Box(
                    modifier = Modifier.size(32.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    NexusText(
                        text = day,
                        color = colorScheme.text.secondary,
                        style = typography.extraSmall,
                    )
                }
            }
        }

        // Days grid
        val daysInMonth = daysInMonth(state.viewYear, state.viewMonth)
        val firstDayOfWeek = dayOfWeek(state.viewYear, state.viewMonth, 1) // 0=Mon
        val totalCells = firstDayOfWeek + daysInMonth

        val rows = (totalCells + 6) / 7
        Column {
            for (row in 0 until rows) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                ) {
                    for (col in 0..6) {
                        val cellIndex = row * 7 + col
                        val dayNum = cellIndex - firstDayOfWeek + 1
                        if (dayNum in 1..daysInMonth) {
                            val date = NexusDate(state.viewYear, state.viewMonth, dayNum)
                            val isSelected = state.selectedDate == date
                            val isToday = false // simplified
                            DayCell(
                                day = dayNum,
                                isSelected = isSelected,
                                isCurrentMonth = true,
                                onClick = {
                                    state.select(date)
                                    onDateChange?.invoke(date)
                                },
                            )
                        } else {
                            Box(modifier = Modifier.size(32.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun NavButton(text: String, onClick: () -> Unit) {
    val colorScheme = NexusTheme.colorScheme
    val typography = NexusTheme.typography

    NexusText(
        text = text,
        color = colorScheme.text.secondary,
        style = typography.base,
        modifier = Modifier
            .clickable(onClick = onClick)
            .pointerHoverIcon(PointerIcon.Hand)
            .padding(4.dp),
    )
}

@Composable
private fun DayCell(
    day: Int,
    isSelected: Boolean,
    isCurrentMonth: Boolean,
    onClick: () -> Unit,
) {
    val colorScheme = NexusTheme.colorScheme
    val typography = NexusTheme.typography

    val bgColor = if (isSelected) colorScheme.primary.base else Color.Transparent
    val textColor = when {
        isSelected -> colorScheme.white
        !isCurrentMonth -> colorScheme.text.placeholder
        else -> colorScheme.text.regular
    }

    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(bgColor)
            .clickable(onClick = onClick)
            .pointerHoverIcon(PointerIcon.Hand),
        contentAlignment = Alignment.Center,
    ) {
        NexusText(
            text = day.toString(),
            color = textColor,
            style = typography.extraSmall,
        )
    }
}

// Utility: days in a given month
private fun daysInMonth(year: Int, month: Int): Int = when (month) {
    1, 3, 5, 7, 8, 10, 12 -> 31
    4, 6, 9, 11 -> 30
    2 -> if (isLeapYear(year)) 29 else 28
    else -> 30
}

private fun isLeapYear(year: Int): Boolean =
    (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)

// Utility: day of week for a given date (0=Monday, 6=Sunday) using Zeller's formula
private fun dayOfWeek(year: Int, month: Int, day: Int): Int {
    var y = year
    var m = month
    if (m < 3) { m += 12; y-- }
    val q = day
    val k = y % 100
    val j = y / 100
    val h = (q + (13 * (m + 1)) / 5 + k + k / 4 + j / 4 + 5 * j) % 7
    // Convert from Zeller (0=Sat) to 0=Mon
    return ((h + 5) % 7)
}
