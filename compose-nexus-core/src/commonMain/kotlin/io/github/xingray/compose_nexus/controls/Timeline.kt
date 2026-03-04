package io.github.xingray.compose_nexus.controls

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import io.github.xingray.compose_nexus.theme.NexusTheme
import io.github.xingray.compose_nexus.theme.NexusType
import io.github.xingray.compose_nexus.theme.typeColor

enum class TimelineMode {
    Start,
    Alternate,
    AlternateReverse,
    End,
}

enum class TimelinePlacement {
    Top,
    Bottom,
}

enum class TimelineItemSize {
    Normal,
    Large,
}

internal data class TimelineItemData(
    val timestamp: String,
    val hideTimestamp: Boolean,
    val center: Boolean,
    val placement: TimelinePlacement,
    val type: NexusType,
    val color: androidx.compose.ui.graphics.Color?,
    val size: TimelineItemSize,
    val icon: (@Composable () -> Unit)?,
    val hollow: Boolean,
    val dot: (@Composable () -> Unit)?,
    val content: @Composable () -> Unit,
)

@Stable
class TimelineScope internal constructor() {
    internal val items = mutableListOf<TimelineItemData>()

    fun item(
        timestamp: String = "",
        hideTimestamp: Boolean = false,
        center: Boolean = false,
        placement: TimelinePlacement = TimelinePlacement.Bottom,
        type: NexusType = NexusType.Info,
        color: androidx.compose.ui.graphics.Color? = null,
        size: TimelineItemSize = TimelineItemSize.Normal,
        icon: (@Composable () -> Unit)? = null,
        hollow: Boolean = false,
        dot: (@Composable () -> Unit)? = null,
        content: @Composable () -> Unit,
    ) {
        items += TimelineItemData(
            timestamp = timestamp,
            hideTimestamp = hideTimestamp,
            center = center,
            placement = placement,
            type = type,
            color = color,
            size = size,
            icon = icon,
            hollow = hollow,
            dot = dot,
            content = content,
        )
    }
}

@Composable
fun NexusTimeline(
    modifier: Modifier = Modifier,
    reverse: Boolean = false,
    mode: TimelineMode = TimelineMode.Start,
    content: TimelineScope.() -> Unit,
) {
    val scope = TimelineScope().also(content)
    val data = if (reverse) scope.items.reversed() else scope.items

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(0.dp),
    ) {
        data.forEachIndexed { index, item ->
            val alignStart = when (mode) {
                TimelineMode.Start -> true
                TimelineMode.End -> false
                TimelineMode.Alternate -> index % 2 == 0
                TimelineMode.AlternateReverse -> index % 2 == 1
            }
            TimelineRow(
                item = item,
                alignStart = alignStart,
                isLast = index == data.lastIndex,
            )
        }
    }
}

@Composable
private fun TimelineRow(
    item: TimelineItemData,
    alignStart: Boolean,
    isLast: Boolean,
) {
    val colorScheme = NexusTheme.colorScheme
    val typography = NexusTheme.typography
    val lineColor = colorScheme.border.light
    val dotColor = item.color ?: (colorScheme.typeColor(item.type)?.base ?: colorScheme.info.base)
    val dotSize = when (item.size) {
        TimelineItemSize.Normal -> 12.dp
        TimelineItemSize.Large -> 16.dp
    }

    @Composable
    fun DotColumn() {
        Column(
            modifier = Modifier.width(26.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (item.dot != null) {
                item.dot()
            } else {
                Box(
                    modifier = Modifier
                        .size(dotSize)
                        .clip(CircleShape)
                        .background(if (item.hollow) colorScheme.fill.blank else dotColor)
                        .then(
                            if (item.hollow) Modifier
                                .background(colorScheme.fill.blank, CircleShape)
                                .padding(1.dp)
                                .clip(CircleShape)
                                .background(dotColor.copy(alpha = 0.18f))
                            else Modifier,
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    if (item.icon != null) {
                        item.icon()
                    }
                }
            }

            if (!isLast) {
                Box(
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .width(1.dp)
                        .height(56.dp)
                        .background(lineColor),
                )
            }
        }
    }

    @Composable
    fun ItemContent(modifier: Modifier = Modifier) {
        Column(
            modifier = modifier
                .defaultMinSize(minHeight = 38.dp)
                .padding(bottom = if (isLast) 0.dp else 6.dp),
            verticalArrangement = if (item.center) Arrangement.Center else Arrangement.Top,
        ) {
            if (!item.hideTimestamp && item.placement == TimelinePlacement.Top && item.timestamp.isNotBlank()) {
                NexusText(
                    text = item.timestamp,
                    color = colorScheme.text.secondary,
                    style = typography.extraSmall,
                )
                Spacer(modifier = Modifier.height(4.dp))
            }

            item.content()

            if (!item.hideTimestamp && item.placement == TimelinePlacement.Bottom && item.timestamp.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                NexusText(
                    text = item.timestamp,
                    color = colorScheme.text.secondary,
                    style = typography.extraSmall,
                )
            }
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = if (item.center) Alignment.CenterVertically else Alignment.Top,
    ) {
        if (alignStart) {
            DotColumn()
            ItemContent(modifier = Modifier.weight(1f).padding(start = 10.dp))
        } else {
            ItemContent(modifier = Modifier.weight(1f).padding(end = 10.dp))
            DotColumn()
        }
    }
}
