package io.github.xingray.compose_nexus.controls

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.xingray.compose_nexus.theme.NexusTheme

@Stable
class LoadingServiceState(initialVisible: Boolean = false) {
    var visible: Boolean by mutableStateOf(initialVisible)
    var text: String? by mutableStateOf(null)
    var lock: Boolean by mutableStateOf(true)
    var spinnerSize: Dp by mutableStateOf(40.dp)
    var backgroundColor: Color? by mutableStateOf(null)
    var spinner: (@Composable () -> Unit)? by mutableStateOf(null)
    var beforeClose: (() -> Boolean)? by mutableStateOf(null)
    var onClosed: (() -> Unit)? by mutableStateOf(null)

    fun open(
        text: String? = null,
        lock: Boolean = true,
        spinnerSize: Dp = 40.dp,
        backgroundColor: Color? = null,
        spinner: (@Composable () -> Unit)? = null,
        beforeClose: (() -> Boolean)? = null,
        onClosed: (() -> Unit)? = null,
    ) {
        this.text = text
        this.lock = lock
        this.spinnerSize = spinnerSize
        this.backgroundColor = backgroundColor
        this.spinner = spinner
        this.beforeClose = beforeClose
        this.onClosed = onClosed
        this.visible = true
    }

    fun close() {
        visible = false
    }
}

@Composable
fun rememberLoadingServiceState(initialVisible: Boolean = false): LoadingServiceState =
    remember { LoadingServiceState(initialVisible) }

@Composable
fun NexusLoadingServiceHost(
    state: LoadingServiceState,
    modifier: Modifier = Modifier,
) {
    NexusLoading(
        modifier = modifier,
        loading = state.visible,
        text = state.text,
        fullscreen = true,
        lock = state.lock,
        spinnerSize = state.spinnerSize,
        backgroundColor = state.backgroundColor,
        spinner = state.spinner,
        beforeClose = state.beforeClose,
        onClosed = state.onClosed,
    )
}

@Composable
fun NexusLoading(
    modifier: Modifier = Modifier,
    loading: Boolean = true,
    text: String? = null,
    fullscreen: Boolean = false,
    lock: Boolean = true,
    spinnerSize: Dp = 40.dp,
    backgroundColor: Color? = null,
    spinner: (@Composable () -> Unit)? = null,
    beforeClose: (() -> Boolean)? = null,
    onClosed: (() -> Unit)? = null,
    content: (@Composable () -> Unit)? = null,
) {
    var internalLoading by remember { mutableStateOf(loading) }
    LaunchedEffect(loading) {
        if (loading) {
            internalLoading = true
        } else {
            val canClose = beforeClose?.invoke() ?: true
            if (canClose) {
                internalLoading = false
                onClosed?.invoke()
            }
        }
    }

    if (content != null) {
        Box(
            modifier = if (fullscreen) modifier.fillMaxSize() else modifier,
        ) {
            content()
            if (internalLoading) {
                LoadingOverlay(
                    modifier = Modifier.fillMaxSize(),
                    text = text,
                    spinnerSize = spinnerSize,
                    lock = lock,
                    backgroundColor = backgroundColor,
                    spinner = spinner,
                )
            }
        }
        return
    }

    if (!internalLoading) return

    if (fullscreen) {
        LoadingOverlay(
            modifier = modifier.fillMaxSize(),
            text = text,
            spinnerSize = spinnerSize,
            lock = lock,
            backgroundColor = backgroundColor,
            spinner = spinner,
        )
    } else {
        LoadingSpinner(
            modifier = modifier,
            text = text,
            spinnerSize = spinnerSize,
            spinner = spinner,
        )
    }
}

@Composable
private fun LoadingOverlay(
    modifier: Modifier = Modifier,
    text: String?,
    spinnerSize: Dp,
    lock: Boolean,
    backgroundColor: Color?,
    spinner: (@Composable () -> Unit)?,
) {
    val colorScheme = NexusTheme.colorScheme

    Box(
        modifier = modifier
            .background(backgroundColor ?: colorScheme.mask.base)
            .then(
                if (lock) {
                    Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                    ) { }
                } else {
                    Modifier
                }
            ),
        contentAlignment = Alignment.Center,
    ) {
        LoadingSpinner(text = text, spinnerSize = spinnerSize, spinner = spinner)
    }
}

@Composable
private fun LoadingSpinner(
    modifier: Modifier = Modifier,
    text: String?,
    spinnerSize: Dp,
    spinner: (@Composable () -> Unit)?,
) {
    val colorScheme = NexusTheme.colorScheme
    val typography = NexusTheme.typography

    val infiniteTransition = rememberInfiniteTransition(label = "loading-spin")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "loading-rotation",
    )

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (spinner != null) {
            Box(modifier = Modifier.size(spinnerSize)) {
                spinner()
            }
        } else {
            Canvas(modifier = Modifier.size(spinnerSize)) {
                val strokeWidth = size.minDimension * 0.1f
                val arcSize = size.minDimension - strokeWidth
                rotate(rotation) {
                    drawArc(
                        color = colorScheme.primary.base,
                        startAngle = 0f,
                        sweepAngle = 270f,
                        useCenter = false,
                        topLeft = androidx.compose.ui.geometry.Offset(strokeWidth / 2, strokeWidth / 2),
                        size = androidx.compose.ui.geometry.Size(arcSize, arcSize),
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
                    )
                }
            }
        }

        if (text != null) {
            Spacer(modifier = Modifier.height(12.dp))
            NexusText(
                text = text,
                color = colorScheme.primary.base,
                style = typography.small,
            )
        }
    }
}
