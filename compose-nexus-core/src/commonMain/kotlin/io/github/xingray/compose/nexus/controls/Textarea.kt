package io.github.xingray.compose.nexus.controls

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import io.github.xingray.compose.nexus.theme.NexusTheme

/**
 * Element Plus Textarea — a multi-line text input field.
 *
 * @param value Current text value.
 * @param onValueChange Callback when text changes.
 * @param modifier Modifier.
 * @param placeholder Placeholder text.
 * @param disabled Disabled state.
 * @param readonly Read-only state.
 * @param rows Minimum visible rows.
 * @param maxLength Maximum character count (0 for unlimited).
 * @param showWordLimit Show character count when [maxLength] > 0.
 * @param keyboardOptions Keyboard options.
 * @param keyboardActions Keyboard actions.
 */
@Composable
fun NexusTextarea(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    disabled: Boolean = false,
    readonly: Boolean = false,
    rows: Int = 3,
    maxLength: Int = 0,
    showWordLimit: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    val colorScheme = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.colorScheme
    val shapes = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.shapes
    val typography = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.typography

    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val isHovered by interactionSource.collectIsHoveredAsState()

    val borderColor = when {
        disabled -> colorScheme.disabled.border
        isFocused -> colorScheme.primary.base
        isHovered -> colorScheme.border.dark
        else -> colorScheme.border.base
    }
    val bgColor = if (disabled) colorScheme.disabled.background else colorScheme.fill.blank
    val textColor = if (disabled) colorScheme.disabled.text else colorScheme.text.regular

    // Approximate min height: rows * lineHeight
    val lineHeightDp = 22.dp // base line height ≈ 22dp
    val minHeight = lineHeightDp * rows

    val filteredOnValueChange: (String) -> Unit = { newValue ->
        if (maxLength > 0 && newValue.length > maxLength) {
            onValueChange(newValue.take(maxLength))
        } else {
            onValueChange(newValue)
        }
    }

    Column(modifier = modifier) {
        BasicTextField(
            value = value,
            onValueChange = filteredOnValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = minHeight)
                .clip(shapes.base)
                .background(bgColor)
                .border(1.dp, borderColor, shapes.base)
                .padding(horizontal = 12.dp, vertical = 8.dp),
            enabled = !disabled && !readonly,
            readOnly = readonly,
            textStyle = typography.base.merge(TextStyle(color = textColor)),
            singleLine = false,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            interactionSource = interactionSource,
            cursorBrush = SolidColor(colorScheme.primary.base),
            decorationBox = { innerTextField ->
                Box(contentAlignment = Alignment.TopStart) {
                    if (value.isEmpty() && placeholder.isNotEmpty()) {
                        _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
                            text = placeholder,
                            color = colorScheme.text.placeholder,
                            style = typography.base,
                        )
                    }
                    innerTextField()
                }
            },
        )

        // Word count
        if (showWordLimit && maxLength > 0) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 2.dp),
                contentAlignment = Alignment.CenterEnd,
            ) {
                _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
                    text = "${value.length} / $maxLength",
                    color = colorScheme.text.placeholder,
                    style = typography.extraSmall,
                )
            }
        }
    }
}
