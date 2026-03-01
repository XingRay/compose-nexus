package io.github.xingray.compose_nexus.controls

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import io.github.xingray.compose_nexus.foundation.ProvideContentColorTextStyle
import io.github.xingray.compose_nexus.theme.NexusTheme

// ============================================================================
// Tabs state
// ============================================================================

@Stable
class TabsState(initialSelected: Int = 0) {
    var selectedIndex: Int by mutableStateOf(initialSelected)
}

@Composable
fun rememberTabsState(initialSelected: Int = 0): TabsState =
    remember { TabsState(initialSelected) }

// ============================================================================
// Tab item data
// ============================================================================

data class TabItem(
    val label: String,
    val disabled: Boolean = false,
)

// ============================================================================
// Tabs type
// ============================================================================

enum class TabsType {
    /** Default: bottom-border style tabs */
    Default,
    /** Card: tabs with card-like borders */
    Card,
    /** BorderCard: tabs with full border card style */
    BorderCard,
}

// ============================================================================
// NexusTabs
// ============================================================================

/**
 * Element Plus Tabs — tab navigation with content panels.
 *
 * @param state Tabs selection state.
 * @param items List of tab definitions.
 * @param modifier Modifier.
 * @param type Visual style of the tab bar.
 * @param content Content composable receiving the selected tab index.
 */
@Composable
fun NexusTabs(
    state: TabsState,
    items: List<TabItem>,
    modifier: Modifier = Modifier,
    type: TabsType = TabsType.Default,
    content: @Composable (selectedIndex: Int) -> Unit,
) {
    val colorScheme = NexusTheme.colorScheme
    val typography = NexusTheme.typography

    Column(modifier = modifier.fillMaxWidth()) {
        // Tab bar
        when (type) {
            TabsType.Default -> DefaultTabBar(state, items, colorScheme, typography)
            TabsType.Card -> CardTabBar(state, items, colorScheme, typography)
            TabsType.BorderCard -> BorderCardTabBar(state, items, colorScheme, typography)
        }

        // Content panel
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .then(
                    if (type == TabsType.BorderCard) {
                        Modifier
                            .border(
                                width = 1.dp,
                                color = colorScheme.border.light,
                                shape = NexusTheme.shapes.base,
                            )
                            .padding(16.dp)
                    } else {
                        Modifier.padding(top = 16.dp)
                    }
                ),
        ) {
            if (state.selectedIndex in items.indices) {
                content(state.selectedIndex)
            }
        }
    }
}

@Composable
private fun DefaultTabBar(
    state: TabsState,
    items: List<TabItem>,
    colorScheme: io.github.xingray.compose_nexus.theme.NexusColorScheme,
    typography: io.github.xingray.compose_nexus.theme.NexusTypography,
) {
    Column {
        Row(modifier = Modifier.fillMaxWidth()) {
            items.forEachIndexed { index, item ->
                val isSelected = state.selectedIndex == index
                val interactionSource = remember { MutableInteractionSource() }
                val isHovered by interactionSource.collectIsHoveredAsState()

                val textColor = when {
                    item.disabled -> colorScheme.disabled.text
                    isSelected -> colorScheme.primary.base
                    isHovered -> colorScheme.primary.base
                    else -> colorScheme.text.regular
                }

                Column(
                    modifier = Modifier
                        .then(
                            if (!item.disabled) {
                                Modifier
                                    .clickable(
                                        interactionSource = interactionSource,
                                        indication = null,
                                    ) { state.selectedIndex = index }
                                    .pointerHoverIcon(PointerIcon.Hand)
                            } else Modifier
                        )
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    NexusText(
                        text = item.label,
                        color = textColor,
                        style = typography.base,
                    )
                }
            }
        }
        // Bottom border
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .background(colorScheme.border.lighter),
        )
    }
}

@Composable
private fun CardTabBar(
    state: TabsState,
    items: List<TabItem>,
    colorScheme: io.github.xingray.compose_nexus.theme.NexusColorScheme,
    typography: io.github.xingray.compose_nexus.theme.NexusTypography,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = colorScheme.border.light,
                shape = NexusTheme.shapes.base,
            )
            .clip(NexusTheme.shapes.base),
    ) {
        items.forEachIndexed { index, item ->
            val isSelected = state.selectedIndex == index
            val interactionSource = remember { MutableInteractionSource() }
            val isHovered by interactionSource.collectIsHoveredAsState()

            val bgColor = when {
                isSelected -> colorScheme.fill.blank
                else -> colorScheme.fill.light
            }
            val textColor = when {
                item.disabled -> colorScheme.disabled.text
                isSelected -> colorScheme.primary.base
                isHovered -> colorScheme.primary.base
                else -> colorScheme.text.regular
            }

            Box(
                modifier = Modifier
                    .background(bgColor)
                    .then(
                        if (!item.disabled) {
                            Modifier
                                .clickable(
                                    interactionSource = interactionSource,
                                    indication = null,
                                ) { state.selectedIndex = index }
                                .pointerHoverIcon(PointerIcon.Hand)
                        } else Modifier
                    )
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                contentAlignment = Alignment.Center,
            ) {
                NexusText(
                    text = item.label,
                    color = textColor,
                    style = typography.base,
                )
            }
        }
    }
}

@Composable
private fun BorderCardTabBar(
    state: TabsState,
    items: List<TabItem>,
    colorScheme: io.github.xingray.compose_nexus.theme.NexusColorScheme,
    typography: io.github.xingray.compose_nexus.theme.NexusTypography,
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        items.forEachIndexed { index, item ->
            val isSelected = state.selectedIndex == index
            val interactionSource = remember { MutableInteractionSource() }
            val isHovered by interactionSource.collectIsHoveredAsState()

            val bgColor = when {
                isSelected -> colorScheme.fill.blank
                else -> colorScheme.fill.light
            }
            val textColor = when {
                item.disabled -> colorScheme.disabled.text
                isSelected -> colorScheme.primary.base
                isHovered -> colorScheme.primary.base
                else -> colorScheme.text.regular
            }

            Box(
                modifier = Modifier
                    .background(bgColor)
                    .border(1.dp, colorScheme.border.light)
                    .then(
                        if (!item.disabled) {
                            Modifier
                                .clickable(
                                    interactionSource = interactionSource,
                                    indication = null,
                                ) { state.selectedIndex = index }
                                .pointerHoverIcon(PointerIcon.Hand)
                        } else Modifier
                    )
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                contentAlignment = Alignment.Center,
            ) {
                NexusText(
                    text = item.label,
                    color = textColor,
                    style = typography.base,
                )
            }
        }
    }
}
