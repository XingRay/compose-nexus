package io.github.xingray.compose_nexus.controls

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import io.github.xingray.compose_nexus.foundation.ProvideContentColorTextStyle
import io.github.xingray.compose_nexus.theme.NexusTheme

enum class TooltipPlacement {
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

enum class TooltipTheme {
    Dark,
    Light,
}

enum class TooltipTrigger {
    Hover,
    Click,
    Focus,
    ContextMenu,
}

@Stable
class TooltipState(initialVisible: Boolean = false) {
    var visible by mutableStateOf(initialVisible)

    fun onOpen(event: Any? = null) {
        visible = true
    }

    fun onClose(event: Any? = null) {
        visible = false
    }

    fun hide(event: Any? = null) {
        visible = false
    }
}

@Composable
fun rememberTooltipState(initialVisible: Boolean = false): TooltipState =
    remember { TooltipState(initialVisible) }

@Composable
fun NexusTooltip(
    text: String = "",
    modifier: Modifier = Modifier,
    placement: TooltipPlacement = TooltipPlacement.Bottom,
    theme: TooltipTheme = TooltipTheme.Dark,
    trigger: TooltipTrigger = TooltipTrigger.Hover,
    disabled: Boolean = false,
    visible: Boolean? = null,
    offset: Int = 12,
    showAfter: Long = 0L,
    hideAfter: Long = 200L,
    autoClose: Long = 0L,
    showArrow: Boolean = true,
    state: TooltipState = rememberTooltipState(),
    onBeforeShow: ((event: Any?) -> Unit)? = null,
    onShow: ((event: Any?) -> Unit)? = null,
    onBeforeHide: ((event: Any?) -> Unit)? = null,
    onHide: ((event: Any?) -> Unit)? = null,
    popupContent: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    val colorScheme = NexusTheme.colorScheme
    val shapes = NexusTheme.shapes
    val typography = NexusTheme.typography
    val scope = rememberCoroutineScope()
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    var showJob by remember { mutableStateOf<Job?>(null) }
    var hideJob by remember { mutableStateOf<Job?>(null) }
    var autoCloseJob by remember { mutableStateOf<Job?>(null) }

    val controlled = visible != null
    val actualVisible = if (controlled) visible == true else state.visible

    fun requestHide(event: Any? = null) {
        showJob?.cancel()
        hideJob?.cancel()
        autoCloseJob?.cancel()
        hideJob = scope.launch {
            if (hideAfter > 0) delay(hideAfter)
            onBeforeHide?.invoke(event)
            if (!controlled) state.visible = false
            onHide?.invoke(event)
        }
    }

    fun requestShow(event: Any? = null) {
        if (disabled) return
        showJob?.cancel()
        hideJob?.cancel()
        autoCloseJob?.cancel()
        showJob = scope.launch {
            if (showAfter > 0) delay(showAfter)
            onBeforeShow?.invoke(event)
            if (!controlled) state.visible = true
            onShow?.invoke(event)
            if (autoClose > 0) {
                autoCloseJob = launch {
                    delay(autoClose)
                    requestHide("auto-close")
                }
            }
        }
    }

    var anchorModifier = modifier
    when (trigger) {
        TooltipTrigger.Hover -> {
            anchorModifier = anchorModifier.hoverable(interactionSource)
            LaunchedEffect(isHovered, disabled) {
                if (disabled) {
                    requestHide("disabled")
                } else if (isHovered) {
                    requestShow("hover")
                } else {
                    requestHide("hover")
                }
            }
        }

        TooltipTrigger.Click, TooltipTrigger.ContextMenu -> {
            anchorModifier = anchorModifier.clickable(
                interactionSource = interactionSource,
                indication = null,
            ) {
                if (actualVisible) requestHide("click") else requestShow("click")
            }
        }

        TooltipTrigger.Focus -> {
            anchorModifier = anchorModifier
                .focusable()
                .onFocusChanged {
                    if (it.isFocused) requestShow("focus") else requestHide("focus")
                }
        }
    }

    val bgColor = when (theme) {
        TooltipTheme.Dark -> colorScheme.text.primary
        TooltipTheme.Light -> colorScheme.fill.blank
    }
    val textColor = when (theme) {
        TooltipTheme.Dark -> colorScheme.white
        TooltipTheme.Light -> colorScheme.text.regular
    }
    val borderColor = when (theme) {
        TooltipTheme.Dark -> Color.Transparent
        TooltipTheme.Light -> colorScheme.border.light
    }

    val (popupAlignment, popupOffset, arrowText) = when (placement) {
        TooltipPlacement.Top -> Triple(Alignment.TopCenter, IntOffset(0, -offset), "▼")
        TooltipPlacement.TopStart -> Triple(Alignment.TopStart, IntOffset(0, -offset), "▼")
        TooltipPlacement.TopEnd -> Triple(Alignment.TopEnd, IntOffset(0, -offset), "▼")
        TooltipPlacement.Bottom -> Triple(Alignment.BottomCenter, IntOffset(0, offset), "▲")
        TooltipPlacement.BottomStart -> Triple(Alignment.BottomStart, IntOffset(0, offset), "▲")
        TooltipPlacement.BottomEnd -> Triple(Alignment.BottomEnd, IntOffset(0, offset), "▲")
        TooltipPlacement.Left -> Triple(Alignment.CenterStart, IntOffset(-offset, 0), "▶")
        TooltipPlacement.LeftStart -> Triple(Alignment.TopStart, IntOffset(-offset, 0), "▶")
        TooltipPlacement.LeftEnd -> Triple(Alignment.BottomStart, IntOffset(-offset, 0), "▶")
        TooltipPlacement.Right -> Triple(Alignment.CenterEnd, IntOffset(offset, 0), "◀")
        TooltipPlacement.RightStart -> Triple(Alignment.TopEnd, IntOffset(offset, 0), "◀")
        TooltipPlacement.RightEnd -> Triple(Alignment.BottomEnd, IntOffset(offset, 0), "◀")
    }

    Box(modifier = anchorModifier) {
        content()

        val hasContent = popupContent != null || text.isNotEmpty()
        if (actualVisible && hasContent && !disabled) {
            Popup(
                alignment = popupAlignment,
                offset = popupOffset,
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (showArrow && (placement == TooltipPlacement.Right || placement == TooltipPlacement.RightStart || placement == TooltipPlacement.RightEnd)) {
                        NexusText(text = arrowText, color = bgColor, style = typography.extraSmall)
                    }
                    if (showArrow && (placement == TooltipPlacement.Bottom || placement == TooltipPlacement.BottomStart || placement == TooltipPlacement.BottomEnd)) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            NexusText(text = arrowText, color = bgColor, style = typography.extraSmall)
                        }
                    }
                    Box(
                        modifier = Modifier
                            .shadow(NexusTheme.shadows.lighter.elevation, RoundedCornerShape(4.dp))
                            .clip(shapes.base)
                            .background(bgColor)
                            .then(
                                if (borderColor != Color.Transparent) {
                                    Modifier
                                } else {
                                    Modifier
                                }
                            )
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                    ) {
                        ProvideContentColorTextStyle(
                            contentColor = textColor,
                            textStyle = typography.extraSmall,
                        ) {
                            if (popupContent != null) {
                                popupContent()
                            } else {
                                NexusText(
                                    text = text,
                                    color = textColor,
                                    style = typography.extraSmall,
                                )
                            }
                        }
                    }
                    if (showArrow && (placement == TooltipPlacement.Left || placement == TooltipPlacement.LeftStart || placement == TooltipPlacement.LeftEnd)) {
                        NexusText(text = arrowText, color = bgColor, style = typography.extraSmall)
                    }
                    if (showArrow && (placement == TooltipPlacement.Top || placement == TooltipPlacement.TopStart || placement == TooltipPlacement.TopEnd)) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            NexusText(text = arrowText, color = bgColor, style = typography.extraSmall)
                        }
                    }
                }
            }
        }
    }
}
