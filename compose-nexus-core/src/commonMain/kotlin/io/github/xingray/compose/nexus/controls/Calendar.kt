package io.github.xingray.compose.nexus.controls

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import io.github.xingray.compose.nexus.theme.ComponentSize
import io.github.xingray.compose.nexus.theme.NexusTheme
import io.github.xingray.compose.nexus.theme.NexusType

enum class CalendarControllerType {
    Button,
    Select,
}

enum class CalendarFormatterType {
    Year,
    Month,
}

enum class CalendarDateType {
    PrevMonth,
    NextMonth,
    PrevYear,
    NextYear,
    Today,
}

enum class CalendarCellType(val value: String) {
    PrevMonth("prev-month"),
    CurrentMonth("current-month"),
    NextMonth("next-month"),
}

data class CalendarDateCellData(
    val type: io.github.xingray.compose.nexus.controls.CalendarCellType,
    val isSelected: Boolean,
    val day: String,
    val date: io.github.xingray.compose.nexus.controls.NexusDate,
)

@Stable
class CalendarState(
    initialYear: Int = 2026,
    initialMonth: Int = 1,
    initialDate: io.github.xingray.compose.nexus.controls.NexusDate? = null,
) {
    var viewYear by mutableIntStateOf(initialYear)
        private set
    var viewMonth by mutableIntStateOf(initialMonth.coerceIn(1, 12))
        private set
    var selectedDate by mutableStateOf(initialDate)
        private set

    fun setView(year: Int, month: Int) {
        viewYear = year
        viewMonth = month.coerceIn(1, 12)
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

    fun prevYear() {
        viewYear--
    }

    fun nextYear() {
        viewYear++
    }

    fun select(date: io.github.xingray.compose.nexus.controls.NexusDate) {
        selectedDate = date
        viewYear = date.year
        viewMonth = date.month
    }

    fun pickDay(day: io.github.xingray.compose.nexus.controls.NexusDate) {
        select(day)
    }

    fun selectDate(type: io.github.xingray.compose.nexus.controls.CalendarDateType) {
        when (type) {
            _root_ide_package_.io.github.xingray.compose.nexus.controls.CalendarDateType.PrevMonth -> prevMonth()
            _root_ide_package_.io.github.xingray.compose.nexus.controls.CalendarDateType.NextMonth -> nextMonth()
            _root_ide_package_.io.github.xingray.compose.nexus.controls.CalendarDateType.PrevYear -> prevYear()
            _root_ide_package_.io.github.xingray.compose.nexus.controls.CalendarDateType.NextYear -> nextYear()
            _root_ide_package_.io.github.xingray.compose.nexus.controls.CalendarDateType.Today -> today()
        }
    }

    fun today() {
        val today = _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusDate(
            _root_ide_package_.io.github.xingray.compose.nexus.controls.TODAY_YEAR,
            _root_ide_package_.io.github.xingray.compose.nexus.controls.TODAY_MONTH,
            _root_ide_package_.io.github.xingray.compose.nexus.controls.TODAY_DAY
        )
        select(today)
    }

    fun calculateValidatedDateRange(start: io.github.xingray.compose.nexus.controls.NexusDate, end: io.github.xingray.compose.nexus.controls.NexusDate): List<Pair<io.github.xingray.compose.nexus.controls.NexusDate, io.github.xingray.compose.nexus.controls.NexusDate>> {
        val valid = _root_ide_package_.io.github.xingray.compose.nexus.controls.validateRange(start, end) ?: return emptyList()
        val result = mutableListOf<Pair<io.github.xingray.compose.nexus.controls.NexusDate, io.github.xingray.compose.nexus.controls.NexusDate>>()
        var cursor = valid.first
        while (_root_ide_package_.io.github.xingray.compose.nexus.controls.compareDate(cursor, valid.second) <= 0) {
            val weekEnd = _root_ide_package_.io.github.xingray.compose.nexus.controls.addDays(cursor, 6)
            result += cursor to weekEnd
            cursor = _root_ide_package_.io.github.xingray.compose.nexus.controls.addDays(cursor, 7)
        }
        return result
    }
}

@Composable
fun rememberCalendarState(
    initialYear: Int = 2026,
    initialMonth: Int = 1,
    initialDate: io.github.xingray.compose.nexus.controls.NexusDate? = null,
): io.github.xingray.compose.nexus.controls.CalendarState = remember { _root_ide_package_.io.github.xingray.compose.nexus.controls.CalendarState(initialYear, initialMonth, initialDate) }

@Composable
fun NexusCalendar(
    state: io.github.xingray.compose.nexus.controls.CalendarState = _root_ide_package_.io.github.xingray.compose.nexus.controls.rememberCalendarState(),
    modifier: Modifier = Modifier,
    modelValue: io.github.xingray.compose.nexus.controls.NexusDate? = state.selectedDate,
    range: Pair<io.github.xingray.compose.nexus.controls.NexusDate, io.github.xingray.compose.nexus.controls.NexusDate>? = null,
    controllerType: io.github.xingray.compose.nexus.controls.CalendarControllerType = _root_ide_package_.io.github.xingray.compose.nexus.controls.CalendarControllerType.Button,
    formatter: ((value: Int, type: io.github.xingray.compose.nexus.controls.CalendarFormatterType) -> Any)? = null,
    onDateSelect: ((io.github.xingray.compose.nexus.controls.NexusDate) -> Unit)? = null,
    onModelValueChange: ((io.github.xingray.compose.nexus.controls.NexusDate) -> Unit)? = null,
    header: (@Composable (date: String) -> Unit)? = null,
    dateCellSlot: (@Composable (data: io.github.xingray.compose.nexus.controls.CalendarDateCellData) -> Unit)? = null,
    dateCell: (@Composable (io.github.xingray.compose.nexus.controls.NexusDate) -> Unit)? = null,
) {
    val colorScheme = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.colorScheme
    val typography = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.typography
    val shapes = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.shapes

    val validRange = remember(range) {
        range?.let { _root_ide_package_.io.github.xingray.compose.nexus.controls.validateRange(it.first, it.second) }
    }

    LaunchedEffect(modelValue) {
        if (modelValue != null && modelValue != state.selectedDate) {
            state.select(modelValue)
        }
    }

    LaunchedEffect(validRange?.first) {
        validRange?.first?.let { start ->
            state.setView(start.year, start.month)
        }
    }

    val cells = remember(state.viewYear, state.viewMonth, validRange) {
        if (validRange != null) {
            _root_ide_package_.io.github.xingray.compose.nexus.controls.buildRangeCells(validRange.first, validRange.second, state.viewYear, state.viewMonth)
        } else {
            _root_ide_package_.io.github.xingray.compose.nexus.controls.buildMonthCells(state.viewYear, state.viewMonth)
        }
    }

    val headerDate = if (validRange != null) {
        "${_root_ide_package_.io.github.xingray.compose.nexus.controls.formatDay(validRange.first)} ~ ${_root_ide_package_.io.github.xingray.compose.nexus.controls.formatDay(validRange.second)}"
    } else {
        "${state.viewYear}-${state.viewMonth.toString().padStart(2, '0')}"
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, colorScheme.border.lighter, shapes.base)
            .background(colorScheme.fill.blank),
    ) {
        if (header != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 10.dp),
            ) {
                header(headerDate)
            }
        } else {
            _root_ide_package_.io.github.xingray.compose.nexus.controls.CalendarHeader(
                state = state,
                headerDate = headerDate,
                hasFixedRange = validRange != null,
                controllerType = controllerType,
                formatter = formatter,
            )
        }

        _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusDivider()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 6.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            _root_ide_package_.io.github.xingray.compose.nexus.controls.WEEKDAY_LABELS.forEach { label ->
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center,
                ) {
                    _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
                        text = label,
                        color = colorScheme.text.regular,
                        style = typography.small,
                    )
                }
            }
        }

        cells.chunked(7).forEach { rowCells ->
            Row(modifier = Modifier.fillMaxWidth()) {
                rowCells.forEach { cell ->
                    val isSelected = state.selectedDate == cell.date
                    val slotData = _root_ide_package_.io.github.xingray.compose.nexus.controls.CalendarDateCellData(
                        type = cell.type,
                        isSelected = isSelected,
                        day = _root_ide_package_.io.github.xingray.compose.nexus.controls.formatDay(cell.date),
                        date = cell.date,
                    )

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(80.dp)
                            .border(1.dp, colorScheme.border.extraLight)
                            .clickable {
                                state.select(cell.date)
                                onDateSelect?.invoke(cell.date)
                                onModelValueChange?.invoke(cell.date)
                            }
                            .pointerHoverIcon(PointerIcon.Hand)
                            .padding(horizontal = 6.dp, vertical = 6.dp),
                    ) {
                        when {
                            dateCellSlot != null -> dateCellSlot(slotData)
                            dateCell != null -> dateCell(cell.date)
                            else -> {
                                _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
                                    text = cell.date.day.toString(),
                                    color = when {
                                        isSelected -> colorScheme.primary.base
                                        cell.type == _root_ide_package_.io.github.xingray.compose.nexus.controls.CalendarCellType.CurrentMonth -> colorScheme.text.primary
                                        else -> colorScheme.text.placeholder
                                    },
                                    style = typography.small,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CalendarHeader(
    state: io.github.xingray.compose.nexus.controls.CalendarState,
    headerDate: String,
    hasFixedRange: Boolean,
    controllerType: io.github.xingray.compose.nexus.controls.CalendarControllerType,
    formatter: ((value: Int, type: io.github.xingray.compose.nexus.controls.CalendarFormatterType) -> Any)?,
) {
    val typography = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.typography
    val colorScheme = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.colorScheme

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
            text = headerDate,
            color = colorScheme.text.primary,
            style = typography.base,
        )

        if (!hasFixedRange) {
            when (controllerType) {
                _root_ide_package_.io.github.xingray.compose.nexus.controls.CalendarControllerType.Button -> {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        _root_ide_package_.io.github.xingray.compose.nexus.controls.CalendarControllerButton("Prev Year") { state.prevYear() }
                        _root_ide_package_.io.github.xingray.compose.nexus.controls.CalendarControllerButton("Prev Month") { state.prevMonth() }
                        _root_ide_package_.io.github.xingray.compose.nexus.controls.CalendarControllerButton("Today") { state.today() }
                        _root_ide_package_.io.github.xingray.compose.nexus.controls.CalendarControllerButton("Next Month") { state.nextMonth() }
                        _root_ide_package_.io.github.xingray.compose.nexus.controls.CalendarControllerButton("Next Year") { state.nextYear() }
                    }
                }

                _root_ide_package_.io.github.xingray.compose.nexus.controls.CalendarControllerType.Select -> {
                    val yearState = _root_ide_package_.io.github.xingray.compose.nexus.controls.rememberSelectState(initialSelected = state.viewYear)
                    val monthState = _root_ide_package_.io.github.xingray.compose.nexus.controls.rememberSelectState(initialSelected = state.viewMonth)

                    LaunchedEffect(state.viewYear) {
                        yearState.selected = state.viewYear
                    }
                    LaunchedEffect(state.viewMonth) {
                        monthState.selected = state.viewMonth
                    }

                    val yearOptions = remember(state.viewYear, formatter) {
                        ((state.viewYear - 10)..(state.viewYear + 10)).map { year ->
                            _root_ide_package_.io.github.xingray.compose.nexus.controls.SelectOption(
                                value = year,
                                label = _root_ide_package_.io.github.xingray.compose.nexus.controls.formatControllerLabel(
                                    year,
                                    _root_ide_package_.io.github.xingray.compose.nexus.controls.CalendarFormatterType.Year,
                                    formatter
                                ),
                            )
                        }
                    }
                    val monthOptions = remember(formatter) {
                        (1..12).map { month ->
                            _root_ide_package_.io.github.xingray.compose.nexus.controls.SelectOption(
                                value = month,
                                label = _root_ide_package_.io.github.xingray.compose.nexus.controls.formatControllerLabel(
                                    month,
                                    _root_ide_package_.io.github.xingray.compose.nexus.controls.CalendarFormatterType.Month,
                                    formatter
                                ),
                            )
                        }
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusSelect(
                            state = yearState,
                            options = yearOptions,
                            onSelect = { state.setView(it, state.viewMonth) },
                            modifier = Modifier.width(120.dp),
                            size = _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Small,
                            fitInputWidth = true,
                        )
                        _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusSelect(
                            state = monthState,
                            options = monthOptions,
                            onSelect = { state.setView(state.viewYear, it) },
                            modifier = Modifier.width(108.dp),
                            size = _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Small,
                            fitInputWidth = true,
                        )
                        _root_ide_package_.io.github.xingray.compose.nexus.controls.CalendarControllerButton("Today") { state.today() }
                    }
                }
            }
        }
    }
}

@Composable
private fun CalendarControllerButton(
    text: String,
    onClick: () -> Unit,
) {
    _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusButton(
        text = text,
        onClick = onClick,
        size = _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Small,
        type = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusType.Info,
        plain = true,
    )
}

private fun formatControllerLabel(
    value: Int,
    type: io.github.xingray.compose.nexus.controls.CalendarFormatterType,
    formatter: ((value: Int, type: io.github.xingray.compose.nexus.controls.CalendarFormatterType) -> Any)?,
): String = formatter?.invoke(value, type)?.toString() ?: when (type) {
    _root_ide_package_.io.github.xingray.compose.nexus.controls.CalendarFormatterType.Year -> "$value"
    _root_ide_package_.io.github.xingray.compose.nexus.controls.CalendarFormatterType.Month -> value.toString().padStart(2, '0')
}

private data class CalendarCell(
    val date: io.github.xingray.compose.nexus.controls.NexusDate,
    val type: io.github.xingray.compose.nexus.controls.CalendarCellType,
)

private fun buildMonthCells(
    year: Int,
    month: Int,
): List<io.github.xingray.compose.nexus.controls.CalendarCell> {
    val firstWeekDay = _root_ide_package_.io.github.xingray.compose.nexus.controls.dayOfWeek(year, month, 1)
    val currentMonthDays = _root_ide_package_.io.github.xingray.compose.nexus.controls.daysInMonth(year, month)
    val prevMonth = if (month == 1) 12 else month - 1
    val prevYear = if (month == 1) year - 1 else year
    val prevMonthDays = _root_ide_package_.io.github.xingray.compose.nexus.controls.daysInMonth(prevYear, prevMonth)
    val nextMonth = if (month == 12) 1 else month + 1
    val nextYear = if (month == 12) year + 1 else year

    val cells = mutableListOf<io.github.xingray.compose.nexus.controls.CalendarCell>()
    for (index in 0 until 42) {
        val day = index - firstWeekDay + 1
        when {
            day <= 0 -> {
                cells += _root_ide_package_.io.github.xingray.compose.nexus.controls.CalendarCell(
                    date = _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusDate(prevYear, prevMonth, prevMonthDays + day),
                    type = _root_ide_package_.io.github.xingray.compose.nexus.controls.CalendarCellType.PrevMonth,
                )
            }

            day > currentMonthDays -> {
                cells += _root_ide_package_.io.github.xingray.compose.nexus.controls.CalendarCell(
                    date = _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusDate(nextYear, nextMonth, day - currentMonthDays),
                    type = _root_ide_package_.io.github.xingray.compose.nexus.controls.CalendarCellType.NextMonth,
                )
            }

            else -> {
                cells += _root_ide_package_.io.github.xingray.compose.nexus.controls.CalendarCell(
                    date = _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusDate(year, month, day),
                    type = _root_ide_package_.io.github.xingray.compose.nexus.controls.CalendarCellType.CurrentMonth,
                )
            }
        }
    }
    return cells
}

private fun buildRangeCells(
    start: io.github.xingray.compose.nexus.controls.NexusDate,
    end: io.github.xingray.compose.nexus.controls.NexusDate,
    viewYear: Int,
    viewMonth: Int,
): List<io.github.xingray.compose.nexus.controls.CalendarCell> {
    val result = mutableListOf<io.github.xingray.compose.nexus.controls.CalendarCell>()
    var cursor = start
    while (_root_ide_package_.io.github.xingray.compose.nexus.controls.compareDate(cursor, end) <= 0) {
        result += _root_ide_package_.io.github.xingray.compose.nexus.controls.CalendarCell(
            date = cursor,
            type = when {
                cursor.year == viewYear && cursor.month == viewMonth -> _root_ide_package_.io.github.xingray.compose.nexus.controls.CalendarCellType.CurrentMonth
                cursor.year * 12 + cursor.month < viewYear * 12 + viewMonth -> _root_ide_package_.io.github.xingray.compose.nexus.controls.CalendarCellType.PrevMonth
                else -> _root_ide_package_.io.github.xingray.compose.nexus.controls.CalendarCellType.NextMonth
            },
        )
        cursor = _root_ide_package_.io.github.xingray.compose.nexus.controls.addDays(cursor, 1)
    }
    return result
}

private fun validateRange(
    start: io.github.xingray.compose.nexus.controls.NexusDate,
    end: io.github.xingray.compose.nexus.controls.NexusDate,
): Pair<io.github.xingray.compose.nexus.controls.NexusDate, io.github.xingray.compose.nexus.controls.NexusDate>? {
    if (_root_ide_package_.io.github.xingray.compose.nexus.controls.compareDate(start, end) > 0) return null
    if (_root_ide_package_.io.github.xingray.compose.nexus.controls.dayOfWeek(start.year, start.month, start.day) != 0) return null
    if (_root_ide_package_.io.github.xingray.compose.nexus.controls.dayOfWeek(end.year, end.month, end.day) != 6) return null
    val monthSpan = (end.year - start.year) * 12 + (end.month - start.month)
    if (monthSpan > 1) return null
    return start to end
}

private fun compareDate(left: io.github.xingray.compose.nexus.controls.NexusDate, right: io.github.xingray.compose.nexus.controls.NexusDate): Int {
    return when {
        left.year != right.year -> left.year.compareTo(right.year)
        left.month != right.month -> left.month.compareTo(right.month)
        else -> left.day.compareTo(right.day)
    }
}

private fun addDays(date: io.github.xingray.compose.nexus.controls.NexusDate, days: Int): io.github.xingray.compose.nexus.controls.NexusDate {
    if (days == 0) return date
    var year = date.year
    var month = date.month
    var day = date.day
    var remaining = days

    if (remaining > 0) {
        while (remaining > 0) {
            val currentMonthDays = _root_ide_package_.io.github.xingray.compose.nexus.controls.daysInMonth(year, month)
            if (day + remaining <= currentMonthDays) {
                day += remaining
                remaining = 0
            } else {
                remaining -= (currentMonthDays - day + 1)
                day = 1
                if (month == 12) {
                    month = 1
                    year++
                } else {
                    month++
                }
            }
        }
    } else {
        while (remaining < 0) {
            if (day + remaining > 0) {
                day += remaining
                remaining = 0
            } else {
                remaining += day
                if (month == 1) {
                    month = 12
                    year--
                } else {
                    month--
                }
                day = _root_ide_package_.io.github.xingray.compose.nexus.controls.daysInMonth(year, month)
            }
        }
    }

    return _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusDate(year, month, day)
}

private fun formatDay(date: io.github.xingray.compose.nexus.controls.NexusDate): String {
    return "${date.year}-${date.month.toString().padStart(2, '0')}-${date.day.toString().padStart(2, '0')}"
}

private fun daysInMonth(year: Int, month: Int): Int = when (month) {
    1, 3, 5, 7, 8, 10, 12 -> 31
    4, 6, 9, 11 -> 30
    2 -> if (_root_ide_package_.io.github.xingray.compose.nexus.controls.isLeapYear(year)) 29 else 28
    else -> 30
}

private fun isLeapYear(year: Int): Boolean {
    return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
}

private fun dayOfWeek(year: Int, month: Int, day: Int): Int {
    var y = year
    var m = month
    if (m < 3) {
        m += 12
        y--
    }
    val q = day
    val k = y % 100
    val j = y / 100
    val h = (q + (13 * (m + 1)) / 5 + k + k / 4 + j / 4 + 5 * j) % 7
    return (h + 5) % 7
}

private val WEEKDAY_LABELS = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

private const val TODAY_YEAR = 2026
private const val TODAY_MONTH = 3
private const val TODAY_DAY = 1
