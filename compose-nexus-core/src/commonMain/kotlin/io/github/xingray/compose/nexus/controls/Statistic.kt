package io.github.xingray.compose.nexus.controls

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import io.github.xingray.compose.nexus.theme.NexusTheme
import kotlinx.coroutines.delay
import kotlin.math.abs
import kotlin.math.round

@Stable
class StatisticState internal constructor(
    initialDisplayValue: String = "0",
) {
    var displayValue by mutableStateOf(initialDisplayValue)
        internal set
}

@Composable
fun rememberStatisticState(): StatisticState = remember { StatisticState() }

@Stable
class CountdownState internal constructor(
    initialDisplayValue: String = "00:00:00",
) {
    var displayValue by mutableStateOf(initialDisplayValue)
        internal set
}

@Composable
fun rememberCountdownState(): CountdownState = remember { CountdownState() }

@Composable
fun NexusStatistic(
    value: Number = 0,
    modifier: Modifier = Modifier,
    state: StatisticState = rememberStatisticState(),
    decimalSeparator: String = ".",
    groupSeparator: String = ",",
    precision: Int = 0,
    formatter: ((Double) -> String)? = null,
    prefix: String? = null,
    suffix: String? = null,
    title: String? = null,
    valueColor: Color? = null,
    valueStyle: TextStyle? = null,
    titleSlot: (@Composable () -> Unit)? = null,
    prefixSlot: (@Composable () -> Unit)? = null,
    suffixSlot: (@Composable () -> Unit)? = null,
) {
    val colorScheme = NexusTheme.colorScheme
    val typography = NexusTheme.typography

    val rawValue = value.toDouble()
    val display = formatter?.invoke(rawValue) ?: formatStatisticNumber(
        value = rawValue,
        precision = precision,
        decimalSeparator = decimalSeparator,
        groupSeparator = groupSeparator,
    )
    state.displayValue = display

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        if (titleSlot != null) {
            titleSlot()
        } else if (!title.isNullOrBlank()) {
            NexusText(
                text = title,
                color = colorScheme.text.secondary,
                style = typography.small,
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (prefixSlot != null) {
                prefixSlot()
            } else if (!prefix.isNullOrBlank()) {
                NexusText(
                    text = prefix,
                    color = colorScheme.text.secondary,
                    style = typography.base,
                )
            }

            NexusText(
                text = state.displayValue,
                color = valueColor ?: colorScheme.text.primary,
                style = valueStyle ?: typography.extraLarge,
            )

            if (suffixSlot != null) {
                suffixSlot()
            } else if (!suffix.isNullOrBlank()) {
                NexusText(
                    text = suffix,
                    color = colorScheme.text.secondary,
                    style = typography.base,
                )
            }
        }
    }
}

@Composable
fun NexusCountdown(
    value: Long,
    modifier: Modifier = Modifier,
    state: CountdownState = rememberCountdownState(),
    format: String = "HH:mm:ss",
    prefix: String? = null,
    suffix: String? = null,
    title: String? = null,
    valueColor: Color? = null,
    valueStyle: TextStyle? = null,
    titleSlot: (@Composable () -> Unit)? = null,
    prefixSlot: (@Composable () -> Unit)? = null,
    suffixSlot: (@Composable () -> Unit)? = null,
    onChange: ((Long) -> Unit)? = null,
    onFinish: (() -> Unit)? = null,
) {
    val initialRemaining = value.coerceAtLeast(0L)

    LaunchedEffect(initialRemaining, format) {
        val tick = if (format.contains('S')) 30L else 1000L
        var current = initialRemaining
        var last = Long.MIN_VALUE
        while (true) {
            if (current != last) {
                onChange?.invoke(current)
                last = current
            }
            state.displayValue = formatCountdown(current, format)
            if (current <= 0L) {
                onFinish?.invoke()
                break
            }
            delay(tick)
            current = (current - tick).coerceAtLeast(0L)
        }
    }

    NexusStatistic(
        value = 0,
        modifier = modifier,
        decimalSeparator = ".",
        groupSeparator = ",",
        precision = 0,
        formatter = { state.displayValue },
        prefix = prefix,
        suffix = suffix,
        title = title,
        valueColor = valueColor,
        valueStyle = valueStyle,
        titleSlot = titleSlot,
        prefixSlot = prefixSlot,
        suffixSlot = suffixSlot,
    )
}

private fun formatStatisticNumber(
    value: Double,
    precision: Int,
    decimalSeparator: String,
    groupSeparator: String,
): String {
    val safePrecision = precision.coerceIn(0, 9)
    val factor = pow10(safePrecision)
    val scaled = round(value * factor).toLong()
    val negative = scaled < 0
    val absScaled = abs(scaled)
    val integerPart = absScaled / factor
    val decimalPart = absScaled % factor

    val groupedInteger = applyGroupSeparator(integerPart.toString(), groupSeparator)
    val decimalText = if (safePrecision > 0) {
        decimalSeparator + decimalPart.toString().padStart(safePrecision, '0')
    } else {
        ""
    }

    return (if (negative) "-" else "") + groupedInteger + decimalText
}

private fun formatCountdown(
    remainingMillis: Long,
    format: String,
): String {
    val totalMillis = remainingMillis.coerceAtLeast(0L)
    val totalSeconds = totalMillis / 1000L
    val totalMinutes = totalSeconds / 60L
    val totalHours = totalMinutes / 60L
    val days = totalHours / 24L
    val hours = totalHours % 24L
    val minutes = totalMinutes % 60L
    val seconds = totalSeconds % 60L
    val millis = totalMillis % 1000L

    val useDay = format.contains("DD") || format.contains("D")
    val hourValue = if (useDay) hours else totalHours

    return format
        .replace("DD", days.toString().padStart(2, '0'))
        .replace("D", days.toString())
        .replace("HH", hourValue.toString().padStart(2, '0'))
        .replace("H", hourValue.toString())
        .replace("mm", minutes.toString().padStart(2, '0'))
        .replace("m", minutes.toString())
        .replace("ss", seconds.toString().padStart(2, '0'))
        .replace("s", seconds.toString())
        .replace("SSS", millis.toString().padStart(3, '0'))
}

private fun applyGroupSeparator(number: String, separator: String): String {
    if (number.length <= 3) return number
    val reversed = number.reversed()
    val grouped = reversed.chunked(3).joinToString(separator)
    return grouped.reversed()
}

private fun pow10(exp: Int): Long {
    var result = 1L
    repeat(exp) {
        result *= 10L
    }
    return result
}
