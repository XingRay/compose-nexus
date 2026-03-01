package io.github.xingray.compose_nexus.controls

import androidx.compose.foundation.background
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import io.github.xingray.compose_nexus.foundation.ProvideContentColorTextStyle
import io.github.xingray.compose_nexus.theme.NexusTheme

/**
 * Tooltip placement relative to the anchor.
 */
enum class TooltipPlacement {
    Top,
    Bottom,
    Left,
    Right,
}

/**
 * Tooltip visual theme.
 */
enum class TooltipTheme {
    Dark,
    Light,
}

/**
 * Element Plus Tooltip — a text hint that appears on hover.
 *
 * @param text Tooltip text to display.
 * @param modifier Modifier applied to the anchor wrapper.
 * @param placement Where the tooltip appears relative to the content.
 * @param theme Visual theme (dark background or light background).
 * @param content The anchor content that triggers the tooltip on hover.
 */
@Composable
fun NexusTooltip(
    text: String,
    modifier: Modifier = Modifier,
    placement: TooltipPlacement = TooltipPlacement.Top,
    theme: TooltipTheme = TooltipTheme.Dark,
    content: @Composable () -> Unit,
) {
    val colorScheme = NexusTheme.colorScheme
    val shapes = NexusTheme.shapes
    val typography = NexusTheme.typography

    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    val bgColor = when (theme) {
        TooltipTheme.Dark -> colorScheme.text.primary
        TooltipTheme.Light -> colorScheme.fill.blank
    }
    val textColor = when (theme) {
        TooltipTheme.Dark -> colorScheme.white
        TooltipTheme.Light -> colorScheme.text.regular
    }
    val borderColor = when (theme) {
        TooltipTheme.Dark -> Color.Transparent
        TooltipTheme.Light -> colorScheme.border.light
    }

    val popupAlignment = when (placement) {
        TooltipPlacement.Top -> Alignment.TopCenter
        TooltipPlacement.Bottom -> Alignment.BottomCenter
        TooltipPlacement.Left -> Alignment.CenterStart
        TooltipPlacement.Right -> Alignment.CenterEnd
    }

    Box(modifier = modifier.hoverable(interactionSource)) {
        content()

        if (isHovered && text.isNotEmpty()) {
            Popup(alignment = popupAlignment) {
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .shadow(NexusTheme.shadows.lighter.elevation, shapes.base)
                        .clip(shapes.base)
                        .background(bgColor)
                        .then(
                            if (borderColor != Color.Transparent) {
                                Modifier.background(bgColor)
                            } else Modifier
                        )
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                ) {
                    ProvideContentColorTextStyle(
                        contentColor = textColor,
                        textStyle = typography.extraSmall,
                    ) {
                        NexusText(
                            text = text,
                            color = textColor,
                            style = typography.extraSmall,
                        )
                    }
                }
            }
        }
    }
}
