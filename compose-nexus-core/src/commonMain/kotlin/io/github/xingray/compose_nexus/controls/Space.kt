package io.github.xingray.compose_nexus.controls

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.xingray.compose_nexus.theme.NexusTheme

/**
 * Element Plus Space — a layout container that adds uniform spacing between children.
 *
 * @param direction Layout direction (horizontal or vertical).
 * @param size Gap size between items. Defaults to [NexusTheme.sizes.spacingSmall] (8dp).
 * @param wrap Whether items should wrap to the next line (horizontal only).
 * @param alignment Alignment of items on the cross axis.
 * @param modifier Modifier for the container.
 * @param content Child composables to space out.
 */
@Composable
fun NexusSpace(
    modifier: Modifier = Modifier,
    direction: SpaceDirection = SpaceDirection.Horizontal,
    size: Dp = NexusTheme.sizes.spacingSmall,
    wrap: Boolean = false,
    alignment: Alignment.Vertical = Alignment.CenterVertically,
    content: @Composable () -> Unit,
) {
    if (direction == SpaceDirection.Horizontal && wrap) {
        FlowRow(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(size),
            verticalArrangement = Arrangement.spacedBy(size),
            content = { content() },
        )
    } else if (direction == SpaceDirection.Horizontal) {
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(size),
            verticalAlignment = alignment,
            content = { content() },
        )
    } else {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(size),
            content = { content() },
        )
    }
}

enum class SpaceDirection {
    Horizontal,
    Vertical,
}
