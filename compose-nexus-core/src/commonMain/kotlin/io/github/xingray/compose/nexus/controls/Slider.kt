package io.github.xingray.compose.nexus.controls

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import io.github.xingray.compose.nexus.theme.ComponentSize
import io.github.xingray.compose.nexus.theme.NexusTheme
import kotlin.math.roundToInt

enum class NexusSliderPlacement {
    Top,
    Bottom,
    Left,
    Right,
}

@Composable
fun NexusSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    min: Float = 0f,
    max: Float = 100f,
    step: Float = 1f,
    disabled: Boolean = false,
    showInput: Boolean = false,
    showInputControls: Boolean = true,
    size: io.github.xingray.compose.nexus.theme.ComponentSize = _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Default,
    inputSize: io.github.xingray.compose.nexus.theme.ComponentSize = size,
    showStops: Boolean = false,
    showTooltip: Boolean = true,
    formatTooltip: ((Float) -> String)? = null,
    vertical: Boolean = false,
    height: Dp = 180.dp,
    placement: io.github.xingray.compose.nexus.controls.NexusSliderPlacement = _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusSliderPlacement.Top,
    marks: Map<Float, String> = emptyMap(),
    onChange: ((Float) -> Unit)? = null,
    onInput: ((Float) -> Unit)? = null,
) {
    val clamped = value.coerceIn(min, max)
    if (!vertical && showInput) {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            _root_ide_package_.io.github.xingray.compose.nexus.controls.SliderSingleTrack(
                value = clamped,
                onValueChange = onValueChange,
                modifier = Modifier.weight(1f),
                min = min,
                max = max,
                step = step,
                disabled = disabled,
                size = size,
                showStops = showStops,
                showTooltip = showTooltip,
                formatTooltip = formatTooltip,
                vertical = false,
                height = height,
                placement = placement,
                marks = marks,
                onChange = onChange,
                onInput = onInput,
            )
            _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusInputNumber(
                value = clamped.toDouble(),
                onValueChange = {
                    val normalized = _root_ide_package_.io.github.xingray.compose.nexus.controls.snapValue(it.toFloat(), min, max, step)
                    onValueChange(normalized)
                    onInput?.invoke(normalized)
                    onChange?.invoke(normalized)
                },
                min = min.toDouble(),
                max = max.toDouble(),
                step = step.toDouble().coerceAtLeast(0.000001),
                size = inputSize,
                disabled = disabled,
                controls = showInputControls,
                modifier = Modifier.width(120.dp),
            )
        }
    } else {
        _root_ide_package_.io.github.xingray.compose.nexus.controls.SliderSingleTrack(
            value = clamped,
            onValueChange = onValueChange,
            modifier = modifier,
            min = min,
            max = max,
            step = step,
            disabled = disabled,
            size = size,
            showStops = showStops,
            showTooltip = showTooltip,
            formatTooltip = formatTooltip,
            vertical = vertical,
            height = height,
            placement = placement,
            marks = marks,
            onChange = onChange,
            onInput = onInput,
        )
    }
}

