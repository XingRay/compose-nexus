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
    val date: io.github.xingray.compose.nexus.controls.NexusDate,
    val time: io.github.xingray.compose.nexus.controls.NexusTime,
)

@Stable
class DateTimePickerState(
    initialDateTime: io.github.xingray.compose.nexus.controls.NexusDateTime? = null,
    initialRangeStart: io.github.xingray.compose.nexus.controls.NexusDateTime? = null,
    initialRangeEnd: io.github.xingray.compose.nexus.controls.NexusDateTime? = null,
) {
    var selectedDateTime by mutableStateOf(initialDateTime)
        private set
    var selectedRangeStart by mutableStateOf(initialRangeStart)
        private set
    var selectedRangeEnd by mutableStateOf(initialRangeEnd)
        private set
    var isOpen by mutableStateOf(false)
        internal set

    val singleDateState = _root_ide_package_.io.github.xingray.compose.nexus.controls.DatePickerState(initialDateTime?.date)
    val rangeStartDateState = _root_ide_package_.io.github.xingray.compose.nexus.controls.DatePickerState(initialRangeStart?.date)
    val rangeEndDateState = _root_ide_package_.io.github.xingray.compose.nexus.controls.DatePickerState(initialRangeEnd?.date)

    var singleTime by mutableStateOf(initialDateTime?.time ?: _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusTime(0, 0, 0))
    var rangeStartTime by mutableStateOf(initialRangeStart?.time ?: _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusTime(0, 0, 0))
    var rangeEndTime by mutableStateOf(initialRangeEnd?.time ?: _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusTime(0, 0, 0))

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
        type: io.github.xingray.compose.nexus.controls.NexusDatePickerPanelType,
        defaultStartTime: io.github.xingray.compose.nexus.controls.NexusTime = _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusTime(0, 0, 0),
        defaultEndTime: io.github.xingray.compose.nexus.controls.NexusTime = _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusTime(0, 0, 0),
    ) {
        if (_root_ide_package_.io.github.xingray.compose.nexus.controls.isRangeType(type)) {
            resetDateState(rangeStartDateState, selectedRangeStart?.date)
            resetDateState(rangeEndDateState, selectedRangeEnd?.date)
            rangeStartTime = selectedRangeStart?.time ?: defaultStartTime
            rangeEndTime = selectedRangeEnd?.time ?: defaultEndTime
        } else {
            resetDateState(singleDateState, selectedDateTime?.date)
            singleTime = selectedDateTime?.time ?: defaultStartTime
        }
    }

    fun commitSingle(): io.github.xingray.compose.nexus.controls.NexusDateTime? {
        val date = singleDateState.selectedDate ?: return null
        val committed = _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusDateTime(date = date, time = singleTime)
        selectedDateTime = committed
        return committed
    }

    fun commitRange(): Pair<io.github.xingray.compose.nexus.controls.NexusDateTime?, io.github.xingray.compose.nexus.controls.NexusDateTime?> {
        var start = rangeStartDateState.selectedDate?.let { _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusDateTime(it, rangeStartTime) }
        var end = rangeEndDateState.selectedDate?.let { _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusDateTime(it, rangeEndTime) }
        if (start != null && end != null && _root_ide_package_.io.github.xingray.compose.nexus.controls.compareDateTime(start, end) > 0) {
            val tmp = start
            start = end
            end = tmp
        }
        selectedRangeStart = start
        selectedRangeEnd = end
        return start to end
    }

    private fun resetDateState(dateState: io.github.xingray.compose.nexus.controls.DatePickerState, date: io.github.xingray.compose.nexus.controls.NexusDate?) {
        if (date != null) {
            dateState.selectWithoutClosing(date)
        } else {
            dateState.clear()
        }
    }
}

@Composable
fun rememberDateTimePickerState(
    initialDateTime: io.github.xingray.compose.nexus.controls.NexusDateTime? = null,
    initialRangeStart: io.github.xingray.compose.nexus.controls.NexusDateTime? = null,
    initialRangeEnd: io.github.xingray.compose.nexus.controls.NexusDateTime? = null,
): io.github.xingray.compose.nexus.controls.DateTimePickerState = remember {
    _root_ide_package_.io.github.xingray.compose.nexus.controls.DateTimePickerState(
        initialDateTime = initialDateTime,
        initialRangeStart = initialRangeStart,
        initialRangeEnd = initialRangeEnd,
    )
}

