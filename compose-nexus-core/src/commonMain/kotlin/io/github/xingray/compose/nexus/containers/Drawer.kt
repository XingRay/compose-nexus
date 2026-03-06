package io.github.xingray.compose.nexus.containers

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import io.github.xingray.compose.nexus.controls.NexusDivider
import io.github.xingray.compose.nexus.controls.NexusText
import io.github.xingray.compose.nexus.foundation.ProvideContentColorTextStyle
import io.github.xingray.compose.nexus.theme.NexusTheme
import kotlinx.coroutines.delay

@Stable
enum class DrawerDirection {
    Left,
    Right,
    Top,
    Bottom,
}

@Composable
fun NexusDrawer(
    state: io.github.xingray.compose.nexus.containers.DialogState,
    modifier: Modifier = Modifier,
    direction: io.github.xingray.compose.nexus.containers.DrawerDirection = _root_ide_package_.io.github.xingray.compose.nexus.containers.DrawerDirection.Right,
    size: Dp = 320.dp,
    title: String? = null,
    withHeader: Boolean = true,
    showClose: Boolean = true,
    modal: Boolean = true,
    modalPenetrable: Boolean = false,
    closeOnClickOverlay: Boolean = true,
    closeOnEscape: Boolean = true,
    closeOnClickModal: Boolean = closeOnClickOverlay,
    closeOnPressEscape: Boolean = closeOnEscape,
    destroyOnClose: Boolean = false,
    resizable: Boolean = false,
    zIndex: Float = 2000f,
    beforeClose: (((cancel: Boolean) -> Unit) -> Unit)? = null,
    onOpen: (() -> Unit)? = null,
    onOpened: (() -> Unit)? = null,
    onClose: (() -> Unit)? = null,
    onClosed: (() -> Unit)? = null,
    onOpenAutoFocus: (() -> Unit)? = null,
    onCloseAutoFocus: (() -> Unit)? = null,
    onResizeStart: ((size: Dp) -> Unit)? = null,
    onResize: ((size: Dp) -> Unit)? = null,
    onResizeEnd: ((size: Dp) -> Unit)? = null,
    header: (@Composable () -> Unit)? = null,
    footer: (@Composable () -> Unit)? = null,
    closeIcon: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    val openCloseDuration = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.motion.durationFast.toLong()
    var lastVisible by remember { mutableStateOf(state.visible) }
    LaunchedEffect(state.visible) {
        if (state.visible && !lastVisible) {
            onOpen?.invoke()
            onOpenAutoFocus?.invoke()
            delay(openCloseDuration)
            onOpened?.invoke()
        } else if (!state.visible && lastVisible) {
            onClose?.invoke()
            onCloseAutoFocus?.invoke()
            delay(openCloseDuration)
            onClosed?.invoke()
        }
        lastVisible = state.visible
    }

    if (!state.visible) return

    val colorScheme = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.colorScheme
    val shadows = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.shadows
    val motion = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.motion
    val density = LocalDensity.current
    var currentSize by remember(direction) { mutableStateOf(size) }

    val requestCloseByUser = {
        val done: (Boolean) -> Unit = { cancel ->
            if (!cancel) state.close()
        }
        if (beforeClose != null) beforeClose(done) else done(false)
    }

    val enterTransition: EnterTransition
    val exitTransition: ExitTransition
    when (direction) {
        _root_ide_package_.io.github.xingray.compose.nexus.containers.DrawerDirection.Left -> {
            enterTransition = fadeIn(motion.tweenFade()) + slideInHorizontally(initialOffsetX = { -it }, animationSpec = motion.tweenFast())
            exitTransition = fadeOut(motion.tweenFadeLinear()) + slideOutHorizontally(targetOffsetX = { -it }, animationSpec = motion.tweenFast())
        }

        _root_ide_package_.io.github.xingray.compose.nexus.containers.DrawerDirection.Right -> {
            enterTransition = fadeIn(motion.tweenFade()) + slideInHorizontally(initialOffsetX = { it }, animationSpec = motion.tweenFast())
            exitTransition = fadeOut(motion.tweenFadeLinear()) + slideOutHorizontally(targetOffsetX = { it }, animationSpec = motion.tweenFast())
        }

        _root_ide_package_.io.github.xingray.compose.nexus.containers.DrawerDirection.Top -> {
            enterTransition = fadeIn(motion.tweenFade()) + slideInVertically(initialOffsetY = { -it }, animationSpec = motion.tweenFast())
            exitTransition = fadeOut(motion.tweenFadeLinear()) + slideOutVertically(targetOffsetY = { -it }, animationSpec = motion.tweenFast())
        }

        _root_ide_package_.io.github.xingray.compose.nexus.containers.DrawerDirection.Bottom -> {
            enterTransition = fadeIn(motion.tweenFade()) + slideInVertically(initialOffsetY = { it }, animationSpec = motion.tweenFast())
            exitTransition = fadeOut(motion.tweenFadeLinear()) + slideOutVertically(targetOffsetY = { it }, animationSpec = motion.tweenFast())
        }
    }

    val popupFocusable = !(!modal && modalPenetrable)
    Popup(
        onDismissRequest = {
            if (closeOnClickModal) requestCloseByUser()
        },
        properties = PopupProperties(focusable = popupFocusable),
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .zIndex(zIndex)
                .then(
                    if (closeOnPressEscape) {
                        Modifier.onKeyEvent {
                            if (it.key == Key.Escape) {
                                requestCloseByUser()
                                true
                            } else {
                                false
                            }
                        }
                    } else {
                        Modifier
                    }
                ),
        ) {
            if (modal) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(colorScheme.overlay.lighter)
                        .then(
                            if (closeOnClickModal) {
                                Modifier.clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                ) { requestCloseByUser() }
                            } else {
                                Modifier
                            }
                        ),
                )
            }

            val panelAlignment = when (direction) {
                _root_ide_package_.io.github.xingray.compose.nexus.containers.DrawerDirection.Left -> Alignment.CenterStart
                _root_ide_package_.io.github.xingray.compose.nexus.containers.DrawerDirection.Right -> Alignment.CenterEnd
                _root_ide_package_.io.github.xingray.compose.nexus.containers.DrawerDirection.Top -> Alignment.TopCenter
                _root_ide_package_.io.github.xingray.compose.nexus.containers.DrawerDirection.Bottom -> Alignment.BottomCenter
            }

            val minSize = 220.dp
            val maxSize = when (direction) {
                _root_ide_package_.io.github.xingray.compose.nexus.containers.DrawerDirection.Left, _root_ide_package_.io.github.xingray.compose.nexus.containers.DrawerDirection.Right -> (maxWidth * 0.95f).coerceAtLeast(minSize)
                _root_ide_package_.io.github.xingray.compose.nexus.containers.DrawerDirection.Top, _root_ide_package_.io.github.xingray.compose.nexus.containers.DrawerDirection.Bottom -> (maxHeight * 0.95f).coerceAtLeast(minSize)
            }
            currentSize = currentSize.coerceIn(minSize, maxSize)

            AnimatedVisibility(
                visible = state.visible,
                enter = enterTransition,
                exit = exitTransition,
            ) {
                Column(
                    modifier = Modifier
                        .align(panelAlignment)
                        .then(
                            when (direction) {
                                _root_ide_package_.io.github.xingray.compose.nexus.containers.DrawerDirection.Left, _root_ide_package_.io.github.xingray.compose.nexus.containers.DrawerDirection.Right -> Modifier.width(currentSize).fillMaxHeight()
                                _root_ide_package_.io.github.xingray.compose.nexus.containers.DrawerDirection.Top, _root_ide_package_.io.github.xingray.compose.nexus.containers.DrawerDirection.Bottom -> Modifier.height(currentSize).fillMaxWidth()
                            }
                        )
                        .shadow(shadows.dark.elevation)
                        .background(colorScheme.fill.blank)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                        ) { }
                        .then(modifier),
                ) {
                    if (withHeader) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Box(modifier = Modifier.weight(1f)) {
                                if (header != null) {
                                    header()
                                } else if (title != null) {
                                    _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
                                        text = title,
                                        color = colorScheme.text.primary,
                                        style = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.typography.large,
                                    )
                                }
                            }
                            if (showClose) {
                                Box(
                                    modifier = Modifier
                                        .clickable(
                                            interactionSource = remember { MutableInteractionSource() },
                                            indication = null,
                                        ) { requestCloseByUser() }
                                        .padding(4.dp),
                                ) {
                                    if (closeIcon != null) {
                                        closeIcon()
                                    } else {
                                        _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
                                            text = "✕",
                                            color = colorScheme.text.placeholder,
                                            style = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.typography.medium,
                                        )
                                    }
                                }
                            }
                        }
                        _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusDivider(color = colorScheme.border.lighter)
                    }

                    _root_ide_package_.io.github.xingray.compose.nexus.foundation.ProvideContentColorTextStyle(
                        contentColor = colorScheme.text.regular,
                        textStyle = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.typography.base,
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(20.dp),
                        ) {
                            if (destroyOnClose) {
                                key(state.visible) { content() }
                            } else {
                                content()
                            }
                        }
                    }

                    if (footer != null) {
                        _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusDivider(color = colorScheme.border.lighter)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            contentAlignment = Alignment.CenterEnd,
                        ) {
                            footer()
                        }
                    }
                }
            }

            if (resizable) {
                val handleModifier = when (direction) {
                    _root_ide_package_.io.github.xingray.compose.nexus.containers.DrawerDirection.Left -> Modifier
                        .align(Alignment.CenterStart)
                        .fillMaxHeight()
                        .width(6.dp)
                    _root_ide_package_.io.github.xingray.compose.nexus.containers.DrawerDirection.Right -> Modifier
                        .align(Alignment.CenterEnd)
                        .fillMaxHeight()
                        .width(6.dp)
                    _root_ide_package_.io.github.xingray.compose.nexus.containers.DrawerDirection.Top -> Modifier
                        .align(Alignment.TopCenter)
                        .fillMaxWidth()
                        .height(6.dp)
                    _root_ide_package_.io.github.xingray.compose.nexus.containers.DrawerDirection.Bottom -> Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .height(6.dp)
                }
                Box(
                    modifier = handleModifier
                        .padding(1.dp)
                        .background(colorScheme.border.lighter)
                        .pointerInput(direction, currentSize, density) {
                            detectDragGestures(
                                onDragStart = { onResizeStart?.invoke(currentSize) },
                                onDragEnd = { onResizeEnd?.invoke(currentSize) },
                                onDragCancel = { onResizeEnd?.invoke(currentSize) },
                            ) { change, dragAmount ->
                                change.consume()
                                val deltaPx = when (direction) {
                                    _root_ide_package_.io.github.xingray.compose.nexus.containers.DrawerDirection.Left -> dragAmount.x
                                    _root_ide_package_.io.github.xingray.compose.nexus.containers.DrawerDirection.Right -> -dragAmount.x
                                    _root_ide_package_.io.github.xingray.compose.nexus.containers.DrawerDirection.Top -> dragAmount.y
                                    _root_ide_package_.io.github.xingray.compose.nexus.containers.DrawerDirection.Bottom -> -dragAmount.y
                                }
                                val delta = with(density) { deltaPx.toDp() }
                                currentSize = (currentSize + delta).coerceIn(minSize, maxSize)
                                onResize?.invoke(currentSize)
                            }
                        },
                )
            }
        }
    }
}