@Composable
fun NexusRangeSlider(
    values: ClosedFloatingPointRange<Float>,
    onValueChange: (ClosedFloatingPointRange<Float>) -> Unit,
    modifier: Modifier = Modifier,
    min: Float = 0f,
    max: Float = 100f,
    step: Float = 1f,
    disabled: Boolean = false,
    size: io.github.xingray.compose.nexus.theme.ComponentSize = _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Default,
    showStops: Boolean = false,
    showTooltip: Boolean = true,
    formatTooltip: ((Float) -> String)? = null,
    placement: io.github.xingray.compose.nexus.controls.NexusSliderPlacement = _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusSliderPlacement.Top,
    marks: Map<Float, String> = emptyMap(),
    onChange: ((ClosedFloatingPointRange<Float>) -> Unit)? = null,
    onInput: ((ClosedFloatingPointRange<Float>) -> Unit)? = null,
) {
    val colorScheme = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.colorScheme
    val trackHeight = when (size) {
        _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Large -> 8.dp
        _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Default -> 6.dp
        _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Small -> 4.dp
    }
    val thumbSize = when (size) {
        _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Large -> 22.dp
        _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Default -> 20.dp
        _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Small -> 16.dp
    }
    val topExtra = if (showTooltip && placement == _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusSliderPlacement.Top) 30.dp else 0.dp
    val bottomExtra = (if (showTooltip && placement == _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusSliderPlacement.Bottom) 30.dp else 0.dp) +
        (if (marks.isNotEmpty()) 20.dp else 0.dp)
    val normalized = _root_ide_package_.io.github.xingray.compose.nexus.controls.normalizeRange(values, min, max, step)

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(thumbSize + topExtra + bottomExtra),
    ) {
        val trackWidthPx = (maxWidth - thumbSize).value
        val trackTop = topExtra + (thumbSize - trackHeight) / 2
        val halfThumbPx = thumbSize.value / 2f
        val startFraction = _root_ide_package_.io.github.xingray.compose.nexus.controls.fractionOf(normalized.start, min, max)
        val endFraction = _root_ide_package_.io.github.xingray.compose.nexus.controls.fractionOf(normalized.endInclusive, min, max)
        val startOffsetPx = startFraction * trackWidthPx
        val endOffsetPx = endFraction * trackWidthPx

        var startDragOffset by remember(normalized) { mutableFloatStateOf(startOffsetPx) }
        var endDragOffset by remember(normalized) { mutableFloatStateOf(endOffsetPx) }
        var lastRange by remember(normalized) { mutableStateOf(normalized) }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(trackHeight)
                .offset(y = trackTop)
                .clip(CircleShape)
                .background(if (disabled) colorScheme.border.lighter else colorScheme.border.light),
        )
        Box(
            modifier = Modifier
                .offset {
                    IntOffset(
                        x = (startOffsetPx + halfThumbPx).roundToInt(),
                        y = trackTop.roundToPx(),
                    )
                }
                .width((endOffsetPx - startOffsetPx).dp)
                .height(trackHeight)
                .clip(CircleShape)
                .background(if (disabled) colorScheme.primary.light5 else colorScheme.primary.base),
        )

        val stopCount = (((max - min) / step).roundToInt()).coerceAtMost(200)
        if (showStops && step > 0f && stopCount > 1) {
            (1 until stopCount).forEach { index ->
                val xPx = index.toFloat() / stopCount.toFloat() * trackWidthPx + halfThumbPx
                Box(
                    modifier = Modifier
                        .size(4.dp)
                        .offset { IntOffset(xPx.roundToInt() - 2, (trackTop + trackHeight + 2.dp).roundToPx()) }
                        .clip(CircleShape)
                        .background(colorScheme.border.base),
                )
            }
        }

        marks.forEach { (markValue, label) ->
            if (markValue in min..max) {
                val markFraction = _root_ide_package_.io.github.xingray.compose.nexus.controls.fractionOf(markValue, min, max)
                val xPx = markFraction * trackWidthPx + halfThumbPx
                _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
                    text = label,
                    style = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.typography.extraSmall,
                    color = colorScheme.text.placeholder,
                    modifier = Modifier.offset { IntOffset(xPx.roundToInt() - 10, (trackTop + thumbSize + 2.dp).roundToPx()) },
                )
            }
        }

        fun updateRange(isStartThumb: Boolean, offsetPx: Float) {
            if (disabled) return
            if (isStartThumb) {
                val clampedPx = offsetPx.coerceIn(0f, endOffsetPx)
                startDragOffset = clampedPx
                val v = _root_ide_package_.io.github.xingray.compose.nexus.controls.snapValue(min + (clampedPx / trackWidthPx) * (max - min), min, max, step)
                lastRange = _root_ide_package_.io.github.xingray.compose.nexus.controls.normalizeRange(v..lastRange.endInclusive, min, max, step)
            } else {
                val clampedPx = offsetPx.coerceIn(startOffsetPx, trackWidthPx)
                endDragOffset = clampedPx
                val v = _root_ide_package_.io.github.xingray.compose.nexus.controls.snapValue(min + (clampedPx / trackWidthPx) * (max - min), min, max, step)
                lastRange = _root_ide_package_.io.github.xingray.compose.nexus.controls.normalizeRange(lastRange.start..v, min, max, step)
            }
            onValueChange(lastRange)
            onInput?.invoke(lastRange)
        }

        _root_ide_package_.io.github.xingray.compose.nexus.controls.SliderThumb(
            modifier = Modifier
                .offset { IntOffset(startOffsetPx.roundToInt(), topExtra.roundToPx()) }
                .pointerInput(disabled, startOffsetPx, endOffsetPx, min, max, step) {
                    detectDragGestures(
                        onDragStart = { startDragOffset = startOffsetPx },
                        onDragEnd = { onChange?.invoke(lastRange) },
                    ) { change, dragAmount ->
                        change.consume()
                        startDragOffset += dragAmount.x
                        updateRange(isStartThumb = true, offsetPx = startDragOffset)
                    }
                },
            size = thumbSize,
            disabled = disabled,
        )

        _root_ide_package_.io.github.xingray.compose.nexus.controls.SliderThumb(
            modifier = Modifier
                .offset { IntOffset(endOffsetPx.roundToInt(), topExtra.roundToPx()) }
                .pointerInput(disabled, startOffsetPx, endOffsetPx, min, max, step) {
                    detectDragGestures(
                        onDragStart = { endDragOffset = endOffsetPx },
                        onDragEnd = { onChange?.invoke(lastRange) },
                    ) { change, dragAmount ->
                        change.consume()
                        endDragOffset += dragAmount.x
                        updateRange(isStartThumb = false, offsetPx = endDragOffset)
                    }
                },
            size = thumbSize,
            disabled = disabled,
        )

        if (showTooltip) {
            val formatter = formatTooltip ?: { it.roundToInt().toString() }
            val y = if (placement == _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusSliderPlacement.Bottom) topExtra + thumbSize + 2.dp else 0.dp
            _root_ide_package_.io.github.xingray.compose.nexus.controls.SliderTooltip(
                text = formatter(lastRange.start),
                modifier = Modifier.offset { IntOffset(startOffsetPx.roundToInt() - 10, y.roundToPx()) },
            )
            _root_ide_package_.io.github.xingray.compose.nexus.controls.SliderTooltip(
                text = formatter(lastRange.endInclusive),
                modifier = Modifier.offset { IntOffset(endOffsetPx.roundToInt() - 10, y.roundToPx()) },
            )
        }
    }
}

