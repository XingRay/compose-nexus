package io.github.xingray.compose_nexus.containers

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import io.github.xingray.compose_nexus.controls.NexusDivider
import io.github.xingray.compose_nexus.foundation.ProvideContentColorTextStyle
import io.github.xingray.compose_nexus.theme.NexusTheme

/**
 * Element Plus Card — a container with optional header, border, and shadow.
 *
 * @param modifier Modifier.
 * @param shadow Shadow display mode: Always / Hover / Never.
 * @param header Optional header content. A divider is drawn below the header.
 * @param content Card body content.
 */
@Composable
fun NexusCard(
    modifier: Modifier = Modifier,
    shadow: CardShadow = CardShadow.Always,
    header: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    val colorScheme = NexusTheme.colorScheme
    val shapes = NexusTheme.shapes
    val shadows = NexusTheme.shadows
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    val elevation = when (shadow) {
        CardShadow.Always -> shadows.light.elevation
        CardShadow.Hover -> if (isHovered) shadows.light.elevation else 0.dp
        CardShadow.Never -> 0.dp
    }

    Column(
        modifier = modifier
            .hoverable(interactionSource)
            .shadow(elevation, shapes.base)
            .clip(shapes.base)
            .background(colorScheme.fill.blank)
            .border(1.dp, colorScheme.border.lighter, shapes.base),
    ) {
        // Header
        if (header != null) {
            ProvideContentColorTextStyle(
                contentColor = colorScheme.text.primary,
                textStyle = NexusTheme.typography.base,
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 18.dp),
                ) {
                    header()
                }
            }
            NexusDivider(color = colorScheme.border.lighter)
        }

        // Body
        ProvideContentColorTextStyle(
            contentColor = colorScheme.text.regular,
            textStyle = NexusTheme.typography.base,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
            ) {
                content()
            }
        }
    }
}

enum class CardShadow {
    Always,
    Hover,
    Never,
}
