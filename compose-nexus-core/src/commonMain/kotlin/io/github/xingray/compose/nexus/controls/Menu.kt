package io.github.xingray.compose.nexus.controls

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import io.github.xingray.compose.nexus.theme.NexusTheme

enum class MenuMode {
    Horizontal,
    Vertical,
}

enum class MenuTrigger {
    Hover,
    Click,
}

data class MenuItem(
    val key: String,
    val label: String,
    val disabled: Boolean = false,
    val route: Any? = null,
    val children: List<MenuItem> = emptyList(),
)

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

    fun open(index: String) {
        openKeys[index] = true
    }

    fun close(index: String) {
        openKeys[index] = false
    }

    fun updateActiveIndex(index: String) {
        activeKey = index
    }

    fun handleResize() {
        // API parity placeholder.
    }
}

@Composable
fun rememberMenuState(
    initialActiveKey: String = "",
    defaultOpeneds: List<String> = emptyList(),
): io.github.xingray.compose.nexus.controls.MenuState {
    val state = remember { _root_ide_package_.io.github.xingray.compose.nexus.controls.MenuState(initialActiveKey) }
    remember(defaultOpeneds) {
        defaultOpeneds.forEach { state.open(it) }
        true
    }
    return state
}

@Composable
fun NexusMenu(
    items: List<io.github.xingray.compose.nexus.controls.MenuItem>,
    state: io.github.xingray.compose.nexus.controls.MenuState = _root_ide_package_.io.github.xingray.compose.nexus.controls.rememberMenuState(),
    modifier: Modifier = Modifier,
    mode: io.github.xingray.compose.nexus.controls.MenuMode = _root_ide_package_.io.github.xingray.compose.nexus.controls.MenuMode.Vertical,
    collapse: Boolean = false,
    defaultOpeneds: List<String> = emptyList(),
    uniqueOpened: Boolean = false,
    menuTrigger: io.github.xingray.compose.nexus.controls.MenuTrigger = _root_ide_package_.io.github.xingray.compose.nexus.controls.MenuTrigger.Hover,
    backgroundColor: Color? = null,
    textColor: Color? = null,
    activeTextColor: Color? = null,
    onSelect: ((index: String, indexPath: List<String>) -> Unit)? = null,
    onOpen: ((index: String, indexPath: List<String>) -> Unit)? = null,
    onClose: ((index: String, indexPath: List<String>) -> Unit)? = null,
) {
    val colorScheme = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.colorScheme
    val menuBg = backgroundColor ?: colorScheme.fill.blank
    val siblings = items.map { it.key }

    remember(defaultOpeneds, items) {
        defaultOpeneds.forEach { state.open(it) }
        true
    }

    when (mode) {
        _root_ide_package_.io.github.xingray.compose.nexus.controls.MenuMode.Horizontal -> {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 56.dp)
                    .background(menuBg),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                items.forEach { item ->
                    _root_ide_package_.io.github.xingray.compose.nexus.controls.HorizontalMenuItemComposable(
                        item = item,
                        state = state,
                        path = listOf(item.key),
                        menuTrigger = menuTrigger,
                        textColor = textColor,
                        activeTextColor = activeTextColor,
                        onSelect = onSelect,
                    )
                }
            }
        }
        _root_ide_package_.io.github.xingray.compose.nexus.controls.MenuMode.Vertical -> {
            Column(
                modifier = modifier
                    .defaultMinSize(minWidth = if (collapse) 64.dp else 200.dp)
                    .background(menuBg),
            ) {
                items.forEach { item ->
                    _root_ide_package_.io.github.xingray.compose.nexus.controls.VerticalMenuItemComposable(
                        item = item,
                        state = state,
                        depth = 0,
                        path = listOf(item.key),
                        siblingsAtLevel = siblings,
                        collapse = collapse,
                        uniqueOpened = uniqueOpened,
                        textColor = textColor,
                        activeTextColor = activeTextColor,
                        onSelect = onSelect,
                        onOpen = onOpen,
                        onClose = onClose,
                    )
                }
            }
        }
    }
}

