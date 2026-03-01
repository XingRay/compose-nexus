package io.github.xingray.compose_nexus.containers

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.xingray.compose_nexus.controls.NexusText
import io.github.xingray.compose_nexus.theme.NexusTheme
import kotlinx.coroutines.delay

/**
 * Carousel state holder.
 */
@Stable
class CarouselState(
    initialIndex: Int = 0,
    val itemCount: Int,
) {
    var currentIndex by mutableIntStateOf(initialIndex.coerceIn(0, (itemCount - 1).coerceAtLeast(0)))
        private set

    private var lastDirection by mutableStateOf(1) // 1 = forward, -1 = backward

    fun goTo(index: Int) {
        val newIndex = index.coerceIn(0, (itemCount - 1).coerceAtLeast(0))
        lastDirection = if (newIndex >= currentIndex) 1 else -1
        currentIndex = newIndex
    }

    fun next() {
        lastDirection = 1
        currentIndex = (currentIndex + 1) % itemCount
    }

    fun prev() {
        lastDirection = -1
        currentIndex = (currentIndex - 1 + itemCount) % itemCount
    }

    internal fun direction(): Int = lastDirection
}

@Composable
fun rememberCarouselState(
    initialIndex: Int = 0,
    itemCount: Int,
): CarouselState = remember(itemCount) {
    CarouselState(initialIndex, itemCount)
}

/**
 * Element Plus Carousel — a rotating content display.
 *
 * @param state Carousel state.
 * @param modifier Modifier.
 * @param height Carousel height.
 * @param autoplay Whether to auto-advance slides.
 * @param interval Auto-advance interval in milliseconds.
 * @param showIndicators Whether to show dot indicators.
 * @param showArrows Whether to show prev/next arrows.
 * @param content Composable content for each slide index.
 */
@Composable
fun NexusCarousel(
    state: CarouselState,
    modifier: Modifier = Modifier,
    height: Dp = 300.dp,
    autoplay: Boolean = true,
    interval: Long = 3000L,
    showIndicators: Boolean = true,
    showArrows: Boolean = true,
    content: @Composable (index: Int) -> Unit,
) {
    val colorScheme = NexusTheme.colorScheme
    val shapes = NexusTheme.shapes
    val typography = NexusTheme.typography

    // Autoplay
    if (autoplay && state.itemCount > 1) {
        LaunchedEffect(state.currentIndex) {
            delay(interval)
            state.next()
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(shapes.base)
            .background(colorScheme.fill.base),
    ) {
        // Slide content with animation
        val direction = state.direction()
        AnimatedContent(
            targetState = state.currentIndex,
            transitionSpec = {
                if (direction > 0) {
                    (slideInHorizontally { it } + fadeIn())
                        .togetherWith(slideOutHorizontally { -it } + fadeOut())
                } else {
                    (slideInHorizontally { -it } + fadeIn())
                        .togetherWith(slideOutHorizontally { it } + fadeOut())
                }
            },
            label = "carousel",
        ) { index ->
            Box(modifier = Modifier.fillMaxSize()) {
                content(index)
            }
        }

        // Arrows
        if (showArrows && state.itemCount > 1) {
            // Left arrow
            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 8.dp)
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(colorScheme.overlay.lighter)
                    .clickable { state.prev() }
                    .pointerHoverIcon(PointerIcon.Hand),
                contentAlignment = Alignment.Center,
            ) {
                NexusText(
                    text = "‹",
                    color = colorScheme.white,
                    style = typography.large,
                )
            }

            // Right arrow
            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 8.dp)
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(colorScheme.overlay.lighter)
                    .clickable { state.next() }
                    .pointerHoverIcon(PointerIcon.Hand),
                contentAlignment = Alignment.Center,
            ) {
                NexusText(
                    text = "›",
                    color = colorScheme.white,
                    style = typography.large,
                )
            }
        }

        // Indicators
        if (showIndicators && state.itemCount > 1) {
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                repeat(state.itemCount) { index ->
                    val isActive = index == state.currentIndex
                    Box(
                        modifier = Modifier
                            .size(
                                width = if (isActive) 20.dp else 8.dp,
                                height = 8.dp,
                            )
                            .clip(CircleShape)
                            .background(
                                if (isActive) colorScheme.white
                                else colorScheme.white.copy(alpha = 0.5f)
                            )
                            .clickable { state.goTo(index) }
                            .pointerHoverIcon(PointerIcon.Hand),
                    )
                }
            }
        }
    }
}
