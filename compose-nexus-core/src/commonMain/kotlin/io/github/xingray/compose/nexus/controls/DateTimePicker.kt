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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
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
import io.github.xingray.compose.nexus.theme.ComponentSize
import io.github.xingray.compose.nexus.theme.NexusTheme
import io.github.xingray.compose.nexus.theme.NexusType

data class NexusDateTime(
    val date: NexusDate,
    val time: NexusTime,
)

@Stable
class DateTimePickerState(
    initialDateTime: NexusDateTime? = null,
    initialRangeStart: NexusDateTime? = null,
    initialRangeEnd: NexusDateTime? = null,
) {
    var selectedDateTime by mutableStateOf(initialDateTime)
        private set
    var selectedRangeStart by mutableStateOf(initialRangeStart)
        private set
    var selectedRangeEnd by mutableStateOf(initialRangeEnd)
        private set
    var isOpen by mutableStateOf(false)
        internal set

    val singleDateState = DatePickerState(initialDateTime?.date)
    val rangeStartDateState = DatePickerState(initialRangeStart?.date)
    val rangeEndDateState = DatePickerState(initialRangeEnd?.date)

    var singleTime by mutableStateOf(initialDateTime?.time ?: NexusTime(0, 0, 0))
    var rangeStartTime by mutableStateOf(initialRangeStart?.time ?: NexusTime(0, 0, 0))
    var rangeEndTime by mutableStateOf(initialRangeEnd?.time ?: NexusTime(0, 0, 0))

    fun open() {
        isOpen = true
    }

    fun close() {
        isOpen = false
    }

    fun clear() {
        selectedDateTime = null
        selectedRangeStart = null
        selectedRangeEnd = null
        singleDateState.clear()
        rangeStartDateState.clear()
        rangeEndDateState.clear()
    }

    fun syncDraftFromSelected(
        type: NexusDatePickerPanelType,
        defaultStartTime: NexusTime = NexusTime(0, 0, 0),
        defaultEndTime: NexusTime = NexusTime(0, 0, 0),
    ) {
        if (isRangeType(type)) {
            resetDateState(rangeStartDateState, selectedRangeStart?.date)
            resetDateState(rangeEndDateState, selectedRangeEnd?.date)
            rangeStartTime = selectedRangeStart?.time ?: defaultStartTime
            rangeEndTime = selectedRangeEnd?.time ?: defaultEndTime
        } else {
            resetDateState(singleDateState, selectedDateTime?.date)
            singleTime = selectedDateTime?.time ?: defaultStartTime
        }
    }

    fun commitSingle(): NexusDateTime? {
        val date = singleDateState.selectedDate ?: return null
        val committed = NexusDateTime(date = date, time = singleTime)
        selectedDateTime = committed
        return committed
    }

    fun commitRange(): Pair<NexusDateTime?, NexusDateTime?> {
        var start = rangeStartDateState.selectedDate?.let { NexusDateTime(it, rangeStartTime) }
        var end = rangeEndDateState.selectedDate?.let { NexusDateTime(it, rangeEndTime) }
        if (start != null && end != null && compareDateTime(start, end) > 0) {
            val tmp = start
            start = end
            end = tmp
        }
        selectedRangeStart = start
        selectedRangeEnd = end
        return start to end
    }

    private fun resetDateState(dateState: DatePickerState, date: NexusDate?) {
        if (date != null) {
            dateState.selectWithoutClosing(date)
        } else {
            dateState.clear()
        }
    }
}

@Composable
fun rememberDateTimePickerState(
    initialDateTime: NexusDateTime? = null,
    initialRangeStart: NexusDateTime? = null,
    initialRangeEnd: NexusDateTime? = null,
): DateTimePickerState = remember {
    DateTimePickerState(
        initialDateTime = initialDateTime,
        initialRangeStart = initialRangeStart,
        initialRangeEnd = initialRangeEnd,
    )
}

