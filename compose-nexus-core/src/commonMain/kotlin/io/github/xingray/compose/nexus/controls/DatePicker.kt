package io.github.xingray.compose.nexus.controls

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
import androidx.compose.runtime.mutableStateListOf
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
import io.github.xingray.compose.nexus.theme.NexusTheme

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
    val selectedDates = mutableStateListOf<NexusDate>()
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

    fun selectWithoutClosing(date: NexusDate) {
        selectedDate = date
        viewYear = date.year
        viewMonth = date.month
    }

    fun toggleDate(date: NexusDate) {
        if (date in selectedDates) {
            selectedDates.remove(date)
        } else {
            selectedDates.add(date)
        }
    }

    fun clear() {
        selectedDate = null
        selectedDates.clear()
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

enum class NexusDatePickerPanelType {
    Year,
    Years,
    Month,
    Months,
    Date,
    Dates,
    DateTime,
    Week,
    DateTimeRange,
    DateRange,
    MonthRange,
    YearRange,
}

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
    disabled: Boolean = false,
    clearable: Boolean = true,
    size: io.github.xingray.compose.nexus.theme.ComponentSize = io.github.xingray.compose.nexus.theme.ComponentSize.Default,
    type: NexusDatePickerPanelType = NexusDatePickerPanelType.Date,
    showFooter: Boolean = true,
    showConfirm: Boolean = true,
    showWeekNumber: Boolean = false,
    onClear: (() -> Unit)? = null,
    onFocus: (() -> Unit)? = null,
    onBlur: (() -> Unit)? = null,
    onVisibleChange: ((Boolean) -> Unit)? = null,
    onPanelChange: ((NexusDate, NexusDatePickerPanelType) -> Unit)? = null,
    onDateChange: ((NexusDate) -> Unit)? = null,
) {
    val colorScheme = NexusTheme.colorScheme
    val typography = NexusTheme.typography

    val displayText = state.selectedDate?.let {
        when (type) {
            NexusDatePickerPanelType.Month, NexusDatePickerPanelType.Months, NexusDatePickerPanelType.MonthRange ->
                "${it.year}-${it.month.toString().padStart(2, '0')}"
            NexusDatePickerPanelType.Year, NexusDatePickerPanelType.Years, NexusDatePickerPanelType.YearRange ->
                "${it.year}"
            else -> "${it.year}-${it.month.toString().padStart(2, '0')}-${it.day.toString().padStart(2, '0')}"
        }
    } ?: ""

    Column(modifier = modifier) {
        // Input trigger
        NexusInput(
            value = displayText,
            onValueChange = { newValue ->
                if (newValue.isEmpty() && displayText.isNotEmpty()) {
                    state.clear()
                    onClear?.invoke()
                }
            },
            placeholder = placeholder,
            size = size,
            disabled = disabled,
            readonly = true,
            modifier = Modifier
                .fillMaxWidth()
                .then(
                    if (!disabled) {
                        Modifier.clickable {
                            state.isOpen = !state.isOpen
                            onVisibleChange?.invoke(state.isOpen)
                            if (state.isOpen) onFocus?.invoke() else onBlur?.invoke()
                        }
                    } else Modifier
                ),
            suffix = {
                if (clearable && displayText.isNotEmpty() && !disabled) {
                    NexusText(
                        text = "✕",
                        style = typography.extraSmall,
                        color = colorScheme.text.placeholder,
                        modifier = Modifier.clickable {
                            state.clear()
                            onClear?.invoke()
                        },
                    )
                } else {
                    NexusText(text = "📅", style = typography.extraSmall)
                }
            },
        )

        // Calendar popup
        if (state.isOpen && !disabled) {
            androidx.compose.ui.window.Popup(
                alignment = Alignment.TopStart,
                properties = androidx.compose.ui.window.PopupProperties(focusable = true),
                onDismissRequest = {
                    state.close()
                    onVisibleChange?.invoke(false)
                    onBlur?.invoke()
                },
            ) {
                NexusDatePickerPanel(
                    state = state,
                    type = type,
                    border = true,
                    showFooter = showFooter,
                    showConfirm = showConfirm,
                    showWeekNumber = showWeekNumber,
                    onPanelChange = onPanelChange,
                    onDateChange = {
                        onDateChange?.invoke(it)
                        if (type !in setOf(
                                NexusDatePickerPanelType.Dates,
                                NexusDatePickerPanelType.Months,
                                NexusDatePickerPanelType.Years,
                            )
                        ) {
                            state.close()
                            onVisibleChange?.invoke(false)
                        }
                    },
                    onClear = onClear,
                )
            }
        }
    }
}

