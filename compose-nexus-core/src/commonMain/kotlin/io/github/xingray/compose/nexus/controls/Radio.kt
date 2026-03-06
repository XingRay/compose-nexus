package io.github.xingray.compose.nexus.controls

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import io.github.xingray.compose.nexus.foundation.ProvideContentColor
import io.github.xingray.compose.nexus.theme.ComponentSize
import io.github.xingray.compose.nexus.theme.NexusTheme

data class NexusRadioOption(
    val value: Any? = null,
    val label: String? = null,
    val disabled: Boolean = false,
    val payload: Map<String, Any?> = emptyMap(),
)

data class NexusRadioOptionProps(
    val value: String = "value",
    val label: String = "label",
    val disabled: String = "disabled",
)

enum class NexusRadioGroupType {
    Radio,
    Button,
}

@Stable
class RadioGroupState<T>(initialSelected: T? = null) {
    var selected: T? by mutableStateOf(initialSelected)

    fun isSelected(value: T): Boolean = selected == value

    fun select(value: T) {
        selected = value
    }
}

@Composable
fun <T> rememberRadioGroupState(
    initialSelected: T? = null,
): io.github.xingray.compose.nexus.controls.RadioGroupState<T> = remember { _root_ide_package_.io.github.xingray.compose.nexus.controls.RadioGroupState(initialSelected) }

internal val LocalRadioGroupState = compositionLocalOf<io.github.xingray.compose.nexus.controls.RadioGroupState<Any?>?> { null }

internal data class RadioGroupConfig(
    val size: io.github.xingray.compose.nexus.theme.ComponentSize? = null,
    val disabled: Boolean = false,
    val type: io.github.xingray.compose.nexus.controls.NexusRadioGroupType = _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusRadioGroupType.Radio,
    val fill: Color = Color.Unspecified,
    val textColor: Color = Color.Unspecified,
    val name: String? = null,
    val onChange: ((Any?) -> Unit)? = null,
    val validateEvent: Boolean = true,
)

internal val LocalRadioGroupConfig = compositionLocalOf { _root_ide_package_.io.github.xingray.compose.nexus.controls.RadioGroupConfig() }

@Composable
fun <T> NexusRadioGroup(
    state: io.github.xingray.compose.nexus.controls.RadioGroupState<T>,
    modifier: Modifier = Modifier,
    size: io.github.xingray.compose.nexus.theme.ComponentSize = _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Default,
    disabled: Boolean = false,
    validateEvent: Boolean = true,
    textColor: Color = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.colorScheme.white,
    fill: Color = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.colorScheme.primary.base,
    name: String? = null,
    options: List<io.github.xingray.compose.nexus.controls.NexusRadioOption> = emptyList(),
    props: io.github.xingray.compose.nexus.controls.NexusRadioOptionProps = _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusRadioOptionProps(),
    type: io.github.xingray.compose.nexus.controls.NexusRadioGroupType = _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusRadioGroupType.Radio,
    onChange: ((T) -> Unit)? = null,
    content: @Composable () -> Unit = {},
) {
    @Suppress("UNCHECKED_CAST")
    val anyState = state as io.github.xingray.compose.nexus.controls.RadioGroupState<Any?>
    val config = _root_ide_package_.io.github.xingray.compose.nexus.controls.RadioGroupConfig(
        size = size,
        disabled = disabled,
        type = type,
        fill = fill,
        textColor = textColor,
        name = name,
        validateEvent = validateEvent,
        onChange = { changed ->
            @Suppress("UNCHECKED_CAST")
            onChange?.invoke(changed as T)
        },
    )

    fun optionValue(option: io.github.xingray.compose.nexus.controls.NexusRadioOption): Any? = option.payload[props.value] ?: option.value
    fun optionLabel(option: io.github.xingray.compose.nexus.controls.NexusRadioOption): String {
        val payloadLabel = option.payload[props.label]?.toString()
        return payloadLabel ?: option.label ?: optionValue(option)?.toString().orEmpty()
    }
    fun optionDisabled(option: io.github.xingray.compose.nexus.controls.NexusRadioOption): Boolean {
        val payloadDisabled = option.payload[props.disabled]
        return when (payloadDisabled) {
            is Boolean -> payloadDisabled
            is String -> payloadDisabled.equals("true", ignoreCase = true)
            else -> option.disabled
        }
    }

    CompositionLocalProvider(
        _root_ide_package_.io.github.xingray.compose.nexus.controls.LocalRadioGroupState provides anyState,
        _root_ide_package_.io.github.xingray.compose.nexus.controls.LocalRadioGroupConfig provides config,
    ) {
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (options.isNotEmpty()) {
                options.forEach { option ->
                    val value = optionValue(option)
                    if (type == _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusRadioGroupType.Button) {
                        _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusRadioButton(
                            value = value,
                            disabled = optionDisabled(option),
                        ) {
                            _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(text = optionLabel(option))
                        }
                    } else {
                        _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusRadio(
                            value = value,
                            disabled = optionDisabled(option),
                        ) {
                            _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(text = optionLabel(option))
                        }
                    }
                }
            }
            content()
        }
    }
}

