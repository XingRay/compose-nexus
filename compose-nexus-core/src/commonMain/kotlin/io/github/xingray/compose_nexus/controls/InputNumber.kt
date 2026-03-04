package io.github.xingray.compose_nexus.controls

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.xingray.compose_nexus.foundation.ProvideContentColor
import io.github.xingray.compose_nexus.theme.ComponentSize
import io.github.xingray.compose_nexus.theme.NexusTheme
import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.pow
import kotlin.math.round
import kotlin.math.roundToLong

enum class NexusInputNumberControlsPosition {
    Default,
    Right,
}

enum class NexusInputNumberAlign {
    Left,
    Center,
    Right,
}

enum class NexusInputNumberValueOnClear {
    NaN,
    Min,
    Max,
    Zero,
}

@Composable
fun NexusInputNumber(
    value: Double,
    onValueChange: (Double) -> Unit,
    modifier: Modifier = Modifier,
    size: ComponentSize = ComponentSize.Default,
    min: Double = -Double.MAX_VALUE,
    max: Double = Double.MAX_VALUE,
    step: Double = 1.0,
    stepStrictly: Boolean = false,
    precision: Int? = null,
    disabled: Boolean = false,
    readonly: Boolean = false,
    controls: Boolean = true,
    controlsPosition: NexusInputNumberControlsPosition = NexusInputNumberControlsPosition.Default,
    placeholder: String = "",
    valueOnClear: NexusInputNumberValueOnClear = NexusInputNumberValueOnClear.NaN,
    align: NexusInputNumberAlign = NexusInputNumberAlign.Center,
    disabledScientific: Boolean = false,
    increaseIcon: (@Composable () -> Unit)? = null,
    decreaseIcon: (@Composable () -> Unit)? = null,
    prefix: (@Composable () -> Unit)? = null,
    suffix: (@Composable () -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    onChange: ((currentValue: Double, oldValue: Double) -> Unit)? = null,
    onFocus: (() -> Unit)? = null,
    onBlur: (() -> Unit)? = null,
) {
    val colorScheme = NexusTheme.colorScheme
    val shapes = NexusTheme.shapes
    val sizes = NexusTheme.sizes
    val typography = NexusTheme.typography

    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val isHovered by interactionSource.collectIsHoveredAsState()

    var focusedSnapshot by remember { mutableStateOf(value) }
    var text by remember { mutableStateOf(formatValue(value, precision)) }

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
    val alignStyle = when (align) {
        NexusInputNumberAlign.Left -> TextAlign.Left
        NexusInputNumberAlign.Center -> TextAlign.Center
        NexusInputNumberAlign.Right -> TextAlign.Right
    }

    val borderColor = when {
        disabled -> colorScheme.disabled.border
        isFocused -> colorScheme.primary.base
        isHovered -> colorScheme.border.dark
        else -> colorScheme.border.base
    }
    val bgColor = if (disabled) colorScheme.disabled.background else colorScheme.fill.blank

    LaunchedEffect(value, precision) {
        if (!isFocused) {
            text = formatValue(value, precision)
        }
    }

    fun normalize(v: Double): Double {
        if (v.isNaN()) return Double.NaN
        val clamped = v.coerceIn(min, max)
        val strictValue = if (stepStrictly && step > 0.0) {
            val ratio = (clamped - min) / step
            min + round(ratio) * step
        } else {
            clamped
        }
        val effectivePrecision = precision ?: if (stepStrictly) decimalPlaces(step) else null
        return applyPrecision(strictValue, effectivePrecision)
            .coerceIn(min, max)
    }

    fun clearValue(): Double = when (valueOnClear) {
        NexusInputNumberValueOnClear.NaN -> Double.NaN
        NexusInputNumberValueOnClear.Min -> min
        NexusInputNumberValueOnClear.Max -> max
        NexusInputNumberValueOnClear.Zero -> 0.0.coerceIn(min, max)
    }

    fun commit(newValue: Double, oldValue: Double) {
        onValueChange(newValue)
        if (!numberEquals(newValue, oldValue)) {
            onChange?.invoke(newValue, oldValue)
        }
    }

    fun applyTextInput(raw: String) {
        val safeRaw = if (disabledScientific) {
            raw.replace("e", "", ignoreCase = true)
        } else {
            raw
        }
        text = safeRaw

        when {
            safeRaw.isBlank() -> {
                val cleared = clearValue()
                onValueChange(cleared)
            }
            safeRaw == "-" || safeRaw == "." || safeRaw == "-." -> {
                // allow intermediate typing
            }
            else -> {
                val parsed = safeRaw.toDoubleOrNull()
                if (parsed != null) {
                    onValueChange(normalize(parsed))
                } else {
                    // Align with Element Plus behavior: invalid input emits NaN.
                    onValueChange(Double.NaN)
                }
            }
        }
    }

    fun changeByStep(delta: Double) {
        if (disabled || readonly) return
        val base = if (value.isNaN()) 0.0 else value
        val old = value
        val next = normalize(base + delta)
        text = formatValue(next, precision)
        commit(next, old)
    }

    val canDecrease = !disabled && !readonly && normalize((if (value.isNaN()) min else value) - step) >= min
    val canIncrease = !disabled && !readonly && normalize((if (value.isNaN()) max else value) + step) <= max

    Row(
        modifier = modifier
            .defaultMinSize(minHeight = height)
            .clip(shapes.base)
            .background(bgColor)
            .border(1.dp, borderColor, shapes.base),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (controls && controlsPosition == NexusInputNumberControlsPosition.Default) {
            StepButton(
                enabled = canDecrease,
                size = height,
                symbol = "−",
                customIcon = decreaseIcon,
                onClick = { changeByStep(-step) },
            )
            Separator(height = height, borderColor = borderColor)
        }

        Row(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            if (prefix != null) {
                ProvideContentColor(colorScheme.text.placeholder) {
                    prefix()
                }
            }

            BasicTextField(
                value = text,
                onValueChange = { applyTextInput(it) },
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 1.dp)
                    .onFocusChanged {
                        if (it.isFocused) {
                            focusedSnapshot = value
                            onFocus?.invoke()
                        } else {
                            onBlur?.invoke()
                            val parsed = text.toDoubleOrNull()
                            val committed = when {
                                text.isBlank() -> clearValue()
                                parsed != null -> normalize(parsed)
                                else -> Double.NaN
                            }
                            text = formatValue(committed, precision)
                            commit(committed, focusedSnapshot)
                        }
                    },
                enabled = !disabled,
                readOnly = readonly,
                textStyle = textStyle.merge(
                    TextStyle(
                        color = if (disabled) colorScheme.disabled.text else colorScheme.text.regular,
                        textAlign = alignStyle,
                    ),
                ),
                singleLine = true,
                keyboardOptions = keyboardOptions,
                keyboardActions = keyboardActions,
                interactionSource = interactionSource,
                cursorBrush = SolidColor(colorScheme.primary.base),
                decorationBox = { innerTextField ->
                    Box(contentAlignment = Alignment.Center) {
                        if (text.isEmpty() && placeholder.isNotEmpty()) {
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

            if (suffix != null) {
                ProvideContentColor(colorScheme.text.placeholder) {
                    suffix()
                }
            }
        }

        if (controls && controlsPosition == NexusInputNumberControlsPosition.Default) {
            Separator(height = height, borderColor = borderColor)
            StepButton(
                enabled = canIncrease,
                size = height,
                symbol = "+",
                customIcon = increaseIcon,
                onClick = { changeByStep(step) },
            )
        } else if (controls && controlsPosition == NexusInputNumberControlsPosition.Right) {
            Separator(height = height, borderColor = borderColor)
            Column(
                modifier = Modifier
                    .width(height)
                    .fillMaxHeight(),
            ) {
                StepButton(
                    enabled = canIncrease,
                    size = height / 2,
                    symbol = "+",
                    customIcon = increaseIcon,
                    onClick = { changeByStep(step) },
                )
                Separator(height = 1.dp, borderColor = borderColor, vertical = false)
                StepButton(
                    enabled = canDecrease,
                    size = height / 2,
                    symbol = "−",
                    customIcon = decreaseIcon,
                    onClick = { changeByStep(-step) },
                )
            }
        }
    }
}

@Composable
private fun StepButton(
    enabled: Boolean,
    size: Dp,
    symbol: String,
    customIcon: (@Composable () -> Unit)?,
    onClick: () -> Unit,
) {
    val colorScheme = NexusTheme.colorScheme
    val typography = NexusTheme.typography
    Box(
        modifier = Modifier
            .size(size)
            .then(
                if (enabled) {
                    Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onClick,
                    )
                } else {
                    Modifier
                }
            ),
        contentAlignment = Alignment.Center,
    ) {
        ProvideContentColor(
            if (enabled) colorScheme.text.regular else colorScheme.disabled.text,
        ) {
            if (customIcon != null) {
                customIcon()
            } else {
                NexusText(text = symbol, style = typography.medium)
            }
        }
    }
}

@Composable
private fun Separator(
    height: Dp,
    borderColor: androidx.compose.ui.graphics.Color,
    vertical: Boolean = true,
) {
    Box(
        modifier = if (vertical) {
            Modifier
                .size(width = 1.dp, height = height)
                .background(borderColor)
        } else {
            Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(borderColor)
        },
    )
}

private fun formatValue(v: Double, precision: Int?): String {
    if (v.isNaN()) return ""
    return if (precision != null && precision >= 0) {
        val factor = 10.0.pow(precision)
        val rounded = (v * factor).roundToLong() / factor
        val parts = rounded.toString().split(".")
        val intPart = parts[0]
        val decPart = if (parts.size > 1) parts[1] else ""
        val paddedDec = decPart.padEnd(precision, '0').take(precision)
        if (precision == 0) intPart else "$intPart.$paddedDec"
    } else {
        val long = v.toLong()
        if (abs(v - long.toDouble()) < 1e-12) long.toString() else v.toString()
    }
}

private fun applyPrecision(v: Double, precision: Int?): Double {
    if (precision == null || precision < 0 || v.isNaN()) return v
    val factor = 10.0.pow(precision)
    return floor(v * factor + 0.5) / factor
}

private fun numberEquals(a: Double, b: Double): Boolean {
    if (a.isNaN() && b.isNaN()) return true
    return abs(a - b) < 1e-12
}

private fun decimalPlaces(value: Double): Int {
    val text = value.toString()
    val point = text.indexOf('.')
    if (point < 0) return 0
    return text.substring(point + 1).trimEnd('0').length
}
