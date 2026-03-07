package io.github.xingray.compose.nexus.containers

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
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
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import io.github.xingray.compose.nexus.controls.NexusText
import io.github.xingray.compose.nexus.foundation.ProvideContentColorTextStyle
import io.github.xingray.compose.nexus.theme.NexusTheme
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

@Stable
class DialogState(initialVisible: Boolean = false) {
    var visible: Boolean by mutableStateOf(initialVisible)
    internal var resetPositionHandler: (() -> Unit)? = null

    fun open() {
        visible = true
    }

    fun close() {
        visible = false
    }

    fun handleClose() {
        close()
    }

    fun resetPosition() {
        resetPositionHandler?.invoke()
    }
}

@Composable
fun rememberDialogState(initialVisible: Boolean = false): DialogState =
    remember { DialogState(initialVisible) }

enum class DialogTransition {
    FadeScale,
    Fade,
    SlideUp,
}

@Composable
fun NexusDialog(
    state: DialogState,
    modifier: Modifier = Modifier,
    title: String? = null,
    width: Dp = 500.dp,
    fullscreen: Boolean = false,
    top: Dp = 72.dp,
    modal: Boolean = true,
    modalPenetrable: Boolean = false,
    closeOnClickOverlay: Boolean = true,
    closeOnEscape: Boolean = true,
    closeOnClickModal: Boolean = closeOnClickOverlay,
    closeOnPressEscape: Boolean = closeOnEscape,
    showClose: Boolean = true,
    draggable: Boolean = false,
    overflow: Boolean = false,
    center: Boolean = false,
    alignCenter: Boolean = false,
    destroyOnClose: Boolean = false,
    zIndex: Float = 2000f,
    transition: DialogTransition = DialogTransition.FadeScale,
    beforeClose: ((done: () -> Unit) -> Unit)? = null,
    onOpen: (() -> Unit)? = null,
    onOpened: (() -> Unit)? = null,
    onClose: (() -> Unit)? = null,
    onClosed: (() -> Unit)? = null,
    onOpenAutoFocus: (() -> Unit)? = null,
    onCloseAutoFocus: (() -> Unit)? = null,
    header: (@Composable () -> Unit)? = null,
    footer: (@Composable () -> Unit)? = null,
    closeIcon: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    val openCloseDuration = NexusTheme.motion.durationFast.toLong()
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

    val colorScheme = NexusTheme.colorScheme
    val shapes = NexusTheme.shapes
    val shadows = NexusTheme.shadows
    val motion = NexusTheme.motion
    val density = LocalDensity.current

    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }
    val panelWidth = if (fullscreen) Dp.Unspecified else width
    val useAlignCenter = fullscreen || alignCenter
    val useDraggable = draggable && !fullscreen

    SideEffect {
        state.resetPositionHandler = {
            offsetX = 0f
            offsetY = 0f
        }
    }

    val enterTransition: EnterTransition
    val exitTransition: ExitTransition
    when (transition) {
        DialogTransition.FadeScale -> {
            enterTransition = fadeIn(motion.tweenFade()) + scaleIn(initialScale = 0.94f, animationSpec = motion.tweenFast())
            exitTransition = fadeOut(motion.tweenFadeLinear()) + scaleOut(targetScale = 0.94f, animationSpec = motion.tweenFast())
        }

        DialogTransition.Fade -> {
            enterTransition = fadeIn(motion.tweenFade())
            exitTransition = fadeOut(motion.tweenFadeLinear())
        }

        DialogTransition.SlideUp -> {
            enterTransition = fadeIn(motion.tweenFade()) + slideInVertically(
                initialOffsetY = { it / 6 },
                animationSpec = motion.tweenFast(),
            )
            exitTransition = fadeOut(motion.tweenFadeLinear()) + slideOutVertically(
                targetOffsetY = { it / 8 },
                animationSpec = motion.tweenFast(),
            )
        }
    }

    val requestCloseByUser = {
        val done = { state.close() }
        if (beforeClose != null) beforeClose(done) else done()
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
            contentAlignment = if (useAlignCenter) Alignment.Center else Alignment.TopCenter,
        ) {
            val maxOffsetX = if (overflow) {
                Float.POSITIVE_INFINITY
            } else {
                with(density) {
                    val panel = if (fullscreen) maxWidth.toPx() else width.toPx().coerceAtMost(maxWidth.toPx())
                    ((maxWidth.toPx() - panel) / 2f).coerceAtLeast(0f)
                }
            }

            val maxOffsetY = if (overflow) {
                Float.POSITIVE_INFINITY
            } else {
                with(density) { (maxHeight.toPx() / 2f).coerceAtLeast(0f) }
            }

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

            AnimatedVisibility(
                visible = state.visible,
                enter = enterTransition,
                exit = exitTransition,
            ) {
                Column(
                    modifier = modifier
                        .then(
                            if (fullscreen) {
                                Modifier.fillMaxSize()
                            } else {
                                Modifier
                                    .width(panelWidth)
                                    .padding(top = if (useAlignCenter) 0.dp else top)
                            }
                        )
                        .offset {
                            IntOffset(
                                x = offsetX.roundToInt(),
                                y = offsetY.roundToInt(),
                            )
                        }
                        .shadow(shadows.default.elevation, shapes.base)
                        .clip(shapes.base)
                        .background(colorScheme.fill.blank)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                        ) { },
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .pointerInput(useDraggable) {
                                if (useDraggable) {
                                    detectDragGestures { change, dragAmount ->
                                        change.consume()
                                        offsetX = (offsetX + dragAmount.x).coerceIn(-maxOffsetX, maxOffsetX)
                                        offsetY = (offsetY + dragAmount.y).coerceIn(-maxOffsetY, maxOffsetY)
                                    }
                                }
                            }
                            .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = if (center) Alignment.Center else Alignment.CenterStart,
                        ) {
                            if (header != null) {
                                ProvideContentColorTextStyle(
                                    contentColor = colorScheme.text.primary,
                                    textStyle = NexusTheme.typography.large,
                                ) {
                                    header()
                                }
                            } else if (title != null) {
                                NexusText(
                                    text = title,
                                    color = colorScheme.text.primary,
                                    style = NexusTheme.typography.large,
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
                                    NexusText(
                                        text = "✕",
                                        color = colorScheme.text.placeholder,
                                        style = NexusTheme.typography.medium,
                                    )
                                }
                            }
                        } else if (center) {
                            Spacer(modifier = Modifier.width(24.dp))
                        }
                    }

                    ProvideContentColorTextStyle(
                        contentColor = colorScheme.text.regular,
                        textStyle = NexusTheme.typography.base,
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp, vertical = 10.dp),
                        ) {
                            if (destroyOnClose) {
                                key(state.visible) { content() }
                            } else {
                                content()
                            }
                        }
                    }

                    if (footer != null) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 20.dp),
                            contentAlignment = if (center) Alignment.Center else Alignment.CenterEnd,
                        ) {
                            footer()
                        }
                    } else if (center) {
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }
        }
    }
}