@Composable
fun NexusRadio(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: io.github.xingray.compose.nexus.theme.ComponentSize = _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Default,
    disabled: Boolean = false,
    border: Boolean = false,
    label: (@Composable () -> Unit)? = null,
) {
    _root_ide_package_.io.github.xingray.compose.nexus.controls.RadioVisual(
        selected = selected,
        onClick = onClick,
        modifier = modifier,
        size = size,
        disabled = disabled,
        border = border,
        label = label,
    )
}

@Composable
fun NexusRadio(
    value: Any?,
    modifier: Modifier = Modifier,
    size: io.github.xingray.compose.nexus.theme.ComponentSize? = null,
    disabled: Boolean = false,
    border: Boolean = false,
    label: (@Composable () -> Unit)? = null,
) {
    val groupState = _root_ide_package_.io.github.xingray.compose.nexus.controls.LocalRadioGroupState.current
    val groupConfig = _root_ide_package_.io.github.xingray.compose.nexus.controls.LocalRadioGroupConfig.current
    val selected = groupState?.selected == value
    val actualDisabled = disabled || groupConfig.disabled
    val actualSize = size ?: groupConfig.size ?: _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Default

    if (groupConfig.type == _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusRadioGroupType.Button) {
        _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusRadioButton(
            value = value,
            modifier = modifier,
            size = actualSize,
            disabled = actualDisabled,
            label = label,
        )
    } else {
        _root_ide_package_.io.github.xingray.compose.nexus.controls.RadioVisual(
            selected = selected,
            onClick = {
                if (!actualDisabled) {
                    groupState?.select(value)
                    groupConfig.onChange?.invoke(value)
                }
            },
            modifier = modifier,
            size = actualSize,
            disabled = actualDisabled,
            border = border,
            label = label,
        )
    }
}

