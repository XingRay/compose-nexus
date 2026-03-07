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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import io.github.xingray.compose.nexus.theme.ComponentSize
import io.github.xingray.compose.nexus.theme.NexusTheme

enum class CheckboxGroupType {
    Checkbox,
    Button,
}

data class CheckboxOption<T>(
    val value: T,
    val label: String,
    val disabled: Boolean = false,
)

@Stable
class CheckboxGroupState<T>(
    initialSelected: Set<T> = emptySet(),
) {
    var selected: Set<T> by mutableStateOf(initialSelected)

    fun isChecked(value: T): Boolean = value in selected

    fun toggle(
        value: T,
        min: Int? = null,
        max: Int? = null,
    ): Boolean {
        val checked = value in selected
        if (checked) {
            if (min != null && selected.size <= min) return false
            selected = selected - value
            return true
        }
        if (max != null && selected.size >= max) return false
        selected = selected + value
        return true
    }
}

@Composable
fun <T> rememberCheckboxGroupState(
    initialSelected: Set<T> = emptySet(),
): CheckboxGroupState<T> = remember { CheckboxGroupState(initialSelected) }

private data class CheckboxGroupConfig(
    val state: CheckboxGroupState<Any>,
    val size: ComponentSize?,
    val disabled: Boolean,
    val min: Int?,
    val max: Int?,
    val type: CheckboxGroupType,
    val onChange: ((Set<Any>) -> Unit)?,
)

private val LocalCheckboxGroupConfig = compositionLocalOf<CheckboxGroupConfig?> { null }

@Composable
fun <T> NexusCheckboxGroup(
    state: CheckboxGroupState<T>,
    modifier: Modifier = Modifier,
    size: ComponentSize? = null,
    disabled: Boolean = false,
    min: Int? = null,
    max: Int? = null,
    type: CheckboxGroupType = CheckboxGroupType.Checkbox,
    options: List<CheckboxOption<T>> = emptyList(),
    onChange: ((Set<T>) -> Unit)? = null,
    content: @Composable () -> Unit = {},
) {
    @Suppress("UNCHECKED_CAST")
    val onAnyChange: ((Set<Any>) -> Unit)? = if (onChange == null) null else { set ->
        @Suppress("UNCHECKED_CAST")
        onChange(set as Set<T>)
    }

    @Suppress("UNCHECKED_CAST")
    CompositionLocalProvider(
        LocalCheckboxGroupConfig provides CheckboxGroupConfig(
            state = state as CheckboxGroupState<Any>,
            size = size,
            disabled = disabled,
            min = min,
            max = max,
            type = type,
            onChange = onAnyChange,
        ),
    ) {
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (options.isNotEmpty()) {
                options.forEach { option ->
                    when (type) {
                        CheckboxGroupType.Checkbox -> {
                            NexusCheckbox(
                                value = option.value,
                                disabled = option.disabled,
                            ) {
                                NexusText(text = option.label)
                            }
                        }
                        CheckboxGroupType.Button -> {
                            NexusCheckboxButton(
                                value = option.value,
                                disabled = option.disabled,
                            ) {
                                NexusText(text = option.label)
                            }
                        }
                    }
                }
            }
            content()
        }
    }
}

@Composable
fun NexusCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    size: ComponentSize = ComponentSize.Default,
    disabled: Boolean = false,
    indeterminate: Boolean = false,
    border: Boolean = false,
    label: (@Composable () -> Unit)? = null,
) {
    NexusCheckboxCore(
        checked = checked,
        onToggle = { onCheckedChange(!checked) },
        modifier = modifier,
        size = size,
        disabled = disabled,
        indeterminate = indeterminate,
        border = border,
        buttonStyle = false,
        label = label,
    )
}

@Composable
fun <T> NexusCheckbox(
    value: T,
    modifier: Modifier = Modifier,
    size: ComponentSize? = null,
    disabled: Boolean = false,
    border: Boolean = false,
    label: (@Composable () -> Unit)? = null,
) {
    val group = LocalCheckboxGroupConfig.current ?: return
    @Suppress("UNCHECKED_CAST")
    val typedValue = value as Any
    val checked = group.state.isChecked(typedValue)
    val resolvedSize = size ?: group.size ?: ComponentSize.Default
    val disabledByLimit = !checked && group.max != null && group.state.selected.size >= group.max
    val mergedDisabled = disabled || group.disabled || disabledByLimit

    if (group.type == CheckboxGroupType.Button) {
        NexusCheckboxButton(
            checked = checked,
            onCheckedChange = {
                if (!mergedDisabled) {
                    val changed = group.state.toggle(typedValue, min = group.min, max = group.max)
                    if (changed) {
                        group.onChange?.invoke(group.state.selected)
                    }
                }
            },
            modifier = modifier,
            size = resolvedSize,
            disabled = mergedDisabled,
            label = label,
        )
    } else {
        NexusCheckboxCore(
            checked = checked,
            onToggle = {
                if (!mergedDisabled) {
                    val changed = group.state.toggle(typedValue, min = group.min, max = group.max)
                    if (changed) {
                        group.onChange?.invoke(group.state.selected)
                    }
                }
            },
            modifier = modifier,
            size = resolvedSize,
            disabled = mergedDisabled,
            indeterminate = false,
            border = border,
            buttonStyle = false,
            label = label,
        )
    }
}

