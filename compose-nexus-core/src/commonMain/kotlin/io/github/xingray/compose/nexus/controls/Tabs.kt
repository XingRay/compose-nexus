package io.github.xingray.compose.nexus.controls

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import io.github.xingray.compose.nexus.theme.NexusTheme

@Stable
class TabsState(initialSelected: Int = 0) {
    var selectedIndex: Int by mutableIntStateOf(initialSelected)
}

@Composable
fun rememberTabsState(initialSelected: Int = 0): io.github.xingray.compose.nexus.controls.TabsState =
    remember { _root_ide_package_.io.github.xingray.compose.nexus.controls.TabsState(initialSelected) }

data class TabItem(
    val label: String,
    val name: String = label,
    val disabled: Boolean = false,
    val closable: Boolean = false,
)

enum class TabsType {
    Default,
    Card,
    BorderCard,
}

enum class TabsPosition {
    Top,
    Right,
    Bottom,
    Left,
}

enum class TabsEditAction {
    Add,
    Remove,
}

@Composable
fun NexusTabs(
    state: io.github.xingray.compose.nexus.controls.TabsState,
    items: List<io.github.xingray.compose.nexus.controls.TabItem>,
    modifier: Modifier = Modifier,
    type: io.github.xingray.compose.nexus.controls.TabsType = _root_ide_package_.io.github.xingray.compose.nexus.controls.TabsType.Default,
    tabPosition: io.github.xingray.compose.nexus.controls.TabsPosition = _root_ide_package_.io.github.xingray.compose.nexus.controls.TabsPosition.Top,
    closable: Boolean = false,
    addable: Boolean = false,
    editable: Boolean = false,
    stretch: Boolean = false,
    beforeLeave: ((newIndex: Int, oldIndex: Int) -> Boolean)? = null,
    onTabClick: ((index: Int) -> Unit)? = null,
    onTabChange: ((index: Int) -> Unit)? = null,
    onTabRemove: ((index: Int) -> Unit)? = null,
    onTabAdd: (() -> Unit)? = null,
    onEdit: ((index: Int?, action: io.github.xingray.compose.nexus.controls.TabsEditAction) -> Unit)? = null,
    addIcon: (@Composable () -> Unit)? = null,
    content: @Composable (selectedIndex: Int) -> Unit,
) {
    val colorScheme = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.colorScheme
    val shapes = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.shapes
    val horizontal = tabPosition == _root_ide_package_.io.github.xingray.compose.nexus.controls.TabsPosition.Top || tabPosition == _root_ide_package_.io.github.xingray.compose.nexus.controls.TabsPosition.Bottom

    fun switchTo(index: Int) {
        if (index !in items.indices) return
        val oldIndex = state.selectedIndex
        if (oldIndex == index) {
            onTabClick?.invoke(index)
            return
        }
        val allowed = beforeLeave?.invoke(index, oldIndex) ?: true
        if (!allowed) return
        state.selectedIndex = index
        onTabClick?.invoke(index)
        onTabChange?.invoke(index)
    }

    @Composable
    fun TabsBar() {
        val barModifier = when {
            horizontal -> Modifier.fillMaxWidth()
            else -> Modifier.fillMaxHeight()
        }
        val containerModifier = when {
            type == _root_ide_package_.io.github.xingray.compose.nexus.controls.TabsType.BorderCard -> barModifier.border(1.dp, colorScheme.border.light, shapes.base).clip(shapes.base)
            type == _root_ide_package_.io.github.xingray.compose.nexus.controls.TabsType.Card -> barModifier.border(1.dp, colorScheme.border.light, shapes.base).clip(shapes.base)
            else -> barModifier
        }

        if (horizontal) {
            Row(
                modifier = containerModifier,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                items.forEachIndexed { index, item ->
                    val selected = state.selectedIndex == index
                    val interactionSource = remember { MutableInteractionSource() }
                    val hovered by interactionSource.collectIsHoveredAsState()
                    val textColor = when {
                        item.disabled -> colorScheme.disabled.text
                        selected -> colorScheme.primary.base
                        hovered -> colorScheme.primary.base
                        else -> colorScheme.text.regular
                    }
                    val background = when {
                        type == _root_ide_package_.io.github.xingray.compose.nexus.controls.TabsType.Default -> colorScheme.fill.blank
                        selected -> colorScheme.fill.blank
                        else -> colorScheme.fill.light
                    }
                    Row(
                        modifier = Modifier
                            .then(if (stretch) Modifier.weight(1f) else Modifier)
                            .background(background)
                            .then(
                                if (!item.disabled) {
                                    Modifier
                                        .clickable(
                                            interactionSource = interactionSource,
                                            indication = null,
                                        ) { switchTo(index) }
                                        .pointerHoverIcon(PointerIcon.Hand)
                                } else {
                                    Modifier
                                }
                            )
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
                            text = item.label,
                            color = textColor,
                            style = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.typography.base
                        )
                        val canClose = (editable || closable || item.closable) && !item.disabled
                        if (canClose) {
                            _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
                                text = "×",
                                color = colorScheme.text.placeholder,
                                style = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.typography.small,
                                modifier = Modifier
                                    .clickable {
                                        onTabRemove?.invoke(index)
                                        onEdit?.invoke(index, _root_ide_package_.io.github.xingray.compose.nexus.controls.TabsEditAction.Remove)
                                    }
                                    .pointerHoverIcon(PointerIcon.Hand),
                            )
                        }
                    }
                }
                if (editable || addable) {
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 10.dp, vertical = 8.dp)
                            .clickable {
                                onTabAdd?.invoke()
                                onEdit?.invoke(null, _root_ide_package_.io.github.xingray.compose.nexus.controls.TabsEditAction.Add)
                            }
                            .pointerHoverIcon(PointerIcon.Hand),
                        contentAlignment = Alignment.Center,
                    ) {
                        if (addIcon != null) addIcon() else _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(text = "+", color = colorScheme.primary.base)
                    }
                }
            }
        } else {
            Column(
                modifier = containerModifier,
                verticalArrangement = Arrangement.spacedBy(0.dp),
            ) {
                items.forEachIndexed { index, item ->
                    val selected = state.selectedIndex == index
                    val interactionSource = remember { MutableInteractionSource() }
                    val hovered by interactionSource.collectIsHoveredAsState()
                    val textColor = when {
                        item.disabled -> colorScheme.disabled.text
                        selected -> colorScheme.primary.base
                        hovered -> colorScheme.primary.base
                        else -> colorScheme.text.regular
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(if (selected) colorScheme.primary.light9 else colorScheme.fill.blank)
                            .then(
                                if (!item.disabled) {
                                    Modifier
                                        .clickable(
                                            interactionSource = interactionSource,
                                            indication = null,
                                        ) { switchTo(index) }
                                        .pointerHoverIcon(PointerIcon.Hand)
                                } else {
                                    Modifier
                                }
                            )
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
                            text = item.label,
                            color = textColor,
                            style = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.typography.base
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun TabsContent() {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .then(
                    if (type == _root_ide_package_.io.github.xingray.compose.nexus.controls.TabsType.BorderCard) {
                        Modifier
                            .border(1.dp, colorScheme.border.light, shapes.base)
                            .padding(16.dp)
                    } else {
                        Modifier.padding(top = if (horizontal && tabPosition == _root_ide_package_.io.github.xingray.compose.nexus.controls.TabsPosition.Top) 12.dp else 0.dp)
                    }
                ),
        ) {
            if (state.selectedIndex in items.indices) {
                content(state.selectedIndex)
            }
        }
    }

    if (horizontal) {
        Column(modifier = modifier.fillMaxWidth()) {
            if (tabPosition == _root_ide_package_.io.github.xingray.compose.nexus.controls.TabsPosition.Top) {
                TabsBar()
                TabsContent()
            } else {
                TabsContent()
                TabsBar()
            }
        }
    } else {
        Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
            if (tabPosition == _root_ide_package_.io.github.xingray.compose.nexus.controls.TabsPosition.Left) {
                Box(modifier = Modifier.width(180.dp)) { TabsBar() }
                Box(modifier = Modifier.weight(1f).padding(start = 12.dp)) { TabsContent() }
            } else {
                Box(modifier = Modifier.weight(1f).padding(end = 12.dp)) { TabsContent() }
                Box(modifier = Modifier.width(180.dp)) { TabsBar() }
            }
        }
    }
}