@Composable
fun NexusDatePickerPanel(
    state: DatePickerState,
    modifier: Modifier = Modifier,
    border: Boolean = true,
    disabled: Boolean = false,
    clearable: Boolean = true,
    type: NexusDatePickerPanelType = NexusDatePickerPanelType.Date,
    showFooter: Boolean = false,
    showConfirm: Boolean = false,
    showWeekNumber: Boolean = false,
    onPanelChange: ((NexusDate, NexusDatePickerPanelType) -> Unit)? = null,
    onClear: (() -> Unit)? = null,
    onDateChange: ((NexusDate) -> Unit)? = null,
) {
    val colorScheme = NexusTheme.colorScheme
    val typography = NexusTheme.typography
    val shapes = NexusTheme.shapes

    Column(
        modifier = modifier
            .size(width = 292.dp, height = 320.dp)
            .clip(shapes.base)
            .background(colorScheme.fill.blank)
            .then(if (border) Modifier.border(1.dp, colorScheme.border.lighter, shapes.base) else Modifier)
            .padding(12.dp)
            .then(if (disabled) Modifier else Modifier),
    ) {
        // Header: navigation
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                NavButton("«", disabled = disabled) {
                    state.prevYear()
                    onPanelChange?.invoke(
                        NexusDate(state.viewYear, state.viewMonth, 1),
                        type,
                    )
                }
                NavButton("‹", disabled = disabled) {
                    state.prevMonth()
                    onPanelChange?.invoke(
                        NexusDate(state.viewYear, state.viewMonth, 1),
                        type,
                    )
                }
            }
            NexusText(
                text = when (type) {
                    NexusDatePickerPanelType.Year, NexusDatePickerPanelType.Years -> "${state.viewYear - 6} - ${state.viewYear + 5}"
                    NexusDatePickerPanelType.Month, NexusDatePickerPanelType.Months, NexusDatePickerPanelType.MonthRange -> "${state.viewYear}"
                    else -> "${state.viewYear} / ${state.viewMonth.toString().padStart(2, '0')}"
                },
                color = colorScheme.text.primary,
                style = typography.base,
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                NavButton("›", disabled = disabled) {
                    state.nextMonth()
                    onPanelChange?.invoke(
                        NexusDate(state.viewYear, state.viewMonth, 1),
                        type,
                    )
                }
                NavButton("»", disabled = disabled) {
                    state.nextYear()
                    onPanelChange?.invoke(
                        NexusDate(state.viewYear, state.viewMonth, 1),
                        type,
                    )
                }
            }
        }

        when (type) {
            NexusDatePickerPanelType.Year,
            NexusDatePickerPanelType.Years,
            NexusDatePickerPanelType.YearRange -> {
                YearPanel(
                    state = state,
                    disabled = disabled,
                    multiple = type == NexusDatePickerPanelType.Years,
                    onDateChange = onDateChange,
                )
            }
            NexusDatePickerPanelType.Month,
            NexusDatePickerPanelType.Months,
            NexusDatePickerPanelType.MonthRange -> {
                MonthPanel(
                    state = state,
                    disabled = disabled,
                    multiple = type == NexusDatePickerPanelType.Months,
                    onDateChange = onDateChange,
                )
            }
            else -> {
                DatePanel(
                    state = state,
                    disabled = disabled,
                    multiple = type == NexusDatePickerPanelType.Dates,
                    showWeekNumber = showWeekNumber,
                    onDateChange = onDateChange,
                )
            }
        }

        if (showFooter || showConfirm || clearable) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (clearable) {
                    NexusText(
                        text = "Clear",
                        color = if (disabled) colorScheme.text.disabled else colorScheme.text.secondary,
                        style = typography.small,
                        modifier = Modifier
                            .then(
                                if (!disabled) {
                                    Modifier
                                        .clickable {
                                            state.clear()
                                            onClear?.invoke()
                                        }
                                        .pointerHoverIcon(PointerIcon.Hand)
                                } else {
                                    Modifier
                                }
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                    )
                }
                if (showConfirm) {
                    NexusButton(
                        text = "Confirm",
                        onClick = { state.close() },
                        size = io.github.xingray.compose.nexus.theme.ComponentSize.Small,
                        disabled = disabled,
                        type = io.github.xingray.compose.nexus.theme.NexusType.Primary,
                    )
                }
            }
        }
    }
}

