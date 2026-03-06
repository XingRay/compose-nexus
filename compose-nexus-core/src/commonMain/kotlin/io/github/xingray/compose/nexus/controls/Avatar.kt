package io.github.xingray.compose.nexus.controls

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
import io.github.xingray.compose.nexus.foundation.ProvideContentColorTextStyle
import io.github.xingray.compose.nexus.theme.NexusTheme

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
    shape: io.github.xingray.compose.nexus.controls.AvatarShape = _root_ide_package_.io.github.xingray.compose.nexus.controls.AvatarShape.Circle,
    backgroundColor: Color = Color(0xFFC0C4CC),
    icon: (@Composable () -> Unit)? = null,
    src: String? = null,
    srcSet: String? = null,
    alt: String = "",
    fit: io.github.xingray.compose.nexus.controls.AvatarFit = _root_ide_package_.io.github.xingray.compose.nexus.controls.AvatarFit.Cover,
    onError: ((String?) -> Unit)? = null,
    content: (@Composable () -> Unit)? = null,
) {
    @Suppress("UNUSED_VARIABLE")
    val _srcSet = srcSet
    @Suppress("UNUSED_VARIABLE")
    val _fit: ContentScale = when (fit) {
        _root_ide_package_.io.github.xingray.compose.nexus.controls.AvatarFit.Fill -> ContentScale.FillBounds
        _root_ide_package_.io.github.xingray.compose.nexus.controls.AvatarFit.Contain -> ContentScale.Fit
        _root_ide_package_.io.github.xingray.compose.nexus.controls.AvatarFit.Cover -> ContentScale.Crop
        _root_ide_package_.io.github.xingray.compose.nexus.controls.AvatarFit.None -> ContentScale.None
        _root_ide_package_.io.github.xingray.compose.nexus.controls.AvatarFit.ScaleDown -> ContentScale.Inside
    }

    val clipShape: Shape = when (shape) {
        _root_ide_package_.io.github.xingray.compose.nexus.controls.AvatarShape.Circle -> CircleShape
        _root_ide_package_.io.github.xingray.compose.nexus.controls.AvatarShape.Square -> _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.shapes.base
    }
    val colorScheme = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.colorScheme

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
        _root_ide_package_.io.github.xingray.compose.nexus.foundation.ProvideContentColorTextStyle(
            contentColor = colorScheme.white,
            textStyle = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.typography.base,
        ) {
            when {
                hasImage && !imageError -> {
                    // Placeholder image rendering in common module.
                    _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
                        text = "IMG",
                        style = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.typography.extraSmall,
                        color = colorScheme.white.copy(alpha = 0.9f),
                    )
                }

                icon != null -> icon()
                content != null -> content()
                alt.isNotBlank() -> {
                    _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
                        text = alt.take(2).uppercase(),
                        maxLines = 1,
                        overflow = TextOverflow.Clip,
                    )
                }

                else -> {
                    _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(text = "?")
                }
            }
        }
    }
}

@Composable
fun NexusAvatarGroup(
    items: List<io.github.xingray.compose.nexus.controls.AvatarGroupItem>,
    modifier: Modifier = Modifier,
    size: Dp = 40.dp,
    shape: io.github.xingray.compose.nexus.controls.AvatarShape = _root_ide_package_.io.github.xingray.compose.nexus.controls.AvatarShape.Circle,
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
            _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusAvatar(
                modifier = Modifier.offset(x = (index * 0).dp),
                size = size,
                shape = shape,
                backgroundColor = item.backgroundColor,
                icon = item.icon,
                src = item.src,
                alt = item.alt,
                content = {
                    if (!item.text.isNullOrBlank()) {
                        _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(text = item.text)
                    } else if (item.alt.isNotBlank()) {
                        _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(text = item.alt.take(2).uppercase())
                    }
                },
            )
        }
        if (hidden > 0) {
            _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusAvatar(
                size = size,
                shape = shape,
                backgroundColor = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.colorScheme.fill.dark,
                content = {
                    _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(text = if (tooltipText.isNullOrBlank()) "+$hidden" else "+$hidden($tooltipText)")
                },
            )
        }
    }
}
