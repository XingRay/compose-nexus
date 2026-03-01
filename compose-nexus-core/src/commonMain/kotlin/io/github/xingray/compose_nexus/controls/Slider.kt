package io.github.xingray.compose_nexus.controls

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import io.github.xingray.compose_nexus.theme.NexusTheme
import kotlin.math.roundToInt

/**
 * Element Plus Slider — a draggable slider for selecting a value in a range.
 *
 * @param value Current value.
 * @param onValueChange Callback when value changes.
 * @param modifier Modifier.
 * @param min Minimum value.
 * @param max Maximum value.
 * @param step Discrete step (0 for continuous).
 * @param disabled Disabled state.
 * @param showTooltip Whether to show value tooltip above the thumb.
 */
@Composable
fun NexusSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    min: Float = 0f,
    max: Float = 100f,
    step: Float = 0f,
    disabled: Boolean = false,
) {
    val colorScheme = NexusTheme.colorScheme
    val density = LocalDensity.current

    val trackHeight = 6.dp
    val thumbSize = 20.dp
    val activeColor = if (disabled) colorScheme.primary.light5 else colorScheme.primary.base
    val inactiveColor = if (disabled) colorScheme.border.lighter else colorScheme.border.light
    val thumbBorderColor = if (disabled) colorScheme.primary.light5 else colorScheme.primary.base

    val range = max - min
    val fraction = if (range > 0f) ((value - min) / range).coerceIn(0f, 1f) else 0f

    fun snapToStep(raw: Float): Float {
        if (step <= 0f) return raw.coerceIn(min, max)
        val steps = ((raw - min) / step).roundToInt()
        return (min + steps * step).coerceIn(min, max)
    }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(thumbSize),
        contentAlignment = Alignment.CenterStart,
    ) {
        val trackWidthPx = with(density) { (maxWidth - thumbSize).toPx() }
        val thumbOffsetPx = fraction * trackWidthPx
        val halfThumbPx = with(density) { (thumbSize / 2).toPx() }

        // Inactive track (background)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(trackHeight)
                .clip(CircleShape)
                .background(inactiveColor)
                .align(Alignment.Center),
        )

        // Active track (filled portion)
        Box(
            modifier = Modifier
                .fillMaxWidth(fraction)
                .height(trackHeight)
                .clip(CircleShape)
                .background(activeColor)
                .align(Alignment.CenterStart),
        )

        // Thumb
        Box(
            modifier = Modifier
                .offset { IntOffset(thumbOffsetPx.roundToInt(), 0) }
                .size(thumbSize)
                .shadow(2.dp, CircleShape)
                .clip(CircleShape)
                .background(colorScheme.white)
                .then(
                    if (!disabled) {
                        Modifier.pointerInput(min, max, step) {
                            detectHorizontalDragGestures { change, dragAmount ->
                                change.consume()
                                val newPx = (thumbOffsetPx + dragAmount).coerceIn(0f, trackWidthPx)
                                val newFraction = newPx / trackWidthPx
                                val newValue = min + newFraction * range
                                onValueChange(snapToStep(newValue))
                            }
                        }
                    } else Modifier
                ),
            contentAlignment = Alignment.Center,
        ) {
            // Inner dot to mimic Element Plus thumb with border
            Box(
                modifier = Modifier
                    .size(thumbSize - 4.dp)
                    .clip(CircleShape)
                    .background(colorScheme.white),
            )
        }
    }
}
