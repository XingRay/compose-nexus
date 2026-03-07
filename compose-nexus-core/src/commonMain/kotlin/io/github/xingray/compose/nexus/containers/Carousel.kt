package io.github.xingray.compose.nexus.containers

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.xingray.compose.nexus.controls.NexusText
import io.github.xingray.compose.nexus.theme.NexusTheme
import kotlinx.coroutines.delay

enum class CarouselTrigger {
    Hover,
    Click,
}

enum class CarouselIndicatorPosition {
    Inside,
    Outside,
    None,
}

enum class CarouselArrow {
    Always,
    Hover,
    Never,
}

enum class CarouselType {
    Default,
    Card,
}

enum class CarouselDirection {
    Horizontal,
    Vertical,
}

@Stable
class CarouselState(
    initialIndex: Int = 0,
    val itemCount: Int,
) {
    var currentIndex by mutableIntStateOf(initialIndex.coerceIn(0, (itemCount - 1).coerceAtLeast(0)))
        private set
    var previousIndex by mutableIntStateOf(currentIndex)
        private set
    private var lastDirection by mutableStateOf(1)

    val activeIndex: Int
        get() = currentIndex

    fun goTo(index: Int) {
        if (itemCount <= 0) return
        val next = index.coerceIn(0, itemCount - 1)
        if (next == currentIndex) return
        previousIndex = currentIndex
        lastDirection = if (next > currentIndex) 1 else -1
        currentIndex = next
    }

    fun setActiveItem(index: Int) {
        goTo(index)
    }

    fun setActiveItem(indexOrName: String) {
        indexOrName.toIntOrNull()?.let { goTo(it) }
    }

    fun next(loop: Boolean = true) {
        if (itemCount <= 1) return
        if (currentIndex == itemCount - 1) {
            if (!loop) return
            previousIndex = currentIndex
            lastDirection = 1
            currentIndex = 0
        } else {
            previousIndex = currentIndex
            lastDirection = 1
            currentIndex++
        }
    }

    fun prev(loop: Boolean = true) {
        if (itemCount <= 1) return
        if (currentIndex == 0) {
            if (!loop) return
            previousIndex = currentIndex
            lastDirection = -1
            currentIndex = itemCount - 1
        } else {
            previousIndex = currentIndex
            lastDirection = -1
            currentIndex--
        }
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

@Composable
fun NexusCarousel(
    state: CarouselState,
    modifier: Modifier = Modifier,
    height: Dp? = 300.dp,
    autoplay: Boolean = true,
    interval: Long = 3000L,
    showIndicators: Boolean = true,
    showArrows: Boolean = true,
    trigger: CarouselTrigger = CarouselTrigger.Hover,
    indicatorPosition: CarouselIndicatorPosition = CarouselIndicatorPosition.Inside,
    arrow: CarouselArrow = CarouselArrow.Hover,
    type: CarouselType = CarouselType.Default,
    cardScale: Float = 0.83f,
    loop: Boolean = true,
    direction: CarouselDirection = CarouselDirection.Horizontal,
    pauseOnHover: Boolean = true,
    motionBlur: Boolean = false,
    onChange: ((current: Int, prev: Int) -> Unit)? = null,
    content: @Composable (index: Int) -> Unit,
) {
    val colorScheme = NexusTheme.colorScheme
    val typography = NexusTheme.typography
    val shapes = NexusTheme.shapes
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    fun emitChangeIfNeeded(prev: Int) {
        if (prev != state.currentIndex) {
            onChange?.invoke(state.currentIndex, prev)
        }
    }

    fun goPrev() {
        val prev = state.currentIndex
        state.prev(loop = loop)
        emitChangeIfNeeded(prev)
    }

    fun goNext() {
        val prev = state.currentIndex
        state.next(loop = loop)
        emitChangeIfNeeded(prev)
    }

    fun goTo(index: Int) {
        val prev = state.currentIndex
        state.goTo(index)
        emitChangeIfNeeded(prev)
    }

    if (autoplay && state.itemCount > 1 && (!pauseOnHover || !isHovered)) {
        LaunchedEffect(state.currentIndex, autoplay, interval, isHovered, pauseOnHover, loop) {
            delay(interval)
            goNext()
        }
    }

    val showArrowByMode = when (arrow) {
        CarouselArrow.Always -> true
        CarouselArrow.Hover -> isHovered
        CarouselArrow.Never -> false
    }
    val shouldShowArrows = showArrows && showArrowByMode && state.itemCount > 1
    val shouldShowIndicators = showIndicators &&
        indicatorPosition != CarouselIndicatorPosition.None &&
        state.itemCount > 1

    Column(modifier = modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .then(if (height != null) Modifier.height(height) else Modifier)
                .clip(shapes.base)
                .background(colorScheme.fill.base)
                .hoverable(interactionSource),
        ) {
            if (type == CarouselType.Card && direction == CarouselDirection.Horizontal && state.itemCount > 1) {
                val prevIndex = previousCardIndex(state.currentIndex, state.itemCount, loop)
                val nextIndex = nextCardIndex(state.currentIndex, state.itemCount, loop)

                Row(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    CarouselCard(
                        modifier = Modifier.weight(1f),
                        scale = cardScale,
                        alpha = 0.66f,
                        clickable = prevIndex != state.currentIndex,
                        onClick = { goTo(prevIndex) },
                    ) {
                        content(prevIndex)
                    }
                    CarouselCard(
                        modifier = Modifier.weight(1.2f),
                        scale = 1f,
                        alpha = 1f,
                        clickable = false,
                        onClick = {},
                    ) {
                        content(state.currentIndex)
                    }
                    CarouselCard(
                        modifier = Modifier.weight(1f),
                        scale = cardScale,
                        alpha = 0.66f,
                        clickable = nextIndex != state.currentIndex,
                        onClick = { goTo(nextIndex) },
                    ) {
                        content(nextIndex)
                    }
                }
            } else {
                val transitionModifier = if (motionBlur) Modifier.blur(1.5.dp) else Modifier
                val directionSign = state.direction()

                AnimatedContent(
                    targetState = state.currentIndex,
                    transitionSpec = {
                        when (direction) {
                            CarouselDirection.Horizontal -> {
                                if (directionSign > 0) {
                                    (slideInHorizontally { it } + fadeIn())
                                        .togetherWith(slideOutHorizontally { -it } + fadeOut())
                                } else {
                                    (slideInHorizontally { -it } + fadeIn())
                                        .togetherWith(slideOutHorizontally { it } + fadeOut())
                                }
                            }

                            CarouselDirection.Vertical -> {
                                if (directionSign > 0) {
                                    (slideInVertically { it } + fadeIn())
                                        .togetherWith(slideOutVertically { -it } + fadeOut())
                                } else {
                                    (slideInVertically { -it } + fadeIn())
                                        .togetherWith(slideOutVertically { it } + fadeOut())
                                }
                            }
                        }
                    },
                    label = "carousel",
                    modifier = Modifier.fillMaxSize().then(transitionModifier),
                ) { index ->
                    Box(modifier = Modifier.fillMaxSize()) {
                        content(index)
                    }
                }
            }

            if (shouldShowArrows) {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 8.dp)
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(colorScheme.overlay.lighter)
                        .clickable { goPrev() }
                        .pointerHoverIcon(PointerIcon.Hand),
                    contentAlignment = Alignment.Center,
                ) {
                    NexusText(
                        text = if (direction == CarouselDirection.Horizontal) "‹" else "˄",
                        color = colorScheme.white,
                        style = typography.large,
                    )
                }
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 8.dp)
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(colorScheme.overlay.lighter)
                        .clickable { goNext() }
                        .pointerHoverIcon(PointerIcon.Hand),
                    contentAlignment = Alignment.Center,
                ) {
                    NexusText(
                        text = if (direction == CarouselDirection.Horizontal) "›" else "˅",
                        color = colorScheme.white,
                        style = typography.large,
                    )
                }
            }

            if (shouldShowIndicators && indicatorPosition == CarouselIndicatorPosition.Inside) {
                IndicatorRow(
                    state = state,
                    trigger = trigger,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 12.dp),
                    onSelect = { goTo(it) },
                )
            }
        }

        if (shouldShowIndicators && indicatorPosition == CarouselIndicatorPosition.Outside) {
            IndicatorRow(
                state = state,
                trigger = trigger,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                onSelect = { goTo(it) },
            )
        }
    }
}

