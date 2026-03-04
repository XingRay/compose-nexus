package io.github.xingray.compose_nexus.controls

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.xingray.compose_nexus.foundation.ProvideContentColorTextStyle
import io.github.xingray.compose_nexus.theme.NexusTheme

enum class AvatarShape {
    Circle,
    Square,
}

enum class AvatarFit {
    Fill,
    Contain,
    Cover,
    None,
    ScaleDown,
}

data class AvatarGroupItem(
    val text: String? = null,
    val src: String? = null,
    val alt: String = "",
    val backgroundColor: Color = Color(0xFFC0C4CC),
    val icon: (@Composable () -> Unit)? = null,
)

@Composable
fun NexusAvatar(
    modifier: Modifier = Modifier,
    size: Dp = 40.dp,
    shape: AvatarShape = AvatarShape.Circle,
    backgroundColor: Color = Color(0xFFC0C4CC),
    icon: (@Composable () -> Unit)? = null,
    src: String? = null,
    srcSet: String? = null,
    alt: String = "",
    fit: AvatarFit = AvatarFit.Cover,
    onError: ((String?) -> Unit)? = null,
    content: (@Composable () -> Unit)? = null,
) {
    @Suppress("UNUSED_VARIABLE")
    val _srcSet = srcSet
    @Suppress("UNUSED_VARIABLE")
    val _fit: ContentScale = when (fit) {
        AvatarFit.Fill -> ContentScale.FillBounds
        AvatarFit.Contain -> ContentScale.Fit
        AvatarFit.Cover -> ContentScale.Crop
        AvatarFit.None -> ContentScale.None
        AvatarFit.ScaleDown -> ContentScale.Inside
    }

    val clipShape: Shape = when (shape) {
        AvatarShape.Circle -> CircleShape
        AvatarShape.Square -> NexusTheme.shapes.base
    }
    val colorScheme = NexusTheme.colorScheme

    var errorReported by remember(src) { mutableStateOf(false) }
    val hasImage = !src.isNullOrBlank()
    val imageError = hasImage && src.startsWith("error:", ignoreCase = true)
    LaunchedEffect(imageError, src) {
        if (imageError && !errorReported) {
            errorReported = true
            onError?.invoke(src)
        }
    }

    Box(
        modifier = modifier
            .size(size)
            .clip(clipShape)
            .background(backgroundColor),
        contentAlignment = Alignment.Center,
    ) {
        ProvideContentColorTextStyle(
            contentColor = colorScheme.white,
            textStyle = NexusTheme.typography.base,
        ) {
            when {
                hasImage && !imageError -> {
                    // Placeholder image rendering in common module.
                    NexusText(
                        text = "IMG",
                        style = NexusTheme.typography.extraSmall,
                        color = colorScheme.white.copy(alpha = 0.9f),
                    )
                }
                icon != null -> icon()
                content != null -> content()
                alt.isNotBlank() -> {
                    NexusText(
                        text = alt.take(2).uppercase(),
                        maxLines = 1,
                        overflow = TextOverflow.Clip,
                    )
                }
                else -> {
                    NexusText(text = "?")
                }
            }
        }
    }
}

@Composable
fun NexusAvatarGroup(
    items: List<AvatarGroupItem>,
    modifier: Modifier = Modifier,
    size: Dp = 40.dp,
    shape: AvatarShape = AvatarShape.Circle,
    collapseAvatars: Boolean = false,
    collapseAvatarsTooltip: Boolean = false,
    maxCollapseAvatars: Int = 1,
) {
    val visibleItems = if (collapseAvatars && items.size > maxCollapseAvatars.coerceAtLeast(1)) {
        items.take(maxCollapseAvatars.coerceAtLeast(1))
    } else {
        items
    }
    val hidden = (items.size - visibleItems.size).coerceAtLeast(0)
    val tooltipText = if (collapseAvatarsTooltip && hidden > 0) {
        items.drop(visibleItems.size).joinToString(", ") { it.alt.ifBlank { it.text.orEmpty() } }
    } else {
        null
    }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy((-8).dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        visibleItems.forEachIndexed { index, item ->
            NexusAvatar(
                modifier = Modifier.offset(x = (index * 0).dp),
                size = size,
                shape = shape,
                backgroundColor = item.backgroundColor,
                icon = item.icon,
                src = item.src,
                alt = item.alt,
                content = {
                    if (!item.text.isNullOrBlank()) {
                        NexusText(text = item.text)
                    } else if (item.alt.isNotBlank()) {
                        NexusText(text = item.alt.take(2).uppercase())
                    }
                },
            )
        }
        if (hidden > 0) {
            NexusAvatar(
                size = size,
                shape = shape,
                backgroundColor = NexusTheme.colorScheme.fill.dark,
                content = {
                    NexusText(text = if (tooltipText.isNullOrBlank()) "+$hidden" else "+$hidden($tooltipText)")
                },
            )
        }
    }
}
