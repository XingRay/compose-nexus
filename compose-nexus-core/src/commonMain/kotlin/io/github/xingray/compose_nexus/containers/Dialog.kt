package io.github.xingray.compose_nexus.containers

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import io.github.xingray.compose_nexus.controls.NexusDivider
import io.github.xingray.compose_nexus.controls.NexusText
import io.github.xingray.compose_nexus.foundation.ProvideContentColorTextStyle
import io.github.xingray.compose_nexus.theme.NexusTheme

// ============================================================================
// Dialog state
// ============================================================================

@Stable
class DialogState(initialVisible: Boolean = false) {
    var visible: Boolean by mutableStateOf(initialVisible)

    fun open() { visible = true }
    fun close() { visible = false }
}

@Composable
fun rememberDialogState(initialVisible: Boolean = false): DialogState =
    remember { DialogState(initialVisible) }

// ============================================================================
// NexusDialog
// ============================================================================

/**
 * Element Plus Dialog — a modal dialog overlay.
 *
 * @param state Dialog visibility state.
 * @param modifier Modifier applied to the dialog panel.
 * @param title Dialog title string. Ignored if [header] is provided.
 * @param width Dialog panel width.
 * @param closeOnClickOverlay Whether clicking the backdrop closes the dialog.
 * @param closeOnEscape Whether pressing Escape closes the dialog.
 * @param showClose Whether to show the close button in the header.
 * @param header Custom header composable. Overrides [title].
 * @param footer Optional footer composable (e.g., action buttons).
 * @param content Dialog body content.
 */
@Composable
fun NexusDialog(
    state: DialogState,
    modifier: Modifier = Modifier,
    title: String? = null,
    width: Dp = 500.dp,
    closeOnClickOverlay: Boolean = true,
    closeOnEscape: Boolean = true,
    showClose: Boolean = true,
    header: (@Composable () -> Unit)? = null,
    footer: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    if (!state.visible) return

    val colorScheme = NexusTheme.colorScheme
    val shapes = NexusTheme.shapes
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
            contentAlignment = Alignment.Center,
        ) {
            // Scrim / backdrop
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

            // Dialog panel
            Column(
                modifier = modifier
                    .width(width)
                    .shadow(shadows.default.elevation, shapes.base)
                    .clip(shapes.base)
                    .background(colorScheme.fill.blank)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                    ) { /* consume click to prevent backdrop dismiss */ },
            ) {
                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        if (header != null) {
                            ProvideContentColorTextStyle(
                                contentColor = colorScheme.text.primary,
                                textStyle = NexusTheme.typography.large,
                            ) {
                                header()
                            }
                        } else if (title != null) {
                            NexusText(
                                text = title,
                                color = colorScheme.text.primary,
                                style = NexusTheme.typography.large,
                            )
                        }
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

                // Body
                ProvideContentColorTextStyle(
                    contentColor = colorScheme.text.regular,
                    textStyle = NexusTheme.typography.base,
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 10.dp),
                    ) {
                        content()
                    }
                }

                // Footer
                if (footer != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 20.dp),
                        contentAlignment = Alignment.CenterEnd,
                    ) {
                        footer()
                    }
                }
            }
        }
    }
}
