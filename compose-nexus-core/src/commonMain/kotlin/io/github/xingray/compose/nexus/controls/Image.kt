package io.github.xingray.compose.nexus.controls

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import io.github.xingray.compose.nexus.theme.ComponentSize
import io.github.xingray.compose.nexus.theme.NexusTheme

enum class NexusImageFit {
    Fill,
    Contain,
    Cover,
    None,
    ScaleDown,
}

enum class NexusImageLoading {
    Eager,
    Lazy,
}

@Stable
class NexusImageState(
    initialIndex: Int = 0,
) {
    var previewVisible by mutableStateOf(false)
        private set
    var activeIndex by mutableIntStateOf(initialIndex.coerceAtLeast(0))
        private set
    var scale by mutableFloatStateOf(1f)
        private set

    fun showPreview(index: Int? = null) {
        if (index != null && index >= 0) {
            activeIndex = index
        }
        previewVisible = true
    }

    fun closePreview() {
        previewVisible = false
    }

    fun setActiveItem(index: Int, total: Int) {
        if (total <= 0) return
        activeIndex = index.coerceIn(0, total - 1)
    }

    fun prev(total: Int, infinite: Boolean): Boolean {
        if (total <= 0) return false
        val old = activeIndex
        activeIndex = if (activeIndex == 0) {
            if (infinite) total - 1 else 0
        } else {
            activeIndex - 1
        }
        return old != activeIndex
    }

    fun next(total: Int, infinite: Boolean): Boolean {
        if (total <= 0) return false
        val old = activeIndex
        activeIndex = if (activeIndex == total - 1) {
            if (infinite) 0 else total - 1
        } else {
            activeIndex + 1
        }
        return old != activeIndex
    }

    fun zoomIn(zoomRate: Float, maxScale: Float) {
        scale = (scale * zoomRate).coerceAtMost(maxScale)
    }

    fun zoomOut(zoomRate: Float, minScale: Float) {
        scale = (scale / zoomRate).coerceAtLeast(minScale)
    }

    fun reset(scale: Float = 1f) {
        this.scale = scale
    }
}

@Composable
fun rememberNexusImageState(
    initialIndex: Int = 0,
): io.github.xingray.compose.nexus.controls.NexusImageState = remember {
    _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusImageState(initialIndex = initialIndex)
}

