package io.github.xingray.compose.nexus.controls

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import io.github.xingray.compose.nexus.controls.drawCheckerboardBackground
import io.github.xingray.compose.nexus.theme.ComponentSize
import io.github.xingray.compose.nexus.theme.NexusTheme
import kotlin.math.roundToInt

@Stable
class NexusColorPickerState(
    initialVisible: Boolean = false,
) {
    var isVisible by mutableStateOf(initialVisible)

    fun show() {
        isVisible = true
    }

    fun hide() {
        isVisible = false
    }
}

@Composable
fun rememberNexusColorPickerState(
    initialVisible: Boolean = false,
): io.github.xingray.compose.nexus.controls.NexusColorPickerState = remember { _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusColorPickerState(initialVisible) }

@Composable
fun NexusColorPicker(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    state: io.github.xingray.compose.nexus.controls.NexusColorPickerState = _root_ide_package_.io.github.xingray.compose.nexus.controls.rememberNexusColorPickerState(),
    disabled: Boolean = false,
    clearable: Boolean = true,
    size: io.github.xingray.compose.nexus.theme.ComponentSize = _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Default,
    showAlpha: Boolean = false,
    colorFormat: io.github.xingray.compose.nexus.controls.NexusColorFormat? = null,
    predefine: List<String> = emptyList(),
    validateEvent: Boolean = true,
    onChange: ((String) -> Unit)? = null,
    onActiveChange: ((String) -> Unit)? = null,
    onFocus: (() -> Unit)? = null,
    onBlur: (() -> Unit)? = null,
    onClear: (() -> Unit)? = null,
) {
    val colors = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.colorScheme
    val shapes = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.shapes
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    var popupOffset by remember { mutableStateOf(IntOffset.Zero) }
    var panelWidthPx by remember { mutableIntStateOf(280) }

    val controlHeight = when (size) {
        _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Large -> 40.dp
        _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Default -> 32.dp
        _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Small -> 24.dp
    }
    val previewSize = when (size) {
        _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Large -> 20.dp
        _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Default -> 16.dp
        _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Small -> 12.dp
    }

    val parsedColor = remember(value) { _root_ide_package_.io.github.xingray.compose.nexus.controls.parsePickerColor(value) }

    Box(modifier = modifier) {
        Row(
            modifier = Modifier
                .height(controlHeight)
                .width(96.dp)
                .clip(shapes.base)
                .background(colors.fill.blank)
                .border(1.dp, if (state.isVisible) colors.primary.base else colors.border.base, shapes.base)
                .onGloballyPositioned { coordinates ->
                    val position = coordinates.positionInWindow()
                    popupOffset = IntOffset(
                        x = position.x.roundToInt(),
                        y = (position.y + coordinates.size.height).roundToInt(),
                    )
                    panelWidthPx = maxOf(260, coordinates.size.width + 180)
                }
                .then(
                    if (!disabled) {
                        Modifier
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null,
                            ) {
                                state.isVisible = !state.isVisible
                                if (state.isVisible) onFocus?.invoke() else onBlur?.invoke()
                            }
                            .pointerHoverIcon(PointerIcon.Hand)
                    } else {
                        Modifier
                    }
                )
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(previewSize)
                    .clip(_root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.shapes.small)
                    .drawCheckerboardBackground()
                    .background(parsedColor ?: colors.fill.blank)
                    .border(1.dp, colors.border.light, _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.shapes.small),
            )

            if (value.isBlank()) {
                _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
                    text = "Pick",
                    style = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.typography.small,
                    color = colors.text.placeholder,
                    modifier = Modifier.weight(1f),
                )
            } else {
                _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
                    text = value,
                    style = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.typography.small,
                    color = if (disabled) colors.text.disabled else colors.text.regular,
                    modifier = Modifier.weight(1f),
                    truncated = true,
                )
            }

            if (clearable && value.isNotBlank() && isHovered && !disabled) {
                _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
                    text = "✕",
                    style = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.typography.extraSmall,
                    color = colors.text.secondary,
                    modifier = Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                    ) {
                        onValueChange("")
                        onClear?.invoke()
                    },
                )
            } else {
                _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
                    text = if (state.isVisible) "▴" else "▾",
                    style = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.typography.extraSmall,
                    color = colors.text.placeholder,
                )
            }
        }

        if (state.isVisible && !disabled) {
            Popup(
                alignment = Alignment.TopStart,
                offset = popupOffset,
                properties = PopupProperties(focusable = true),
                onDismissRequest = {
                    state.hide()
                    onBlur?.invoke()
                },
            ) {
                _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusColorPickerPanel(
                    value = if (value.isBlank()) "#409EFF" else value,
                    onValueChange = {
                        onValueChange(it)
                        onActiveChange?.invoke(it)
                        onChange?.invoke(it)
                    },
                    showAlpha = showAlpha,
                    colorFormat = colorFormat,
                    predefine = predefine,
                    validateEvent = validateEvent,
                    modifier = Modifier
                        .width((panelWidthPx / 1.1f).roundToInt().dp)
                        .offset(y = 6.dp),
                )
            }
        }
    }
}