@Composable
private fun SliderSingleTrack(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier,
    min: Float,
    max: Float,
    step: Float,
    disabled: Boolean,
    size: io.github.xingray.compose.nexus.theme.ComponentSize,
    showStops: Boolean,
    showTooltip: Boolean,
    formatTooltip: ((Float) -> String)?,
    vertical: Boolean,
    height: Dp,
    placement: io.github.xingray.compose.nexus.controls.NexusSliderPlacement,
    marks: Map<Float, String>,
    onChange: ((Float) -> Unit)?,
    onInput: ((Float) -> Unit)?,
) {
    val colorScheme = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.colorScheme
    val trackThickness = when (size) {
        _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Large -> 8.dp
        _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Default -> 6.dp
        _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Small -> 4.dp
    }
    val thumbSize = when (size) {
        _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Large -> 22.dp
        _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Default -> 20.dp
        _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Small -> 16.dp
    }
    val normalizedValue = _root_ide_package_.io.github.xingray.compose.nexus.controls.snapValue(value, min, max, step)
    var lastValue by remember(normalizedValue) { mutableFloatStateOf(normalizedValue) }

    if (!vertical) {
        val topExtra = if (showTooltip && placement == _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusSliderPlacement.Top) 30.dp else 0.dp
        val bottomExtra = (if (showTooltip && placement == _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusSliderPlacement.Bottom) 30.dp else 0.dp) +
            (if (marks.isNotEmpty() || showStops) 20.dp else 0.dp)
        BoxWithConstraints(
            modifier = modifier
                .fillMaxWidth()
                .height(thumbSize + topExtra + bottomExtra),
        ) {
            val trackWidthPx = (maxWidth - thumbSize).value
            val fraction = _root_ide_package_.io.github.xingray.compose.nexus.controls.fractionOf(normalizedValue, min, max)
            val thumbOffsetPx = fraction * trackWidthPx
            val halfThumbPx = thumbSize.value / 2f
            val trackTop = topExtra + (thumbSize - trackThickness) / 2

            fun emitFromOffset(offsetPx: Float) {
                val raw = min + (offsetPx / trackWidthPx) * (max - min)
                val next = _root_ide_package_.io.github.xingray.compose.nexus.controls.snapValue(raw, min, max, step)
                lastValue = next
                onValueChange(next)
                onInput?.invoke(next)
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(trackThickness)
                    .offset(y = trackTop)
                    .clip(CircleShape)
                    .background(if (disabled) colorScheme.border.lighter else colorScheme.border.light),
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth(fraction)
                    .height(trackThickness)
                    .offset(y = trackTop)
                    .clip(CircleShape)
                    .background(if (disabled) colorScheme.primary.light5 else colorScheme.primary.base),
            )

            val stopCount = (((max - min) / step).roundToInt()).coerceAtMost(200)
            if (showStops && step > 0f && stopCount > 1) {
                (1 until stopCount).forEach { index ->
                    val xPx = index.toFloat() / stopCount.toFloat() * trackWidthPx + halfThumbPx
                    Box(
                        modifier = Modifier
                            .size(4.dp)
                            .offset { IntOffset(xPx.roundToInt() - 2, (trackTop + trackThickness + 2.dp).roundToPx()) }
                            .clip(CircleShape)
                            .background(colorScheme.border.base),
                    )
                }
            }

            marks.forEach { (markValue, label) ->
                if (markValue in min..max) {
                    val markFraction = _root_ide_package_.io.github.xingray.compose.nexus.controls.fractionOf(markValue, min, max)
                    val xPx = markFraction * trackWidthPx + halfThumbPx
                    _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
                        text = label,
                        style = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.typography.extraSmall,
                        color = colorScheme.text.placeholder,
                        modifier = Modifier.offset { IntOffset(xPx.roundToInt() - 10, (trackTop + thumbSize + 2.dp).roundToPx()) },
                    )
                }
            }

            _root_ide_package_.io.github.xingray.compose.nexus.controls.SliderThumb(
                modifier = Modifier
                    .offset { IntOffset(thumbOffsetPx.roundToInt(), topExtra.roundToPx()) }
                    .pointerInput(disabled, value, min, max, step) {
                        detectDragGestures(
                            onDragEnd = { onChange?.invoke(lastValue) },
                        ) { change, _ ->
                            if (disabled) return@detectDragGestures
                            val x = (change.position.x - halfThumbPx).coerceIn(0f, trackWidthPx)
                            emitFromOffset(x)
                            change.consume()
                        }
                    },
                size = thumbSize,
                disabled = disabled,
            )

            if (showTooltip) {
                val formatter = formatTooltip ?: { it.roundToInt().toString() }
                val y = if (placement == _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusSliderPlacement.Bottom) topExtra + thumbSize + 2.dp else 0.dp
                _root_ide_package_.io.github.xingray.compose.nexus.controls.SliderTooltip(
                    text = formatter(normalizedValue),
                    modifier = Modifier.offset { IntOffset(thumbOffsetPx.roundToInt() - 10, y.roundToPx()) },
                )
            }
        }
    } else {
        val leftExtra = if (showTooltip && placement == _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusSliderPlacement.Left) 42.dp else 0.dp
        val rightExtra = if (showTooltip && placement == _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusSliderPlacement.Right) 42.dp else 0.dp
        BoxWithConstraints(
            modifier = modifier
                .width(thumbSize + leftExtra + rightExtra)
                .height(height),
        ) {
            val trackHeightPx = (maxHeight - thumbSize).value
            val fraction = _root_ide_package_.io.github.xingray.compose.nexus.controls.fractionOf(normalizedValue, min, max)
            val thumbOffsetYPx = (1f - fraction) * trackHeightPx
            val trackLeft = leftExtra + (thumbSize - trackThickness) / 2

            fun emitFromOffset(offsetYPx: Float) {
                val raw = min + (1f - offsetYPx / trackHeightPx) * (max - min)
                val next = _root_ide_package_.io.github.xingray.compose.nexus.controls.snapValue(raw, min, max, step)
                lastValue = next
                onValueChange(next)
                onInput?.invoke(next)
            }

            Box(
                modifier = Modifier
                    .width(trackThickness)
                    .fillMaxHeight()
                    .offset(x = trackLeft)
                    .clip(CircleShape)
                    .background(if (disabled) colorScheme.border.lighter else colorScheme.border.light),
            )
            Box(
                modifier = Modifier
                    .width(trackThickness)
                    .height((fraction * trackHeightPx).dp)
                    .offset {
                        IntOffset(
                            x = trackLeft.roundToPx(),
                            y = (thumbOffsetYPx + thumbSize.value / 2f).roundToInt(),
                        )
                    }
                    .clip(CircleShape)
                    .background(if (disabled) colorScheme.primary.light5 else colorScheme.primary.base),
            )

            _root_ide_package_.io.github.xingray.compose.nexus.controls.SliderThumb(
                modifier = Modifier
                    .offset { IntOffset(leftExtra.roundToPx(), thumbOffsetYPx.roundToInt()) }
                    .pointerInput(disabled, value, min, max, step) {
                        detectDragGestures(
                            onDragEnd = { onChange?.invoke(lastValue) },
                        ) { change, _ ->
                            if (disabled) return@detectDragGestures
                            val y = (change.position.y - thumbSize.value / 2f).coerceIn(0f, trackHeightPx)
                            emitFromOffset(y)
                            change.consume()
                        }
                    },
                size = thumbSize,
                disabled = disabled,
            )

            if (showTooltip) {
                val formatter = formatTooltip ?: { it.roundToInt().toString() }
                val x = if (placement == _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusSliderPlacement.Left) 0.dp else leftExtra + thumbSize + 2.dp
                _root_ide_package_.io.github.xingray.compose.nexus.controls.SliderTooltip(
                    text = formatter(normalizedValue),
                    modifier = Modifier.offset { IntOffset(x.roundToPx(), thumbOffsetYPx.roundToInt()) },
                )
            }
        }
    }
}

