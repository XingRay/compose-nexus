package io.github.xingray.compose_nexus.controls

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import io.github.xingray.compose_nexus.theme.NexusTheme
import io.github.xingray.compose_nexus.theme.NexusType
import io.github.xingray.compose_nexus.theme.typeColor

enum class AlertEffect {
    Light,
    Dark,
}

/**
 * Element Plus Alert — a non-overlay feedback component for important messages.
 *
 * @param title Alert title text.
 * @param modifier Modifier.
 * @param type Semantic color type (Primary, Success, Warning, Danger, Info).
 * @param description Optional description text below the title.
 * @param closable Whether the alert can be closed.
 * @param center Whether to center text.
 * @param closeText Custom close button text.
 * @param showIcon Whether to show a type icon placeholder.
 * @param effect Alert visual theme, light or dark.
 * @param onClose Callback when the alert is closed.
 * @param titleContent Optional slot to customize title.
 * @param descriptionContent Optional slot to customize description.
 * @param icon Optional slot to customize icon.
 */
@Composable
fun NexusAlert(
    title: String,
    modifier: Modifier = Modifier,
    type: NexusType = NexusType.Info,
    description: String? = null,
    closable: Boolean = true,
    center: Boolean = false,
    closeText: String? = null,
    showIcon: Boolean = false,
    effect: AlertEffect = AlertEffect.Light,
    onClose: (() -> Unit)? = null,
    titleContent: (@Composable () -> Unit)? = null,
    descriptionContent: (@Composable () -> Unit)? = null,
    icon: (@Composable () -> Unit)? = null,
) {
    val colorScheme = NexusTheme.colorScheme
    val typography = NexusTheme.typography
    val shapes = NexusTheme.shapes

    var visible by remember { mutableStateOf(true) }

    val tc = colorScheme.typeColor(type) ?: colorScheme.info
    val hasDescription = description != null || descriptionContent != null
    val bgColor = if (effect == AlertEffect.Light) tc.light9 else tc.base
    val borderColor = if (effect == AlertEffect.Light) tc.light8 else tc.base
    val titleColor = if (effect == AlertEffect.Light) tc.base else colorScheme.white
    val descriptionColor = if (effect == AlertEffect.Light) tc.light1 else colorScheme.white.copy(alpha = 0.86f)
    val closeColor = if (effect == AlertEffect.Light) tc.base else colorScheme.white

    val iconText = when (type) {
        NexusType.Primary -> "i"
        NexusType.Success -> "✓"
        NexusType.Warning -> "!"
        NexusType.Danger -> "✕"
        NexusType.Info -> "i"
        NexusType.Default -> "i"
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(),
        exit = fadeOut() + shrinkVertically(),
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .clip(shapes.base)
                .background(bgColor)
                .border(1.dp, borderColor, shapes.base)
                .padding(horizontal = 16.dp, vertical = if (hasDescription) 10.dp else 8.dp),
            verticalAlignment = if (hasDescription) Alignment.Top else Alignment.CenterVertically,
        ) {
            if (showIcon) {
                Row(modifier = Modifier.padding(end = 8.dp)) {
                    if (icon != null) {
                        icon()
                    } else {
                        NexusText(
                            text = iconText,
                            color = titleColor,
                            style = if (hasDescription) typography.large else typography.base,
                        )
                    }
                }
            }

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = if (center) Alignment.CenterHorizontally else Alignment.Start,
            ) {
                if (titleContent != null) {
                    titleContent()
                } else {
                    NexusText(
                        text = title,
                        color = titleColor,
                        style = if (hasDescription) typography.small else typography.extraSmall,
                    )
                }
                if (descriptionContent != null || description != null) {
                    Row(modifier = Modifier.padding(top = 4.dp)) {
                        if (descriptionContent != null) {
                            descriptionContent()
                        } else {
                            NexusText(
                                text = description.orEmpty(),
                                color = descriptionColor,
                                style = typography.extraSmall,
                            )
                        }
                    }
                }
            }

            if (closable) {
                Spacer(modifier = Modifier.width(8.dp))
                NexusText(
                    text = closeText ?: "✕",
                    color = closeColor,
                    style = typography.extraSmall,
                    modifier = Modifier.clickable {
                        visible = false
                        onClose?.invoke()
                    },
                )
            }
        }
    }
}
