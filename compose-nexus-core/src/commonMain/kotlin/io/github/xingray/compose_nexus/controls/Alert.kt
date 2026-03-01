package io.github.xingray.compose_nexus.controls

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.xingray.compose_nexus.theme.NexusTheme
import io.github.xingray.compose_nexus.theme.NexusType
import io.github.xingray.compose_nexus.theme.typeColor

/**
 * Element Plus Alert — a non-overlay feedback component for important messages.
 *
 * @param title Alert title text.
 * @param modifier Modifier.
 * @param type Semantic color type (Success, Warning, Danger, Info).
 * @param description Optional description text below the title.
 * @param closable Whether the alert can be closed.
 * @param showIcon Whether to show a type icon placeholder.
 * @param center Whether to center text.
 * @param onClose Callback when the alert is closed.
 */
@Composable
fun NexusAlert(
    title: String,
    modifier: Modifier = Modifier,
    type: NexusType = NexusType.Info,
    description: String? = null,
    closable: Boolean = true,
    showIcon: Boolean = false,
    center: Boolean = false,
    onClose: (() -> Unit)? = null,
) {
    val colorScheme = NexusTheme.colorScheme
    val typography = NexusTheme.typography
    val shapes = NexusTheme.shapes

    var visible by remember { mutableStateOf(true) }

    val tc = colorScheme.typeColor(type) ?: colorScheme.info
    val bgColor = tc.light9
    val borderColor = tc.light8
    val titleColor = tc.base
    val descriptionColor = tc.light1

    val iconText = when (type) {
        NexusType.Success -> "✓"
        NexusType.Warning -> "!"
        NexusType.Danger -> "✕"
        NexusType.Info, NexusType.Primary -> "i"
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
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = if (description == null) Alignment.CenterVertically else Alignment.Top,
        ) {
            if (showIcon) {
                NexusText(
                    text = iconText,
                    color = titleColor,
                    style = if (description != null) typography.large else typography.base,
                    modifier = Modifier.padding(end = 8.dp),
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = if (center) Alignment.CenterHorizontally else Alignment.Start,
            ) {
                NexusText(
                    text = title,
                    color = titleColor,
                    style = if (description != null) typography.small else typography.extraSmall,
                )
                if (description != null) {
                    NexusText(
                        text = description,
                        color = descriptionColor,
                        style = typography.extraSmall,
                        modifier = Modifier.padding(top = 4.dp),
                    )
                }
            }

            if (closable) {
                Spacer(modifier = Modifier.width(8.dp))
                NexusText(
                    text = "✕",
                    color = titleColor,
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
