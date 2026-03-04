package io.github.xingray.compose_nexus.controls

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
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
    verticalSize: Dp = size,
    wrap: Boolean = false,
    alignment: Alignment.Vertical = Alignment.CenterVertically,
    content: @Composable () -> Unit,
) {
    if (direction == SpaceDirection.Horizontal && wrap) {
        FlowRow(
            modifier = modifier,
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(size),
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(verticalSize),
            content = { content() },
        )
        return
    }

    if (direction == SpaceDirection.Horizontal) {
        Row(
            modifier = modifier,
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(size),
            verticalAlignment = alignment,
            content = { content() },
        )
    } else {
        Column(
            modifier = modifier,
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(verticalSize),
            content = { content() },
        )
    }
}

/**
 * Item-list API that supports Element Plus `spacer` / `fill` / `fill-ratio` use cases.
 */
@Composable
fun NexusSpace(
    items: List<@Composable () -> Unit>,
    modifier: Modifier = Modifier,
    direction: SpaceDirection = SpaceDirection.Horizontal,
    spaceSize: NexusSpaceSize = NexusSpaceSize.Small,
    wrap: Boolean = false,
    alignment: Alignment.Vertical = Alignment.CenterVertically,
    spacer: NexusSpaceSpacer? = null,
    fill: Boolean = false,
    fillRatio: Int = 100,
) {
    if (items.isEmpty()) {
        return
    }

    val fraction = (fillRatio / 100f).coerceIn(0.01f, 1f)

    if (direction == SpaceDirection.Horizontal && wrap) {
        FlowRow(
            modifier = modifier,
            horizontalArrangement = if (spacer == null) {
                androidx.compose.foundation.layout.Arrangement.spacedBy(spaceSize.horizontal)
            } else {
                androidx.compose.foundation.layout.Arrangement.Start
            },
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(spaceSize.vertical),
        ) {
            items.forEachIndexed { index, item ->
                Box(
                    modifier = if (fill) Modifier.fillMaxWidth(fraction) else Modifier,
                ) {
                    item()
                }
                if (index < items.lastIndex && spacer != null) {
                    RenderSpaceSpacer(spacer = spacer, direction = direction, size = spaceSize)
                }
            }
        }
        return
    }

    if (direction == SpaceDirection.Horizontal) {
        Row(
            modifier = modifier,
            verticalAlignment = alignment,
        ) {
            items.forEachIndexed { index, item ->
                Box(
                    modifier = if (fill) Modifier.weight(fillRatio.coerceAtLeast(1).toFloat()) else Modifier,
                ) {
                    item()
                }
                if (index < items.lastIndex) {
                    if (spacer != null) {
                        RenderSpaceSpacer(spacer = spacer, direction = direction, size = spaceSize)
                    } else {
                        Spacer(modifier = Modifier.width(spaceSize.horizontal))
                    }
                }
            }
        }
    } else {
        Column(modifier = modifier) {
            items.forEachIndexed { index, item ->
                Box(
                    modifier = if (fill) Modifier.fillMaxWidth(fraction) else Modifier,
                ) {
                    item()
                }
                if (index < items.lastIndex) {
                    if (spacer != null) {
                        RenderSpaceSpacer(spacer = spacer, direction = direction, size = spaceSize)
                    } else {
                        Spacer(modifier = Modifier.height(spaceSize.vertical))
                    }
                }
            }
        }
    }
}

sealed interface NexusSpaceSpacer {
    data class Literal(val text: String) : NexusSpaceSpacer
    data class Numeric(val value: Number) : NexusSpaceSpacer
    class Content(val content: @Composable () -> Unit) : NexusSpaceSpacer
}

data class NexusSpaceSize(
    val horizontal: Dp,
    val vertical: Dp,
) {
    companion object {
        val Small = NexusSpaceSize(horizontal = 8.dp, vertical = 8.dp)
        val Default = NexusSpaceSize(horizontal = 12.dp, vertical = 12.dp)
        val Large = NexusSpaceSize(horizontal = 16.dp, vertical = 16.dp)

        fun uniform(size: Dp): NexusSpaceSize = NexusSpaceSize(horizontal = size, vertical = size)
    }
}

@Composable
private fun RenderSpaceSpacer(
    spacer: NexusSpaceSpacer,
    direction: SpaceDirection,
    size: NexusSpaceSize,
) {
    when (spacer) {
        is NexusSpaceSpacer.Literal -> NexusText(text = spacer.text, color = NexusTheme.colorScheme.text.secondary)
        is NexusSpaceSpacer.Numeric -> NexusText(text = spacer.value.toString(), color = NexusTheme.colorScheme.text.secondary)
        is NexusSpaceSpacer.Content -> spacer.content()
    }

    if (direction == SpaceDirection.Horizontal) {
        Spacer(modifier = Modifier.width(size.horizontal))
    } else {
        Spacer(modifier = Modifier.height(size.vertical))
    }
}

enum class SpaceDirection {
    Horizontal,
    Vertical,
}
