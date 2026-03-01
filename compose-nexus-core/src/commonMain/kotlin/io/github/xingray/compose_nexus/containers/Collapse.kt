package io.github.xingray.compose_nexus.containers

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import io.github.xingray.compose_nexus.controls.NexusDivider
import io.github.xingray.compose_nexus.controls.NexusText
import io.github.xingray.compose_nexus.foundation.ProvideContentColorTextStyle
import io.github.xingray.compose_nexus.theme.NexusTheme

// ============================================================================
// Collapse state
// ============================================================================

/**
 * State for [NexusCollapse]. Tracks which items are expanded.
 *
 * @param initialExpanded Initial set of expanded item names.
 * @param accordion If true, only one item can be expanded at a time.
 */
@Stable
class CollapseState(
    initialExpanded: Set<String> = emptySet(),
    val accordion: Boolean = false,
) {
    var expanded: Set<String> by mutableStateOf(initialExpanded)

    fun isExpanded(name: String): Boolean = name in expanded

    fun toggle(name: String) {
        expanded = if (name in expanded) {
            expanded - name
        } else {
            if (accordion) setOf(name) else expanded + name
        }
    }
}

@Composable
fun rememberCollapseState(
    initialExpanded: Set<String> = emptySet(),
    accordion: Boolean = false,
): CollapseState = remember { CollapseState(initialExpanded, accordion) }

// ============================================================================
// Collapse scope
// ============================================================================

@Stable
class CollapseScope internal constructor(
    internal val state: CollapseState,
)

// ============================================================================
// NexusCollapse
// ============================================================================

/**
 * Element Plus Collapse — a set of expandable/collapsible panels.
 *
 * @param state Collapse state controlling which panels are expanded.
 * @param modifier Modifier.
 * @param content Content using [CollapseScope] receiver for [NexusCollapseItem].
 */
@Composable
fun NexusCollapse(
    state: CollapseState = rememberCollapseState(),
    modifier: Modifier = Modifier,
    content: @Composable CollapseScope.() -> Unit,
) {
    val colorScheme = NexusTheme.colorScheme
    val scope = remember(state) { CollapseScope(state) }

    Column(
        modifier = modifier
            .clip(NexusTheme.shapes.base)
            .border(1.dp, colorScheme.border.lighter, NexusTheme.shapes.base),
    ) {
        scope.content()
    }
}

/**
 * Element Plus CollapseItem — a single expandable panel within [NexusCollapse].
 *
 * @param name Unique identifier for this item.
 * @param title Title composable shown in the header.
 * @param disabled Whether this item can be toggled.
 * @param content Content shown when expanded.
 */
@Composable
fun CollapseScope.NexusCollapseItem(
    name: String,
    modifier: Modifier = Modifier,
    disabled: Boolean = false,
    title: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {
    val colorScheme = NexusTheme.colorScheme
    val motion = NexusTheme.motion
    val isExpanded = state.isExpanded(name)

    Column(modifier = modifier.fillMaxWidth()) {
        // Divider between items
        NexusDivider(color = colorScheme.border.lighter)

        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(colorScheme.fill.blank)
                .then(
                    if (!disabled) {
                        Modifier
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                            ) { state.toggle(name) }
                            .pointerHoverIcon(PointerIcon.Hand)
                    } else Modifier
                )
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Arrow indicator
            NexusText(
                text = "▶",
                modifier = Modifier
                    .padding(end = 8.dp)
                    .rotate(if (isExpanded) 90f else 0f),
                color = colorScheme.text.secondary,
                style = NexusTheme.typography.extraSmall,
            )

            ProvideContentColorTextStyle(
                contentColor = if (disabled) colorScheme.disabled.text else colorScheme.text.primary,
                textStyle = NexusTheme.typography.base,
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    title()
                }
            }
        }

        // Content panel
        AnimatedVisibility(
            visible = isExpanded,
            enter = expandVertically(animationSpec = motion.tweenFast()),
            exit = shrinkVertically(animationSpec = motion.tweenFast()),
        ) {
            ProvideContentColorTextStyle(
                contentColor = colorScheme.text.regular,
                textStyle = NexusTheme.typography.base,
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(colorScheme.fill.blank)
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                ) {
                    content()
                }
            }
        }
    }
}
