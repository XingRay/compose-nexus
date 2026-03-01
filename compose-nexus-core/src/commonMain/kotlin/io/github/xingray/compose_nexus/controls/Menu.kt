package io.github.xingray.compose_nexus.controls

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.hoverable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import io.github.xingray.compose_nexus.theme.NexusTheme

/**
 * Menu mode.
 */
enum class MenuMode {
    Horizontal,
    Vertical,
}

/**
 * A menu item definition.
 */
data class MenuItem(
    val key: String,
    val label: String,
    val disabled: Boolean = false,
    val children: List<MenuItem> = emptyList(),
)

/**
 * Menu state holder.
 */
@Stable
class MenuState(
    initialActiveKey: String = "",
) {
    var activeKey by mutableStateOf(initialActiveKey)
        private set
    internal val openKeys = mutableStateMapOf<String, Boolean>()

    fun select(key: String) {
        activeKey = key
    }

    fun isOpen(key: String): Boolean = openKeys[key] == true

    fun toggleOpen(key: String) {
        openKeys[key] = !(openKeys[key] ?: false)
    }
}

@Composable
fun rememberMenuState(
    initialActiveKey: String = "",
): MenuState = remember { MenuState(initialActiveKey) }

/**
 * Element Plus Menu — a navigation menu (horizontal or vertical).
 *
 * @param items Menu items, supporting nested sub-menus.
 * @param state Menu state for active/open tracking.
 * @param modifier Modifier.
 * @param mode Horizontal or Vertical layout.
 * @param onSelect Callback when a leaf menu item is selected.
 */
@Composable
fun NexusMenu(
    items: List<MenuItem>,
    state: MenuState = rememberMenuState(),
    modifier: Modifier = Modifier,
    mode: MenuMode = MenuMode.Vertical,
    onSelect: ((String) -> Unit)? = null,
) {
    val colorScheme = NexusTheme.colorScheme

    when (mode) {
        MenuMode.Horizontal -> {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 56.dp)
                    .background(colorScheme.fill.blank),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                items.forEach { item ->
                    HorizontalMenuItemComposable(
                        item = item,
                        state = state,
                        onSelect = onSelect,
                    )
                }
            }
        }
        MenuMode.Vertical -> {
            Column(
                modifier = modifier
                    .defaultMinSize(minWidth = 200.dp)
                    .background(colorScheme.fill.blank),
            ) {
                items.forEach { item ->
                    VerticalMenuItemComposable(
                        item = item,
                        state = state,
                        depth = 0,
                        onSelect = onSelect,
                    )
                }
            }
        }
    }
}

@Composable
private fun HorizontalMenuItemComposable(
    item: MenuItem,
    state: MenuState,
    onSelect: ((String) -> Unit)?,
) {
    val colorScheme = NexusTheme.colorScheme
    val typography = NexusTheme.typography
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val isActive = state.activeKey == item.key

    val textColor = when {
        item.disabled -> colorScheme.text.disabled
        isActive -> colorScheme.primary.base
        isHovered -> colorScheme.primary.base
        else -> colorScheme.text.primary
    }
    val borderColor = if (isActive) colorScheme.primary.base else Color.Transparent

    Column(
        modifier = Modifier
            .hoverable(interactionSource)
            .then(
                if (!item.disabled && item.children.isEmpty()) {
                    Modifier
                        .clickable {
                            state.select(item.key)
                            onSelect?.invoke(item.key)
                        }
                        .pointerHoverIcon(PointerIcon.Hand)
                } else Modifier
            )
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier.defaultMinSize(minHeight = 56.dp),
            contentAlignment = Alignment.Center,
        ) {
            NexusText(
                text = item.label,
                color = textColor,
                style = typography.base,
            )
        }
        // Active indicator line
        Box(
            modifier = Modifier
                .height(2.dp)
                .fillMaxWidth()
                .background(borderColor),
        )
    }
}

@Composable
private fun VerticalMenuItemComposable(
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

    // Menu item row
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 40.dp)
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
                start = (depth * 20 + 20).dp,
                end = 20.dp,
                top = 10.dp,
                bottom = 10.dp,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        NexusText(
            text = item.label,
            color = textColor,
            style = typography.base,
            modifier = Modifier.weight(1f),
        )
        if (hasChildren) {
            NexusText(
                text = if (isOpen) "▾" else "▸",
                color = colorScheme.text.placeholder,
                style = typography.extraSmall,
            )
        }
    }

    // Sub-menu items (animated)
    if (hasChildren) {
        AnimatedVisibility(
            visible = isOpen,
            enter = expandVertically(),
            exit = shrinkVertically(),
        ) {
            Column {
                item.children.forEach { child ->
                    VerticalMenuItemComposable(
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