private fun Modifier.drawCheckerboardBackground(): Modifier = drawBehind {
    val cell = 4.dp.toPx()
    val light = androidx.compose.ui.graphics.Color(0xFFF3F3F3)
    val dark = androidx.compose.ui.graphics.Color(0xFFE2E2E2)
    var y = 0f
    var row = 0
    while (y < size.height) {
        var x = 0f
        var col = row % 2
        while (x < size.width) {
            drawRect(
                color = if (col % 2 == 0) light else dark,
                topLeft = Offset(x, y),
                size = Size(cell, cell),
            )
            col += 1
            x += cell
        }
        row += 1
        y += cell
    }
}

private fun parsePickerColor(value: String): androidx.compose.ui.graphics.Color? {
    val text = value.trim()
    if (text.isEmpty()) return null
    val normalized = text.lowercase()
    return when {
        normalized.startsWith("#") -> {
            val hex = normalized.removePrefix("#")
            when (hex.length) {
                3 -> {
                    val r = hex[0].toString().repeat(2).toIntOrNull(16) ?: return null
                    val g = hex[1].toString().repeat(2).toIntOrNull(16) ?: return null
                    val b = hex[2].toString().repeat(2).toIntOrNull(16) ?: return null
                    androidx.compose.ui.graphics.Color(r / 255f, g / 255f, b / 255f, 1f)
                }
                4 -> {
                    val r = hex[0].toString().repeat(2).toIntOrNull(16) ?: return null
                    val g = hex[1].toString().repeat(2).toIntOrNull(16) ?: return null
                    val b = hex[2].toString().repeat(2).toIntOrNull(16) ?: return null
                    val a = hex[3].toString().repeat(2).toIntOrNull(16) ?: return null
                    androidx.compose.ui.graphics.Color(r / 255f, g / 255f, b / 255f, a / 255f)
                }
                6 -> {
                    val r = hex.substring(0, 2).toIntOrNull(16) ?: return null
                    val g = hex.substring(2, 4).toIntOrNull(16) ?: return null
                    val b = hex.substring(4, 6).toIntOrNull(16) ?: return null
                    androidx.compose.ui.graphics.Color(r / 255f, g / 255f, b / 255f, 1f)
                }
                8 -> {
                    val r = hex.substring(0, 2).toIntOrNull(16) ?: return null
                    val g = hex.substring(2, 4).toIntOrNull(16) ?: return null
                    val b = hex.substring(4, 6).toIntOrNull(16) ?: return null
                    val a = hex.substring(6, 8).toIntOrNull(16) ?: return null
                    androidx.compose.ui.graphics.Color(r / 255f, g / 255f, b / 255f, a / 255f)
                }
                else -> null
            }
        }
        normalized.startsWith("rgb(") || normalized.startsWith("rgba(") -> {
            val body = normalized.substringAfter("(").substringBeforeLast(")")
            val parts = body.split(",").map { it.trim() }
            if (parts.size !in 3..4) return null
            val r = parts[0].toFloatOrNull()?.coerceIn(0f, 255f)?.div(255f) ?: return null
            val g = parts[1].toFloatOrNull()?.coerceIn(0f, 255f)?.div(255f) ?: return null
            val b = parts[2].toFloatOrNull()?.coerceIn(0f, 255f)?.div(255f) ?: return null
            val a = parts.getOrNull(3)?.toFloatOrNull()?.coerceIn(0f, 1f) ?: 1f
            androidx.compose.ui.graphics.Color(r, g, b, a)
        }
        else -> null
    }
}
