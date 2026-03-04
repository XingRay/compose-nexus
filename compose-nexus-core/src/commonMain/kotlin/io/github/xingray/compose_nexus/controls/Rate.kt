package io.github.xingray.compose_nexus.controls

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.github.xingray.compose_nexus.theme.ComponentSize
import io.github.xingray.compose_nexus.theme.NexusTheme
import kotlin.math.abs
import kotlin.math.ceil

@Composable
fun NexusRate(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    max: Int = 5,
    size: ComponentSize = ComponentSize.Default,
    disabled: Boolean = false,
    allowHalf: Boolean = false,
    lowThreshold: Int = 2,
    highThreshold: Int = 4,
    colors: List<Color> = listOf(
        Color(0xFFF7BA2A),
        Color(0xFFF7BA2A),
        Color(0xFFF7BA2A),
    ),
    voidColor: Color = Color(0xFFC6D1DE),
    disabledVoidColor: Color = Color(0xFFEFF2F7),
    icons: List<String> = listOf("★", "★", "★"),
    voidIcon: String = "☆",
    disabledVoidIcon: String = "★",
    showText: Boolean = false,
    showScore: Boolean = false,
    textColor: Color = NexusTheme.colorScheme.text.regular,
    texts: List<String> = listOf("Extremely bad", "Disappointed", "Fair", "Satisfied", "Surprise"),
    scoreTemplate: String = "{value}",
    clearable: Boolean = false,
    onChange: ((Float) -> Unit)? = null,
) {
    val colorScheme = NexusTheme.colorScheme
    val typography = NexusTheme.typography
    var hoverValue by remember { mutableFloatStateOf(0f) }
    val displayValue = if (!disabled && hoverValue > 0f) hoverValue else value
    val starSize = when (size) {
        ComponentSize.Large -> 24.dp
        ComponentSize.Default -> 20.dp
        ComponentSize.Small -> 16.dp
    }
    val levelColor = when {
        displayValue <= lowThreshold -> colors.getOrElse(0) { colors.firstOrNull() ?: Color(0xFFF7BA2A) }
        displayValue < highThreshold -> colors.getOrElse(1) { colors.lastOrNull() ?: Color(0xFFF7BA2A) }
        else -> colors.getOrElse(2) { colors.lastOrNull() ?: Color(0xFFF7BA2A) }
    }
    val levelIcon = when {
        displayValue <= lowThreshold -> icons.getOrElse(0) { "★" }
        displayValue < highThreshold -> icons.getOrElse(1) { "★" }
        else -> icons.getOrElse(2) { "★" }
    }

    fun commit(next: Float) {
        val normalized = next.coerceIn(0f, max.toFloat())
        onValueChange(normalized)
        onChange?.invoke(normalized)
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            repeat(max) { idx ->
                val score = idx + 1
                val starValue = displayValue - idx
                val fraction = starValue.coerceIn(0f, 1f)

                Box(
                    modifier = Modifier.size(starSize),
                    contentAlignment = Alignment.Center,
                ) {
                    NexusText(
                        text = if (disabled) disabledVoidIcon else voidIcon,
                        color = if (disabled) disabledVoidColor else voidColor,
                        style = typography.large,
                    )
                    if (fraction > 0f) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(if (allowHalf) fraction else 1f)
                                .clipToBounds(),
                            contentAlignment = Alignment.CenterStart,
                        ) {
                            NexusText(
                                text = levelIcon,
                                color = if (disabled) levelColor.copy(alpha = 0.6f) else levelColor,
                                style = typography.large,
                            )
                        }
                    }

                    if (!disabled) {
                        if (allowHalf) {
                            Row(modifier = Modifier.fillMaxWidth()) {
                                Box(
                                    modifier = Modifier
                                        .width(starSize / 2)
                                        .fillMaxHeight()
                                        .clickable {
                                            val target = score - 0.5f
                                            if (clearable && abs(value - target) < 0.001f) commit(0f) else commit(target)
                                        }
                                        .pointerHoverIcon(PointerIcon.Hand),
                                )
                                Box(
                                    modifier = Modifier
                                        .width(starSize / 2)
                                        .fillMaxHeight()
                                        .clickable {
                                            val target = score.toFloat()
                                            if (clearable && abs(value - target) < 0.001f) commit(0f) else commit(target)
                                        }
                                        .pointerHoverIcon(PointerIcon.Hand),
                                )
                            }
                        } else {
                            Box(
                                modifier = Modifier
                                    .matchParentSize()
                                    .clickable {
                                        val target = score.toFloat()
                                        if (clearable && abs(value - target) < 0.001f) commit(0f) else commit(target)
                                    }
                                    .pointerHoverIcon(PointerIcon.Hand),
                            )
                        }
                    }
                }
            }
        }

        if (showText || showScore) {
            val text = if (showScore) {
                val scoreValue = if (value % 1f == 0f) {
                    value.toInt().toString()
                } else {
                    oneDecimal(value)
                }
                scoreTemplate.replace("{value}", scoreValue)
            } else {
                val index = ceil(value.coerceAtLeast(1f)).toInt().coerceIn(1, texts.size) - 1
                texts.getOrElse(index) { "" }
            }
            NexusText(
                text = text,
                color = if (disabled) colorScheme.text.disabled else textColor,
                style = typography.small.merge(androidx.compose.ui.text.TextStyle(textAlign = TextAlign.Start)),
                modifier = Modifier
                    .width(140.dp)
                    .background(Color.Transparent, CircleShape),
            )
        }
    }
}

private fun oneDecimal(value: Float): String {
    val scaled = (value * 10f).toInt() / 10f
    val integerPart = scaled.toInt()
    val decimalPart = ((scaled - integerPart) * 10f).toInt()
    return "$integerPart.$decimalPart"
}
