package io.github.xingray.compose_nexus.controls

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.xingray.compose_nexus.foundation.LocalContentColor
import io.github.xingray.compose_nexus.foundation.ProvideContentColor
import io.github.xingray.compose_nexus.theme.ComponentSize
import io.github.xingray.compose_nexus.theme.NexusTheme

/**
 * Element Plus Input — a single-line text input field.
 *
 * @param value Current text value.
 * @param onValueChange Callback when text changes.
 * @param modifier Modifier.
 * @param size Component size.
 * @param placeholder Placeholder text.
 * @param disabled Disabled state.
 * @param readonly Read-only state.
 * @param clearable Show clear button when text is not empty.
 * @param showPassword Toggle password visibility.
 * @param maxLength Maximum character count (0 for unlimited).
 * @param prefix Prefix slot (e.g., icon).
 * @param suffix Suffix slot (e.g., icon).
 * @param prepend Prepend slot (before the input, outside border).
 * @param append Append slot (after the input, outside border).
 * @param keyboardOptions Keyboard options.
 * @param keyboardActions Keyboard actions.
 * @param singleLine Whether input is single line.
 */
@Composable
fun NexusInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    size: ComponentSize = ComponentSize.Default,
    placeholder: String = "",
    disabled: Boolean = false,
    readonly: Boolean = false,
    clearable: Boolean = false,
    showPassword: Boolean = false,
    maxLength: Int = 0,
    prefix: (@Composable () -> Unit)? = null,
    suffix: (@Composable () -> Unit)? = null,
    prepend: (@Composable () -> Unit)? = null,
    append: (@Composable () -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true,
) {
    val colorScheme = NexusTheme.colorScheme
    val shapes = NexusTheme.shapes
    val sizes = NexusTheme.sizes
    val typography = NexusTheme.typography

    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val isHovered by interactionSource.collectIsHoveredAsState()

    var passwordVisible by remember { mutableStateOf(false) }

    // Sizing
    val height: Dp = when (size) {
        ComponentSize.Large -> sizes.componentLarge
        ComponentSize.Default -> sizes.componentDefault
        ComponentSize.Small -> sizes.componentSmall
    }
    val horizontalPadding: Dp = when (size) {
        ComponentSize.Large -> sizes.inputPaddingLarge
        ComponentSize.Default -> sizes.inputPaddingDefault
        ComponentSize.Small -> sizes.inputPaddingSmall
    }
    val textStyle: TextStyle = when (size) {
        ComponentSize.Large -> typography.base
        ComponentSize.Default -> typography.base
        ComponentSize.Small -> typography.extraSmall
    }

    // Border color
    val borderColor = when {
        disabled -> colorScheme.disabled.border
        isFocused -> colorScheme.primary.base
        isHovered -> colorScheme.border.dark
        else -> colorScheme.border.base
    }
    val bgColor = if (disabled) colorScheme.disabled.background else colorScheme.fill.blank
    val textColor = if (disabled) colorScheme.disabled.text else colorScheme.text.regular

    // Visual transformation
    val visualTransformation = if (showPassword && !passwordVisible) {
        PasswordVisualTransformation()
    } else {
        VisualTransformation.None
    }

    // Enforce maxLength
    val filteredOnValueChange: (String) -> Unit = { newValue ->
        if (maxLength > 0 && newValue.length > maxLength) {
            onValueChange(newValue.take(maxLength))
        } else {
            onValueChange(newValue)
        }
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Prepend slot
        if (prepend != null) {
            ProvideContentColor(colorScheme.text.regular) {
                prepend()
            }
        }

        // Main input container
        Row(
            modifier = Modifier
                .weight(1f)
                .defaultMinSize(minHeight = height)
                .clip(shapes.base)
                .background(bgColor)
                .border(1.dp, borderColor, shapes.base)
                .padding(horizontal = horizontalPadding),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Prefix
            if (prefix != null) {
                ProvideContentColor(colorScheme.text.placeholder) {
                    Box(modifier = Modifier.padding(end = 4.dp)) {
                        prefix()
                    }
                }
            }

            // Text field
            BasicTextField(
                value = value,
                onValueChange = filteredOnValueChange,
                modifier = Modifier.weight(1f),
                enabled = !disabled && !readonly,
                readOnly = readonly,
                textStyle = textStyle.merge(TextStyle(color = textColor)),
                singleLine = singleLine,
                keyboardOptions = keyboardOptions,
                keyboardActions = keyboardActions,
                interactionSource = interactionSource,
                cursorBrush = SolidColor(colorScheme.primary.base),
                visualTransformation = visualTransformation,
                decorationBox = { innerTextField ->
                    Box(contentAlignment = Alignment.CenterStart) {
                        if (value.isEmpty() && placeholder.isNotEmpty()) {
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

            // Clearable button
            if (clearable && value.isNotEmpty() && !disabled && !readonly) {
                Box(
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .size(16.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                        ) { onValueChange("") },
                    contentAlignment = Alignment.Center,
                ) {
                    NexusText(
                        text = "✕",
                        color = colorScheme.text.placeholder,
                        style = typography.extraSmall,
                    )
                }
            }

            // Password toggle
            if (showPassword) {
                Box(
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .size(16.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                        ) { passwordVisible = !passwordVisible },
                    contentAlignment = Alignment.Center,
                ) {
                    NexusText(
                        text = if (passwordVisible) "◉" else "◎",
                        color = colorScheme.text.placeholder,
                        style = typography.extraSmall,
                    )
                }
            }

            // Suffix
            if (suffix != null) {
                ProvideContentColor(colorScheme.text.placeholder) {
                    Box(modifier = Modifier.padding(start = 4.dp)) {
                        suffix()
                    }
                }
            }
        }

        // Append slot
        if (append != null) {
            ProvideContentColor(colorScheme.text.regular) {
                append()
            }
        }
    }
}