@Composable
fun NexusCheckboxButton(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    size: ComponentSize = ComponentSize.Default,
    disabled: Boolean = false,
    label: (@Composable () -> Unit)? = null,
) {
    NexusCheckboxCore(
        checked = checked,
        onToggle = { onCheckedChange(!checked) },
        modifier = modifier,
        size = size,
        disabled = disabled,
        indeterminate = false,
        border = false,
        buttonStyle = true,
        label = label,
    )
}

@Composable
fun <T> NexusCheckboxButton(
    value: T,
    modifier: Modifier = Modifier,
    size: ComponentSize? = null,
    disabled: Boolean = false,
    label: (@Composable () -> Unit)? = null,
) {
    NexusCheckbox(
        value = value,
        modifier = modifier,
        size = size,
        disabled = disabled,
        label = label,
    )
}

@Composable
private fun NexusCheckboxCore(
    checked: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier,
    size: ComponentSize,
    disabled: Boolean,
    indeterminate: Boolean,
    border: Boolean,
    buttonStyle: Boolean,
    label: (@Composable () -> Unit)?,
) {
    val colorScheme = NexusTheme.colorScheme
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val shape = RoundedCornerShape(4.dp)

    val boxSize = when (size) {
        ComponentSize.Large -> 16.dp
        ComponentSize.Default -> 14.dp
        ComponentSize.Small -> 12.dp
    }
    val textStyle = when (size) {
        ComponentSize.Large -> NexusTheme.typography.base
        ComponentSize.Default -> NexusTheme.typography.base
        ComponentSize.Small -> NexusTheme.typography.small
    }

    val isActive = checked || indeterminate
    val borderColor = when {
        disabled && isActive -> colorScheme.primary.light5
        disabled -> colorScheme.disabled.border
        isActive -> colorScheme.primary.base
        isHovered -> colorScheme.primary.base
        else -> colorScheme.border.base
    }
    val bgColor = when {
        buttonStyle && disabled && isActive -> colorScheme.primary.light7
        buttonStyle && isActive -> colorScheme.primary.base
        buttonStyle -> colorScheme.fill.blank
        disabled && isActive -> colorScheme.primary.light5
        isActive -> colorScheme.primary.base
        else -> colorScheme.fill.blank
    }
    val labelColor = when {
        disabled -> colorScheme.disabled.text
        buttonStyle && isActive -> colorScheme.white
        else -> colorScheme.text.regular
    }

    val clickableModifier = if (!disabled) {
        Modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                role = Role.Checkbox,
                onClick = onToggle,
            )
            .pointerHoverIcon(PointerIcon.Hand)
    } else {
        Modifier
    }

    val rowModifier = if (buttonStyle) {
        Modifier
            .defaultMinSize(minHeight = when (size) {
                ComponentSize.Large -> 40.dp
                ComponentSize.Default -> 32.dp
                ComponentSize.Small -> 24.dp
            })
            .clip(shape)
            .background(bgColor)
            .border(1.dp, borderColor, shape)
            .padding(horizontal = 14.dp, vertical = 6.dp)
    } else if (border) {
        Modifier
            .clip(shape)
            .border(1.dp, borderColor, shape)
            .padding(horizontal = 10.dp, vertical = 6.dp)
    } else {
        Modifier
    }

    Row(
        modifier = modifier.then(clickableModifier).then(rowModifier),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(if (buttonStyle) 0.dp else 8.dp),
    ) {
        if (!buttonStyle) {
            Box(
                modifier = Modifier
                    .size(boxSize)
                    .clip(RoundedCornerShape(2.dp))
                    .background(bgColor)
                    .border(1.dp, borderColor, RoundedCornerShape(2.dp)),
                contentAlignment = Alignment.Center,
            ) {
                if (indeterminate) {
                    Box(
                        modifier = Modifier
                            .size(width = boxSize - 4.dp, height = 2.dp)
                            .background(colorScheme.white),
                    )
                } else if (checked) {
                    NexusText(
                        text = "✓",
                        color = colorScheme.white,
                        style = NexusTheme.typography.extraSmall,
                    )
                }
            }
        }

        if (label != null) {
            _root_ide_package_.io.github.xingray.compose.nexus.foundation.ProvideContentColor(labelColor) {
                CompositionLocalProvider(
                    _root_ide_package_.io.github.xingray.compose.nexus.foundation.LocalTextStyle provides textStyle,
                ) {
                    label()
                }
            }
        }
    }
}
