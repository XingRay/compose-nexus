package io.github.xingray.compose_nexus.controls

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.xingray.compose_nexus.foundation.ProvideContentColor
import io.github.xingray.compose_nexus.theme.ComponentSize
import io.github.xingray.compose_nexus.theme.NexusTheme
import kotlin.math.pow
import kotlin.math.roundToLong

/**
 * Element Plus InputNumber — a numeric input with increase/decrease buttons.
 *
 * @param value Current numeric value.
 * @param onValueChange Callback when value changes.
 * @param modifier Modifier.
 * @param size Component size.
 * @param min Minimum allowed value.
 * @param max Maximum allowed value.
 * @param step Step increment/decrement.
 * @param precision Decimal precision (number of digits after decimal point).
 * @param disabled Disabled state.
 * @param controls Show increase/decrease buttons.
 * @param placeholder Placeholder text.
 */
@Composable
fun NexusInputNumber(
    value: Double,
    onValueChange: (Double) -> Unit,
    modifier: Modifier = Modifier,
    size: ComponentSize = ComponentSize.Default,
    min: Double = -Double.MAX_VALUE,
    max: Double = Double.MAX_VALUE,
    step: Double = 1.0,
    precision: Int = 0,
    disabled: Boolean = false,
    controls: Boolean = true,
    placeholder: String = "",
) {
    val colorScheme = NexusTheme.colorScheme
    val shapes = NexusTheme.shapes
    val sizes = NexusTheme.sizes
    val typography = NexusTheme.typography

    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val isHovered by interactionSource.collectIsHoveredAsState()

    val height: Dp = when (size) {
        ComponentSize.Large -> sizes.componentLarge
        ComponentSize.Default -> sizes.componentDefault
        ComponentSize.Small -> sizes.componentSmall
    }
    val textStyle = when (size) {
        ComponentSize.Large -> typography.base
        ComponentSize.Default -> typography.base
        ComponentSize.Small -> typography.extraSmall
    }

    val borderColor = when {
        disabled -> colorScheme.disabled.border
        isFocused -> colorScheme.primary.base
        isHovered -> colorScheme.border.dark
        else -> colorScheme.border.base
    }
    val bgColor = if (disabled) colorScheme.disabled.background else colorScheme.fill.blank

    val canDecrease = !disabled && value - step >= min
    val canIncrease = !disabled && value + step <= max

    fun formatValue(v: Double): String {
        return if (precision > 0) {
            val factor = 10.0.pow(precision)
            val rounded = (v * factor).roundToLong() / factor
            val parts = rounded.toString().split(".")
            val intPart = parts[0]
            val decPart = if (parts.size > 1) parts[1] else ""
            val paddedDec = decPart.padEnd(precision, '0').take(precision)
            "$intPart.$paddedDec"
        } else {
            val long = v.toLong()
            if (v == long.toDouble()) long.toString() else v.toString()
        }
    }

    val displayText = formatValue(value)

    Row(
        modifier = modifier
            .defaultMinSize(minHeight = height)
            .clip(shapes.base)
            .background(bgColor)
            .border(1.dp, borderColor, shapes.base),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Decrease button
        if (controls) {
            ProvideContentColor(
                if (canDecrease) colorScheme.text.regular else colorScheme.disabled.text
            ) {
                Box(
                    modifier = Modifier
                        .size(height)
                        .then(
                            if (canDecrease) {
                                Modifier.clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                ) {
                                    val newValue = (value - step).coerceAtLeast(min)
                                    onValueChange(newValue)
                                }
                            } else Modifier
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    NexusText(text = "−", style = typography.medium)
                }
            }

            // Left separator
            Box(
                modifier = Modifier
                    .size(width = 1.dp, height = height)
                    .background(borderColor),
            )
        }

        // Text field
        BasicTextField(
            value = displayText,
            onValueChange = { newText ->
                val parsed = newText.toDoubleOrNull()
                if (parsed != null) {
                    val clamped = parsed.coerceIn(min, max)
                    onValueChange(clamped)
                } else if (newText.isEmpty() || newText == "-" || newText == ".") {
                    // Allow intermediate typing states
                }
            },
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp),
            enabled = !disabled,
            textStyle = textStyle.merge(
                TextStyle(
                    color = if (disabled) colorScheme.disabled.text else colorScheme.text.regular,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                )
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            keyboardActions = KeyboardActions.Default,
            interactionSource = interactionSource,
            cursorBrush = SolidColor(colorScheme.primary.base),
            decorationBox = { innerTextField ->
                Box(contentAlignment = Alignment.Center) {
                    if (displayText.isEmpty() && placeholder.isNotEmpty()) {
                        NexusText(
                            text = placeholder,
                            color = colorScheme.text.placeholder,
                            style = textStyle,
                        )
                    }
                    innerTextField()
                }
            },
        )

        // Increase button
        if (controls) {
            // Right separator
            Box(
                modifier = Modifier
                    .size(width = 1.dp, height = height)
                    .background(borderColor),
            )

            ProvideContentColor(
                if (canIncrease) colorScheme.text.regular else colorScheme.disabled.text
            ) {
                Box(
                    modifier = Modifier
                        .size(height)
                        .then(
                            if (canIncrease) {
                                Modifier.clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                ) {
                                    val newValue = (value + step).coerceAtMost(max)
                                    onValueChange(newValue)
                                }
                            } else Modifier
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    NexusText(text = "+", style = typography.medium)
                }
            }
        }
    }
}