@Composable
fun NexusDateTimePicker(
    state: DateTimePickerState = rememberDateTimePickerState(),
    modifier: Modifier = Modifier,
    type: NexusDatePickerPanelType = NexusDatePickerPanelType.DateTime,
    placeholder: String = "Select date and time",
    startPlaceholder: String = "Start date and time",
    endPlaceholder: String = "End date and time",
    rangeSeparator: String = " - ",
    size: ComponentSize = ComponentSize.Default,
    disabled: Boolean = false,
    clearable: Boolean = true,
    showFooter: Boolean = true,
    showConfirm: Boolean = true,
    showWeekNumber: Boolean = false,
    showNow: Boolean = true,
    format: String = "YYYY-MM-DD HH:mm:ss",
    dateFormat: String = "YYYY-MM-DD",
    timeFormat: String = "HH:mm:ss",
    defaultTime: Pair<NexusTime, NexusTime>? = null,
    onClear: (() -> Unit)? = null,
    onFocus: (() -> Unit)? = null,
    onBlur: (() -> Unit)? = null,
    onVisibleChange: ((Boolean) -> Unit)? = null,
    onPanelChange: ((NexusDate, NexusDatePickerPanelType) -> Unit)? = null,
    onCalendarChange: ((NexusDate?, NexusDate?) -> Unit)? = null,
    onChange: ((NexusDateTime?) -> Unit)? = null,
    onRangeChange: ((NexusDateTime?, NexusDateTime?) -> Unit)? = null,
) {
    val typography = NexusTheme.typography
    val isRange = isRangeType(type)
    val defaultStartTime = defaultTime?.first ?: NexusTime(0, 0, 0)
    val defaultEndTime = defaultTime?.second ?: NexusTime(0, 0, 0)

    val displayText = if (isRange) {
        val startText = state.selectedRangeStart?.let { formatDateTime(it, format) }.orEmpty()
        val endText = state.selectedRangeEnd?.let { formatDateTime(it, format) }.orEmpty()
        if (startText.isEmpty() && endText.isEmpty()) {
            ""
        } else {
            "$startText$rangeSeparator$endText"
        }
    } else {
        state.selectedDateTime?.let { formatDateTime(it, format) }.orEmpty()
    }

    val effectivePlaceholder = if (isRange) {
        "$startPlaceholder$rangeSeparator$endPlaceholder"
    } else {
        placeholder
    }

    Column(modifier = modifier) {
        NexusInput(
            value = displayText,
            onValueChange = { newValue ->
                if (newValue.isEmpty() && displayText.isNotEmpty()) {
                    state.clear()
                    onClear?.invoke()
                    if (isRange) onRangeChange?.invoke(null, null) else onChange?.invoke(null)
                }
            },
            placeholder = effectivePlaceholder,
            size = size,
            disabled = disabled,
            readonly = true,
            modifier = Modifier
                .fillMaxWidth()
                .then(
                    if (!disabled) {
                        Modifier.clickable {
                            state.syncDraftFromSelected(type, defaultStartTime, defaultEndTime)
                            state.isOpen = !state.isOpen
                            onVisibleChange?.invoke(state.isOpen)
                            if (state.isOpen) onFocus?.invoke() else onBlur?.invoke()
                        }
                    } else {
                        Modifier
                    },
                ),
            suffix = {
                if (clearable && displayText.isNotEmpty() && !disabled) {
                    NexusText(
                        text = "✕",
                        style = typography.extraSmall,
                        modifier = Modifier.clickable {
                            state.clear()
                            onClear?.invoke()
                            if (isRange) onRangeChange?.invoke(null, null) else onChange?.invoke(null)
                        },
                    )
                } else {
                    NexusText(text = "🗓", style = typography.extraSmall)
                }
            },
        )

        if (state.isOpen && !disabled) {
            androidx.compose.ui.window.Popup(
                alignment = Alignment.TopStart,
                properties = androidx.compose.ui.window.PopupProperties(focusable = true),
                onDismissRequest = {
                    state.close()
                    onVisibleChange?.invoke(false)
                    onBlur?.invoke()
                    if (isRange) {
                        val committed = state.commitRange()
                        onRangeChange?.invoke(committed.first, committed.second)
                    } else {
                        onChange?.invoke(state.commitSingle())
                    }
                },
            ) {
                NexusDateTimePickerPanel(
                    state = state,
                    type = type,
                    showFooter = showFooter,
                    showConfirm = showConfirm,
                    showWeekNumber = showWeekNumber,
                    showNow = showNow,
                    clearable = clearable,
                    dateFormat = dateFormat,
                    timeFormat = timeFormat,
                    defaultStartTime = defaultStartTime,
                    defaultEndTime = defaultEndTime,
                    onPanelChange = onPanelChange,
                    onCalendarChange = onCalendarChange,
                    onClear = {
                        onClear?.invoke()
                        if (isRange) onRangeChange?.invoke(null, null) else onChange?.invoke(null)
                    },
                    onChange = onChange,
                    onRangeChange = onRangeChange,
                    onConfirm = {
                        state.close()
                        onVisibleChange?.invoke(false)
                        onBlur?.invoke()
                    },
                )
            }
        }
    }
}

