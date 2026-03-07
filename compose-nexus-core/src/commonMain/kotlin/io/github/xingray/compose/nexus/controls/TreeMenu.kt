package io.github.xingray.compose.nexus.controls

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.hoverable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import io.github.xingray.compose.nexus.theme.NexusTheme

/**
 * Element Plus TreeMenu — a vertical tree-shaped navigation menu.
 *
 * Combines Menu and Tree patterns for hierarchical navigation with icons.
 *
 * @param items Menu items with optional children.
 * @param state Menu state for active/open tracking.
 * @param modifier Modifier.
 * @param onSelect Callback when a leaf item is selected.
 */
@Composable
fun NexusTreeMenu(
    items: List<MenuItem>,
    state: MenuState = rememberMenuState(),
    modifier: Modifier = Modifier,
    onSelect: ((String) -> Unit)? = null,
) {
    val colorScheme = NexusTheme.colorScheme

    Column(
        modifier = modifier
            .defaultMinSize(minWidth = 200.dp)
            .background(colorScheme.fill.blank),
    ) {
        items.forEach { item ->
            TreeMenuItem(
                item = item,
                state = state,
                depth = 0,
                onSelect = onSelect,
            )
        }
    }
}

@Composable
private fun TreeMenuItem(
    item: MenuItem,
    state: MenuState,
    depth: Int,
    onSelect: ((String) -> Unit)?,
) {
    val colorScheme = NexusTheme.colorScheme
    val typography = NexusTheme.typography
    val hasChildren = item.children.isNotEmpty()
    val isOpen = state.isOpen(item.key)
    val isActive = state.activeKey == item.key

    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    val bgColor = when {
        isActive && !hasChildren -> colorScheme.primary.light9
        isHovered -> colorScheme.fill.light
        else -> Color.Transparent
    }
    val textColor = when {
        item.disabled -> colorScheme.text.disabled
        isActive && !hasChildren -> colorScheme.primary.base
        isHovered -> colorScheme.primary.base
        else -> colorScheme.text.primary
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 36.dp)
            .hoverable(interactionSource)
            .background(bgColor)
            .then(
                if (!item.disabled) {
                    Modifier
                        .clickable {
                            if (hasChildren) {
                                state.toggleOpen(item.key)
                            } else {
                                state.select(item.key)
                                onSelect?.invoke(item.key)
                            }
                        }
                        .pointerHoverIcon(PointerIcon.Hand)
                } else Modifier
            )
            .padding(
                start = (depth * 16 + 16).dp,
                end = 16.dp,
                top = 8.dp,
                bottom = 8.dp,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (hasChildren) {
            NexusText(
                text = if (isOpen) "▾" else "▸",
                color = colorScheme.text.placeholder,
                style = typography.extraSmall,
                modifier = Modifier.padding(end = 6.dp),
            )
        }

        NexusText(
            text = item.label,
            color = textColor,
            style = typography.base,
            modifier = Modifier.weight(1f),
        )
    }

    if (hasChildren) {
        AnimatedVisibility(
            visible = isOpen,
            enter = expandVertically(),
            exit = shrinkVertically(),
        ) {
            Column {
                item.children.forEach { child ->
                    TreeMenuItem(
                        item = child,
                        state = state,
                        depth = depth + 1,
                        onSelect = onSelect,
                    )
                }
            }
        }
    }
}
