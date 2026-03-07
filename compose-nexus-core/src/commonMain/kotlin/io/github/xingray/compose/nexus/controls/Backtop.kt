package io.github.xingray.compose.nexus.controls

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import io.github.xingray.compose.nexus.theme.NexusTheme
import kotlinx.coroutines.launch

/**
 * Element Plus Backtop.
 */
@Composable
fun NexusBacktop(
    modifier: Modifier = Modifier,
    scrollState: ScrollState? = null,
    target: String? = null,
    visibilityHeight: Int = 200,
    right: Int = 40,
    bottom: Int = 40,
    onClick: (() -> Unit)? = null,
    content: (@Composable () -> Unit)? = null,
) {
    // `target` is kept for API parity in current simplified implementation.
    @Suppress("UNUSED_VARIABLE")
    val _unusedTarget = target

    val scope = rememberCoroutineScope()
    val visible = (scrollState?.value ?: 0) >= visibilityHeight
    if (!visible) return

    Popup(
        alignment = Alignment.BottomEnd,
        offset = IntOffset(-right, -bottom),
        properties = PopupProperties(focusable = false),
    ) {
        AnimatedVisibility(visible = visible) {
            Box(
                modifier = modifier
                    .shadow(NexusTheme.shadows.default.elevation, CircleShape)
                    .clip(CircleShape)
                    .background(NexusTheme.colorScheme.fill.blank)
                    .clickable {
                        scope.launch {
                            scrollState?.animateScrollTo(0)
                        }
                        onClick?.invoke()
                    }
                    .defaultMinSize(minWidth = 40.dp, minHeight = 40.dp)
                    .padding(horizontal = 10.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center,
            ) {
                if (content != null) {
                    content()
                } else {
                    NexusText(
                        text = "↑",
                        color = NexusTheme.colorScheme.primary.base,
                        style = NexusTheme.typography.base,
                    )
                }
            }
        }
    }
}

