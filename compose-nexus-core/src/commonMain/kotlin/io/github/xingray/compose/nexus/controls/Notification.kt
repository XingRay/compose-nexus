package io.github.xingray.compose.nexus.controls

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
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

enum class NotificationPosition {
    TopRight,
    TopLeft,
    BottomRight,
    BottomLeft,
}

class NotificationHandle internal constructor(private val onClose: () -> Unit) {
    fun close() {
        onClose()
    }
}

@Stable
class NotificationEntry(
    val title: String,
    val message: String = "",
    val type: NexusType = NexusType.Default,
    val duration: Long = 4500L,
    val position: NotificationPosition = NotificationPosition.TopRight,
    val showClose: Boolean = true,
    val offset: Dp = 16.dp,
    val onClose: (() -> Unit)? = null,
    val onClick: (() -> Unit)? = null,
    val icon: (@Composable () -> Unit)? = null,
    val closeIcon: (@Composable () -> Unit)? = null,
) {
    var visible by mutableStateOf(true)
        internal set
    internal val id = nextId++

    companion object {
        private var nextId = 0
    }
}

@Stable
class NotificationState {
    internal val notifications = mutableStateListOf<NotificationEntry>()

    fun show(
        title: String,
        message: String = "",
        type: NexusType = NexusType.Default,
        duration: Long = 4500L,
        position: NotificationPosition = NotificationPosition.TopRight,
        showClose: Boolean = true,
        offset: Dp = 16.dp,
        onClose: (() -> Unit)? = null,
        onClick: (() -> Unit)? = null,
        icon: (@Composable () -> Unit)? = null,
        closeIcon: (@Composable () -> Unit)? = null,
    ): NotificationHandle {
        val entry = NotificationEntry(
            title = title,
            message = message,
            type = type,
            duration = duration,
            position = position,
            showClose = showClose,
            offset = offset,
            onClose = onClose,
            onClick = onClick,
            icon = icon,
            closeIcon = closeIcon,
        )
        notifications.add(entry)
        return NotificationHandle { close(entry) }
    }

    fun primary(title: String, message: String = "", duration: Long = 4500L) =
        show(title, message, NexusType.Primary, duration)

    fun success(title: String, message: String = "", duration: Long = 4500L) =
        show(title, message, NexusType.Success, duration)

    fun warning(title: String, message: String = "", duration: Long = 4500L) =
        show(title, message, NexusType.Warning, duration)

    fun error(title: String, message: String = "", duration: Long = 4500L) =
        show(title, message, NexusType.Danger, duration)

    fun info(title: String, message: String = "", duration: Long = 4500L) =
        show(title, message, NexusType.Info, duration)

    fun close(entry: NotificationEntry) {
        if (!entry.visible) {
            remove(entry)
            return
        }
        entry.visible = false
    }

    fun closeAll() {
        val snapshot = notifications.toList()
        snapshot.forEach { close(it) }
    }

    internal fun remove(entry: NotificationEntry) {
        if (notifications.remove(entry)) {
            entry.onClose?.invoke()
        }
    }
}

@Composable
fun rememberNotificationState(): NotificationState = remember { NotificationState() }

@Composable
fun NexusNotificationHost(
    state: NotificationState,
    modifier: Modifier = Modifier,
) {
    if (state.notifications.isEmpty()) return

    NotificationPosition.entries.forEach { position ->
        val entries = state.notifications.filter { it.position == position }
        if (entries.isEmpty()) return@forEach

        val alignment = when (position) {
            NotificationPosition.TopRight -> Alignment.TopEnd
            NotificationPosition.TopLeft -> Alignment.TopStart
            NotificationPosition.BottomRight -> Alignment.BottomEnd
            NotificationPosition.BottomLeft -> Alignment.BottomStart
        }
        val slideFromRight = position == NotificationPosition.TopRight || position == NotificationPosition.BottomRight

        Popup(
            alignment = alignment,
            properties = PopupProperties(focusable = false),
        ) {
            Column(
                modifier = modifier.padding(
                    top = if (position == NotificationPosition.TopRight || position == NotificationPosition.TopLeft) entries.first().offset else 0.dp,
                    bottom = if (position == NotificationPosition.BottomRight || position == NotificationPosition.BottomLeft) entries.first().offset else 0.dp,
                    start = if (position == NotificationPosition.TopLeft || position == NotificationPosition.BottomLeft) 16.dp else 0.dp,
                    end = if (position == NotificationPosition.TopRight || position == NotificationPosition.BottomRight) 16.dp else 0.dp,
                ),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = if (position == NotificationPosition.TopLeft || position == NotificationPosition.BottomLeft) Alignment.Start else Alignment.End,
            ) {
                entries.forEach { entry ->
                    key(entry.id) {
                        NotificationItem(
                            entry = entry,
                            slideFromRight = slideFromRight,
                            onDismiss = { state.remove(entry) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun NotificationItem(
    entry: NotificationEntry,
    slideFromRight: Boolean,
    onDismiss: () -> Unit,
) {
    val colorScheme = NexusTheme.colorScheme
    val typography = NexusTheme.typography
    val shapes = NexusTheme.shapes
    val shadows = NexusTheme.shadows
    val tc = colorScheme.typeColor(entry.type)

    val iconText = when (entry.type) {
        NexusType.Primary -> "P"
        NexusType.Success -> "✓"
        NexusType.Warning -> "!"
        NexusType.Danger -> "✕"
        NexusType.Info -> "i"
        NexusType.Default -> null
    }

    LaunchedEffect(entry.id) {
        if (entry.duration > 0) {
            delay(entry.duration)
            entry.visible = false
            delay(220)
            onDismiss()
        }
    }

    AnimatedVisibility(
        visible = entry.visible,
        enter = if (slideFromRight) fadeIn() + slideInHorizontally { it } else fadeIn() + slideInHorizontally { -it },
        exit = if (slideFromRight) fadeOut() + slideOutHorizontally { it } else fadeOut() + slideOutHorizontally { -it },
    ) {
        Box(
            modifier = Modifier
                .width(340.dp)
                .shadow(shadows.default.elevation, shapes.base)
                .clip(shapes.base)
                .background(colorScheme.fill.blank)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                ) { entry.onClick?.invoke() }
                .padding(16.dp),
        ) {
            Row {
                if (entry.icon != null) {
                    Box(modifier = Modifier.padding(end = 10.dp)) {
                        entry.icon.invoke()
                    }
                } else if (iconText != null && tc != null) {
                    NexusText(
                        text = iconText,
                        color = tc.base,
                        style = typography.large,
                        modifier = Modifier.padding(end = 10.dp),
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    NexusText(
                        text = entry.title,
                        color = colorScheme.text.primary,
                        style = typography.base,
                    )
                    if (entry.message.isNotBlank()) {
                        Spacer(modifier = Modifier.padding(top = 6.dp))
                        NexusText(
                            text = entry.message,
                            color = colorScheme.text.regular,
                            style = typography.small,
                        )
                    }
                }

                if (entry.showClose) {
                    Box(
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                            ) {
                                entry.visible = false
                                onDismiss()
                            },
                    ) {
                        if (entry.closeIcon != null) {
                            entry.closeIcon.invoke()
                        } else {
                            NexusText(
                                text = "✕",
                                color = colorScheme.text.placeholder,
                                style = typography.extraSmall,
                            )
                        }
                    }
                }
            }
        }
    }
}
