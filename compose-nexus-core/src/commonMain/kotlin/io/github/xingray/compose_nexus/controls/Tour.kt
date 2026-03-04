package io.github.xingray.compose_nexus.controls

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import io.github.xingray.compose_nexus.theme.NexusTheme
import io.github.xingray.compose_nexus.theme.NexusType
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

enum class TourPlacement {
    Top,
    TopStart,
    TopEnd,
    Bottom,
    BottomStart,
    BottomEnd,
    Left,
    LeftStart,
    LeftEnd,
    Right,
    RightStart,
    RightEnd,
}

enum class TourType {
    Default,
    Primary,
}

data class TourGap(
    val offset: Dp = 6.dp,
    val radius: Dp = 2.dp,
)

/**
 * A single tour step definition.
 * Step-level properties have higher priority than tour-level properties.
 */
data class TourStep(
    val title: String,
    val description: String = "",
    val target: String? = null,
    val showArrow: Boolean? = null,
    val placement: TourPlacement? = null,
    val mask: Boolean? = null,
    val type: TourType? = null,
    val showClose: Boolean? = null,
    val nextButtonText: String? = null,
    val prevButtonText: String? = null,
    val finishButtonText: String? = null,
    val header: (@Composable () -> Unit)? = null,
    val content: (@Composable () -> Unit)? = null,
)

internal data class TourTargetBounds(
    val left: Int,
    val top: Int,
    val right: Int,
    val bottom: Int,
) {
    val width: Int get() = max(0, right - left)
    val height: Int get() = max(0, bottom - top)
    val centerX: Int get() = (left + right) / 2
    val centerY: Int get() = (top + bottom) / 2
}

@Stable
class TourState(
    val steps: List<TourStep>,
) {
    private val targetMap = mutableStateMapOf<String, TourTargetBounds>()

    var currentStep by mutableIntStateOf(0)
        private set
    var isActive by mutableStateOf(false)
        private set

    fun start(step: Int = 0) {
        if (steps.isEmpty()) return
        currentStep = step.coerceIn(0, steps.lastIndex)
        isActive = true
    }

    fun next(): Boolean {
        if (steps.isEmpty()) return false
        return if (currentStep < steps.lastIndex) {
            currentStep++
            false
        } else {
            isActive = false
            true
        }
    }

    fun prev() {
        if (steps.isEmpty()) return
        if (currentStep > 0) currentStep--
    }

    fun finish() {
        isActive = false
    }

    fun goTo(step: Int) {
        if (steps.isEmpty()) return
        currentStep = step.coerceIn(0, steps.lastIndex)
    }

    internal fun registerTarget(target: String, bounds: TourTargetBounds) {
        targetMap[target] = bounds
    }

    internal fun unregisterTarget(target: String) {
        targetMap.remove(target)
    }

    internal fun getTargetBounds(target: String?): TourTargetBounds? {
        if (target.isNullOrBlank()) return null
        return targetMap[target]
    }
}

@Composable
fun rememberTourState(
    steps: List<TourStep>,
): TourState = remember(steps) { TourState(steps) }

/**
 * Register a composable node as a tour target.
 */
@Composable
fun NexusTourTarget(
    state: TourState,
    target: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier.onGloballyPositioned { coordinates ->
            val position = coordinates.positionInWindow()
            val size = coordinates.size
            state.registerTarget(
                target = target,
                bounds = TourTargetBounds(
                    left = position.x.roundToInt(),
                    top = position.y.roundToInt(),
                    right = (position.x + size.width).roundToInt(),
                    bottom = (position.y + size.height).roundToInt(),
                ),
            )
        },
    ) {
        content()
    }
}

/**
 * Element Plus Tour — a guided tour overlay with step-by-step instructions.
 */
