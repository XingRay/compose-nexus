package io.github.xingray.compose_nexus.controls

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import io.github.xingray.compose_nexus.foundation.ProvideContentColorTextStyle
import io.github.xingray.compose_nexus.theme.ComponentSize
import io.github.xingray.compose_nexus.theme.NexusTheme

// ============================================================================
// Select option
// ============================================================================

data class SelectOption<T>(
    val value: T,
    val label: String,
    val disabled: Boolean = false,
)

// ============================================================================
// Select state
// ============================================================================

@Stable
class SelectState<T>(
    initialSelected: T? = null,
) {
    var selected: T? by mutableStateOf(initialSelected)
    var expanded: Boolean by mutableStateOf(false)
}

@Composable
fun <T> rememberSelectState(
    initialSelected: T? = null,
): SelectState<T> = remember { SelectState(initialSelected) }

// ============================================================================
// NexusSelect
// ============================================================================

/**
 * Element Plus Select — a dropdown selector.
 *
 * @param state Select state (selected value + expanded).
 * @param options List of selectable options.
 * @param onSelect Callback when an option is selected.
 * @param modifier Modifier.
 * @param size Component size.
 * @param placeholder Placeholder when nothing is selected.
 * @param disabled Disabled state.
 * @param clearable Show clear button to deselect.
 */
@Composable
fun <T> NexusSelect(
    state: SelectState<T>,
    options: List<SelectOption<T>>,
    onSelect: (T) -> Unit,
    modifier: Modifier = Modifier,
    size: ComponentSize = ComponentSize.Default,
    placeholder: String = "Select",
    disabled: Boolean = false,
    clearable: Boolean = false,
) {
    val colorScheme = NexusTheme.colorScheme
    val shapes = NexusTheme.shapes
    val sizes = NexusTheme.sizes
    val typography = NexusTheme.typography
    val shadows = NexusTheme.shadows

    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

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
    val textStyle = when (size) {
        ComponentSize.Large -> typography.base
        ComponentSize.Default -> typography.base
        ComponentSize.Small -> typography.extraSmall
    }

    val borderColor = when {
        disabled -> colorScheme.disabled.border
        state.expanded -> colorScheme.primary.base
        isHovered -> colorScheme.border.dark
        else -> colorScheme.border.base
    }
    val bgColor = if (disabled) colorScheme.disabled.background else colorScheme.fill.blank
    val selectedLabel = options.find { it.value == state.selected }?.label

    Box(modifier = modifier) {
        // Trigger
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = height)
                .clip(shapes.base)
                .background(bgColor)
                .border(1.dp, borderColor, shapes.base)
                .then(
                    if (!disabled) {
                        Modifier
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null,
                            ) { state.expanded = !state.expanded }
                            .pointerHoverIcon(PointerIcon.Hand)
                    } else Modifier
                )
                .padding(horizontal = horizontalPadding),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Selected text or placeholder
            Box(modifier = Modifier.weight(1f)) {
                NexusText(
                    text = selectedLabel ?: placeholder,
                    color = if (selectedLabel != null) colorScheme.text.regular else colorScheme.text.placeholder,
                    style = textStyle,
                )
            }

            // Clear button
            if (clearable && state.selected != null && !disabled) {
                Box(
                    modifier = Modifier
                        .padding(end = 4.dp)
                        .size(16.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                        ) {
                            state.selected = null
                            onSelect as? ((Nothing?) -> Unit) // won't call, just clear
                        },
                    contentAlignment = Alignment.Center,
                ) {
                    NexusText(
                        text = "✕",
                        color = colorScheme.text.placeholder,
                        style = typography.extraSmall,
                    )
                }
            }

            // Arrow indicator
            NexusText(
                text = if (state.expanded) "▲" else "▼",
                color = colorScheme.text.placeholder,
                style = typography.extraSmall,
            )
        }

        // Dropdown popup
        if (state.expanded) {
            Popup(
                alignment = Alignment.TopStart,
                onDismissRequest = { state.expanded = false },
                properties = PopupProperties(focusable = true),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                        .shadow(shadows.light.elevation, shapes.base)
                        .clip(shapes.base)
                        .background(colorScheme.fill.blank)
                        .border(1.dp, colorScheme.border.lighter, shapes.base)
                        .heightIn(max = 274.dp)
                        .verticalScroll(rememberScrollState()),
                ) {
                    options.forEach { option ->
                        val optionInteraction = remember { MutableInteractionSource() }
                        val optionHovered by optionInteraction.collectIsHoveredAsState()
                        val isSelected = option.value == state.selected

                        val optionBg = when {
                            isSelected -> colorScheme.primary.light9
                            optionHovered && !option.disabled -> colorScheme.fill.light
                            else -> Color.Transparent
                        }
                        val optionTextColor = when {
                            option.disabled -> colorScheme.disabled.text
                            isSelected -> colorScheme.primary.base
                            else -> colorScheme.text.regular
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(optionBg)
                                .then(
                                    if (!option.disabled) {
                                        Modifier
                                            .clickable(
                                                interactionSource = optionInteraction,
                                                indication = null,
                                            ) {
                                                state.selected = option.value
                                                state.expanded = false
                                                onSelect(option.value)
                                            }
                                            .pointerHoverIcon(PointerIcon.Hand)
                                    } else Modifier
                                )
                                .padding(horizontal = horizontalPadding, vertical = 8.dp),
                            contentAlignment = Alignment.CenterStart,
                        ) {
                            NexusText(
                                text = option.label,
                                color = optionTextColor,
                                style = textStyle,
                            )
                        }
                    }
                }
            }
        }
    }
}
