package io.github.xingray.compose_nexus.controls

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.xingray.compose_nexus.theme.NexusTheme
import kotlinx.coroutines.delay

data class SkeletonThrottle(
    val leading: Int? = null,
    val trailing: Int? = null,
    val initVal: Boolean = false,
)

enum class SkeletonVariant {
    P,
    Text,
    H1,
    H3,
    Caption,
    Button,
    Image,
    Circle,
    Rect,
}

@Composable
fun NexusSkeleton(
    modifier: Modifier = Modifier,
    loading: Boolean = true,
    animated: Boolean = true,
    count: Int = 1,
    rows: Int = 3,
    avatar: Boolean = false,
    throttle: Int = 0,
    throttleConfig: SkeletonThrottle? = null,
    template: (@Composable (key: Int) -> Unit)? = null,
    content: (@Composable () -> Unit)? = null,
) {
    val leading = throttleConfig?.leading ?: throttle
    val trailing = throttleConfig?.trailing ?: 0
    var firstRender by remember { mutableStateOf(true) }
    var visibleLoading by remember {
        mutableStateOf(if (throttleConfig?.initVal == true) loading else loading && leading <= 0)
    }

    LaunchedEffect(loading, leading, trailing, throttleConfig?.initVal) {
        if (loading) {
            val shouldSkipLeading = firstRender && throttleConfig?.initVal == true
            if (!shouldSkipLeading && leading > 0) delay(leading.toLong())
            visibleLoading = true
        } else {
            if (trailing > 0) delay(trailing.toLong())
            visibleLoading = false
        }
        firstRender = false
    }

    if (!visibleLoading && content != null) {
        content()
        return
    }

    val baseColor = NexusTheme.colorScheme.fill.base
    val shimmerAlpha = if (animated) {
        val transition = rememberInfiniteTransition(label = "skeleton")
        val alpha by transition.animateFloat(
            initialValue = 1f,
            targetValue = 0.4f,
            animationSpec = infiniteRepeatable(
                animation = tween(1500, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse,
            ),
            label = "skeleton-alpha",
        )
        alpha
    } else {
        1f
    }
    val color = baseColor.copy(alpha = shimmerAlpha)

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        repeat(count.coerceAtLeast(1)) { index ->
            if (template != null) {
                template(index)
            } else {
                DefaultSkeletonTemplate(
                    rows = rows,
                    avatar = avatar,
                    color = color,
                )
            }
        }
    }
}

@Composable
fun NexusSkeletonItem(
    variant: SkeletonVariant = SkeletonVariant.Text,
    modifier: Modifier = Modifier,
    color: Color? = null,
) {
    val resolvedColor = color ?: NexusTheme.colorScheme.fill.base
    val (w, h, circle) = when (variant) {
        SkeletonVariant.P, SkeletonVariant.Text -> Triple(1f, 14.dp, false)
        SkeletonVariant.H1 -> Triple(0.5f, 26.dp, false)
        SkeletonVariant.H3 -> Triple(0.35f, 20.dp, false)
        SkeletonVariant.Caption -> Triple(0.24f, 12.dp, false)
        SkeletonVariant.Button -> Triple(0.22f, 32.dp, false)
        SkeletonVariant.Image -> Triple(1f, 120.dp, false)
        SkeletonVariant.Circle -> Triple(0f, 40.dp, true)
        SkeletonVariant.Rect -> Triple(1f, 56.dp, false)
    }
    val shape = if (circle) CircleShape else NexusTheme.shapes.base

    Box(
        modifier = modifier
            .then(
                if (circle) {
                    Modifier.size(h)
                } else {
                    Modifier
                        .fillMaxWidth(w)
                        .height(h)
                }
            )
            .clip(shape)
            .background(resolvedColor),
    )
}

@Composable
private fun DefaultSkeletonTemplate(
    rows: Int,
    avatar: Boolean,
    color: Color,
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        if (avatar) {
            NexusSkeletonItem(
                variant = SkeletonVariant.Circle,
                color = color,
            )
            Spacer(modifier = Modifier.width(16.dp))
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            // Title row, always rendered; rows define the additional rows.
            NexusSkeletonItem(
                variant = SkeletonVariant.H3,
                modifier = Modifier.fillMaxWidth(0.35f),
                color = color,
            )
            repeat(rows.coerceAtLeast(0)) { index ->
                val width = if (index == rows - 1) 0.6f else 1f
                NexusSkeletonItem(
                    variant = SkeletonVariant.Text,
                    modifier = Modifier.fillMaxWidth(width),
                    color = color,
                )
            }
        }
    }
}
