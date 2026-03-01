package io.github.xingray.compose_nexus.controls

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import io.github.xingray.compose_nexus.foundation.ProvideContentColorTextStyle
import io.github.xingray.compose_nexus.theme.NexusTheme

// ============================================================================
// Dropdown state
// ============================================================================

@Stable
class DropdownState {
    var expanded: Boolean by mutableStateOf(false)

    fun open() { expanded = true }
    fun close() { expanded = false }
    fun toggle() { expanded = !expanded }
}

@Composable
fun rememberDropdownState(): DropdownState = remember { DropdownState() }

// ============================================================================
// NexusDropdown
// ============================================================================

/**
 * Element Plus Dropdown — a dropdown menu triggered by a composable.
 *
 * @param state Dropdown expanded state.
 * @param trigger The trigger composable (e.g., a button or link).
 * @param modifier Modifier.
 * @param content Dropdown menu content (use [NexusDropdownItem] inside).
 */
@Composable
fun NexusDropdown(
    state: DropdownState = rememberDropdownState(),
    modifier: Modifier = Modifier,
    trigger: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {
    val colorScheme = NexusTheme.colorScheme
    val shapes = NexusTheme.shapes
    val shadows = NexusTheme.shadows

    Box(modifier = modifier) {
        // Trigger - wrapped to capture click
        Box(
            modifier = Modifier.clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
            ) { state.toggle() },
        ) {
            trigger()
        }

        // Popup menu
        if (state.expanded) {
            Popup(
                onDismissRequest = { state.close() },
                properties = PopupProperties(focusable = true),
            ) {
                Column(
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .widthIn(min = 120.dp)
                        .shadow(shadows.light.elevation, shapes.base)
                        .clip(shapes.base)
                        .background(colorScheme.fill.blank)
                        .border(1.dp, colorScheme.border.lighter, shapes.base)
                        .padding(vertical = 6.dp),
                ) {
                    content()
                }
            }
        }
    }
}

// ============================================================================
// NexusDropdownItem
// ============================================================================

/**
 * Element Plus DropdownItem — a single item in a [NexusDropdown].
 *
 * @param text Item text.
 * @param onClick Click handler.
 * @param modifier Modifier.
 * @param disabled Disabled state.
 * @param divided Show a top divider above this item.
 */
@Composable
fun NexusDropdownItem(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    disabled: Boolean = false,
    divided: Boolean = false,
) {
    val colorScheme = NexusTheme.colorScheme
    val typography = NexusTheme.typography
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    if (divided) {
        NexusDivider(
            color = colorScheme.border.lighter,
            modifier = Modifier.padding(vertical = 6.dp),
        )
    }

    val bgColor = when {
        isHovered && !disabled -> colorScheme.primary.light9
        else -> Color.Transparent
    }
    val textColor = when {
        disabled -> colorScheme.disabled.text
        isHovered -> colorScheme.primary.base
        else -> colorScheme.text.regular
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(bgColor)
            .then(
                if (!disabled) {
                    Modifier
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null,
                            onClick = onClick,
                        )
                        .pointerHoverIcon(PointerIcon.Hand)
                } else Modifier
            )
            .padding(horizontal = 16.dp, vertical = 6.dp),
    ) {
        NexusText(
            text = text,
            color = textColor,
            style = typography.base,
        )
    }
}
