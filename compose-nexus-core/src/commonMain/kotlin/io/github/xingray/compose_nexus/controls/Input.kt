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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.xingray.compose_nexus.foundation.ProvideContentColor
import io.github.xingray.compose_nexus.theme.ComponentSize
import io.github.xingray.compose_nexus.theme.NexusTheme

enum class NexusInputType {
    Text,
    Textarea,
    Number,
    Password,
    Email,
    Search,
    Tel,
    Url,
}

enum class NexusWordLimitPosition {
    Inside,
    Outside,
}

enum class NexusTextareaResize {
    None,
    Both,
    Horizontal,
    Vertical,
}

data class NexusTextareaAutosize(
    val minRows: Int = 2,
    val maxRows: Int = 6,
)

@Composable
fun NexusInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    type: NexusInputType = NexusInputType.Text,
    size: ComponentSize = ComponentSize.Default,
    placeholder: String = "",
    disabled: Boolean = false,
    readonly: Boolean = false,
    clearable: Boolean = false,
    clearIcon: (@Composable () -> Unit)? = null,
    showPassword: Boolean = false,
    maxLength: Int = 0,
    minLength: Int = 0,
    showWordLimit: Boolean = false,
    wordLimitPosition: NexusWordLimitPosition = NexusWordLimitPosition.Inside,
    formatter: ((String) -> String)? = null,
    parser: ((String) -> String)? = null,
    prefix: (@Composable () -> Unit)? = null,
    suffix: (@Composable () -> Unit)? = null,
    prepend: (@Composable () -> Unit)? = null,
    append: (@Composable () -> Unit)? = null,
    rows: Int = 2,
    autosize: Boolean = false,
    autosizeOptions: NexusTextareaAutosize? = null,
    resize: NexusTextareaResize = NexusTextareaResize.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true,
    validateEvent: Boolean = true,
    onFocusChanged: ((Boolean) -> Unit)? = null,
    onInput: ((String) -> Unit)? = null,
    onChange: ((String) -> Unit)? = null,
    onFocus: (() -> Unit)? = null,
    onBlur: (() -> Unit)? = null,
    onClear: (() -> Unit)? = null,
) {
    val colorScheme = NexusTheme.colorScheme
    val shapes = NexusTheme.shapes
    val sizes = NexusTheme.sizes
    val typography = NexusTheme.typography

    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val isHovered by interactionSource.collectIsHoveredAsState()

    var passwordVisible by remember { mutableStateOf(false) }
    var focusedSnapshot by remember { mutableStateOf(value) }

    val isTextarea = type == NexusInputType.Textarea
    val isPasswordMode = type == NexusInputType.Password || showPassword

    val controlHeight: Dp = when (size) {
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

    val borderColor = when {
        disabled -> colorScheme.disabled.border
        isFocused -> colorScheme.primary.base
        isHovered -> colorScheme.border.dark
        else -> colorScheme.border.base
    }
    val bgColor = if (disabled) colorScheme.disabled.background else colorScheme.fill.blank
    val textColor = if (disabled) colorScheme.disabled.text else colorScheme.text.regular

    val displayValue = when {
        isTextarea -> value
        type == NexusInputType.Text && formatter != null -> formatter(value)
        else -> value
    }

    val visualTransformation = if (isPasswordMode && !passwordVisible) {
        PasswordVisualTransformation()
    } else {
        VisualTransformation.None
    }

    val wordLimitEnabled = showWordLimit && maxLength > 0
    val wordCountText = "${value.length} / $maxLength"
    val lineHeightDp = 22.dp
    val baseRows = rows.coerceAtLeast(1)
    val rowCount = if (isTextarea && (autosize || autosizeOptions != null)) {
        val lines = (value.count { it == '\n' } + 1).coerceAtLeast(1)
        val minRows = autosizeOptions?.minRows ?: baseRows
        val maxRows = autosizeOptions?.maxRows ?: maxOf(minRows, 6)
        lines.coerceIn(minRows, maxRows)
    } else {
        baseRows
    }

    val filteredOnValueChange: (String) -> Unit = { rawInput ->
        val parsed = if (!isTextarea && type == NexusInputType.Text && parser != null) {
            parser(rawInput)
        } else {
            rawInput
        }
        val withMaxLimit = if (maxLength > 0 && parsed.length > maxLength) parsed.take(maxLength) else parsed
        onValueChange(withMaxLimit)
        onInput?.invoke(withMaxLimit)
    }

    val formValidationEnabled = validateEvent
    @Suppress("UNUSED_VARIABLE")
    val resizeMode = resize
    @Suppress("UNUSED_VARIABLE")
    val minLengthHint = minLength
    @Suppress("UNUSED_VARIABLE")
    val shouldValidateOnInputEvent = formValidationEnabled

    val fieldModifier = Modifier
        .onPreviewKeyEvent { keyEvent ->
            if (keyEvent.type == KeyEventType.KeyUp && keyEvent.key == Key.Enter) {
                if (value != focusedSnapshot) {
                    onChange?.invoke(value)
                    focusedSnapshot = value
                }
                false
            } else {
                false
            }
        }
        .defaultMinSize(
            minHeight = if (isTextarea) lineHeightDp * rowCount else controlHeight,
        )
        .then(
            if (isTextarea) Modifier.heightIn(min = lineHeightDp * rowCount) else Modifier
        )
        .clip(shapes.base)
        .background(bgColor)
        .border(1.dp, borderColor, shapes.base)
        .padding(
            horizontal = horizontalPadding,
            vertical = if (isTextarea) 8.dp else 0.dp,
        )

    Column(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (!isTextarea && prepend != null) {
                ProvideContentColor(colorScheme.text.regular) {
                    prepend()
                }
            }

            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .then(fieldModifier),
                    contentAlignment = if (isTextarea) Alignment.TopStart else Alignment.CenterStart,
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = if (isTextarea) Alignment.Top else Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        if (!isTextarea && prefix != null) {
                            ProvideContentColor(colorScheme.text.placeholder) {
                                Box(modifier = Modifier.padding(end = 2.dp)) {
                                    prefix()
                                }
                            }
                        }

                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = if (isTextarea) Alignment.TopStart else Alignment.CenterStart,
                        ) {
                            BasicTextField(
                                value = displayValue,
                                onValueChange = filteredOnValueChange,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .defaultMinSize(minHeight = if (isTextarea) lineHeightDp * rowCount else Dp.Unspecified)
                                    .padding(vertical = if (isTextarea) 2.dp else 0.dp)
                                    .onFocusChanged {
                                        onFocusChanged?.invoke(it.isFocused)
                                        if (it.isFocused) {
                                            focusedSnapshot = value
                                            onFocus?.invoke()
                                        } else {
                                            onBlur?.invoke()
                                            if (value != focusedSnapshot) {
                                                onChange?.invoke(value)
                                                focusedSnapshot = value
                                            }
                                        }
                                    },
                                enabled = !disabled && !readonly,
                                readOnly = readonly,
                                textStyle = textStyle.merge(TextStyle(color = textColor)),
                                singleLine = !isTextarea && singleLine,
                                keyboardOptions = keyboardOptions,
                                keyboardActions = keyboardActions,
                                interactionSource = interactionSource,
                                cursorBrush = SolidColor(colorScheme.primary.base),
                                visualTransformation = visualTransformation,
                                decorationBox = { innerTextField ->
                                    Box(
                                        modifier = Modifier.fillMaxWidth(),
                                        contentAlignment = if (isTextarea) Alignment.TopStart else Alignment.CenterStart,
                                    ) {
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
                        }

                        if (!isTextarea && clearable && value.isNotEmpty() && !disabled && !readonly) {
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = null,
                                    ) {
                                        onValueChange("")
                                        onInput?.invoke("")
                                        onClear?.invoke()
                                        if (focusedSnapshot.isNotEmpty()) {
                                            onChange?.invoke("")
                                            focusedSnapshot = ""
                                        }
                                    },
                                contentAlignment = Alignment.Center,
                            ) {
                                if (clearIcon != null) {
                                    clearIcon()
                                } else {
                                    NexusText(
                                        text = "✕",
                                        color = colorScheme.text.placeholder,
                                        style = typography.extraSmall,
                                    )
                                }
                            }
                        }

                        if (!isTextarea && isPasswordMode) {
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .then(
                                        if (!disabled && !readonly) {
                                            Modifier.clickable(
                                                interactionSource = remember { MutableInteractionSource() },
                                                indication = null,
                                            ) { passwordVisible = !passwordVisible }
                                        } else {
                                            Modifier
                                        }
                                    ),
                                contentAlignment = Alignment.Center,
                            ) {
                                NexusText(
                                    text = if (passwordVisible) "◉" else "◎",
                                    color = colorScheme.text.placeholder,
                                    style = typography.extraSmall,
                                )
                            }
                        }

                        if (!isTextarea && suffix != null) {
                            ProvideContentColor(colorScheme.text.placeholder) {
                                Box(modifier = Modifier.padding(start = 2.dp)) {
                                    suffix()
                                }
                            }
                        }

                        if (!isTextarea && wordLimitEnabled && wordLimitPosition == NexusWordLimitPosition.Inside) {
                            NexusText(
                                text = wordCountText,
                                color = if (value.length < minLength && value.isNotEmpty()) {
                                    colorScheme.warning.base
                                } else {
                                    colorScheme.text.placeholder
                                },
                                style = typography.extraSmall,
                            )
                        }
                    }

                    if (isTextarea && wordLimitEnabled && wordLimitPosition == NexusWordLimitPosition.Inside) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 2.dp, bottom = 2.dp),
                            contentAlignment = Alignment.BottomEnd,
                        ) {
                            NexusText(
                                text = wordCountText,
                                color = if (value.length < minLength && value.isNotEmpty()) {
                                    colorScheme.warning.base
                                } else {
                                    colorScheme.text.placeholder
                                },
                                style = typography.extraSmall,
                            )
                        }
                    }
                }
            }

            if (!isTextarea && append != null) {
                ProvideContentColor(colorScheme.text.regular) {
                    append()
                }
            }
        }

        if (wordLimitEnabled && wordLimitPosition == NexusWordLimitPosition.Outside) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 2.dp),
                contentAlignment = Alignment.CenterEnd,
            ) {
                NexusText(
                    text = wordCountText,
                    color = if (value.length < minLength && value.isNotEmpty()) {
                        colorScheme.warning.base
                    } else {
                        colorScheme.text.placeholder
                    },
                    style = typography.extraSmall,
                )
            }
        }
    }
}
