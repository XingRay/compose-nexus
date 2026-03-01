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
import io.github.xingray.compose_nexus.theme.NexusTheme

/**
 * Simple time holder (hour, minute, second).
 */
data class NexusTime(
    val hour: Int,   // 0-23
    val minute: Int, // 0-59
    val second: Int = 0, // 0-59
)

/**
 * TimePicker state holder.
 */
@Stable
class TimePickerState(
    initialTime: NexusTime? = null,
) {
    var selectedTime by mutableStateOf(initialTime)
    var isOpen by mutableStateOf(false)
        internal set

    fun open() { isOpen = true }
    fun close() { isOpen = false }

    fun select(time: NexusTime) {
        selectedTime = time
        isOpen = false
    }
}

@Composable
fun rememberTimePickerState(
    initialTime: NexusTime? = null,
): TimePickerState = remember { TimePickerState(initialTime) }

/**
 * Element Plus TimePicker — a time selection component with scrollable columns.
 *
 * @param state TimePicker state.
 * @param modifier Modifier.
 * @param placeholder Input placeholder text.
 * @param showSeconds Whether to show seconds column.
 * @param onTimeChange Callback when time is selected.
 */
@Composable
fun NexusTimePicker(
    state: TimePickerState = rememberTimePickerState(),
    modifier: Modifier = Modifier,
    placeholder: String = "Select time",
    showSeconds: Boolean = false,
    onTimeChange: ((NexusTime) -> Unit)? = null,
) {
    val colorScheme = NexusTheme.colorScheme
    val typography = NexusTheme.typography
    val shapes = NexusTheme.shapes

    val displayText = state.selectedTime?.let {
        val base = "${it.hour.toString().padStart(2, '0')}:${it.minute.toString().padStart(2, '0')}"
        if (showSeconds) "$base:${it.second.toString().padStart(2, '0')}" else base
    } ?: ""

    Column(modifier = modifier) {
        NexusInput(
            value = displayText,
            onValueChange = {},
            placeholder = placeholder,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { state.isOpen = !state.isOpen },
            suffix = { NexusText(text = "🕐", style = typography.extraSmall) },
        )

        if (state.isOpen) {
            androidx.compose.ui.window.Popup(
                alignment = Alignment.TopStart,
                properties = androidx.compose.ui.window.PopupProperties(focusable = true),
                onDismissRequest = { state.close() },
            ) {
                TimePickerPanel(
                    state = state,
                    showSeconds = showSeconds,
                    onTimeChange = onTimeChange,
                )
            }
        }
    }
}

@Composable
private fun TimePickerPanel(
    state: TimePickerState,
    showSeconds: Boolean,
    onTimeChange: ((NexusTime) -> Unit)?,
) {
    val colorScheme = NexusTheme.colorScheme
    val typography = NexusTheme.typography
    val shapes = NexusTheme.shapes

    var selectedHour by remember { mutableStateOf(state.selectedTime?.hour ?: 0) }
    var selectedMinute by remember { mutableStateOf(state.selectedTime?.minute ?: 0) }
    var selectedSecond by remember { mutableStateOf(state.selectedTime?.second ?: 0) }

    Column(
        modifier = Modifier
            .clip(shapes.base)
            .background(colorScheme.fill.blank)
            .border(1.dp, colorScheme.border.lighter, shapes.base)
            .padding(8.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(0.dp),
        ) {
            // Hours column
            TimeColumn(
                values = (0..23).toList(),
                selected = selectedHour,
                onSelect = { selectedHour = it },
            )

            // Minutes column
            TimeColumn(
                values = (0..59).toList(),
                selected = selectedMinute,
                onSelect = { selectedMinute = it },
            )

            // Seconds column
            if (showSeconds) {
                TimeColumn(
                    values = (0..59).toList(),
                    selected = selectedSecond,
                    onSelect = { selectedSecond = it },
                )
            }
        }

        // Confirm button
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            horizontalArrangement = Arrangement.End,
        ) {
            NexusButton(
                onClick = {
                    val time = NexusTime(selectedHour, selectedMinute, selectedSecond)
                    state.select(time)
                    onTimeChange?.invoke(time)
                },
                type = io.github.xingray.compose_nexus.theme.NexusType.Primary,
                size = io.github.xingray.compose_nexus.theme.ComponentSize.Small,
            ) {
                NexusText(text = "OK")
            }
        }
    }
}

@Composable
private fun TimeColumn(
    values: List<Int>,
    selected: Int,
    onSelect: (Int) -> Unit,
) {
    val colorScheme = NexusTheme.colorScheme
    val typography = NexusTheme.typography

    Column(
        modifier = Modifier
            .width(60.dp)
            .heightIn(max = 200.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        values.forEach { value ->
            val isSelected = value == selected
            val bgColor = if (isSelected) colorScheme.primary.light9 else Color.Transparent
            val textColor = if (isSelected) colorScheme.primary.base else colorScheme.text.regular

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp)
                    .background(bgColor)
                    .clickable { onSelect(value) }
                    .pointerHoverIcon(PointerIcon.Hand),
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