@Composable
fun NexusDateTimePickerPanel(
    state: DateTimePickerState,
    modifier: Modifier = Modifier,
    type: NexusDatePickerPanelType = NexusDatePickerPanelType.DateTime,
    border: Boolean = true,
    disabled: Boolean = false,
    clearable: Boolean = true,
    showFooter: Boolean = true,
    showConfirm: Boolean = true,
    showWeekNumber: Boolean = false,
    showNow: Boolean = true,
    dateFormat: String = "YYYY-MM-DD",
    timeFormat: String = "HH:mm:ss",
    defaultStartTime: NexusTime = NexusTime(0, 0, 0),
    defaultEndTime: NexusTime = NexusTime(0, 0, 0),
    onPanelChange: ((NexusDate, NexusDatePickerPanelType) -> Unit)? = null,
    onCalendarChange: ((NexusDate?, NexusDate?) -> Unit)? = null,
    onClear: (() -> Unit)? = null,
    onChange: ((NexusDateTime?) -> Unit)? = null,
    onRangeChange: ((NexusDateTime?, NexusDateTime?) -> Unit)? = null,
    onConfirm: (() -> Unit)? = null,
) {
    val colorScheme = NexusTheme.colorScheme
    val typography = NexusTheme.typography
    val isRange = isRangeType(type)
    val showSeconds = timeFormat.contains("ss")

    Column(
        modifier = modifier
            .clip(NexusTheme.shapes.base)
            .background(colorScheme.fill.blank)
            .then(if (border) Modifier.border(1.dp, colorScheme.border.lighter, NexusTheme.shapes.base) else Modifier)
            .padding(12.dp),
    ) {
        if (isRange) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    NexusText(text = "Start", style = typography.small, color = colorScheme.text.secondary)
                    NexusDatePickerPanel(
                        state = state.rangeStartDateState,
                        border = false,
                        disabled = disabled,
                        clearable = false,
                        type = NexusDatePickerPanelType.Date,
                        showFooter = false,
                        showConfirm = false,
                        showWeekNumber = showWeekNumber,
                        onPanelChange = { date, panelType ->
                            onPanelChange?.invoke(date, panelType)
                        },
                        onDateChange = {
                            onCalendarChange?.invoke(state.rangeStartDateState.selectedDate, state.rangeEndDateState.selectedDate)
                            if (!showConfirm) {
                                val committed = state.commitRange()
                                onRangeChange?.invoke(committed.first, committed.second)
                            }
                        },
                    )
                    DateTimeTimePanel(
                        time = state.rangeStartTime,
                        showSeconds = showSeconds,
                        disabled = disabled,
                        title = "Time ($dateFormat)",
                        onTimeChange = {
                            state.rangeStartTime = it
                            if (!showConfirm) {
                                val committed = state.commitRange()
                                onRangeChange?.invoke(committed.first, committed.second)
                            }
                        },
                    )
                }
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    NexusText(text = "End", style = typography.small, color = colorScheme.text.secondary)
                    NexusDatePickerPanel(
                        state = state.rangeEndDateState,
                        border = false,
                        disabled = disabled,
                        clearable = false,
                        type = NexusDatePickerPanelType.Date,
                        showFooter = false,
                        showConfirm = false,
                        showWeekNumber = showWeekNumber,
                        onPanelChange = { date, panelType ->
                            onPanelChange?.invoke(date, panelType)
                        },
                        onDateChange = {
                            onCalendarChange?.invoke(state.rangeStartDateState.selectedDate, state.rangeEndDateState.selectedDate)
                            if (!showConfirm) {
                                val committed = state.commitRange()
                                onRangeChange?.invoke(committed.first, committed.second)
                            }
                        },
                    )
                    DateTimeTimePanel(
                        time = state.rangeEndTime,
                        showSeconds = showSeconds,
                        disabled = disabled,
                        title = "Time ($dateFormat)",
                        onTimeChange = {
                            state.rangeEndTime = it
                            if (!showConfirm) {
                                val committed = state.commitRange()
                                onRangeChange?.invoke(committed.first, committed.second)
                            }
                        },
                    )
                }
            }
        } else {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                NexusDatePickerPanel(
                    state = state.singleDateState,
                    border = false,
                    disabled = disabled,
                    clearable = false,
                    type = NexusDatePickerPanelType.Date,
                    showFooter = false,
                    showConfirm = false,
                    showWeekNumber = showWeekNumber,
                    onPanelChange = { date, panelType ->
                        onPanelChange?.invoke(date, panelType)
                    },
                    onDateChange = {
                        if (!showConfirm) {
                            onChange?.invoke(state.commitSingle())
                        }
                    },
                )
                DateTimeTimePanel(
                    time = state.singleTime,
                    showSeconds = showSeconds,
                    disabled = disabled,
                    title = "Time ($dateFormat)",
                    onTimeChange = {
                        state.singleTime = it
                        if (!showConfirm) {
                            onChange?.invoke(state.commitSingle())
                        }
                    },
                )
            }
        }

        if (showFooter || showConfirm || clearable || showNow) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (clearable) {
                    NexusText(
                        text = "Clear",
                        style = typography.small,
                        color = if (disabled) colorScheme.text.disabled else colorScheme.text.secondary,
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
                if (showNow) {
                    NexusText(
                        text = "Now",
                        style = typography.small,
                        color = if (disabled) colorScheme.text.disabled else colorScheme.primary.base,
                        modifier = Modifier
                            .then(
                                if (!disabled) {
                                    Modifier
                                        .clickable {
                                            if (isRange) {
                                                state.rangeStartDateState.selectWithoutClosing(currentPanelDate(state.rangeStartDateState))
                                                state.rangeEndDateState.selectWithoutClosing(currentPanelDate(state.rangeEndDateState))
                                                state.rangeStartTime = defaultStartTime
                                                state.rangeEndTime = defaultEndTime
                                                val committed = state.commitRange()
                                                onRangeChange?.invoke(committed.first, committed.second)
                                            } else {
                                                state.singleDateState.selectWithoutClosing(currentPanelDate(state.singleDateState))
                                                onChange?.invoke(state.commitSingle())
                                            }
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
                        onClick = {
                            if (isRange) {
                                val committed = state.commitRange()
                                onRangeChange?.invoke(committed.first, committed.second)
                            } else {
                                onChange?.invoke(state.commitSingle())
                            }
                            onConfirm?.invoke()
                        },
                        type = NexusType.Primary,
                        size = ComponentSize.Small,
                        disabled = disabled,
                    )
                }
            }
        }
    }
}

@Composable
private fun DateTimeTimePanel(
    time: NexusTime,
    showSeconds: Boolean,
    disabled: Boolean,
    title: String,
    onTimeChange: (NexusTime) -> Unit,
) {
    val colorScheme = NexusTheme.colorScheme
    val typography = NexusTheme.typography

    var selectedHour by remember(time) { mutableStateOf(time.hour) }
    var selectedMinute by remember(time) { mutableStateOf(time.minute) }
    var selectedSecond by remember(time) { mutableStateOf(time.second) }

    Column(
        modifier = Modifier.width(198.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        NexusText(
            text = title,
            style = typography.small,
            color = colorScheme.text.secondary,
        )
        Row(horizontalArrangement = Arrangement.spacedBy(0.dp)) {
            DateTimeTimeColumn(
                values = (0..23).toList(),
                selected = selectedHour,
                disabled = disabled,
                onSelect = {
                    selectedHour = it
                    onTimeChange(NexusTime(selectedHour, selectedMinute, selectedSecond))
                },
            )
            DateTimeTimeColumn(
                values = (0..59).toList(),
                selected = selectedMinute,
                disabled = disabled,
                onSelect = {
                    selectedMinute = it
                    onTimeChange(NexusTime(selectedHour, selectedMinute, selectedSecond))
                },
            )
            if (showSeconds) {
                DateTimeTimeColumn(
                    values = (0..59).toList(),
                    selected = selectedSecond,
                    disabled = disabled,
                    onSelect = {
                        selectedSecond = it
                        onTimeChange(NexusTime(selectedHour, selectedMinute, selectedSecond))
                    },
                )
            }
        }
    }
}

@Composable
private fun DateTimeTimeColumn(
    values: List<Int>,
    selected: Int,
    disabled: Boolean,
    onSelect: (Int) -> Unit,
) {
    val colorScheme = NexusTheme.colorScheme
    val typography = NexusTheme.typography

    Column(
        modifier = Modifier
            .width(66.dp)
            .heightIn(max = 192.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        values.forEach { value ->
            val isSelected = value == selected
            val bgColor = if (isSelected) colorScheme.primary.light9 else Color.Transparent
            val textColor = when {
                disabled -> colorScheme.text.disabled
                isSelected -> colorScheme.primary.base
                else -> colorScheme.text.regular
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp)
                    .background(bgColor)
                    .then(
                        if (!disabled) {
                            Modifier
                                .clickable { onSelect(value) }
                                .pointerHoverIcon(PointerIcon.Hand)
                        } else {
                            Modifier
                        }
                    ),
                contentAlignment = Alignment.Center,
            ) {
                NexusText(
                    text = value.toString().padStart(2, '0'),
                    style = typography.small,
                    color = textColor,
                )
            }
        }
    }
}

private fun isRangeType(type: NexusDatePickerPanelType): Boolean =
    type == NexusDatePickerPanelType.DateTimeRange

private fun formatDateTime(value: NexusDateTime, pattern: String): String {
    return pattern
        .replace("YYYY", value.date.year.toString().padStart(4, '0'))
        .replace("MM", value.date.month.toString().padStart(2, '0'))
        .replace("DD", value.date.day.toString().padStart(2, '0'))
        .replace("HH", value.time.hour.toString().padStart(2, '0'))
        .replace("mm", value.time.minute.toString().padStart(2, '0'))
        .replace("ss", value.time.second.toString().padStart(2, '0'))
}

private fun compareDateTime(left: NexusDateTime, right: NexusDateTime): Int {
    if (left.date.year != right.date.year) return left.date.year - right.date.year
    if (left.date.month != right.date.month) return left.date.month - right.date.month
    if (left.date.day != right.date.day) return left.date.day - right.date.day
    if (left.time.hour != right.time.hour) return left.time.hour - right.time.hour
    if (left.time.minute != right.time.minute) return left.time.minute - right.time.minute
    return left.time.second - right.time.second
}

private fun currentPanelDate(state: DatePickerState): NexusDate =
    state.selectedDate ?: NexusDate(
        year = state.viewYear,
        month = state.viewMonth,
        day = 1,
    )
