package io.github.xingray.compose.nexus.controls

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import io.github.xingray.compose.nexus.controls.parseTimeToMinutes
import io.github.xingray.compose.nexus.theme.ComponentSize

@Stable
class TimeSelectState(
    initialValue: String? = null,
) {
    var value by mutableStateOf(initialValue)
    var expanded by mutableStateOf(false)
        internal set

    fun clear() {
        value = null
    }
}

@Composable
fun rememberTimeSelectState(
    initialValue: String? = null,
): TimeSelectState = remember { TimeSelectState(initialValue) }

@Composable
fun NexusTimeSelect(
    state: TimeSelectState = rememberTimeSelectState(),
    modifier: Modifier = Modifier,
    start: String = "09:00",
    end: String = "18:00",
    step: String = "00:30",
    minTime: String? = null,
    maxTime: String? = null,
    includeEndTime: Boolean = false,
    format: String = "HH:mm",
    placeholder: String = "Select time",
    disabled: Boolean = false,
    editable: Boolean = true,
    clearable: Boolean = true,
    size: ComponentSize = ComponentSize.Default,
    prefixIcon: (@Composable () -> Unit)? = null,
    clearIcon: (@Composable () -> Unit)? = null,
    emptyValues: Set<Any?> = setOf(null, ""),
    valueOnClear: String? = null,
    onChange: ((String?) -> Unit)? = null,
    onFocus: (() -> Unit)? = null,
    onBlur: (() -> Unit)? = null,
    onClear: (() -> Unit)? = null,
) {
    val options = remember(start, end, step, minTime, maxTime, includeEndTime, format) {
        buildTimeSelectOptions(
            start = start,
            end = end,
            step = step,
            minTime = minTime,
            maxTime = maxTime,
            includeEndTime = includeEndTime,
            format = format,
        )
    }
    val selectState = rememberSelectState<String>()

    LaunchedEffect(state.value) {
        if (selectState.selected != state.value) {
            selectState.selected = state.value
        }
    }

    NexusSelect(
        state = selectState,
        options = options,
        onSelect = {
            state.value = it
            onChange?.invoke(it)
        },
        modifier = modifier,
        size = size,
        placeholder = placeholder,
        disabled = disabled,
        clearable = clearable,
        clearIcon = clearIcon,
        filterable = editable,
        prefix = prefixIcon ?: { NexusText(text = "🕐") },
        emptyValues = emptyValues,
        valueOnClear = valueOnClear,
        onVisibleChange = { state.expanded = it },
        onChange = { changed ->
            when (changed) {
                null -> {
                    state.value = valueOnClear
                    onChange?.invoke(valueOnClear)
                }

                is String -> {
                    state.value = changed
                    onChange?.invoke(changed)
                }
            }
        },
        onFocus = onFocus,
        onBlur = onBlur,
        onClear = {
            state.value = valueOnClear
            onClear?.invoke()
        },
    )
}

private fun buildTimeSelectOptions(
    start: String,
    end: String,
    step: String,
    minTime: String?,
    maxTime: String?,
    includeEndTime: Boolean,
    format: String,
): List<SelectOption<String>> {
    val startMinute = parseTimeToMinutes(start) ?: return emptyList()
    val endMinute = parseTimeToMinutes(end) ?: return emptyList()
    val stepMinute = parseTimeToMinutes(step)?.takeIf { it > 0 } ?: 30
    val minMinute = minTime?.let(::parseTimeToMinutes)
    val maxMinute = maxTime?.let(::parseTimeToMinutes)
    if (startMinute > endMinute) return emptyList()

    val result = mutableListOf<SelectOption<String>>()
    var current = startMinute
    while (current < endMinute || (includeEndTime && current == endMinute)) {
        val label = formatMinutes(current, format)
        val disabled = (minMinute != null && current < minMinute) ||
            (maxMinute != null && current > maxMinute)
        result.add(
            SelectOption(
                value = label,
                label = label,
                disabled = disabled,
            ),
        )
        current += stepMinute
    }
    return result
}

private fun parseTimeToMinutes(value: String): Int? {
    val parts = value.trim().split(":")
    if (parts.size != 2) return null
    val hour = parts[0].toIntOrNull() ?: return null
    val minute = parts[1].toIntOrNull() ?: return null
    if (hour !in 0..23 || minute !in 0..59) return null
    return hour * 60 + minute
}

private fun formatMinutes(totalMinutes: Int, format: String): String {
    val hour24 = (totalMinutes / 60).coerceIn(0, 23)
    val minute = (totalMinutes % 60).coerceIn(0, 59)
    val hour12 = when {
        hour24 == 0 -> 12
        hour24 > 12 -> hour24 - 12
        else -> hour24
    }
    val ampm = if (hour24 < 12) "AM" else "PM"

    return format
        .replace("HH", hour24.toString().padStart(2, '0'))
        .replace("hh", hour12.toString().padStart(2, '0'))
        .replace("mm", minute.toString().padStart(2, '0'))
        .replace("H", hour24.toString())
        .replace("h", hour12.toString())
        .replace("A", ampm)
        .replace("a", ampm.lowercase())
}
