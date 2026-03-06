package io.github.xingray.compose.nexus.controls

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import kotlin.math.roundToInt

enum class AffixPosition {
    Top,
    Bottom,
}

@Stable
class AffixState internal constructor() {
    var fixed by mutableStateOf(false)
        internal set

    fun update(fixed: Boolean) {
        this.fixed = fixed
    }

    fun updateRoot() {
        // Kept for API parity. Root rect is updated via onGloballyPositioned.
    }
}

@Composable
fun rememberAffixState(): io.github.xingray.compose.nexus.controls.AffixState = remember { _root_ide_package_.io.github.xingray.compose.nexus.controls.AffixState() }

@Composable
fun NexusAffix(
    modifier: Modifier = Modifier,
    state: io.github.xingray.compose.nexus.controls.AffixState = _root_ide_package_.io.github.xingray.compose.nexus.controls.rememberAffixState(),
    scrollState: ScrollState? = null,
    offset: Dp = 0.dp,
    position: io.github.xingray.compose.nexus.controls.AffixPosition = _root_ide_package_.io.github.xingray.compose.nexus.controls.AffixPosition.Top,
    zIndex: Int = 100,
    onChange: ((fixed: Boolean) -> Unit)? = null,
    onScroll: ((scrollTop: Int, fixed: Boolean) -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    val density = androidx.compose.ui.platform.LocalDensity.current
    val offsetPx = with(density) { offset.roundToPx() }

    var anchorPosition by remember { mutableStateOf(IntOffset.Zero) }
    var anchorSize by remember { mutableStateOf(IntSize.Zero) }

    val scrollTop = scrollState?.value ?: 0
    val fixedNow = when {
        scrollState == null -> false
        position == _root_ide_package_.io.github.xingray.compose.nexus.controls.AffixPosition.Top -> scrollTop > offsetPx
        else -> (scrollState.maxValue - scrollTop) > offsetPx
    }

    LaunchedEffect(scrollTop, fixedNow) {
        state.update(fixedNow)
        onScroll?.invoke(scrollTop, fixedNow)
    }

    var previousFixed by remember { mutableStateOf<Boolean?>(null) }
    LaunchedEffect(fixedNow) {
        if (previousFixed != fixedNow) {
            onChange?.invoke(fixedNow)
            previousFixed = fixedNow
        }
    }

    val placeholderModifier = modifier.onGloballyPositioned { coordinates ->
        val positionInWindow = coordinates.positionInWindow()
        anchorPosition = IntOffset(
            x = positionInWindow.x.roundToInt(),
            y = positionInWindow.y.roundToInt(),
        )
        anchorSize = coordinates.size
    }

    Box(modifier = placeholderModifier) {
        if (fixedNow) {
            Spacer(
                modifier = Modifier
                    .width(with(density) { anchorSize.width.toDp() })
                    .height(with(density) { anchorSize.height.toDp() }),
            )
            Popup(
                alignment = if (position == _root_ide_package_.io.github.xingray.compose.nexus.controls.AffixPosition.Top) Alignment.TopStart else Alignment.BottomStart,
                offset = if (position == _root_ide_package_.io.github.xingray.compose.nexus.controls.AffixPosition.Top) {
                    IntOffset(anchorPosition.x, offsetPx)
                } else {
                    IntOffset(anchorPosition.x, -offsetPx)
                },
                properties = PopupProperties(focusable = false),
            ) {
                Box {
                    content()
                }
            }
        } else {
            content()
        }
    }
}

