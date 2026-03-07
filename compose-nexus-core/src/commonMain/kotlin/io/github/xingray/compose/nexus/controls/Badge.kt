package io.github.xingray.compose.nexus.controls

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.github.xingray.compose.nexus.theme.NexusTheme
import io.github.xingray.compose.nexus.theme.NexusType
import io.github.xingray.compose.nexus.theme.typeColor

@Composable
fun NexusBadge(
    value: Any? = "",
    modifier: Modifier = Modifier,
    max: Int = 99,
    isDot: Boolean = false,
    hidden: Boolean = false,
    type: NexusType = NexusType.Danger,
    showZero: Boolean = true,
    color: Color? = null,
    offset: Pair<Int, Int> = 0 to 0,
    contentSlot: (@Composable (String) -> Unit)? = null,
    content: (@Composable () -> Unit)? = null,
) {
    val colorScheme = NexusTheme.colorScheme
    val typography = NexusTheme.typography
    val badgeColor = color ?: (colorScheme.typeColor(type)?.base ?: colorScheme.danger.base)

    val valueText = when (value) {
        is Int -> {
            if (value > max) "${max}+" else value.toString()
        }
        is Long -> {
            if (value > max.toLong()) "${max}+" else value.toString()
        }
        is Float -> {
            val intValue = value.toInt()
            if (value == intValue.toFloat()) {
                if (intValue > max) "${max}+" else intValue.toString()
            } else {
                value.toString()
            }
        }
        is Double -> {
            val intValue = value.toInt()
            if (value == intValue.toDouble()) {
                if (intValue > max) "${max}+" else intValue.toString()
            } else {
                value.toString()
            }
        }
        null -> ""
        else -> value.toString()
    }

    val isZeroValue = when (value) {
        is Int -> value == 0
        is Long -> value == 0L
        is Float -> value == 0f
        is Double -> value == 0.0
        is String -> value == "0"
        else -> false
    }
    val shouldShowBadge = !hidden &&
        (isDot || valueText.isNotEmpty()) &&
        (showZero || !isZeroValue)

    Box(modifier = modifier) {
        if (content != null) {
            content()
        }

        if (shouldShowBadge) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = offset.first.dp, y = offset.second.dp)
                    .clip(if (isDot) CircleShape else RoundedCornerShape(10.dp))
                    .background(badgeColor)
                    .then(
                        if (isDot) {
                            Modifier
                                .defaultMinSize(minWidth = 8.dp, minHeight = 8.dp)
                        } else {
                            Modifier
                                .defaultMinSize(minWidth = 18.dp, minHeight = 18.dp)
                                .padding(horizontal = 6.dp, vertical = 1.dp)
                        }
                    ),
                contentAlignment = Alignment.Center,
            ) {
                if (!isDot) {
                    if (contentSlot != null) {
                        contentSlot(valueText)
                    } else {
                        NexusText(
                            text = valueText,
                            color = colorScheme.white,
                            style = typography.extraSmall,
                            maxLines = 1,
                            overflow = TextOverflow.Clip,
                        )
                    }
                }
            }
        }
    }
}
