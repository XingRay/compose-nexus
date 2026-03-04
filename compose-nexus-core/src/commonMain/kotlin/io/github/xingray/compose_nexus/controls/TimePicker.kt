package io.github.xingray.compose_nexus.controls

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
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import io.github.xingray.compose_nexus.theme.ComponentSize
import io.github.xingray.compose_nexus.theme.NexusTheme
import io.github.xingray.compose_nexus.theme.NexusType

data class NexusTime(
    val hour: Int,
    val minute: Int,
    val second: Int = 0,
)

@Stable
class TimePickerState(
    initialTime: NexusTime? = null,
    initialRange: Pair<NexusTime, NexusTime>? = null,
) {
    var selectedTime by mutableStateOf(initialTime)
    var selectedRangeStart by mutableStateOf(initialRange?.first)
    var selectedRangeEnd by mutableStateOf(initialRange?.second)
    var isOpen by mutableStateOf(false)
        internal set

    fun open() {
        isOpen = true
    }

    fun close() {
        isOpen = false
    }

    fun select(time: NexusTime) {
        selectedTime = time
        close()
    }

    fun selectRange(start: NexusTime, end: NexusTime) {
        selectedRangeStart = start
        selectedRangeEnd = end
        close()
    }

    fun clearSingle() {
        selectedTime = null
    }

    fun clearRange() {
        selectedRangeStart = null
        selectedRangeEnd = null
    }
}

@Composable
fun rememberTimePickerState(
    initialTime: NexusTime? = null,
    initialRange: Pair<NexusTime, NexusTime>? = null,
): TimePickerState = remember { TimePickerState(initialTime, initialRange) }

