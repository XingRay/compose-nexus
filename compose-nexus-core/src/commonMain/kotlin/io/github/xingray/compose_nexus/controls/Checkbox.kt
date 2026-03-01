package io.github.xingray.compose_nexus.controls

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
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
import io.github.xingray.compose_nexus.theme.ComponentSize
import io.github.xingray.compose_nexus.theme.NexusTheme

// ============================================================================
// CheckboxGroup state
// ============================================================================

@Stable
class CheckboxGroupState<T>(
    initialSelected: Set<T> = emptySet(),
) {
    var selected: Set<T> by mutableStateOf(initialSelected)

    fun isChecked(value: T): Boolean = value in selected

    fun toggle(value: T) {
        selected = if (value in selected) selected - value else selected + value
    }
}

@Composable
fun <T> rememberCheckboxGroupState(
    initialSelected: Set<T> = emptySet(),
): CheckboxGroupState<T> = remember { CheckboxGroupState(initialSelected) }

internal val LocalCheckboxGroupState = compositionLocalOf<CheckboxGroupState<Any>?> { null }

/**
 * Element Plus CheckboxGroup — groups multiple checkboxes with shared selection state.
 */
@Composable
fun <T> NexusCheckboxGroup(
    state: CheckboxGroupState<T>,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    @Suppress("UNCHECKED_CAST")
    CompositionLocalProvider(
        LocalCheckboxGroupState provides (state as CheckboxGroupState<Any>),
    ) {
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            content()
        }
    }
}

// ============================================================================
// NexusCheckbox
// ============================================================================

/**
 * Element Plus Checkbox — a check box with optional label.
 *
 * Can be used standalone or inside [NexusCheckboxGroup].
 *
 * @param checked Whether the checkbox is checked.
 * @param onCheckedChange Callback when check state changes.
 * @param modifier Modifier.
 * @param size Component size.
 * @param disabled Disabled state.
 * @param indeterminate Show indeterminate (half-check) state.
 * @param label Optional label content.
 */
@Composable
fun NexusCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    size: ComponentSize = ComponentSize.Default,
    disabled: Boolean = false,
    indeterminate: Boolean = false,
    label: (@Composable () -> Unit)? = null,
) {
    val colorScheme = NexusTheme.colorScheme
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    val boxSize = when (size) {
        ComponentSize.Large -> 16.dp
        ComponentSize.Default -> 14.dp
        ComponentSize.Small -> 12.dp
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
        disabled && isActive -> colorScheme.primary.light5
        isActive -> colorScheme.primary.base
        else -> colorScheme.fill.blank
    }
    val checkColor = colorScheme.white
    val textColor = if (disabled) colorScheme.disabled.text else colorScheme.text.regular

    Row(
        modifier = modifier
            .then(
                if (!disabled) {
                    Modifier
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null,
                            role = Role.Checkbox,
                        ) { onCheckedChange(!checked) }
                        .pointerHoverIcon(PointerIcon.Hand)
                } else Modifier
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        // Checkbox box
        Box(
            modifier = Modifier
                .size(boxSize)
                .clip(RoundedCornerShape(2.dp))
                .background(bgColor)
                .border(1.dp, borderColor, RoundedCornerShape(2.dp)),
            contentAlignment = Alignment.Center,
        ) {
            if (indeterminate) {
                // Indeterminate: horizontal line
                Box(
                    modifier = Modifier
                        .size(width = boxSize - 4.dp, height = 2.dp)
                        .background(checkColor),
                )
            } else if (checked) {
                // Checkmark: simple "✓" text
                NexusText(
                    text = "✓",
                    color = checkColor,
                    style = NexusTheme.typography.extraSmall,
                )
            }
        }

        // Label
        if (label != null) {
            io.github.xingray.compose_nexus.foundation.ProvideContentColor(textColor) {
                label()
            }
        }
    }
}