@Composable
private fun SliderThumb(
    modifier: Modifier,
    size: Dp,
    disabled: Boolean,
) {
    val colorScheme = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.colorScheme
    Box(
        modifier = modifier
            .size(size)
            .shadow(2.dp, CircleShape)
            .clip(CircleShape)
            .background(colorScheme.white),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .size(size - 4.dp)
                .clip(CircleShape)
                .background(if (disabled) colorScheme.primary.light5 else colorScheme.white),
        )
    }
}

@Composable
private fun SliderTooltip(
    text: String,
    modifier: Modifier = Modifier,
) {
    val colorScheme = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.colorScheme
    Box(
        modifier = modifier
            .background(colorScheme.text.primary, _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.shapes.base)
            .padding(horizontal = 8.dp, vertical = 4.dp),
    ) {
        _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
            text = text,
            color = colorScheme.white,
            style = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.typography.extraSmall,
        )
    }
}

private fun snapValue(value: Float, min: Float, max: Float, step: Float): Float {
    if (max <= min) return min
    val clamped = value.coerceIn(min, max)
    if (step <= 0f) return clamped
    val stepCount = ((clamped - min) / step).roundToInt()
    return (min + stepCount * step).coerceIn(min, max)
}

private fun fractionOf(value: Float, min: Float, max: Float): Float {
    val range = max - min
    if (range <= 0f) return 0f
    return ((value - min) / range).coerceIn(0f, 1f)
}

private fun normalizeRange(
    values: ClosedFloatingPointRange<Float>,
    min: Float,
    max: Float,
    step: Float,
): ClosedFloatingPointRange<Float> {
    val start = _root_ide_package_.io.github.xingray.compose.nexus.controls.snapValue(values.start, min, max, step)
    val end = _root_ide_package_.io.github.xingray.compose.nexus.controls.snapValue(values.endInclusive, min, max, step)
    return if (start <= end) start..end else end..start
}
