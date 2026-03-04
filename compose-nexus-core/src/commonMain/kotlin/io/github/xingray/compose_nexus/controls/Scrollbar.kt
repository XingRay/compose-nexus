package io.github.xingray.compose_nexus.controls

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import io.github.xingray.compose_nexus.theme.NexusTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

enum class NexusScrollbarDirection {
    Top,
    Bottom,
    Left,
    Right,
}

class NexusScrollbarState internal constructor(
    val verticalScrollState: androidx.compose.foundation.ScrollState,
    val horizontalScrollState: androidx.compose.foundation.ScrollState,
    private val scope: CoroutineScope,
) {
    fun setScrollTop(scrollTop: Int) {
        scope.launch {
            verticalScrollState.scrollTo(scrollTop.coerceAtLeast(0))
        }
    }

    fun setScrollLeft(scrollLeft: Int) {
        scope.launch {
            horizontalScrollState.scrollTo(scrollLeft.coerceAtLeast(0))
        }
    }
}

@Composable
fun rememberNexusScrollbarState(
    initialScrollTop: Int = 0,
    initialScrollLeft: Int = 0,
): NexusScrollbarState {
    val verticalScrollState = rememberScrollState(initial = initialScrollTop)
    val horizontalScrollState = rememberScrollState(initial = initialScrollLeft)
    val scope = rememberCoroutineScope()
    return remember(verticalScrollState, horizontalScrollState, scope) {
        NexusScrollbarState(
            verticalScrollState = verticalScrollState,
            horizontalScrollState = horizontalScrollState,
            scope = scope,
        )
    }
}

@Composable
fun NexusScrollbar(
    modifier: Modifier = Modifier,
    state: NexusScrollbarState = rememberNexusScrollbarState(),
    height: Dp? = null,
    maxHeight: Dp? = null,
    always: Boolean = false,
    minSize: Dp = 20.dp,
    distance: Int = 0,
    onScroll: ((scrollLeft: Int, scrollTop: Int) -> Unit)? = null,
    onEndReached: ((NexusScrollbarDirection) -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    val colors = NexusTheme.colorScheme
    val shapes = NexusTheme.shapes
    var topReached by remember { mutableStateOf(false) }
    var bottomReached by remember { mutableStateOf(false) }
    var leftReached by remember { mutableStateOf(false) }
    var rightReached by remember { mutableStateOf(false) }

    LaunchedEffect(state.verticalScrollState.value, state.horizontalScrollState.value) {
        onScroll?.invoke(state.horizontalScrollState.value, state.verticalScrollState.value)

        if (onEndReached != null) {
            val top = state.verticalScrollState.value <= distance
            val bottom = state.verticalScrollState.value >= (state.verticalScrollState.maxValue - distance).coerceAtLeast(0)
            val left = state.horizontalScrollState.value <= distance
            val right = state.horizontalScrollState.value >= (state.horizontalScrollState.maxValue - distance).coerceAtLeast(0)

            if (top && !topReached) onEndReached(NexusScrollbarDirection.Top)
            if (bottom && !bottomReached) onEndReached(NexusScrollbarDirection.Bottom)
            if (left && !leftReached) onEndReached(NexusScrollbarDirection.Left)
            if (right && !rightReached) onEndReached(NexusScrollbarDirection.Right)

            topReached = top
            bottomReached = bottom
            leftReached = left
            rightReached = right
        }
    }

    BoxWithConstraints(
        modifier = modifier
            .then(if (height != null) Modifier.height(height) else Modifier)
            .then(if (maxHeight != null) Modifier.heightIn(max = maxHeight) else Modifier),
    ) {
        val viewportHeight = this.maxHeight
        val viewportWidth = this.maxWidth
        val verticalMax = state.verticalScrollState.maxValue
        val horizontalMax = state.horizontalScrollState.maxValue
        val verticalContentHeight = viewportHeight + verticalMax.dp
        val horizontalContentWidth = viewportWidth + horizontalMax.dp

        val verticalThumbHeight = if (verticalContentHeight <= 0.dp) {
            minSize
        } else {
            val ratio = (viewportHeight.value / verticalContentHeight.value).coerceIn(0f, 1f)
            (viewportHeight * ratio).coerceAtLeast(minSize)
        }
        val verticalTrack = (viewportHeight - verticalThumbHeight).coerceAtLeast(0.dp)
        val verticalThumbOffset = if (verticalMax > 0) {
            verticalTrack * (state.verticalScrollState.value.toFloat() / verticalMax.toFloat())
        } else {
            0.dp
        }

        val horizontalThumbWidth = if (horizontalContentWidth <= 0.dp) {
            minSize
        } else {
            val ratio = (viewportWidth.value / horizontalContentWidth.value).coerceIn(0f, 1f)
            (viewportWidth * ratio).coerceAtLeast(minSize)
        }
        val horizontalTrack = (viewportWidth - horizontalThumbWidth).coerceAtLeast(0.dp)
        val horizontalThumbOffset = if (horizontalMax > 0) {
            horizontalTrack * (state.horizontalScrollState.value.toFloat() / horizontalMax.toFloat())
        } else {
            0.dp
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(state.verticalScrollState)
                .horizontalScroll(state.horizontalScrollState)
                .padding(end = 8.dp, bottom = 8.dp),
            content = content,
        )

        val showBar = always || state.verticalScrollState.isScrollInProgress || state.horizontalScrollState.isScrollInProgress
        if (showBar && verticalMax > 0) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .width(6.dp)
                    .fillMaxSize()
                    .background(colors.fill.light, shapes.small),
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .offset { IntOffset(0, verticalThumbOffset.roundToPx()) }
                        .width(6.dp)
                        .height(verticalThumbHeight)
                        .background(colors.text.placeholder.copy(alpha = 0.6f), shapes.small),
                )
            }
        }
        if (showBar && horizontalMax > 0) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .height(6.dp)
                    .fillMaxWidth()
                    .background(colors.fill.light, shapes.small),
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .offset { IntOffset(horizontalThumbOffset.roundToPx(), 0) }
                        .width(horizontalThumbWidth)
                        .height(6.dp)
                        .background(colors.text.placeholder.copy(alpha = 0.6f), shapes.small),
                )
            }
        }
    }
}