@Composable
fun NexusTour(
    state: TourState,
    modifier: Modifier = Modifier,
    showArrow: Boolean = true,
    placement: TourPlacement = TourPlacement.Bottom,
    mask: Boolean = true,
    maskColor: Color? = null,
    gap: TourGap = TourGap(),
    type: TourType = TourType.Default,
    showClose: Boolean = true,
    closeOnPressEscape: Boolean = true,
    targetAreaClickable: Boolean = true,
    indicators: (@Composable (current: Int, total: Int) -> Unit)? = null,
    onClose: ((current: Int) -> Unit)? = null,
    onFinish: (() -> Unit)? = null,
    onChange: ((current: Int) -> Unit)? = null,
) {
    if (!state.isActive || state.steps.isEmpty()) return
    val step = state.steps.getOrNull(state.currentStep) ?: return

    LaunchedEffect(state.isActive, state.currentStep) {
        if (state.isActive) onChange?.invoke(state.currentStep)
    }

    val colorScheme = NexusTheme.colorScheme
    val typography = NexusTheme.typography
    val shapes = NexusTheme.shapes
    val shadows = NexusTheme.shadows

    val effectivePlacement = step.placement ?: placement
    val effectiveShowArrow = step.showArrow ?: showArrow
    val effectiveMask = step.mask ?: mask
    val effectiveType = step.type ?: type
    val effectiveShowClose = step.showClose ?: showClose

    val cardBackground = if (effectiveType == TourType.Primary) colorScheme.primary.base else colorScheme.fill.blank
    val titleColor = if (effectiveType == TourType.Primary) colorScheme.white else colorScheme.text.primary
    val bodyColor = if (effectiveType == TourType.Primary) colorScheme.white.copy(alpha = 0.92f) else colorScheme.text.regular
    val secondaryColor = if (effectiveType == TourType.Primary) colorScheme.white.copy(alpha = 0.72f) else colorScheme.text.secondary
    val overlayColor = maskColor ?: colorScheme.overlay.lighter

    Popup(
        properties = PopupProperties(focusable = closeOnPressEscape),
    ) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val density = androidx.compose.ui.platform.LocalDensity.current
            val viewportWidthPx = with(density) { maxWidth.roundToPx() }
            val viewportHeightPx = with(density) { maxHeight.roundToPx() }
            val gapPx = with(density) { gap.offset.roundToPx() }
            val radiusPx = with(density) { gap.radius.toPx() }
            val marginPx = with(density) { 12.dp.roundToPx() }
            val targetBounds = state.getTargetBounds(step.target)?.inflate(
                offset = gapPx,
                viewportWidth = viewportWidthPx,
                viewportHeight = viewportHeightPx,
            )

            if (effectiveMask) {
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                        ) { },
                ) {
                    drawRect(overlayColor)
                    if (targetBounds != null) {
                        drawRoundRect(
                            color = Color.Transparent,
                            topLeft = Offset(targetBounds.left.toFloat(), targetBounds.top.toFloat()),
                            size = Size(targetBounds.width.toFloat(), targetBounds.height.toFloat()),
                            cornerRadius = CornerRadius(radiusPx, radiusPx),
                            blendMode = BlendMode.Clear,
                        )
                    }
                }
            }

            if (effectiveMask && targetBounds != null) {
                val width = with(density) { targetBounds.width.toDp() }
                val height = with(density) { targetBounds.height.toDp() }
                Box(
                    modifier = Modifier
                        .offset { IntOffset(targetBounds.left, targetBounds.top) }
                        .size(width = width, height = height)
                        .border(
                            width = 1.dp,
                            color = colorScheme.white.copy(alpha = if (targetAreaClickable) 0.9f else 0.6f),
                            shape = RoundedCornerShape(gap.radius),
                        ),
                )
            }

            var cardSize by remember(state.currentStep, state.isActive) { mutableStateOf(IntSize.Zero) }
            val fallbackCardWidth = with(density) { 360.dp.roundToPx() }
            val fallbackCardHeight = with(density) { 220.dp.roundToPx() }
            val cardWidthPx = if (cardSize.width > 0) cardSize.width else fallbackCardWidth
            val cardHeightPx = if (cardSize.height > 0) cardSize.height else fallbackCardHeight
            val cardOffset = remember(
                targetBounds,
                effectivePlacement,
                cardWidthPx,
                cardHeightPx,
                viewportWidthPx,
                viewportHeightPx,
                marginPx,
                gapPx,
            ) {
                calculateTourCardOffset(
                    target = targetBounds,
                    placement = effectivePlacement,
                    cardWidth = cardWidthPx,
                    cardHeight = cardHeightPx,
                    viewportWidth = viewportWidthPx,
                    viewportHeight = viewportHeightPx,
                    margin = marginPx,
                    gap = gapPx,
                )
            }

            Box(
                modifier = modifier
                    .offset { cardOffset }
                    .width(360.dp)
                    .onSizeChanged { cardSize = it }
                    .shadow(shadows.default.elevation, shapes.base)
                    .clip(shapes.base)
                    .background(cardBackground),
            ) {
                if (effectiveShowArrow && targetBounds != null) {
                    TourArrow(
                        placement = effectivePlacement,
                        color = cardBackground,
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                ) {
                    if (effectiveShowClose) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End,
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = null,
                                    ) {
                                        val current = state.currentStep
                                        state.finish()
                                        onClose?.invoke(current)
                                    }
                                    .padding(horizontal = 6.dp, vertical = 2.dp),
                            ) {
                                NexusText(
                                    text = "x",
                                    color = secondaryColor,
                                    style = typography.small,
                                )
                            }
                        }
                    }

                    if (step.header != null) {
                        step.header.invoke()
                    } else {
                        NexusText(
                            text = step.title,
                            color = titleColor,
                            style = typography.large,
                        )
                    }

                    if (step.content != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        step.content.invoke()
                    } else if (step.description.isNotEmpty()) {
                        NexusText(
                            text = step.description,
                            color = bodyColor,
                            style = typography.base,
                            modifier = Modifier.padding(top = 8.dp),
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    if (indicators != null) {
                        indicators(state.currentStep, state.steps.size)
                    } else {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            repeat(state.steps.size) { index ->
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(
                                            if (index == state.currentStep) {
                                                if (effectiveType == TourType.Primary) colorScheme.white
                                                else colorScheme.primary.base
                                            } else {
                                                if (effectiveType == TourType.Primary) colorScheme.white.copy(alpha = 0.35f)
                                                else colorScheme.fill.dark
                                            },
                                        ),
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        if (state.currentStep > 0) {
                            NexusButton(
                                text = step.prevButtonText ?: "Previous",
                                onClick = { state.prev() },
                                size = io.github.xingray.compose_nexus.theme.ComponentSize.Small,
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                        }

                        if (state.currentStep < state.steps.lastIndex) {
                            NexusButton(
                                text = step.nextButtonText ?: "Next",
                                onClick = { state.next() },
                                type = NexusType.Primary,
                                size = io.github.xingray.compose_nexus.theme.ComponentSize.Small,
                            )
                        } else {
                            NexusButton(
                                text = step.finishButtonText ?: "Finish",
                                onClick = {
                                    state.finish()
                                    onFinish?.invoke()
                                },
                                type = NexusType.Primary,
                                size = io.github.xingray.compose_nexus.theme.ComponentSize.Small,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TourArrow(
    placement: TourPlacement,
    color: Color,
) {
    val alignment: Alignment
    val offset: IntOffset

    when (placement) {
        TourPlacement.Top -> {
            alignment = Alignment.BottomCenter
            offset = IntOffset(0, 5)
        }
        TourPlacement.TopStart -> {
            alignment = Alignment.BottomStart
            offset = IntOffset(30, 5)
        }
        TourPlacement.TopEnd -> {
            alignment = Alignment.BottomEnd
            offset = IntOffset(-30, 5)
        }
        TourPlacement.Bottom -> {
            alignment = Alignment.TopCenter
            offset = IntOffset(0, -5)
        }
        TourPlacement.BottomStart -> {
            alignment = Alignment.TopStart
            offset = IntOffset(30, -5)
        }
        TourPlacement.BottomEnd -> {
            alignment = Alignment.TopEnd
            offset = IntOffset(-30, -5)
        }
        TourPlacement.Left -> {
            alignment = Alignment.CenterEnd
            offset = IntOffset(5, 0)
        }
        TourPlacement.LeftStart -> {
            alignment = Alignment.TopEnd
            offset = IntOffset(5, 24)
        }
        TourPlacement.LeftEnd -> {
            alignment = Alignment.BottomEnd
            offset = IntOffset(5, -24)
        }
        TourPlacement.Right -> {
            alignment = Alignment.CenterStart
            offset = IntOffset(-5, 0)
        }
        TourPlacement.RightStart -> {
            alignment = Alignment.TopStart
            offset = IntOffset(-5, 24)
        }
        TourPlacement.RightEnd -> {
            alignment = Alignment.BottomStart
            offset = IntOffset(-5, -24)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        Box(
            modifier = Modifier
                .align(alignment)
                .offset { offset }
                .size(10.dp)
                .rotate(45f)
                .background(color, RectangleShape),
        )
    }
}

private fun TourTargetBounds.inflate(
    offset: Int,
    viewportWidth: Int,
    viewportHeight: Int,
): TourTargetBounds {
    val left = (this.left - offset).coerceIn(0, viewportWidth)
    val top = (this.top - offset).coerceIn(0, viewportHeight)
    val right = (this.right + offset).coerceIn(0, viewportWidth)
    val bottom = (this.bottom + offset).coerceIn(0, viewportHeight)
    return TourTargetBounds(left, top, right, bottom)
}

private fun calculateTourCardOffset(
    target: TourTargetBounds?,
    placement: TourPlacement,
    cardWidth: Int,
    cardHeight: Int,
    viewportWidth: Int,
    viewportHeight: Int,
    margin: Int,
    gap: Int,
): IntOffset {
    if (target == null) {
        return IntOffset(
            x = ((viewportWidth - cardWidth) / 2).coerceAtLeast(margin),
            y = ((viewportHeight - cardHeight) / 2).coerceAtLeast(margin),
        )
    }

    val rawX: Int
    val rawY: Int
    when (placement) {
        TourPlacement.Top -> {
            rawX = target.centerX - cardWidth / 2
            rawY = target.top - gap - cardHeight
        }
        TourPlacement.TopStart -> {
            rawX = target.left
            rawY = target.top - gap - cardHeight
        }
        TourPlacement.TopEnd -> {
            rawX = target.right - cardWidth
            rawY = target.top - gap - cardHeight
        }
        TourPlacement.Bottom -> {
            rawX = target.centerX - cardWidth / 2
            rawY = target.bottom + gap
        }
        TourPlacement.BottomStart -> {
            rawX = target.left
            rawY = target.bottom + gap
        }
        TourPlacement.BottomEnd -> {
            rawX = target.right - cardWidth
            rawY = target.bottom + gap
        }
        TourPlacement.Left -> {
            rawX = target.left - gap - cardWidth
            rawY = target.centerY - cardHeight / 2
        }
        TourPlacement.LeftStart -> {
            rawX = target.left - gap - cardWidth
            rawY = target.top
        }
        TourPlacement.LeftEnd -> {
            rawX = target.left - gap - cardWidth
            rawY = target.bottom - cardHeight
        }
        TourPlacement.Right -> {
            rawX = target.right + gap
            rawY = target.centerY - cardHeight / 2
        }
        TourPlacement.RightStart -> {
            rawX = target.right + gap
            rawY = target.top
        }
        TourPlacement.RightEnd -> {
            rawX = target.right + gap
            rawY = target.bottom - cardHeight
        }
    }

    val maxX = max(margin, viewportWidth - cardWidth - margin)
    val maxY = max(margin, viewportHeight - cardHeight - margin)
    val x = min(max(rawX, margin), maxX)
    val y = min(max(rawY, margin), maxY)
    return IntOffset(x, y)
}