@Composable
private fun NavButton(text: String, disabled: Boolean = false, onClick: () -> Unit) {
    val colorScheme = NexusTheme.colorScheme
    val typography = NexusTheme.typography

    NexusText(
        text = text,
        color = if (disabled) colorScheme.text.disabled else colorScheme.text.secondary,
        style = typography.base,
        modifier = Modifier
            .then(
                if (!disabled) {
                    Modifier
                        .clickable(onClick = onClick)
                        .pointerHoverIcon(PointerIcon.Hand)
                } else {
                    Modifier
                }
            )
            .padding(4.dp),
    )
}

@Composable
private fun DatePanel(
    state: DatePickerState,
    disabled: Boolean,
    multiple: Boolean,
    showWeekNumber: Boolean,
    onDateChange: ((NexusDate) -> Unit)?,
) {
    val colorScheme = NexusTheme.colorScheme
    val typography = NexusTheme.typography

    val weekdays = listOf("Mo", "Tu", "We", "Th", "Fr", "Sa", "Su")
    Row(
        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        if (showWeekNumber) {
            Box(modifier = Modifier.size(24.dp), contentAlignment = Alignment.Center) {
                NexusText(text = "W", color = colorScheme.text.secondary, style = typography.extraSmall)
            }
        }
        weekdays.forEach { day ->
            Box(modifier = Modifier.size(32.dp), contentAlignment = Alignment.Center) {
                NexusText(text = day, color = colorScheme.text.secondary, style = typography.extraSmall)
            }
        }
    }

    val daysInMonth = daysInMonth(state.viewYear, state.viewMonth)
    val firstDayOfWeek = dayOfWeek(state.viewYear, state.viewMonth, 1)
    val totalCells = firstDayOfWeek + daysInMonth
    val rows = (totalCells + 6) / 7

    Column {
        for (row in 0 until rows) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                if (showWeekNumber) {
                    val weekStart = row * 7 - firstDayOfWeek + 1
                    val weekNo = if (weekStart > 0) ((weekStart - 1) / 7 + 1) else row + 1
                    Box(modifier = Modifier.size(24.dp), contentAlignment = Alignment.Center) {
                        NexusText(text = weekNo.toString(), color = colorScheme.text.placeholder, style = typography.extraSmall)
                    }
                }
                for (col in 0..6) {
                    val cellIndex = row * 7 + col
                    val dayNum = cellIndex - firstDayOfWeek + 1
                    if (dayNum in 1..daysInMonth) {
                        val date = NexusDate(state.viewYear, state.viewMonth, dayNum)
                        val isSelected = if (multiple) date in state.selectedDates else state.selectedDate == date
                        DayCell(
                            day = dayNum,
                            isSelected = isSelected,
                            isCurrentMonth = true,
                            disabled = disabled,
                            onClick = {
                                if (multiple) {
                                    state.toggleDate(date)
                                } else {
                                    state.selectWithoutClosing(date)
                                }
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

@Composable
private fun MonthPanel(
    state: DatePickerState,
    disabled: Boolean,
    multiple: Boolean,
    onDateChange: ((NexusDate) -> Unit)?,
) {
    val months = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
    Column(modifier = Modifier.padding(top = 10.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
        for (row in 0 until 4) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                for (col in 0 until 3) {
                    val month = row * 3 + col + 1
                    val date = NexusDate(state.viewYear, month, 1)
                    val selected = if (multiple) date in state.selectedDates else state.selectedDate?.let { it.year == date.year && it.month == date.month } == true
                    Box(
                        modifier = Modifier
                            .size(width = 80.dp, height = 44.dp)
                            .clip(CircleShape)
                            .background(if (selected) NexusTheme.colorScheme.primary.base else Color.Transparent)
                            .then(
                                if (!disabled) {
                                    Modifier
                                        .clickable {
                                            if (multiple) {
                                                state.toggleDate(date)
                                            } else {
                                                state.selectWithoutClosing(date)
                                            }
                                            onDateChange?.invoke(date)
                                        }
                                        .pointerHoverIcon(PointerIcon.Hand)
                                } else {
                                    Modifier
                                }
                            ),
                        contentAlignment = Alignment.Center,
                    ) {
                        NexusText(
                            text = months[month - 1],
                            color = if (selected) NexusTheme.colorScheme.white else if (disabled) NexusTheme.colorScheme.text.disabled else NexusTheme.colorScheme.text.regular,
                            style = NexusTheme.typography.small,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun YearPanel(
    state: DatePickerState,
    disabled: Boolean,
    multiple: Boolean,
    onDateChange: ((NexusDate) -> Unit)?,
) {
    val startYear = state.viewYear - 6
    Column(modifier = Modifier.padding(top = 10.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
        for (row in 0 until 4) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                for (col in 0 until 3) {
                    val year = startYear + row * 3 + col
                    val date = NexusDate(year, 1, 1)
                    val selected = if (multiple) date in state.selectedDates else state.selectedDate?.year == year
                    Box(
                        modifier = Modifier
                            .size(width = 80.dp, height = 44.dp)
                            .clip(CircleShape)
                            .background(if (selected) NexusTheme.colorScheme.primary.base else Color.Transparent)
                            .then(
                                if (!disabled) {
                                    Modifier
                                        .clickable {
                                            if (multiple) {
                                                state.toggleDate(date)
                                            } else {
                                                state.selectWithoutClosing(date)
                                            }
                                            onDateChange?.invoke(date)
                                        }
                                        .pointerHoverIcon(PointerIcon.Hand)
                                } else {
                                    Modifier
                                }
                            ),
                        contentAlignment = Alignment.Center,
                    ) {
                        NexusText(
                            text = year.toString(),
                            color = if (selected) NexusTheme.colorScheme.white else if (disabled) NexusTheme.colorScheme.text.disabled else NexusTheme.colorScheme.text.regular,
                            style = NexusTheme.typography.small,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DayCell(
    day: Int,
    isSelected: Boolean,
    isCurrentMonth: Boolean,
    disabled: Boolean = false,
    onClick: () -> Unit,
) {
    val colorScheme = NexusTheme.colorScheme
    val typography = NexusTheme.typography

    val bgColor = if (isSelected) colorScheme.primary.base else Color.Transparent
    val textColor = when {
        isSelected -> colorScheme.white
        disabled -> colorScheme.text.disabled
        !isCurrentMonth -> colorScheme.text.placeholder
        else -> colorScheme.text.regular
    }

    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(bgColor)
            .then(
                if (!disabled) {
                    Modifier
                        .clickable(onClick = onClick)
                        .pointerHoverIcon(PointerIcon.Hand)
                } else {
                    Modifier
                }
            ),
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