@Composable
fun NexusDateTimePicker(
    state: io.github.xingray.compose.nexus.controls.DateTimePickerState = _root_ide_package_.io.github.xingray.compose.nexus.controls.rememberDateTimePickerState(),
    modifier: Modifier = Modifier,
    type: io.github.xingray.compose.nexus.controls.NexusDatePickerPanelType = _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusDatePickerPanelType.DateTime,
    placeholder: String = "Select date and time",
    startPlaceholder: String = "Start date and time",
    endPlaceholder: String = "End date and time",
    rangeSeparator: String = " - ",
    size: io.github.xingray.compose.nexus.theme.ComponentSize = _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Default,
    disabled: Boolean = false,
    clearable: Boolean = true,
    showFooter: Boolean = true,
    showConfirm: Boolean = true,
    showWeekNumber: Boolean = false,
    showNow: Boolean = true,
    format: String = "YYYY-MM-DD HH:mm:ss",
    dateFormat: String = "YYYY-MM-DD",
    timeFormat: String = "HH:mm:ss",
    defaultTime: Pair<io.github.xingray.compose.nexus.controls.NexusTime, io.github.xingray.compose.nexus.controls.NexusTime>? = null,
    onClear: (() -> Unit)? = null,
    onFocus: (() -> Unit)? = null,
    onBlur: (() -> Unit)? = null,
    onVisibleChange: ((Boolean) -> Unit)? = null,
    onPanelChange: ((io.github.xingray.compose.nexus.controls.NexusDate, io.github.xingray.compose.nexus.controls.NexusDatePickerPanelType) -> Unit)? = null,
    onCalendarChange: ((io.github.xingray.compose.nexus.controls.NexusDate?, io.github.xingray.compose.nexus.controls.NexusDate?) -> Unit)? = null,
    onChange: ((io.github.xingray.compose.nexus.controls.NexusDateTime?) -> Unit)? = null,
    onRangeChange: ((io.github.xingray.compose.nexus.controls.NexusDateTime?, io.github.xingray.compose.nexus.controls.NexusDateTime?) -> Unit)? = null,
) {
    val typography = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.typography
    val isRange = _root_ide_package_.io.github.xingray.compose.nexus.controls.isRangeType(type)
    val defaultStartTime = defaultTime?.first ?: _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusTime(0, 0, 0)
    val defaultEndTime = defaultTime?.second ?: _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusTime(0, 0, 0)

    val displayText = if (isRange) {
        val startText = state.selectedRangeStart?.let { _root_ide_package_.io.github.xingray.compose.nexus.controls.formatDateTime(it, format) }.orEmpty()
        val endText = state.selectedRangeEnd?.let { _root_ide_package_.io.github.xingray.compose.nexus.controls.formatDateTime(it, format) }.orEmpty()
        if (startText.isEmpty() && endText.isEmpty()) {
            ""
        } else {
            "$startText$rangeSeparator$endText"
        }
    } else {
        state.selectedDateTime?.let { _root_ide_package_.io.github.xingray.compose.nexus.controls.formatDateTime(it, format) }.orEmpty()
    }

    val effectivePlaceholder = if (isRange) {
        "$startPlaceholder$rangeSeparator$endPlaceholder"
    } else {
        placeholder
    }

    Column(modifier = modifier) {
        _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusInput(
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
                    _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
                        text = "✕",
                        style = typography.extraSmall,
                        modifier = Modifier.clickable {
                            state.clear()
                            onClear?.invoke()
                            if (isRange) onRangeChange?.invoke(null, null) else onChange?.invoke(null)
                        },
                    )
                } else {
                    _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(text = "🗓", style = typography.extraSmall)
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
                _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusDateTimePickerPanel(
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
    state: io.github.xingray.compose.nexus.controls.DateTimePickerState,
    modifier: Modifier = Modifier,
    type: io.github.xingray.compose.nexus.controls.NexusDatePickerPanelType = _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusDatePickerPanelType.DateTime,
    border: Boolean = true,
    disabled: Boolean = false,
    clearable: Boolean = true,
    showFooter: Boolean = true,
    showConfirm: Boolean = true,
    showWeekNumber: Boolean = false,
    showNow: Boolean = true,
    dateFormat: String = "YYYY-MM-DD",
    timeFormat: String = "HH:mm:ss",
    defaultStartTime: io.github.xingray.compose.nexus.controls.NexusTime = _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusTime(0, 0, 0),
    defaultEndTime: io.github.xingray.compose.nexus.controls.NexusTime = _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusTime(0, 0, 0),
    onPanelChange: ((io.github.xingray.compose.nexus.controls.NexusDate, io.github.xingray.compose.nexus.controls.NexusDatePickerPanelType) -> Unit)? = null,
    onCalendarChange: ((io.github.xingray.compose.nexus.controls.NexusDate?, io.github.xingray.compose.nexus.controls.NexusDate?) -> Unit)? = null,
    onClear: (() -> Unit)? = null,
    onChange: ((io.github.xingray.compose.nexus.controls.NexusDateTime?) -> Unit)? = null,
    onRangeChange: ((io.github.xingray.compose.nexus.controls.NexusDateTime?, io.github.xingray.compose.nexus.controls.NexusDateTime?) -> Unit)? = null,
    onConfirm: (() -> Unit)? = null,
) {
    val colorScheme = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.colorScheme
    val typography = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.typography
    val isRange = _root_ide_package_.io.github.xingray.compose.nexus.controls.isRangeType(type)
    val showSeconds = timeFormat.contains("ss")

    Column(
        modifier = modifier
            .clip(_root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.shapes.base)
            .background(colorScheme.fill.blank)
            .then(if (border) Modifier.border(1.dp, colorScheme.border.lighter, _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.shapes.base) else Modifier)
            .padding(12.dp),
    ) {
        if (isRange) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(text = "Start", style = typography.small, color = colorScheme.text.secondary)
                    _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusDatePickerPanel(
                        state = state.rangeStartDateState,
                        border = false,
                        disabled = disabled,
                        clearable = false,
                        type = _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusDatePickerPanelType.Date,
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
                    _root_ide_package_.io.github.xingray.compose.nexus.controls.DateTimeTimePanel(
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
                    _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(text = "End", style = typography.small, color = colorScheme.text.secondary)
                    _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusDatePickerPanel(
                        state = state.rangeEndDateState,
                        border = false,
                        disabled = disabled,
                        clearable = false,
                        type = _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusDatePickerPanelType.Date,
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
                    _root_ide_package_.io.github.xingray.compose.nexus.controls.DateTimeTimePanel(
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
                _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusDatePickerPanel(
                    state = state.singleDateState,
                    border = false,
                    disabled = disabled,
                    clearable = false,
                    type = _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusDatePickerPanelType.Date,
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
                _root_ide_package_.io.github.xingray.compose.nexus.controls.DateTimeTimePanel(
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
                    _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
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
                    _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
                        text = "Now",
                        style = typography.small,
                        color = if (disabled) colorScheme.text.disabled else colorScheme.primary.base,
                        modifier = Modifier
                            .then(
                                if (!disabled) {
                                    Modifier
                                        .clickable {
                                            if (isRange) {
                                                state.rangeStartDateState.selectWithoutClosing(_root_ide_package_.io.github.xingray.compose.nexus.controls.currentPanelDate(state.rangeStartDateState))
                                                state.rangeEndDateState.selectWithoutClosing(_root_ide_package_.io.github.xingray.compose.nexus.controls.currentPanelDate(state.rangeEndDateState))
                                                state.rangeStartTime = defaultStartTime
                                                state.rangeEndTime = defaultEndTime
                                                val committed = state.commitRange()
                                                onRangeChange?.invoke(committed.first, committed.second)
                                            } else {
                                                state.singleDateState.selectWithoutClosing(_root_ide_package_.io.github.xingray.compose.nexus.controls.currentPanelDate(state.singleDateState))
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
                    _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusButton(
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
                        type = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusType.Primary,
                        size = _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Small,
                        disabled = disabled,
                    )
                }
            }
        }
    }
}

@Composable
private fun DateTimeTimePanel(
    time: io.github.xingray.compose.nexus.controls.NexusTime,
    showSeconds: Boolean,
    disabled: Boolean,
    title: String,
    onTimeChange: (io.github.xingray.compose.nexus.controls.NexusTime) -> Unit,
) {
    val colorScheme = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.colorScheme
    val typography = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.typography

    var selectedHour by remember(time) { mutableStateOf(time.hour) }
    var selectedMinute by remember(time) { mutableStateOf(time.minute) }
    var selectedSecond by remember(time) { mutableStateOf(time.second) }

    Column(
        modifier = Modifier.width(198.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
            text = title,
            style = typography.small,
            color = colorScheme.text.secondary,
        )
        Row(horizontalArrangement = Arrangement.spacedBy(0.dp)) {
            _root_ide_package_.io.github.xingray.compose.nexus.controls.DateTimeTimeColumn(
                values = (0..23).toList(),
                selected = selectedHour,
                disabled = disabled,
                onSelect = {
                    selectedHour = it
                    onTimeChange(_root_ide_package_.io.github.xingray.compose.nexus.controls.NexusTime(selectedHour, selectedMinute, selectedSecond))
                },
            )
            _root_ide_package_.io.github.xingray.compose.nexus.controls.DateTimeTimeColumn(
                values = (0..59).toList(),
                selected = selectedMinute,
                disabled = disabled,
                onSelect = {
                    selectedMinute = it
                    onTimeChange(_root_ide_package_.io.github.xingray.compose.nexus.controls.NexusTime(selectedHour, selectedMinute, selectedSecond))
                },
            )
            if (showSeconds) {
                _root_ide_package_.io.github.xingray.compose.nexus.controls.DateTimeTimeColumn(
                    values = (0..59).toList(),
                    selected = selectedSecond,
                    disabled = disabled,
                    onSelect = {
                        selectedSecond = it
                        onTimeChange(_root_ide_package_.io.github.xingray.compose.nexus.controls.NexusTime(selectedHour, selectedMinute, selectedSecond))
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
    val colorScheme = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.colorScheme
    val typography = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.typography

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
                _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
                    text = value.toString().padStart(2, '0'),
                    style = typography.small,
                    color = textColor,
                )
            }
        }
    }
}

private fun isRangeType(type: io.github.xingray.compose.nexus.controls.NexusDatePickerPanelType): Boolean =
    type == _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusDatePickerPanelType.DateTimeRange

private fun formatDateTime(value: io.github.xingray.compose.nexus.controls.NexusDateTime, pattern: String): String {
    return pattern
        .replace("YYYY", value.date.year.toString().padStart(4, '0'))
        .replace("MM", value.date.month.toString().padStart(2, '0'))
        .replace("DD", value.date.day.toString().padStart(2, '0'))
        .replace("HH", value.time.hour.toString().padStart(2, '0'))
        .replace("mm", value.time.minute.toString().padStart(2, '0'))
        .replace("ss", value.time.second.toString().padStart(2, '0'))
}

private fun compareDateTime(left: io.github.xingray.compose.nexus.controls.NexusDateTime, right: io.github.xingray.compose.nexus.controls.NexusDateTime): Int {
    if (left.date.year != right.date.year) return left.date.year - right.date.year
    if (left.date.month != right.date.month) return left.date.month - right.date.month
    if (left.date.day != right.date.day) return left.date.day - right.date.day
    if (left.time.hour != right.time.hour) return left.time.hour - right.time.hour
    if (left.time.minute != right.time.minute) return left.time.minute - right.time.minute
    return left.time.second - right.time.second
}

private fun currentPanelDate(state: io.github.xingray.compose.nexus.controls.DatePickerState): io.github.xingray.compose.nexus.controls.NexusDate =
    state.selectedDate ?: _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusDate(
        year = state.viewYear,
        month = state.viewMonth,
        day = 1,
    )
