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
    val type: io.github.xingray.compose.nexus.theme.NexusType = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusType.Default,
    val duration: Long = 4500L,
    val position: io.github.xingray.compose.nexus.controls.NotificationPosition = _root_ide_package_.io.github.xingray.compose.nexus.controls.NotificationPosition.TopRight,
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
    internal val notifications = mutableStateListOf<io.github.xingray.compose.nexus.controls.NotificationEntry>()

    fun show(
        title: String,
        message: String = "",
        type: io.github.xingray.compose.nexus.theme.NexusType = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusType.Default,
        duration: Long = 4500L,
        position: io.github.xingray.compose.nexus.controls.NotificationPosition = _root_ide_package_.io.github.xingray.compose.nexus.controls.NotificationPosition.TopRight,
        showClose: Boolean = true,
        offset: Dp = 16.dp,
        onClose: (() -> Unit)? = null,
        onClick: (() -> Unit)? = null,
        icon: (@Composable () -> Unit)? = null,
        closeIcon: (@Composable () -> Unit)? = null,
    ): io.github.xingray.compose.nexus.controls.NotificationHandle {
        val entry = _root_ide_package_.io.github.xingray.compose.nexus.controls.NotificationEntry(
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
        return _root_ide_package_.io.github.xingray.compose.nexus.controls.NotificationHandle { close(entry) }
    }

    fun primary(title: String, message: String = "", duration: Long = 4500L) =
        show(title, message, _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusType.Primary, duration)

    fun success(title: String, message: String = "", duration: Long = 4500L) =
        show(title, message, _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusType.Success, duration)

    fun warning(title: String, message: String = "", duration: Long = 4500L) =
        show(title, message, _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusType.Warning, duration)

    fun error(title: String, message: String = "", duration: Long = 4500L) =
        show(title, message, _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusType.Danger, duration)

    fun info(title: String, message: String = "", duration: Long = 4500L) =
        show(title, message, _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusType.Info, duration)

    fun close(entry: io.github.xingray.compose.nexus.controls.NotificationEntry) {
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

    internal fun remove(entry: io.github.xingray.compose.nexus.controls.NotificationEntry) {
        if (notifications.remove(entry)) {
            entry.onClose?.invoke()
        }
    }
}

@Composable
fun rememberNotificationState(): io.github.xingray.compose.nexus.controls.NotificationState = remember { _root_ide_package_.io.github.xingray.compose.nexus.controls.NotificationState() }

@Composable
fun NexusNotificationHost(
    state: io.github.xingray.compose.nexus.controls.NotificationState,
    modifier: Modifier = Modifier,
) {
    if (state.notifications.isEmpty()) return

    _root_ide_package_.io.github.xingray.compose.nexus.controls.NotificationPosition.entries.forEach { position ->
        val entries = state.notifications.filter { it.position == position }
        if (entries.isEmpty()) return@forEach

        val alignment = when (position) {
            _root_ide_package_.io.github.xingray.compose.nexus.controls.NotificationPosition.TopRight -> Alignment.TopEnd
            _root_ide_package_.io.github.xingray.compose.nexus.controls.NotificationPosition.TopLeft -> Alignment.TopStart
            _root_ide_package_.io.github.xingray.compose.nexus.controls.NotificationPosition.BottomRight -> Alignment.BottomEnd
            _root_ide_package_.io.github.xingray.compose.nexus.controls.NotificationPosition.BottomLeft -> Alignment.BottomStart
        }
        val slideFromRight = position == _root_ide_package_.io.github.xingray.compose.nexus.controls.NotificationPosition.TopRight || position == _root_ide_package_.io.github.xingray.compose.nexus.controls.NotificationPosition.BottomRight

        Popup(
            alignment = alignment,
            properties = PopupProperties(focusable = false),
        ) {
            Column(
                modifier = modifier.padding(
                    top = if (position == _root_ide_package_.io.github.xingray.compose.nexus.controls.NotificationPosition.TopRight || position == _root_ide_package_.io.github.xingray.compose.nexus.controls.NotificationPosition.TopLeft) entries.first().offset else 0.dp,
                    bottom = if (position == _root_ide_package_.io.github.xingray.compose.nexus.controls.NotificationPosition.BottomRight || position == _root_ide_package_.io.github.xingray.compose.nexus.controls.NotificationPosition.BottomLeft) entries.first().offset else 0.dp,
                    start = if (position == _root_ide_package_.io.github.xingray.compose.nexus.controls.NotificationPosition.TopLeft || position == _root_ide_package_.io.github.xingray.compose.nexus.controls.NotificationPosition.BottomLeft) 16.dp else 0.dp,
                    end = if (position == _root_ide_package_.io.github.xingray.compose.nexus.controls.NotificationPosition.TopRight || position == _root_ide_package_.io.github.xingray.compose.nexus.controls.NotificationPosition.BottomRight) 16.dp else 0.dp,
                ),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = if (position == _root_ide_package_.io.github.xingray.compose.nexus.controls.NotificationPosition.TopLeft || position == _root_ide_package_.io.github.xingray.compose.nexus.controls.NotificationPosition.BottomLeft) Alignment.Start else Alignment.End,
            ) {
                entries.forEach { entry ->
                    key(entry.id) {
                        _root_ide_package_.io.github.xingray.compose.nexus.controls.NotificationItem(
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
    entry: io.github.xingray.compose.nexus.controls.NotificationEntry,
    slideFromRight: Boolean,
    onDismiss: () -> Unit,
) {
    val colorScheme = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.colorScheme
    val typography = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.typography
    val shapes = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.shapes
    val shadows = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.shadows
    val tc = colorScheme.typeColor(entry.type)

    val iconText = when (entry.type) {
        _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusType.Primary -> "P"
        _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusType.Success -> "✓"
        _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusType.Warning -> "!"
        _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusType.Danger -> "✕"
        _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusType.Info -> "i"
        _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusType.Default -> null
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
                    _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
                        text = iconText,
                        color = tc.base,
                        style = typography.large,
                        modifier = Modifier.padding(end = 10.dp),
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
                        text = entry.title,
                        color = colorScheme.text.primary,
                        style = typography.base,
                    )
                    if (entry.message.isNotBlank()) {
                        Spacer(modifier = Modifier.padding(top = 6.dp))
                        _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
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
                            _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
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
