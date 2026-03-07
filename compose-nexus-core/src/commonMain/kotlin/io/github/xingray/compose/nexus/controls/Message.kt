package io.github.xingray.compose.nexus.controls

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import io.github.xingray.compose.nexus.theme.NexusTheme
import io.github.xingray.compose.nexus.theme.NexusType
import io.github.xingray.compose.nexus.theme.typeColor
import kotlinx.coroutines.delay

enum class MessagePlacement {
    Top,
    TopLeft,
    TopRight,
    Bottom,
    BottomLeft,
    BottomRight,
}

class MessageHandle internal constructor(private val onClose: () -> Unit) {
    fun close() {
        onClose()
    }
}

@Stable
class MessageEntry(
    val text: String,
    val type: NexusType = NexusType.Info,
    val plain: Boolean = false,
    val duration: Long = 3000L,
    val showClose: Boolean = false,
    val placement: MessagePlacement = MessagePlacement.Top,
    val offset: Dp = 16.dp,
    val onClose: (() -> Unit)? = null,
    val icon: (@Composable () -> Unit)? = null,
    repeatNum: Int = 1,
) {
    var visible by mutableStateOf(true)
        internal set
    var repeatNum by mutableIntStateOf(repeatNum)
        internal set
    var restartKey by mutableIntStateOf(0)
        internal set
    internal val id = nextId++

    companion object {
        private var nextId = 0
    }
}

@Stable
class MessageState {
    internal val messages = mutableStateListOf<MessageEntry>()

    fun show(
        text: String,
        type: NexusType = NexusType.Info,
        plain: Boolean = false,
        duration: Long = 3000L,
        showClose: Boolean = false,
        placement: MessagePlacement = MessagePlacement.Top,
        offset: Dp = 16.dp,
        grouping: Boolean = false,
        repeatNum: Int = 1,
        onClose: (() -> Unit)? = null,
        icon: (@Composable () -> Unit)? = null,
    ): MessageHandle {
        if (grouping) {
            val existing = messages.lastOrNull { it.text == text && it.placement == placement && it.visible }
            if (existing != null) {
                existing.repeatNum += repeatNum
                existing.restartKey += 1
                existing.visible = true
                return MessageHandle { close(existing) }
            }
        }

        val entry = MessageEntry(
            text = text,
            type = type,
            plain = plain,
            duration = duration,
            showClose = showClose,
            placement = placement,
            offset = offset,
            onClose = onClose,
            icon = icon,
            repeatNum = repeatNum,
        )
        messages.add(entry)
        return MessageHandle { close(entry) }
    }

    fun primary(text: String, duration: Long = 3000L) = show(text, type = NexusType.Primary, duration = duration)
    fun success(text: String, duration: Long = 3000L) = show(text, type = NexusType.Success, duration = duration)
    fun warning(text: String, duration: Long = 3000L) = show(text, type = NexusType.Warning, duration = duration)
    fun error(text: String, duration: Long = 3000L) = show(text, type = NexusType.Danger, duration = duration)
    fun info(text: String, duration: Long = 3000L) = show(text, type = NexusType.Info, duration = duration)

    fun closeAll() {
        val snapshot = messages.toList()
        snapshot.forEach { close(it) }
    }

    fun close(entry: MessageEntry) {
        if (!entry.visible) {
            remove(entry)
            return
        }
        entry.visible = false
    }

    internal fun remove(entry: MessageEntry) {
        if (messages.remove(entry)) {
            entry.onClose?.invoke()
        }
    }
}

@Composable
fun rememberMessageState(): MessageState = remember { MessageState() }

@Composable
fun NexusMessageHost(
    state: MessageState,
    modifier: Modifier = Modifier,
) {
    if (state.messages.isEmpty()) return

    MessagePlacement.entries.forEach { placement ->
        val entries = state.messages.filter { it.placement == placement }
        if (entries.isEmpty()) return@forEach

        Popup(
            alignment = when (placement) {
                MessagePlacement.Top -> Alignment.TopCenter
                MessagePlacement.TopLeft -> Alignment.TopStart
                MessagePlacement.TopRight -> Alignment.TopEnd
                MessagePlacement.Bottom -> Alignment.BottomCenter
                MessagePlacement.BottomLeft -> Alignment.BottomStart
                MessagePlacement.BottomRight -> Alignment.BottomEnd
            },
            properties = PopupProperties(focusable = false),
        ) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(
                        top = if (placement == MessagePlacement.Top || placement == MessagePlacement.TopLeft || placement == MessagePlacement.TopRight) {
                            entries.first().offset
                        } else {
                            0.dp
                        },
                        bottom = if (placement == MessagePlacement.Bottom || placement == MessagePlacement.BottomLeft || placement == MessagePlacement.BottomRight) {
                            entries.first().offset
                        } else {
                            0.dp
                        },
                        start = if (placement == MessagePlacement.TopLeft || placement == MessagePlacement.BottomLeft) 16.dp else 0.dp,
                        end = if (placement == MessagePlacement.TopRight || placement == MessagePlacement.BottomRight) 16.dp else 0.dp,
                    ),
                horizontalAlignment = when (placement) {
                    MessagePlacement.Top, MessagePlacement.Bottom -> Alignment.CenterHorizontally
                    MessagePlacement.TopLeft, MessagePlacement.BottomLeft -> Alignment.Start
                    MessagePlacement.TopRight, MessagePlacement.BottomRight -> Alignment.End
                },
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                entries.forEach { entry ->
                    key(entry.id) {
                        MessageItem(entry = entry, onDismiss = { state.remove(entry) })
                    }
                }
            }
        }
    }
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
        NexusType.Primary -> "P"
        NexusType.Success -> "✓"
        NexusType.Warning -> "!"
        NexusType.Danger -> "✕"
        NexusType.Info -> "i"
        NexusType.Default -> "i"
    }

    LaunchedEffect(entry.id, entry.restartKey) {
        if (entry.duration > 0) {
            delay(entry.duration)
            entry.visible = false
            delay(220)
            onDismiss()
        }
    }

    AnimatedVisibility(
        visible = entry.visible,
        enter = fadeIn() + slideInVertically { -it / 2 },
        exit = fadeOut() + slideOutVertically { -it / 2 },
    ) {
        Row(
            modifier = Modifier
                .shadow(shadows.light.elevation, shapes.base)
                .clip(shapes.base)
                .background(if (entry.plain) tc.light9 else colorScheme.fill.blank)
                .border(1.dp, if (entry.plain) tc.light7 else colorScheme.border.light, shapes.base)
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            if (entry.icon != null) {
                entry.icon.invoke()
            } else {
                NexusText(text = iconText, color = tc.base, style = typography.base)
            }
            NexusText(
                text = entry.text,
                color = if (entry.plain) tc.base else colorScheme.text.regular,
                style = typography.base,
            )
            if (entry.repeatNum > 1) {
                NexusText(
                    text = "x${entry.repeatNum}",
                    color = colorScheme.text.secondary,
                    style = typography.extraSmall,
                    modifier = Modifier
                        .clip(shapes.round)
                        .background(colorScheme.fill.light)
                        .padding(horizontal = 6.dp, vertical = 2.dp),
                )
            }
            if (entry.showClose) {
                NexusText(
                    text = "✕",
                    color = colorScheme.text.placeholder,
                    style = typography.small,
                    modifier = Modifier
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                        ) {
                            entry.visible = false
                            onDismiss()
                        },
                )
            }
        }
    }
}
