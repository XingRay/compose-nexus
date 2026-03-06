package io.github.xingray.compose.nexus.controls

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import io.github.xingray.compose.nexus.theme.NexusTheme
import io.github.xingray.compose.nexus.theme.NexusType
import io.github.xingray.compose.nexus.theme.typeColor

enum class NexusLinkUnderline {
    Always,
    Hover,
    Never,
}

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
    type: io.github.xingray.compose.nexus.theme.NexusType = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusType.Default,
    underline: io.github.xingray.compose.nexus.controls.NexusLinkUnderline = _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusLinkUnderline.Hover,
    disabled: Boolean = false,
    icon: (@Composable () -> Unit)? = null,
) {
    val colorScheme = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.colorScheme
    val config = _root_ide_package_.io.github.xingray.compose.nexus.controls.LocalNexusConfig.current.link
    val resolvedType = if (type == _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusType.Default) config.type ?: type else type
    val resolvedUnderline = if (underline == _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusLinkUnderline.Hover) config.underline ?: underline else underline
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    val baseColor = when {
        disabled -> colorScheme.text.disabled
        resolvedType == _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusType.Default -> colorScheme.text.regular
        else -> colorScheme.typeColor(resolvedType)?.base ?: colorScheme.text.regular
    }
    val hoverColor = when {
        disabled -> colorScheme.text.disabled
        resolvedType == _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusType.Default -> colorScheme.primary.base
        else -> colorScheme.typeColor(resolvedType)?.light3 ?: colorScheme.primary.base
    }
    val textColor = if (isHovered && !disabled) hoverColor else baseColor
    val decoration = when {
        disabled -> TextDecoration.None
        resolvedUnderline == _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusLinkUnderline.Always -> TextDecoration.Underline
        resolvedUnderline == _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusLinkUnderline.Hover && isHovered -> TextDecoration.Underline
        else -> TextDecoration.None
    }

    val style = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.typography.base.merge(
        TextStyle(
            color = textColor,
            textDecoration = decoration,
        )
    )

    Row(
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
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        if (icon != null) {
            icon()
        }
        BasicText(
            text = text,
            style = style,
        )
    }
}