@Composable
fun NexusTimePicker(
    state: TimePickerState = rememberTimePickerState(),
    modifier: Modifier = Modifier,
    placeholder: String = "Select time",
    startPlaceholder: String = "Start time",
    endPlaceholder: String = "End time",
    showSeconds: Boolean = false,
    size: ComponentSize = ComponentSize.Default,
    readonly: Boolean = false,
    disabled: Boolean = false,
    editable: Boolean = true,
    clearable: Boolean = true,
    isRange: Boolean = false,
    arrowControl: Boolean = false,
    rangeSeparator: String = " - ",
    format: String? = null,
    valueFormat: String? = null,
    prefixIcon: (@Composable () -> Unit)? = null,
    clearIcon: (@Composable () -> Unit)? = null,
    disabledHours: ((role: String) -> List<Int>)? = null,
    disabledMinutes: ((hour: Int, role: String) -> List<Int>)? = null,
    disabledSeconds: ((hour: Int, minute: Int, role: String) -> List<Int>)? = null,
    emptyValues: Set<Any?> = setOf(null),
    valueOnClear: Any? = null,
    onTimeChange: ((NexusTime) -> Unit)? = null,
    onRangeChange: ((NexusTime, NexusTime) -> Unit)? = null,
    onChange: ((Any?) -> Unit)? = null,
    onFocus: (() -> Unit)? = null,
    onBlur: (() -> Unit)? = null,
    onClear: (() -> Unit)? = null,
    onVisibleChange: ((Boolean) -> Unit)? = null,
) {
    val typography = NexusTheme.typography
    val hasValue = if (isRange) {
        val start = state.selectedRangeStart
        val end = state.selectedRangeEnd
        start != null && end != null && !emptyValues.contains(start) && !emptyValues.contains(end)
    } else {
        state.selectedTime != null && !emptyValues.contains(state.selectedTime)
    }

    fun setVisible(visible: Boolean) {
        if (state.isOpen != visible) {
            state.isOpen = visible
            onVisibleChange?.invoke(visible)
        }
    }

    fun displayTime(time: NexusTime): String = formatTime(
        time = time,
        showSeconds = showSeconds,
        pattern = format,
    )

    val displayText = if (isRange) {
        val startText = state.selectedRangeStart?.let { displayTime(it) }.orEmpty()
        val endText = state.selectedRangeEnd?.let { displayTime(it) }.orEmpty()
        when {
            startText.isNotEmpty() && endText.isNotEmpty() -> "$startText$rangeSeparator$endText"
            startText.isNotEmpty() -> startText
            endText.isNotEmpty() -> endText
            else -> ""
        }
    } else {
        state.selectedTime?.let { displayTime(it) }.orEmpty()
    }

    val placeholderText = if (isRange) "$startPlaceholder$rangeSeparator$endPlaceholder" else placeholder

    Column(modifier = modifier) {
        NexusInput(
            value = displayText,
            onValueChange = {},
            placeholder = placeholderText,
            disabled = disabled,
            readonly = readonly || !editable,
            clearable = false,
            size = size,
            modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled = !disabled) {
                    setVisible(!state.isOpen)
                },
            prefix = prefixIcon ?: { NexusText(text = "🕐", style = typography.extraSmall) },
            suffix = {
                if (clearable && hasValue && !disabled) {
                    Box(
                        modifier = Modifier
                            .clickable {
                                if (isRange) {
                                    state.clearRange()
                                } else {
                                    state.clearSingle()
                                }
                                onChange?.invoke(valueOnClear)
                                onClear?.invoke()
                            }
                            .pointerHoverIcon(PointerIcon.Hand),
                    ) {
                        if (clearIcon != null) {
                            clearIcon()
                        } else {
                            NexusText(
                                text = "✕",
                                color = NexusTheme.colorScheme.text.placeholder,
                                style = typography.extraSmall,
                            )
                        }
                    }
                } else {
                    NexusText(
                        text = if (state.isOpen) "▲" else "▼",
                        color = NexusTheme.colorScheme.text.placeholder,
                        style = typography.extraSmall,
                    )
                }
            },
            onFocus = onFocus,
            onBlur = onBlur,
        )

        if (state.isOpen) {
            Popup(
                alignment = Alignment.TopStart,
                properties = PopupProperties(focusable = true),
                onDismissRequest = { setVisible(false) },
            ) {
                TimePickerPanel(
                    state = state,
                    isRange = isRange,
                    showSeconds = showSeconds,
                    arrowControl = arrowControl,
                    disabledHours = disabledHours,
                    disabledMinutes = disabledMinutes,
                    disabledSeconds = disabledSeconds,
                    format = format,
                    valueFormat = valueFormat,
                    onTimeChange = onTimeChange,
                    onRangeChange = onRangeChange,
                    onChange = onChange,
                    onClose = { setVisible(false) },
                )
            }
        }
    }
}

