package io.github.xingray.compose_nexus.containers

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import io.github.xingray.compose_nexus.controls.NexusDivider
import io.github.xingray.compose_nexus.controls.NexusText
import io.github.xingray.compose_nexus.foundation.ProvideContentColorTextStyle
import io.github.xingray.compose_nexus.theme.NexusTheme

/**
 * Element Plus Drawer — a panel that slides in from an edge.
 *
 * Reuses [DialogState] for visibility control.
 *
 * @param state Visibility state.
 * @param modifier Modifier applied to the drawer panel.
 * @param direction Which edge the drawer slides from.
 * @param size Drawer width (for left/right) or height (for top/bottom).
 * @param title Drawer title.
 * @param showClose Show close button.
 * @param closeOnClickOverlay Clicking backdrop closes the drawer.
 * @param closeOnEscape Pressing Escape closes the drawer.
 * @param content Drawer body content.
 */
@Composable
fun NexusDrawer(
    state: DialogState,
    modifier: Modifier = Modifier,
    direction: DrawerDirection = DrawerDirection.Right,
    size: Dp = 300.dp,
    title: String? = null,
    showClose: Boolean = true,
    closeOnClickOverlay: Boolean = true,
    closeOnEscape: Boolean = true,
    content: @Composable () -> Unit,
) {
    if (!state.visible) return

    val colorScheme = NexusTheme.colorScheme
    val shadows = NexusTheme.shadows
    val motion = NexusTheme.motion

    Popup(
        onDismissRequest = if (closeOnClickOverlay) {{ state.close() }} else null,
        properties = PopupProperties(focusable = true),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .then(
                    if (closeOnEscape) {
                        Modifier.onKeyEvent {
                            if (it.key == Key.Escape) { state.close(); true } else false
                        }
                    } else Modifier
                ),
        ) {
            // Scrim
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colorScheme.overlay.lighter)
                    .then(
                        if (closeOnClickOverlay) {
                            Modifier.clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                            ) { state.close() }
                        } else Modifier
                    ),
            )

            // Drawer panel
            val panelAlignment = when (direction) {
                DrawerDirection.Left -> Alignment.CenterStart
                DrawerDirection.Right -> Alignment.CenterEnd
                DrawerDirection.Top -> Alignment.TopCenter
                DrawerDirection.Bottom -> Alignment.BottomCenter
            }

            val panelModifier = when (direction) {
                DrawerDirection.Left, DrawerDirection.Right -> Modifier
                    .width(size)
                    .fillMaxHeight()
                DrawerDirection.Top, DrawerDirection.Bottom -> Modifier
                    .height(size)
                    .fillMaxWidth()
            }

            Column(
                modifier = Modifier
                    .align(panelAlignment)
                    .then(panelModifier)
                    .shadow(shadows.dark.elevation)
                    .background(colorScheme.fill.blank)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                    ) { /* consume click */ }
                    .then(modifier),
            ) {
                // Header
                if (title != null || showClose) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        if (title != null) {
                            Box(modifier = Modifier.weight(1f)) {
                                NexusText(
                                    text = title,
                                    color = colorScheme.text.primary,
                                    style = NexusTheme.typography.large,
                                )
                            }
                        } else {
                            Box(modifier = Modifier.weight(1f))
                        }
                        if (showClose) {
                            Box(
                                modifier = Modifier
                                    .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = null,
                                    ) { state.close() }
                                    .padding(4.dp),
                            ) {
                                NexusText(
                                    text = "✕",
                                    color = colorScheme.text.placeholder,
                                    style = NexusTheme.typography.medium,
                                )
                            }
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
                            .weight(1f)
                            .padding(20.dp),
                    ) {
                        content()
                    }
                }
            }
        }
    }
}

enum class DrawerDirection {
    Left,
    Right,
    Top,
    Bottom,
}
