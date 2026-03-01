package io.github.xingray.compose_nexus.controls

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import io.github.xingray.compose_nexus.theme.NexusTheme
import io.github.xingray.compose_nexus.theme.NexusType
import io.github.xingray.compose_nexus.theme.typeColor
import kotlinx.coroutines.delay

/**
 * Notification position on screen.
 */
enum class NotificationPosition {
    TopRight,
    TopLeft,
    BottomRight,
    BottomLeft,
}

/**
 * A single notification entry.
 */
@Stable
class NotificationEntry(
    val title: String,
    val message: String = "",
    val type: NexusType = NexusType.Default,
    val duration: Long = 4500L,
) {
    var visible by mutableStateOf(true)
        internal set
    internal val id = nextId++

    companion object {
        private var nextId = 0
    }
}

/**
 * State holder that manages a stack of notifications.
 */
@Stable
class NotificationState {
    internal val notifications = mutableStateListOf<NotificationEntry>()

    fun show(
        title: String,
        message: String = "",
        type: NexusType = NexusType.Default,
        duration: Long = 4500L,
    ): NotificationEntry {
        val entry = NotificationEntry(title, message, type, duration)
        notifications.add(entry)
        return entry
    }

    fun success(title: String, message: String = "", duration: Long = 4500L) =
        show(title, message, NexusType.Success, duration)

    fun warning(title: String, message: String = "", duration: Long = 4500L) =
        show(title, message, NexusType.Warning, duration)

    fun error(title: String, message: String = "", duration: Long = 4500L) =
        show(title, message, NexusType.Danger, duration)

    fun info(title: String, message: String = "", duration: Long = 4500L) =
        show(title, message, NexusType.Info, duration)

    internal fun remove(entry: NotificationEntry) {
        notifications.remove(entry)
    }
}

@Composable
fun rememberNotificationState(): NotificationState = remember { NotificationState() }

/**
 * Element Plus Notification — a global notification panel that appears at a corner.
 *
 * Place this composable at the root of your app layout. Notifications are triggered via [NotificationState].
 *
 * @param state The notification state manager.
 * @param modifier Modifier.
 * @param position Corner position on screen.
 */
@Composable
fun NexusNotificationHost(
    state: NotificationState,
    modifier: Modifier = Modifier,
    position: NotificationPosition = NotificationPosition.TopRight,
) {
    if (state.notifications.isEmpty()) return

    val alignment = when (position) {
        NotificationPosition.TopRight -> Alignment.TopEnd
        NotificationPosition.TopLeft -> Alignment.TopStart
        NotificationPosition.BottomRight -> Alignment.BottomEnd
        NotificationPosition.BottomLeft -> Alignment.BottomStart
    }

    val slideFromRight = position == NotificationPosition.TopRight ||
            position == NotificationPosition.BottomRight

    Popup(
        alignment = alignment,
        properties = PopupProperties(focusable = false),
    ) {
        Column(
            modifier = modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            state.notifications.toList().forEach { entry ->
                NotificationItem(
                    entry = entry,
                    slideFromRight = slideFromRight,
                    onDismiss = { state.remove(entry) },
                )
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
        NexusType.Success -> "✓"
        NexusType.Warning -> "!"
        NexusType.Danger -> "✕"
        NexusType.Info -> "i"
        NexusType.Primary -> "i"
        NexusType.Default -> null
    }

    // Auto dismiss
    LaunchedEffect(entry.id) {
        if (entry.duration > 0) {
            delay(entry.duration)
            entry.visible = false
            delay(300)
            onDismiss()
        }
    }

    val enterTransition = if (slideFromRight) {
        fadeIn() + slideInHorizontally { it }
    } else {
        fadeIn() + slideInHorizontally { -it }
    }
    val exitTransition = if (slideFromRight) {
        fadeOut() + slideOutHorizontally { it }
    } else {
        fadeOut() + slideOutHorizontally { -it }
    }

    AnimatedVisibility(
        visible = entry.visible,
        enter = enterTransition,
        exit = exitTransition,
    ) {
        Box(
            modifier = Modifier
                .width(330.dp)
                .shadow(shadows.default.elevation, shapes.base)
                .clip(shapes.base)
                .background(colorScheme.fill.blank)
                .padding(16.dp),
        ) {
            Row {
                if (iconText != null && tc != null) {
                    NexusText(
                        text = iconText,
                        color = tc.base,
                        style = typography.large,
                        modifier = Modifier.padding(end = 12.dp),
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    NexusText(
                        text = entry.title,
                        color = colorScheme.text.primary,
                        style = typography.base,
                    )
                    if (entry.message.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(6.dp))
                        NexusText(
                            text = entry.message,
                            color = colorScheme.text.regular,
                            style = typography.small,
                        )
                    }
                }

                NexusText(
                    text = "✕",
                    color = colorScheme.text.placeholder,
                    style = typography.extraSmall,
                    modifier = Modifier
                        .clickable {
                            entry.visible = false
                            onDismiss()
                        }
                        .padding(start = 8.dp),
                )
            }
        }
    }
}