@Composable
private fun TimePickerPanel(
    state: TimePickerState,
    isRange: Boolean,
    showSeconds: Boolean,
    arrowControl: Boolean,
    disabledHours: ((role: String) -> List<Int>)?,
    disabledMinutes: ((hour: Int, role: String) -> List<Int>)?,
    disabledSeconds: ((hour: Int, minute: Int, role: String) -> List<Int>)?,
    format: String?,
    valueFormat: String?,
    onTimeChange: ((NexusTime) -> Unit)?,
    onRangeChange: ((NexusTime, NexusTime) -> Unit)?,
    onChange: ((Any?) -> Unit)?,
    onClose: () -> Unit,
) {
    val colorScheme = NexusTheme.colorScheme
    val shapes = NexusTheme.shapes

    Column(
        modifier = Modifier
            .clip(shapes.base)
            .background(colorScheme.fill.blank)
            .border(1.dp, colorScheme.border.lighter, shapes.base)
            .padding(8.dp),
    ) {
        if (isRange) {
            var startHour by remember(state.selectedRangeStart) { mutableStateOf(state.selectedRangeStart?.hour ?: 0) }
            var startMinute by remember(state.selectedRangeStart) { mutableStateOf(state.selectedRangeStart?.minute ?: 0) }
            var startSecond by remember(state.selectedRangeStart) { mutableStateOf(state.selectedRangeStart?.second ?: 0) }
            var endHour by remember(state.selectedRangeEnd) { mutableStateOf(state.selectedRangeEnd?.hour ?: 23) }
            var endMinute by remember(state.selectedRangeEnd) { mutableStateOf(state.selectedRangeEnd?.minute ?: 59) }
            var endSecond by remember(state.selectedRangeEnd) { mutableStateOf(state.selectedRangeEnd?.second ?: 59) }

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                TimeSelectionColumns(
                    title = "Start",
                    role = "start",
                    showSeconds = showSeconds,
                    arrowControl = arrowControl,
                    hour = startHour,
                    minute = startMinute,
                    second = startSecond,
                    disabledHours = disabledHours,
                    disabledMinutes = disabledMinutes,
                    disabledSeconds = disabledSeconds,
                    onHourChange = { startHour = it },
                    onMinuteChange = { startMinute = it },
                    onSecondChange = { startSecond = it },
                )
                TimeSelectionColumns(
                    title = "End",
                    role = "end",
                    showSeconds = showSeconds,
                    arrowControl = arrowControl,
                    hour = endHour,
                    minute = endMinute,
                    second = endSecond,
                    disabledHours = disabledHours,
                    disabledMinutes = disabledMinutes,
                    disabledSeconds = disabledSeconds,
                    onHourChange = { endHour = it },
                    onMinuteChange = { endMinute = it },
                    onSecondChange = { endSecond = it },
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.End,
            ) {
                NexusButton(
                    onClick = {
                        val start = NexusTime(startHour, startMinute, startSecond)
                        val end = NexusTime(endHour, endMinute, endSecond)
                        state.selectRange(start, end)
                        onRangeChange?.invoke(start, end)

                        val emitValue = if (valueFormat != null) {
                            listOf(
                                formatTime(start, showSeconds, valueFormat),
                                formatTime(end, showSeconds, valueFormat),
                            )
                        } else {
                            listOf(start, end)
                        }
                        onChange?.invoke(emitValue)
                        onClose()
                    },
                    type = NexusType.Primary,
                    size = ComponentSize.Small,
                ) {
                    NexusText(text = "OK")
                }
            }
        } else {
            var selectedHour by remember(state.selectedTime) { mutableStateOf(state.selectedTime?.hour ?: 0) }
            var selectedMinute by remember(state.selectedTime) { mutableStateOf(state.selectedTime?.minute ?: 0) }
            var selectedSecond by remember(state.selectedTime) { mutableStateOf(state.selectedTime?.second ?: 0) }

            TimeSelectionColumns(
                title = null,
                role = "single",
                showSeconds = showSeconds,
                arrowControl = arrowControl,
                hour = selectedHour,
                minute = selectedMinute,
                second = selectedSecond,
                disabledHours = disabledHours,
                disabledMinutes = disabledMinutes,
                disabledSeconds = disabledSeconds,
                onHourChange = { selectedHour = it },
                onMinuteChange = { selectedMinute = it },
                onSecondChange = { selectedSecond = it },
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.End,
            ) {
                NexusButton(
                    onClick = {
                        val time = NexusTime(selectedHour, selectedMinute, selectedSecond)
                        state.select(time)
                        onTimeChange?.invoke(time)
                        val emitValue = if (valueFormat != null) {
                            formatTime(time, showSeconds, valueFormat)
                        } else {
                            time
                        }
                        onChange?.invoke(emitValue)
                        onClose()
                    },
                    type = NexusType.Primary,
                    size = ComponentSize.Small,
                ) {
                    NexusText(text = "OK")
                }
            }
        }
    }
}

