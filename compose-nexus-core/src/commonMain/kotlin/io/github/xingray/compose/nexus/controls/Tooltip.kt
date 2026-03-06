package io.github.xingray.compose.nexus.controls

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
import io.github.xingray.compose.nexus.foundation.ProvideContentColorTextStyle
import io.github.xingray.compose.nexus.theme.NexusTheme

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
fun rememberTooltipState(initialVisible: Boolean = false): io.github.xingray.compose.nexus.controls.TooltipState =
    remember { _root_ide_package_.io.github.xingray.compose.nexus.controls.TooltipState(initialVisible) }

@Composable
fun NexusTooltip(
    text: String = "",
    modifier: Modifier = Modifier,
    placement: io.github.xingray.compose.nexus.controls.TooltipPlacement = _root_ide_package_.io.github.xingray.compose.nexus.controls.TooltipPlacement.Bottom,
    theme: io.github.xingray.compose.nexus.controls.TooltipTheme = _root_ide_package_.io.github.xingray.compose.nexus.controls.TooltipTheme.Dark,
    trigger: io.github.xingray.compose.nexus.controls.TooltipTrigger = _root_ide_package_.io.github.xingray.compose.nexus.controls.TooltipTrigger.Hover,
    disabled: Boolean = false,
    visible: Boolean? = null,
    offset: Int = 12,
    showAfter: Long = 0L,
    hideAfter: Long = 200L,
    autoClose: Long = 0L,
    showArrow: Boolean = true,
    state: io.github.xingray.compose.nexus.controls.TooltipState = _root_ide_package_.io.github.xingray.compose.nexus.controls.rememberTooltipState(),
    onBeforeShow: ((event: Any?) -> Unit)? = null,
    onShow: ((event: Any?) -> Unit)? = null,
    onBeforeHide: ((event: Any?) -> Unit)? = null,
    onHide: ((event: Any?) -> Unit)? = null,
    popupContent: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    val colorScheme = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.colorScheme
    val shapes = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.shapes
    val typography = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.typography
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
        _root_ide_package_.io.github.xingray.compose.nexus.controls.TooltipTrigger.Hover -> {
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

        _root_ide_package_.io.github.xingray.compose.nexus.controls.TooltipTrigger.Click, _root_ide_package_.io.github.xingray.compose.nexus.controls.TooltipTrigger.ContextMenu -> {
            anchorModifier = anchorModifier.clickable(
                interactionSource = interactionSource,
                indication = null,
            ) {
                if (actualVisible) requestHide("click") else requestShow("click")
            }
        }

        _root_ide_package_.io.github.xingray.compose.nexus.controls.TooltipTrigger.Focus -> {
            anchorModifier = anchorModifier
                .focusable()
                .onFocusChanged {
                    if (it.isFocused) requestShow("focus") else requestHide("focus")
                }
        }
    }

    val bgColor = when (theme) {
        _root_ide_package_.io.github.xingray.compose.nexus.controls.TooltipTheme.Dark -> colorScheme.text.primary
        _root_ide_package_.io.github.xingray.compose.nexus.controls.TooltipTheme.Light -> colorScheme.fill.blank
    }
    val textColor = when (theme) {
        _root_ide_package_.io.github.xingray.compose.nexus.controls.TooltipTheme.Dark -> colorScheme.white
        _root_ide_package_.io.github.xingray.compose.nexus.controls.TooltipTheme.Light -> colorScheme.text.regular
    }
    val borderColor = when (theme) {
        _root_ide_package_.io.github.xingray.compose.nexus.controls.TooltipTheme.Dark -> Color.Transparent
        _root_ide_package_.io.github.xingray.compose.nexus.controls.TooltipTheme.Light -> colorScheme.border.light
    }

    val (popupAlignment, popupOffset, arrowText) = when (placement) {
        _root_ide_package_.io.github.xingray.compose.nexus.controls.TooltipPlacement.Top -> Triple(Alignment.TopCenter, IntOffset(0, -offset), "▼")
        _root_ide_package_.io.github.xingray.compose.nexus.controls.TooltipPlacement.TopStart -> Triple(Alignment.TopStart, IntOffset(0, -offset), "▼")
        _root_ide_package_.io.github.xingray.compose.nexus.controls.TooltipPlacement.TopEnd -> Triple(Alignment.TopEnd, IntOffset(0, -offset), "▼")
        _root_ide_package_.io.github.xingray.compose.nexus.controls.TooltipPlacement.Bottom -> Triple(Alignment.BottomCenter, IntOffset(0, offset), "▲")
        _root_ide_package_.io.github.xingray.compose.nexus.controls.TooltipPlacement.BottomStart -> Triple(Alignment.BottomStart, IntOffset(0, offset), "▲")
        _root_ide_package_.io.github.xingray.compose.nexus.controls.TooltipPlacement.BottomEnd -> Triple(Alignment.BottomEnd, IntOffset(0, offset), "▲")
        _root_ide_package_.io.github.xingray.compose.nexus.controls.TooltipPlacement.Left -> Triple(Alignment.CenterStart, IntOffset(-offset, 0), "▶")
        _root_ide_package_.io.github.xingray.compose.nexus.controls.TooltipPlacement.LeftStart -> Triple(Alignment.TopStart, IntOffset(-offset, 0), "▶")
        _root_ide_package_.io.github.xingray.compose.nexus.controls.TooltipPlacement.LeftEnd -> Triple(Alignment.BottomStart, IntOffset(-offset, 0), "▶")
        _root_ide_package_.io.github.xingray.compose.nexus.controls.TooltipPlacement.Right -> Triple(Alignment.CenterEnd, IntOffset(offset, 0), "◀")
        _root_ide_package_.io.github.xingray.compose.nexus.controls.TooltipPlacement.RightStart -> Triple(Alignment.TopEnd, IntOffset(offset, 0), "◀")
        _root_ide_package_.io.github.xingray.compose.nexus.controls.TooltipPlacement.RightEnd -> Triple(Alignment.BottomEnd, IntOffset(offset, 0), "◀")
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
                    if (showArrow && (placement == _root_ide_package_.io.github.xingray.compose.nexus.controls.TooltipPlacement.Right || placement == _root_ide_package_.io.github.xingray.compose.nexus.controls.TooltipPlacement.RightStart || placement == _root_ide_package_.io.github.xingray.compose.nexus.controls.TooltipPlacement.RightEnd)) {
                        _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(text = arrowText, color = bgColor, style = typography.extraSmall)
                    }
                    if (showArrow && (placement == _root_ide_package_.io.github.xingray.compose.nexus.controls.TooltipPlacement.Bottom || placement == _root_ide_package_.io.github.xingray.compose.nexus.controls.TooltipPlacement.BottomStart || placement == _root_ide_package_.io.github.xingray.compose.nexus.controls.TooltipPlacement.BottomEnd)) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(text = arrowText, color = bgColor, style = typography.extraSmall)
                        }
                    }
                    Box(
                        modifier = Modifier
                            .shadow(_root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.shadows.lighter.elevation, RoundedCornerShape(4.dp))
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
                        _root_ide_package_.io.github.xingray.compose.nexus.foundation.ProvideContentColorTextStyle(
                            contentColor = textColor,
                            textStyle = typography.extraSmall,
                        ) {
                            if (popupContent != null) {
                                popupContent()
                            } else {
                                _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
                                    text = text,
                                    color = textColor,
                                    style = typography.extraSmall,
                                )
                            }
                        }
                    }
                    if (showArrow && (placement == _root_ide_package_.io.github.xingray.compose.nexus.controls.TooltipPlacement.Left || placement == _root_ide_package_.io.github.xingray.compose.nexus.controls.TooltipPlacement.LeftStart || placement == _root_ide_package_.io.github.xingray.compose.nexus.controls.TooltipPlacement.LeftEnd)) {
                        _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(text = arrowText, color = bgColor, style = typography.extraSmall)
                    }
                    if (showArrow && (placement == _root_ide_package_.io.github.xingray.compose.nexus.controls.TooltipPlacement.Top || placement == _root_ide_package_.io.github.xingray.compose.nexus.controls.TooltipPlacement.TopStart || placement == _root_ide_package_.io.github.xingray.compose.nexus.controls.TooltipPlacement.TopEnd)) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(text = arrowText, color = bgColor, style = typography.extraSmall)
                        }
                    }
                }
            }
        }
    }
}