@Composable
private fun HorizontalMenuItemComposable(
    item: io.github.xingray.compose.nexus.controls.MenuItem,
    state: io.github.xingray.compose.nexus.controls.MenuState,
    path: List<String>,
    menuTrigger: io.github.xingray.compose.nexus.controls.MenuTrigger,
    textColor: Color?,
    activeTextColor: Color?,
    onSelect: ((String, List<String>) -> Unit)?,
) {
    val colorScheme = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.colorScheme
    val typography = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.typography
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val isActive = state.activeKey == item.key
    val hasChildren = item.children.isNotEmpty()

    val normalColor = textColor ?: colorScheme.text.primary
    val activeColor = activeTextColor ?: colorScheme.primary.base
    val resolvedTextColor = when {
        item.disabled -> colorScheme.text.disabled
        isActive -> activeColor
        isHovered -> activeColor
        else -> normalColor
    }
    val borderColor = if (isActive) activeColor else Color.Transparent

    @Composable
    fun TriggerContent() {
        Column(
            modifier = Modifier
                .hoverable(interactionSource)
                .then(
                    if (!item.disabled && !hasChildren) {
                        Modifier
                            .clickable {
                                state.select(item.key)
                                onSelect?.invoke(item.key, path)
                            }
                            .pointerHoverIcon(PointerIcon.Hand)
                    } else {
                        Modifier
                    }
                )
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier.defaultMinSize(minHeight = 56.dp),
                contentAlignment = Alignment.Center,
            ) {
                _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
                    text = item.label,
                    color = resolvedTextColor,
                    style = typography.base,
                )
            }
            Box(
                modifier = Modifier
                    .height(2.dp)
                    .fillMaxWidth()
                    .background(borderColor),
            )
        }
    }

    if (hasChildren && !item.disabled) {
        val dropdownState = _root_ide_package_.io.github.xingray.compose.nexus.controls.rememberDropdownState()
        _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusDropdown(
            state = dropdownState,
            triggerMode = if (menuTrigger == _root_ide_package_.io.github.xingray.compose.nexus.controls.MenuTrigger.Hover) _root_ide_package_.io.github.xingray.compose.nexus.controls.DropdownTrigger.Hover else _root_ide_package_.io.github.xingray.compose.nexus.controls.DropdownTrigger.Click,
            placement = _root_ide_package_.io.github.xingray.compose.nexus.controls.DropdownPlacement.BottomStart,
            trigger = { TriggerContent() },
            onCommand = { command ->
                val key = command?.toString() ?: return@NexusDropdown
                state.select(key)
                val childPath = path + key
                onSelect?.invoke(key, childPath)
            },
        ) {
            item.children.forEach { child ->
                _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusDropdownItem(
                    text = child.label,
                    command = child.key,
                    disabled = child.disabled,
                )
            }
        }
    } else {
        TriggerContent()
    }
}

@Composable
private fun VerticalMenuItemComposable(
    item: io.github.xingray.compose.nexus.controls.MenuItem,
    state: io.github.xingray.compose.nexus.controls.MenuState,
    depth: Int,
    path: List<String>,
    siblingsAtLevel: List<String>,
    collapse: Boolean,
    uniqueOpened: Boolean,
    textColor: Color?,
    activeTextColor: Color?,
    onSelect: ((String, List<String>) -> Unit)?,
    onOpen: ((String, List<String>) -> Unit)?,
    onClose: ((String, List<String>) -> Unit)?,
) {
    if (collapse && depth > 0) return

    val colorScheme = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.colorScheme
    val typography = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.typography
    val hasChildren = item.children.isNotEmpty()
    val isOpen = state.isOpen(item.key)
    val isActive = state.activeKey == item.key

    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    val normalColor = textColor ?: colorScheme.text.primary
    val activeColor = activeTextColor ?: colorScheme.primary.base
    val bgColor = when {
        isActive && !hasChildren -> colorScheme.primary.light9
        isHovered -> colorScheme.fill.light
        else -> Color.Transparent
    }
    val resolvedTextColor = when {
        item.disabled -> colorScheme.text.disabled
        isActive && !hasChildren -> activeColor
        isHovered -> activeColor
        else -> normalColor
    }

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
                                val willOpen = !isOpen
                                if (willOpen) {
                                    if (uniqueOpened) {
                                        siblingsAtLevel.forEach { sibling ->
                                            if (sibling != item.key) state.close(sibling)
                                        }
                                    }
                                    state.open(item.key)
                                    onOpen?.invoke(item.key, path)
                                } else {
                                    state.close(item.key)
                                    onClose?.invoke(item.key, path)
                                }
                            } else {
                                state.select(item.key)
                                onSelect?.invoke(item.key, path)
                            }
                        }
                        .pointerHoverIcon(PointerIcon.Hand)
                } else {
                    Modifier
                }
            )
            .padding(
                start = (depth * 20 + 20).dp,
                end = 20.dp,
                top = 10.dp,
                bottom = 10.dp,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
            text = if (collapse) item.label.take(1) else item.label,
            color = resolvedTextColor,
            style = typography.base,
            modifier = Modifier.weight(1f),
        )
        if (hasChildren && !collapse) {
            _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
                text = if (isOpen) "▾" else "▸",
                color = colorScheme.text.placeholder,
                style = typography.extraSmall,
            )
        }
    }

    if (!collapse && hasChildren) {
        AnimatedVisibility(
            visible = isOpen,
            enter = expandVertically(),
            exit = shrinkVertically(),
        ) {
            Column {
                val childSiblings = item.children.map { it.key }
                item.children.forEach { child ->
                    VerticalMenuItemComposable(
                        item = child,
                        state = state,
                        depth = depth + 1,
                        path = path + child.key,
                        siblingsAtLevel = childSiblings,
                        collapse = collapse,
                        uniqueOpened = uniqueOpened,
                        textColor = textColor,
                        activeTextColor = activeTextColor,
                        onSelect = onSelect,
                        onOpen = onOpen,
                        onClose = onClose,
                    )
                }
            }
        }
    }
}