@Composable
private fun CarouselCard(
    modifier: Modifier,
    scale: Float,
    alpha: Float,
    clickable: Boolean,
    onClick: () -> Unit,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                this.alpha = alpha
            }
            .fillMaxSize()
            .then(
                if (clickable) {
                    Modifier
                        .clickable(onClick = onClick)
                        .pointerHoverIcon(PointerIcon.Hand)
                } else {
                    Modifier
                }
            ),
    ) {
        content()
    }
}

@Composable
private fun IndicatorRow(
    state: CarouselState,
    trigger: CarouselTrigger,
    modifier: Modifier = Modifier,
    onSelect: (Int) -> Unit,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        repeat(state.itemCount) { index ->
            val isActive = index == state.currentIndex
            val interactionSource = remember(index) { MutableInteractionSource() }
            val isHovered by interactionSource.collectIsHoveredAsState()

            if (trigger == CarouselTrigger.Hover) {
                LaunchedEffect(isHovered) {
                    if (isHovered) onSelect(index)
                }
            }

            Box(
                modifier = Modifier
                    .width(if (isActive) 20.dp else 8.dp)
                    .height(8.dp)
                    .clip(CircleShape)
                    .background(
                        if (isActive) NexusTheme.colorScheme.primary.base
                        else NexusTheme.colorScheme.text.placeholder.copy(alpha = 0.6f),
                    )
                    .then(
                        if (trigger == CarouselTrigger.Click) {
                            Modifier
                                .clickable { onSelect(index) }
                                .pointerHoverIcon(PointerIcon.Hand)
                        } else {
                            Modifier
                                .hoverable(interactionSource)
                                .pointerHoverIcon(PointerIcon.Hand)
                        }
                    ),
            )
        }
    }
}

private fun previousCardIndex(current: Int, itemCount: Int, loop: Boolean): Int {
    if (itemCount <= 1) return 0
    return when {
        current > 0 -> current - 1
        loop -> itemCount - 1
        else -> 0
    }
}

private fun nextCardIndex(current: Int, itemCount: Int, loop: Boolean): Int {
    if (itemCount <= 1) return 0
    return when {
        current < itemCount - 1 -> current + 1
        loop -> 0
        else -> itemCount - 1
    }
}