@Composable
private fun TimeSelectionColumns(
    title: String?,
    role: String,
    showSeconds: Boolean,
    arrowControl: Boolean,
    hour: Int,
    minute: Int,
    second: Int,
    disabledHours: ((role: String) -> List<Int>)?,
    disabledMinutes: ((hour: Int, role: String) -> List<Int>)?,
    disabledSeconds: ((hour: Int, minute: Int, role: String) -> List<Int>)?,
    onHourChange: (Int) -> Unit,
    onMinuteChange: (Int) -> Unit,
    onSecondChange: (Int) -> Unit,
) {
    val disabledHourSet = disabledHours?.invoke(role)?.toSet() ?: emptySet()
    val disabledMinuteSet = disabledMinutes?.invoke(hour, role)?.toSet() ?: emptySet()
    val disabledSecondSet = disabledSeconds?.invoke(hour, minute, role)?.toSet() ?: emptySet()

    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        if (title != null) {
            NexusText(
                text = title,
                style = NexusTheme.typography.extraSmall,
                color = NexusTheme.colorScheme.text.secondary,
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(0.dp)) {
            TimeColumn(
                values = (0..23).toList(),
                selected = hour,
                disabledValues = disabledHourSet,
                arrowControl = arrowControl,
                onSelect = onHourChange,
            )
            TimeColumn(
                values = (0..59).toList(),
                selected = minute,
                disabledValues = disabledMinuteSet,
                arrowControl = arrowControl,
                onSelect = onMinuteChange,
            )
            if (showSeconds) {
                TimeColumn(
                    values = (0..59).toList(),
                    selected = second,
                    disabledValues = disabledSecondSet,
                    arrowControl = arrowControl,
                    onSelect = onSecondChange,
                )
            }
        }
    }
}

@Composable
private fun TimeColumn(
    values: List<Int>,
    selected: Int,
    disabledValues: Set<Int>,
    arrowControl: Boolean,
    onSelect: (Int) -> Unit,
) {
    val colorScheme = NexusTheme.colorScheme
    val typography = NexusTheme.typography

    if (arrowControl) {
        fun findAvailable(current: Int, delta: Int): Int {
            var next = current
            repeat(values.size) {
                next += delta
                if (next < values.first()) next = values.last()
                if (next > values.last()) next = values.first()
                if (!disabledValues.contains(next)) return next
            }
            return current
        }

        Column(
            modifier = Modifier
                .width(60.dp)
                .height(120.dp)
                .border(1.dp, colorScheme.border.lighter, NexusTheme.shapes.base),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            NexusText(
                text = "▲",
                color = colorScheme.text.secondary,
                style = typography.extraSmall,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onSelect(findAvailable(selected, -1))
                    }
                    .padding(vertical = 8.dp),
            )
            NexusText(
                text = selected.toString().padStart(2, '0'),
                color = if (disabledValues.contains(selected)) colorScheme.disabled.text else colorScheme.primary.base,
                style = typography.base,
            )
            NexusText(
                text = "▼",
                color = colorScheme.text.secondary,
                style = typography.extraSmall,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onSelect(findAvailable(selected, 1))
                    }
                    .padding(vertical = 8.dp),
            )
        }
    } else {
        Column(
            modifier = Modifier
                .width(60.dp)
                .heightIn(max = 200.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            values.forEach { value ->
                val isSelected = value == selected
                val isDisabled = disabledValues.contains(value)
                val bgColor = if (isSelected) colorScheme.primary.light9 else Color.Transparent
                val textColor = when {
                    isDisabled -> colorScheme.disabled.text
                    isSelected -> colorScheme.primary.base
                    else -> colorScheme.text.regular
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(32.dp)
                        .background(bgColor)
                        .then(
                            if (!isDisabled) {
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
                        color = textColor,
                        style = typography.small,
                    )
                }
            }
        }
    }
}

private fun formatTime(
    time: NexusTime,
    showSeconds: Boolean,
    pattern: String?,
): String {
    val hh = time.hour.toString().padStart(2, '0')
    val mm = time.minute.toString().padStart(2, '0')
    val ss = time.second.toString().padStart(2, '0')
    val template = pattern ?: if (showSeconds) "HH:mm:ss" else "HH:mm"
    return template
        .replace("HH", hh)
        .replace("mm", mm)
        .replace("ss", ss)
}