@Composable
fun NexusImage(
    src: String,
    modifier: Modifier = Modifier,
    fit: io.github.xingray.compose.nexus.controls.NexusImageFit = _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusImageFit.Cover,
    hideOnClickModal: Boolean = false,
    loading: io.github.xingray.compose.nexus.controls.NexusImageLoading = _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusImageLoading.Eager,
    lazy: Boolean = false,
    scrollContainer: Any? = null,
    alt: String = "",
    referrerPolicy: String? = null,
    crossorigin: String? = null,
    previewSrcList: List<String> = emptyList(),
    zIndex: Int? = null,
    initialIndex: Int = 0,
    closeOnPressEscape: Boolean = true,
    previewTeleported: Boolean = false,
    infinite: Boolean = true,
    zoomRate: Float = 1.2f,
    scale: Float = 1f,
    minScale: Float = 0.2f,
    maxScale: Float = 7f,
    showProgress: Boolean = false,
    state: io.github.xingray.compose.nexus.controls.NexusImageState = _root_ide_package_.io.github.xingray.compose.nexus.controls.rememberNexusImageState(initialIndex = initialIndex),
    placeholder: (@Composable () -> Unit)? = null,
    error: (@Composable () -> Unit)? = null,
    viewerError: (@Composable (activeIndex: Int, src: String) -> Unit)? = null,
    progress: (@Composable (activeIndex: Int, total: Int) -> Unit)? = null,
    toolbar: (@Composable (
        activeIndex: Int,
        total: Int,
        setActiveItem: (Int) -> Unit,
        prev: () -> Unit,
        next: () -> Unit,
        reset: () -> Unit,
        zoomIn: () -> Unit,
        zoomOut: () -> Unit,
    ) -> Unit)? = null,
    onLoad: (() -> Unit)? = null,
    onError: (() -> Unit)? = null,
    onSwitch: ((index: Int) -> Unit)? = null,
    onClose: (() -> Unit)? = null,
    onShow: (() -> Unit)? = null,
) {
    @Suppress("UNUSED_VARIABLE")
    val _unusedNative = listOf(scrollContainer, referrerPolicy, crossorigin, zIndex, previewTeleported)
    val colorScheme = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.colorScheme
    val typography = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.typography
    val previewList = if (previewSrcList.isNotEmpty()) previewSrcList else if (src.isNotBlank()) listOf(src) else emptyList()
    val total = previewList.size

    var requestedLoad by remember(src, lazy, loading) {
        mutableStateOf(!(lazy || loading == _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusImageLoading.Lazy))
    }
    var loadNotified by remember(src) { mutableStateOf(false) }
    var errorNotified by remember(src) { mutableStateOf(false) }
    val loadFailed = requestedLoad && src.startsWith("error:", ignoreCase = true)

    LaunchedEffect(scale) {
        state.reset(scale)
    }

    LaunchedEffect(requestedLoad, loadFailed, src) {
        if (requestedLoad && !loadFailed && !loadNotified) {
            loadNotified = true
            onLoad?.invoke()
        }
        if (loadFailed && !errorNotified) {
            errorNotified = true
            onError?.invoke()
        }
    }

    var wasPreviewVisible by remember { mutableStateOf(state.previewVisible) }
    var lastIndex by remember { mutableIntStateOf(state.activeIndex) }
    LaunchedEffect(state.previewVisible) {
        if (state.previewVisible && !wasPreviewVisible) onShow?.invoke()
        if (!state.previewVisible && wasPreviewVisible) onClose?.invoke()
        wasPreviewVisible = state.previewVisible
    }
    LaunchedEffect(state.activeIndex) {
        if (state.activeIndex != lastIndex) {
            onSwitch?.invoke(state.activeIndex)
            lastIndex = state.activeIndex
        }
    }

    fun showPreview() {
        val start = initialIndex.coerceIn(0, (total - 1).coerceAtLeast(0))
        state.showPreview(start)
    }

    Box(
        modifier = modifier
            .clip(_root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.shapes.base)
            .background(colorScheme.fill.light)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
            ) {
                if (!requestedLoad) {
                    requestedLoad = true
                } else if (previewList.isNotEmpty()) {
                    showPreview()
                }
            }
            .pointerHoverIcon(PointerIcon.Hand),
        contentAlignment = Alignment.Center,
    ) {
        when {
            !requestedLoad -> {
                if (placeholder != null) {
                    placeholder()
                } else {
                    _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
                        text = "Loading...",
                        color = colorScheme.text.placeholder,
                        style = typography.small,
                    )
                }
            }

            loadFailed -> {
                if (error != null) {
                    error()
                } else {
                    _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
                        text = if (alt.isBlank()) "Load Failed" else alt,
                        color = colorScheme.danger.base,
                        style = typography.small,
                    )
                }
            }

            else -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
                        text = "IMG",
                        color = colorScheme.primary.base,
                        style = typography.large,
                    )
                    _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
                        text = "fit=${fit.name.lowercase()}",
                        color = colorScheme.text.placeholder,
                        style = typography.extraSmall,
                    )
                    if (alt.isNotBlank()) {
                        _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
                            text = alt,
                            color = colorScheme.text.secondary,
                            style = typography.extraSmall,
                        )
                    }
                }
            }
        }
    }

    if (state.previewVisible && previewList.isNotEmpty()) {
        val safeIndex = state.activeIndex.coerceIn(0, (previewList.size - 1).coerceAtLeast(0))
        val activeSrc = previewList[safeIndex]
        val previewFailed = activeSrc.startsWith("error:", ignoreCase = true)

        fun closePreview() {
            state.closePreview()
        }
        fun prevImage() {
            state.prev(total = previewList.size, infinite = infinite)
        }
        fun nextImage() {
            state.next(total = previewList.size, infinite = infinite)
        }
        fun setActiveItem(index: Int) {
            state.setActiveItem(index, previewList.size)
        }

        Popup(
            onDismissRequest = if (hideOnClickModal) ({ closePreview() }) else null,
            properties = PopupProperties(focusable = true),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .then(
                        if (closeOnPressEscape) {
                            Modifier.onKeyEvent {
                                if (it.key == Key.Escape) {
                                    closePreview()
                                    true
                                } else {
                                    false
                                }
                            }
                        } else {
                            Modifier
                        },
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(colorScheme.overlay.base.copy(alpha = 0.84f))
                        .then(
                            if (hideOnClickModal) {
                                Modifier.clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                ) { closePreview() }
                            } else {
                                Modifier
                            }
                        ),
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 56.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    if (toolbar != null) {
                        toolbar(
                            safeIndex,
                            previewList.size,
                            { setActiveItem(it) },
                            { prevImage() },
                            { nextImage() },
                            { state.reset(scale) },
                            { state.zoomIn(zoomRate, maxScale) },
                            { state.zoomOut(zoomRate, minScale) },
                        )
                    } else {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusButton(
                                text = "Close",
                                onClick = { closePreview() },
                                size = _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Small,
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(380.dp)
                            .clip(_root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.shapes.base)
                            .background(colorScheme.fill.blank),
                        contentAlignment = Alignment.Center,
                    ) {
                        if (previewFailed) {
                            if (viewerError != null) {
                                viewerError(safeIndex, activeSrc)
                            } else if (error != null) {
                                error()
                            } else {
                                _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
                                    text = "Viewer Error",
                                    color = colorScheme.danger.base,
                                )
                            }
                        } else {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(6.dp),
                            ) {
                                _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
                                    text = "Preview ${safeIndex + 1}",
                                    color = colorScheme.primary.base,
                                    style = typography.large,
                                )
                                _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
                                    text = activeSrc,
                                    color = colorScheme.text.secondary,
                                    style = typography.extraSmall,
                                )
                                _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
                                    text = "scale=${_root_ide_package_.io.github.xingray.compose.nexus.controls.toShortScale(state.scale)}",
                                    color = colorScheme.text.placeholder,
                                    style = typography.extraSmall,
                                )
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            _root_ide_package_.io.github.xingray.compose.nexus.controls.ControlDot(text = "‹", onClick = { prevImage() })
                            _root_ide_package_.io.github.xingray.compose.nexus.controls.ControlDot(text = "›", onClick = { nextImage() })
                        }
                        if (progress != null) {
                            progress(safeIndex, previewList.size)
                        } else if (showProgress) {
                            _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
                                text = "${safeIndex + 1}/${previewList.size}",
                                color = colorScheme.white,
                                style = typography.small,
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun toShortScale(value: Float): String {
    val rounded = (value * 100).toInt() / 100f
    val text = rounded.toString()
    return if (text.contains('.')) text.trimEnd('0').trimEnd('.') else text
}

@Composable
private fun ControlDot(
    text: String,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(30.dp)
            .clip(CircleShape)
            .background(_root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.colorScheme.overlay.lighter)
            .clickable(onClick = onClick)
            .pointerHoverIcon(PointerIcon.Hand),
        contentAlignment = Alignment.Center,
    ) {
        _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
            text = text,
            color = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.colorScheme.white,
            style = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.typography.base,
        )
    }
}
