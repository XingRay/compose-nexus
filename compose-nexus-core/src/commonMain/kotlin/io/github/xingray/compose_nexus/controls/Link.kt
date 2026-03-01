package io.github.xingray.compose_nexus.controls

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import io.github.xingray.compose_nexus.theme.NexusTheme
import io.github.xingray.compose_nexus.theme.NexusType
import io.github.xingray.compose_nexus.theme.typeColor

/**
 * Element Plus Link — a text hyperlink with type color and underline on hover.
 *
 * @param text Link text.
 * @param onClick Click handler.
 * @param modifier Modifier.
 * @param type Semantic color type.
 * @param underline Whether to show underline on hover.
 * @param disabled Disabled state.
 */
@Composable
fun NexusLink(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    type: NexusType = NexusType.Default,
    underline: Boolean = true,
    disabled: Boolean = false,
) {
    val colorScheme = NexusTheme.colorScheme
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    val baseColor = when {
        disabled -> colorScheme.text.disabled
        type == NexusType.Default -> colorScheme.text.regular
        else -> colorScheme.typeColor(type)?.base ?: colorScheme.text.regular
    }
    val hoverColor = when {
        disabled -> colorScheme.text.disabled
        type == NexusType.Default -> colorScheme.primary.base
        else -> colorScheme.typeColor(type)?.light3 ?: colorScheme.primary.base
    }
    val textColor = if (isHovered && !disabled) hoverColor else baseColor
    val decoration = if (isHovered && underline && !disabled) TextDecoration.Underline else TextDecoration.None

    val style = NexusTheme.typography.base.merge(
        TextStyle(
            color = textColor,
            textDecoration = decoration,
        )
    )

    BasicText(
        text = text,
        modifier = modifier
            .then(
                if (!disabled) {
                    Modifier
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null,
                            onClick = onClick,
                        )
                        .pointerHoverIcon(PointerIcon.Hand)
                } else {
                    Modifier
                }
            ),
        style = style,
    )
}