@Composable
fun NexusRadioButton(
    value: Any?,
    modifier: Modifier = Modifier,
    size: io.github.xingray.compose.nexus.theme.ComponentSize? = null,
    disabled: Boolean = false,
    label: (@Composable () -> Unit)? = null,
) {
    val groupState = _root_ide_package_.io.github.xingray.compose.nexus.controls.LocalRadioGroupState.current
    val groupConfig = _root_ide_package_.io.github.xingray.compose.nexus.controls.LocalRadioGroupConfig.current
    val colorScheme = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.colorScheme
    val typography = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.typography
    val actualSize = size ?: groupConfig.size ?: _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Default
    val selected = groupState?.selected == value
    val actualDisabled = disabled || groupConfig.disabled
    val fillColor = if (groupConfig.fill == Color.Unspecified) colorScheme.primary.base else groupConfig.fill
    val activeTextColor = if (groupConfig.textColor == Color.Unspecified) colorScheme.white else groupConfig.textColor

    val height = when (actualSize) {
        _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Large -> 40.dp
        _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Default -> 32.dp
        _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Small -> 24.dp
    }
    val textStyle = when (actualSize) {
        _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Large -> typography.base
        _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Default -> typography.base
        _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Small -> typography.extraSmall
    }

    val bg = when {
        selected && actualDisabled -> fillColor.copy(alpha = 0.5f)
        selected -> fillColor
        actualDisabled -> colorScheme.disabled.background
        else -> colorScheme.fill.blank
    }
    val borderColor = when {
        selected && actualDisabled -> fillColor.copy(alpha = 0.5f)
        selected -> fillColor
        actualDisabled -> colorScheme.disabled.border
        else -> colorScheme.border.base
    }
    val textColor = when {
        selected -> activeTextColor
        actualDisabled -> colorScheme.disabled.text
        else -> colorScheme.text.regular
    }

    Box(
        modifier = modifier
            .defaultMinSize(minHeight = height)
            .clip(_root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.shapes.base)
            .background(bg)
            .border(1.dp, borderColor, _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.shapes.base)
            .then(
                if (!actualDisabled) {
                    Modifier
                        .clickable(
                            role = Role.RadioButton,
                            onClick = {
                                groupState?.select(value)
                                groupConfig.onChange?.invoke(value)
                            },
                        )
                        .pointerHoverIcon(PointerIcon.Hand)
                } else {
                    Modifier
                }
            )
            .padding(horizontal = 12.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center,
    ) {
        _root_ide_package_.io.github.xingray.compose.nexus.foundation.ProvideContentColor(textColor) {
            if (label != null) {
                label()
            } else {
                _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(text = value?.toString().orEmpty(), style = textStyle)
            }
        }
    }
}

@Composable
private fun RadioVisual(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier,
    size: io.github.xingray.compose.nexus.theme.ComponentSize,
    disabled: Boolean,
    border: Boolean,
    label: (@Composable () -> Unit)?,
) {
    val colorScheme = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.colorScheme
    val typography = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.typography
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    val outerSize = when (size) {
        _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Large -> 16.dp
        _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Default -> 14.dp
        _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Small -> 12.dp
    }
    val innerSize = outerSize - 6.dp
    val radioHeight = when (size) {
        _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Large -> 40.dp
        _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Default -> 32.dp
        _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Small -> 24.dp
    }
    val textStyle = when (size) {
        _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Large -> typography.base
        _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Default -> typography.base
        _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Small -> typography.extraSmall
    }

    val borderColor = when {
        disabled && selected -> colorScheme.primary.light5
        disabled -> colorScheme.disabled.border
        selected -> colorScheme.primary.base
        isHovered -> colorScheme.primary.base
        else -> colorScheme.border.base
    }
    val dotColor = if (disabled) colorScheme.primary.light5 else colorScheme.primary.base
    val textColor = if (disabled) colorScheme.disabled.text else colorScheme.text.regular

    Row(
        modifier = modifier
            .then(
                if (border) {
                    Modifier
                        .defaultMinSize(minHeight = radioHeight)
                        .clip(_root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.shapes.base)
                        .border(1.dp, if (selected) colorScheme.primary.base else colorScheme.border.base, _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.shapes.base)
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                } else {
                    Modifier
                }
            )
            .then(
                if (!disabled) {
                    Modifier
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null,
                            role = Role.RadioButton,
                        ) { onClick() }
                        .pointerHoverIcon(PointerIcon.Hand)
                } else {
                    Modifier
                }
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Box(
            modifier = Modifier
                .size(outerSize)
                .clip(CircleShape)
                .background(colorScheme.fill.blank)
                .border(1.dp, borderColor, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            if (selected) {
                Box(
                    modifier = Modifier
                        .size(innerSize)
                        .clip(CircleShape)
                        .background(dotColor),
                )
            }
        }

        if (label != null) {
            _root_ide_package_.io.github.xingray.compose.nexus.foundation.ProvideContentColor(textColor) {
                label()
            }
        }
    }

    if (label == null) {
        // keep style used for default text label when needed in future
        @Suppress("UNUSED_VARIABLE")
        val unused = textStyle
    }
}
