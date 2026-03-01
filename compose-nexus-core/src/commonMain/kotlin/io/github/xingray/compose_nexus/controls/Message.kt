package io.github.xingray.compose_nexus.controls

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import io.github.xingray.compose_nexus.theme.NexusTheme
import io.github.xingray.compose_nexus.theme.NexusType
import io.github.xingray.compose_nexus.theme.typeColor
import kotlinx.coroutines.delay

/**
 * A single message entry.
 */
@Stable
class MessageEntry(
    val text: String,
    val type: NexusType = NexusType.Info,
    val duration: Long = 3000L,
) {
    var visible by mutableStateOf(true)
        internal set
    internal val id = nextId++

    companion object {
        private var nextId = 0
    }
}

/**
 * State holder that manages a stack of messages.
 */
@Stable
class MessageState {
    internal val messages = mutableStateListOf<MessageEntry>()

    fun show(
        text: String,
        type: NexusType = NexusType.Info,
        duration: Long = 3000L,
    ): MessageEntry {
        val entry = MessageEntry(text, type, duration)
        messages.add(entry)
        return entry
    }

    fun success(text: String, duration: Long = 3000L) = show(text, NexusType.Success, duration)
    fun warning(text: String, duration: Long = 3000L) = show(text, NexusType.Warning, duration)
    fun error(text: String, duration: Long = 3000L) = show(text, NexusType.Danger, duration)
    fun info(text: String, duration: Long = 3000L) = show(text, NexusType.Info, duration)

    internal fun remove(entry: MessageEntry) {
        messages.remove(entry)
    }
}

@Composable
fun rememberMessageState(): MessageState = remember { MessageState() }

/**
 * Element Plus Message — a global message notification that appears at the top center.
 *
 * Place this composable at the root of your app layout. Messages are triggered via [MessageState].
 *
 * Usage:
 * ```
 * val messageState = rememberMessageState()
 * NexusMessageHost(state = messageState)
 *
 * // Trigger:
 * messageState.success("Operation completed!")
 * ```
 *
 * @param state The message state manager.
 * @param modifier Modifier.
 */
@Composable
fun NexusMessageHost(
    state: MessageState,
    modifier: Modifier = Modifier,
) {
    if (state.messages.isEmpty()) return

    Popup(
        alignment = Alignment.TopCenter,
        properties = PopupProperties(focusable = false),
    ) {
        Column(
            modifier = modifier
                .padding(top = 20.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            state.messages.toList().forEach { entry ->
                key(entry.id) {
                    MessageItem(entry = entry, onDismiss = { state.remove(entry) })
                }
            }
        }
    }
}

@Composable
private fun key(id: Int, content: @Composable () -> Unit) {
    content()
}

@Composable
private fun MessageItem(
    entry: MessageEntry,
    onDismiss: () -> Unit,
) {
    val colorScheme = NexusTheme.colorScheme
    val typography = NexusTheme.typography
    val shapes = NexusTheme.shapes
    val shadows = NexusTheme.shadows

    val tc = colorScheme.typeColor(entry.type) ?: colorScheme.info

    val iconText = when (entry.type) {
        NexusType.Success -> "✓"
        NexusType.Warning -> "!"
        NexusType.Danger -> "✕"
        NexusType.Info, NexusType.Primary -> "i"
        NexusType.Default -> "i"
    }

    // Auto dismiss
    LaunchedEffect(entry.id) {
        if (entry.duration > 0) {
            delay(entry.duration)
            entry.visible = false
            delay(300) // animation time
            onDismiss()
        }
    }

    AnimatedVisibility(
        visible = entry.visible,
        enter = fadeIn() + slideInVertically { -it },
        exit = fadeOut() + slideOutVertically { -it },
    ) {
        Row(
            modifier = Modifier
                .shadow(shadows.light.elevation, shapes.base)
                .clip(shapes.base)
                .background(colorScheme.fill.blank)
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            NexusText(
                text = iconText,
                color = tc.base,
                style = typography.base,
            )
            NexusText(
                text = entry.text,
                color = colorScheme.text.regular,
                style = typography.base,
            )
        }
    }
}
