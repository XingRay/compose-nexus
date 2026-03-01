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
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import io.github.xingray.compose_nexus.foundation.ProvideContentColor
import io.github.xingray.compose_nexus.theme.ComponentSize
import io.github.xingray.compose_nexus.theme.NexusTheme

// ============================================================================
// RadioGroup state
// ============================================================================

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
): RadioGroupState<T> = remember { RadioGroupState(initialSelected) }

internal val LocalRadioGroupState = compositionLocalOf<RadioGroupState<Any>?> { null }

/**
 * Element Plus RadioGroup — groups multiple radio buttons with shared selection state.
 */
@Composable
fun <T> NexusRadioGroup(
    state: RadioGroupState<T>,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    @Suppress("UNCHECKED_CAST")
    CompositionLocalProvider(
        LocalRadioGroupState provides (state as RadioGroupState<Any>),
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
// NexusRadio
// ============================================================================

/**
 * Element Plus Radio — a radio button with optional label.
 *
 * Can be used standalone or inside [NexusRadioGroup].
 *
 * @param selected Whether this radio is currently selected.
 * @param onClick Callback when clicked.
 * @param modifier Modifier.
 * @param size Component size.
 * @param disabled Disabled state.
 * @param label Optional label content.
 */
@Composable
fun NexusRadio(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: ComponentSize = ComponentSize.Default,
    disabled: Boolean = false,
    label: (@Composable () -> Unit)? = null,
) {
    val colorScheme = NexusTheme.colorScheme
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    val outerSize = when (size) {
        ComponentSize.Large -> 16.dp
        ComponentSize.Default -> 14.dp
        ComponentSize.Small -> 12.dp
    }
    val innerSize = outerSize - 6.dp

    val borderColor = when {
        disabled && selected -> colorScheme.primary.light5
        disabled -> colorScheme.disabled.border
        selected -> colorScheme.primary.base
        isHovered -> colorScheme.primary.base
        else -> colorScheme.border.base
    }
    val dotColor = when {
        disabled -> colorScheme.primary.light5
        else -> colorScheme.primary.base
    }
    val textColor = if (disabled) colorScheme.disabled.text else colorScheme.text.regular

    Row(
        modifier = modifier
            .then(
                if (!disabled) {
                    Modifier
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null,
                            role = Role.RadioButton,
                        ) { onClick() }
                        .pointerHoverIcon(PointerIcon.Hand)
                } else Modifier
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        // Radio circle
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

        // Label
        if (label != null) {
            ProvideContentColor(textColor) {
                label()
            }
        }
    }
}
