package io.github.xingray.compose_nexus.controls

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.xingray.compose_nexus.theme.NexusTheme
import kotlin.math.max
import kotlin.math.min

enum class NexusSplitterLayout {
    Horizontal,
    Vertical,
}

enum class NexusSplitterCollapseType {
    Start,
    End,
}

sealed interface NexusSplitterPanelSize {
    data class Percent(val value: Float) : NexusSplitterPanelSize
    data class Pixels(val value: Float) : NexusSplitterPanelSize
    data class DpSize(val value: Dp) : NexusSplitterPanelSize
}

fun splitterPercent(value: Number): NexusSplitterPanelSize = NexusSplitterPanelSize.Percent(value.toFloat())
fun splitterPx(value: Number): NexusSplitterPanelSize = NexusSplitterPanelSize.Pixels(value.toFloat())
fun splitterDp(value: Dp): NexusSplitterPanelSize = NexusSplitterPanelSize.DpSize(value)

internal data class NexusSplitterPanelData(
    val size: NexusSplitterPanelSize?,
    val min: NexusSplitterPanelSize?,
    val max: NexusSplitterPanelSize?,
    val resizable: Boolean,
    val collapsible: Boolean,
    val onSizeChange: ((Float) -> Unit)?,
    val startCollapsible: (@Composable () -> Unit)?,
    val endCollapsible: (@Composable () -> Unit)?,
    val content: @Composable BoxScope.() -> Unit,
)

class NexusSplitterScope internal constructor() {
    private val panels: MutableList<NexusSplitterPanelData> = mutableListOf()

    internal fun clear() {
        panels.clear()
    }

    internal fun snapshot(): List<NexusSplitterPanelData> = panels.toList()

    fun panel(
        size: NexusSplitterPanelSize? = null,
        min: NexusSplitterPanelSize? = null,
        max: NexusSplitterPanelSize? = null,
        resizable: Boolean = true,
        collapsible: Boolean = false,
        onSizeChange: ((Float) -> Unit)? = null,
        startCollapsible: (@Composable () -> Unit)? = null,
        endCollapsible: (@Composable () -> Unit)? = null,
        content: @Composable BoxScope.() -> Unit,
    ) {
        panels += NexusSplitterPanelData(
            size = size,
            min = min,
            max = max,
            resizable = resizable,
            collapsible = collapsible,
            onSizeChange = onSizeChange,
            startCollapsible = startCollapsible,
            endCollapsible = endCollapsible,
            content = content,
        )
    }
}

@Composable
fun NexusSplitter(
    modifier: Modifier = Modifier,
    layout: NexusSplitterLayout = NexusSplitterLayout.Horizontal,
    lazy: Boolean = false,
    barSize: Dp = 16.dp,
    onResizeStart: ((index: Int, sizes: List<Float>) -> Unit)? = null,
    onResize: ((index: Int, sizes: List<Float>) -> Unit)? = null,
    onResizeEnd: ((index: Int, sizes: List<Float>) -> Unit)? = null,
    onCollapse: ((index: Int, type: NexusSplitterCollapseType, sizes: List<Float>) -> Unit)? = null,
    content: NexusSplitterScope.() -> Unit,
) {
    val colors = NexusTheme.colorScheme
    val scope = remember { NexusSplitterScope() }
    scope.clear()
    scope.content()
    val panels = scope.snapshot()
    if (panels.size < 2) {
        return
    }

    val panelConfigKey = remember(panels) {
        buildString {
            panels.forEachIndexed { index, panel ->
                append(index)
                append(':')
                append(panel.size)
                append(':')
                append(panel.min)
                append(':')
                append(panel.max)
                append(':')
                append(panel.resizable)
                append(':')
                append(panel.collapsible)
                append('|')
            }
        }
    }

    BoxWithConstraints(
        modifier = modifier
            .clip(NexusTheme.shapes.base)
            .border(1.dp, colors.border.light, NexusTheme.shapes.base)
            .background(colors.fill.blank),
    ) {
        val mainSizeDp = if (layout == NexusSplitterLayout.Horizontal) maxWidth else maxHeight
        if (!mainSizeDp.value.isFinite() || mainSizeDp <= 0.dp) {
            // Without a bounded size we cannot provide reliable drag behavior.
            if (layout == NexusSplitterLayout.Horizontal) {
                Row(modifier = Modifier.fillMaxSize()) {
                    panels.forEach { panel ->
                        Box(modifier = Modifier.weight(1f).fillMaxHeight(), content = panel.content)
                    }
                }
            } else {
                Column(modifier = Modifier.fillMaxSize()) {
                    panels.forEach { panel ->
                        Box(modifier = Modifier.weight(1f).fillMaxWidth(), content = panel.content)
                    }
                }
            }
            return@BoxWithConstraints
        }

        val density = LocalDensity.current
        val mainSizePx = with(density) { mainSizeDp.toPx() }
        val barPx = with(density) { barSize.toPx() }
        val panelAreaPx = (mainSizePx - barPx * (panels.size - 1)).coerceAtLeast(1f)
        val panelAreaDp = with(density) { panelAreaPx.toDp() }

        var ratios by remember(panelConfigKey, panels.size, panelAreaPx) {
            mutableStateOf(computeInitialRatios(panels, panelAreaPx, density))
        }
        var activeBarIndex by remember { mutableStateOf<Int?>(null) }
        var lazyOffsetPx by remember { mutableStateOf(0f) }
        var pendingRatios by remember { mutableStateOf<List<Float>?>(null) }
        val collapseCache = remember { mutableStateMapOf<Pair<Int, NexusSplitterCollapseType>, Float>() }

        fun currentSizesPx(targetRatios: List<Float>): List<Float> {
            return targetRatios.map { it * panelAreaPx }
        }

        fun notifyPanelSizeChange(targetRatios: List<Float>) {
            val sizes = currentSizesPx(targetRatios)
            panels.forEachIndexed { index, panel ->
                panel.onSizeChange?.invoke(sizes[index])
            }
        }

        fun collapse(index: Int, type: NexusSplitterCollapseType) {
            val panelIndex = if (type == NexusSplitterCollapseType.Start) index else index + 1
            val neighborIndex = if (type == NexusSplitterCollapseType.Start) index + 1 else index
            if (panelIndex !in panels.indices || neighborIndex !in panels.indices) {
                return
            }

            val mutable = ratios.toMutableList()
            val current = mutable[panelIndex]
            val key = index to type
            if (current > 0.0001f) {
                collapseCache[key] = current
                mutable[panelIndex] = 0f
                mutable[neighborIndex] += current
            } else {
                val restore = collapseCache[key] ?: 0.2f
                val neighborMin = resolveRatio(panels[neighborIndex].min, panelAreaPx, density)?.coerceIn(0f, 1f) ?: 0f
                val available = (mutable[neighborIndex] - neighborMin).coerceAtLeast(0f)
                val restored = restore.coerceAtMost(available)
                mutable[panelIndex] += restored
                mutable[neighborIndex] -= restored
            }

            ratios = normalizeRatios(mutable)
            val sizes = currentSizesPx(ratios)
            notifyPanelSizeChange(ratios)
            onCollapse?.invoke(index, type, sizes)
        }

        fun updateRatiosByDelta(index: Int, deltaPx: Float, baseRatios: List<Float>): List<Float> {
            if (index < 0 || index >= panels.lastIndex) {
                return baseRatios
            }
            val startPanel = panels[index]
            val endPanel = panels[index + 1]
            if (!startPanel.resizable || !endPanel.resizable) {
                return baseRatios
            }

            val deltaRatio = deltaPx / panelAreaPx
            val oldStart = baseRatios[index]
            val oldEnd = baseRatios[index + 1]
            val pairTotal = oldStart + oldEnd

            val startMin = resolveRatio(startPanel.min, panelAreaPx, density)?.coerceIn(0f, 1f) ?: 0f
            val endMin = resolveRatio(endPanel.min, panelAreaPx, density)?.coerceIn(0f, 1f) ?: 0f
            val startMax = resolveRatio(startPanel.max, panelAreaPx, density)?.coerceIn(0f, 1f) ?: 1f
            val endMax = resolveRatio(endPanel.max, panelAreaPx, density)?.coerceIn(0f, 1f) ?: 1f

            val minStart = max(startMin, pairTotal - endMax)
            val maxStart = min(startMax, pairTotal - endMin)
            val nextStart = (oldStart + deltaRatio).coerceIn(minStart, maxStart)
            val nextEnd = (pairTotal - nextStart).coerceAtLeast(0f)

            val updated = baseRatios.toMutableList()
            updated[index] = nextStart
            updated[index + 1] = nextEnd
            return normalizeRatios(updated)
        }

        LaunchedEffect(ratios, panelAreaPx) {
            notifyPanelSizeChange(ratios)
        }

        val renderRatios = pendingRatios?.takeIf { lazy } ?: ratios

        if (layout == NexusSplitterLayout.Horizontal) {
            Row(modifier = Modifier.fillMaxSize()) {
                panels.forEachIndexed { index, panel ->
                    Box(
                        modifier = Modifier
                            .width(panelAreaDp * renderRatios[index])
                            .fillMaxHeight(),
                        content = panel.content,
                    )

                    if (index < panels.lastIndex) {
                        val canResize = panel.resizable && panels[index + 1].resizable
                        val startCollapsible = panel.collapsible
                        val endCollapsible = panels[index + 1].collapsible
                        val dragState = rememberDraggableState { delta ->
                            val base = pendingRatios ?: ratios
                            val next = updateRatiosByDelta(index, delta, base)
                            val moved = (next[index] - base[index]) * panelAreaPx
                            lazyOffsetPx += moved
                            if (lazy) {
                                pendingRatios = next
                            } else {
                                ratios = next
                            }
                            onResize?.invoke(index, currentSizesPx(next))
                        }

                        NexusSplitterBar(
                            modifier = Modifier
                                .width(barSize)
                                .fillMaxHeight(),
                            layout = layout,
                            lazy = lazy,
                            resizable = canResize,
                            showStartCollapse = startCollapsible,
                            showEndCollapse = endCollapsible,
                            lazyOffset = with(density) { (if (activeBarIndex == index) lazyOffsetPx else 0f).toDp() },
                            startCollapsible = panel.startCollapsible,
                            endCollapsible = panels[index + 1].endCollapsible,
                            onCollapseStart = { collapse(index, NexusSplitterCollapseType.Start) },
                            onCollapseEnd = { collapse(index, NexusSplitterCollapseType.End) },
                            draggableModifier = Modifier.draggable(
                                state = dragState,
                                orientation = Orientation.Horizontal,
                                enabled = canResize,
                                onDragStarted = {
                                    activeBarIndex = index
                                    pendingRatios = null
                                    lazyOffsetPx = 0f
                                    onResizeStart?.invoke(index, currentSizesPx(ratios))
                                },
                                onDragStopped = {
                                    val pending = pendingRatios
                                    if (lazy && pending != null) {
                                        ratios = pending
                                    }
                                    val finalRatios = pending ?: ratios
                                    onResizeEnd?.invoke(index, currentSizesPx(finalRatios))
                                    pendingRatios = null
                                    activeBarIndex = null
                                    lazyOffsetPx = 0f
                                },
                            ),
                        )
                    }
                }
            }
        } else {
            Column(modifier = Modifier.fillMaxSize()) {
                panels.forEachIndexed { index, panel ->
                    Box(
                        modifier = Modifier
                            .height(panelAreaDp * renderRatios[index])
                            .fillMaxWidth(),
                        content = panel.content,
                    )

                    if (index < panels.lastIndex) {
                        val canResize = panel.resizable && panels[index + 1].resizable
                        val startCollapsible = panel.collapsible
                        val endCollapsible = panels[index + 1].collapsible
                        val dragState = rememberDraggableState { delta ->
                            val base = pendingRatios ?: ratios
                            val next = updateRatiosByDelta(index, delta, base)
                            val moved = (next[index] - base[index]) * panelAreaPx
                            lazyOffsetPx += moved
                            if (lazy) {
                                pendingRatios = next
                            } else {
                                ratios = next
                            }
                            onResize?.invoke(index, currentSizesPx(next))
                        }

                        NexusSplitterBar(
                            modifier = Modifier
                                .height(barSize)
                                .fillMaxWidth(),
                            layout = layout,
                            lazy = lazy,
                            resizable = canResize,
                            showStartCollapse = startCollapsible,
                            showEndCollapse = endCollapsible,
                            lazyOffset = with(density) { (if (activeBarIndex == index) lazyOffsetPx else 0f).toDp() },
                            startCollapsible = panel.startCollapsible,
                            endCollapsible = panels[index + 1].endCollapsible,
                            onCollapseStart = { collapse(index, NexusSplitterCollapseType.Start) },
                            onCollapseEnd = { collapse(index, NexusSplitterCollapseType.End) },
                            draggableModifier = Modifier.draggable(
                                state = dragState,
                                orientation = Orientation.Vertical,
                                enabled = canResize,
                                onDragStarted = {
                                    activeBarIndex = index
                                    pendingRatios = null
                                    lazyOffsetPx = 0f
                                    onResizeStart?.invoke(index, currentSizesPx(ratios))
                                },
                                onDragStopped = {
                                    val pending = pendingRatios
                                    if (lazy && pending != null) {
                                        ratios = pending
                                    }
                                    val finalRatios = pending ?: ratios
                                    onResizeEnd?.invoke(index, currentSizesPx(finalRatios))
                                    pendingRatios = null
                                    activeBarIndex = null
                                    lazyOffsetPx = 0f
                                },
                            ),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun NexusSplitterBar(
    modifier: Modifier,
    layout: NexusSplitterLayout,
    lazy: Boolean,
    resizable: Boolean,
    showStartCollapse: Boolean,
    showEndCollapse: Boolean,
    lazyOffset: Dp,
    startCollapsible: (@Composable () -> Unit)?,
    endCollapsible: (@Composable () -> Unit)?,
    onCollapseStart: () -> Unit,
    onCollapseEnd: () -> Unit,
    draggableModifier: Modifier,
) {
    val colors = NexusTheme.colorScheme
    val isHorizontal = layout == NexusSplitterLayout.Horizontal
    Box(
        modifier = modifier
            .background(colors.fill.blank)
            .then(draggableModifier),
    ) {
        Box(
            modifier = if (isHorizontal) {
                Modifier
                    .align(Alignment.Center)
                    .width(2.dp)
                    .fillMaxHeight()
                    .offset(x = if (lazy) lazyOffset else 0.dp)
                    .background(if (resizable) colors.border.base else colors.border.light)
            } else {
                Modifier
                    .align(Alignment.Center)
                    .height(2.dp)
                    .fillMaxWidth()
                    .offset(y = if (lazy) lazyOffset else 0.dp)
                    .background(if (resizable) colors.border.base else colors.border.light)
            }
        )

        if (showStartCollapse) {
            Box(
                modifier = if (isHorizontal) {
                    Modifier
                        .align(Alignment.CenterStart)
                        .offset(x = (-12).dp)
                        .width(14.dp)
                        .height(24.dp)
                } else {
                    Modifier
                        .align(Alignment.TopCenter)
                        .offset(y = (-12).dp)
                        .width(24.dp)
                        .height(14.dp)
                }
                    .background(colors.border.light, NexusTheme.shapes.small)
                    .clickable { onCollapseStart() },
                contentAlignment = Alignment.Center,
            ) {
                if (startCollapsible != null) {
                    startCollapsible()
                } else {
                    BasicText(if (isHorizontal) "<" else "^", style = NexusTheme.typography.small.copy(color = colors.text.regular))
                }
            }
        }

        if (showEndCollapse) {
            Box(
                modifier = if (isHorizontal) {
                    Modifier
                        .align(Alignment.CenterEnd)
                        .offset(x = 12.dp)
                        .width(14.dp)
                        .height(24.dp)
                } else {
                    Modifier
                        .align(Alignment.BottomCenter)
                        .offset(y = 12.dp)
                        .width(24.dp)
                        .height(14.dp)
                }
                    .background(colors.border.light, NexusTheme.shapes.small)
                    .clickable { onCollapseEnd() },
                contentAlignment = Alignment.Center,
            ) {
                if (endCollapsible != null) {
                    endCollapsible()
                } else {
                    BasicText(if (isHorizontal) ">" else "v", style = NexusTheme.typography.small.copy(color = colors.text.regular))
                }
            }
        }
    }
}

private fun computeInitialRatios(
    panels: List<NexusSplitterPanelData>,
    panelAreaPx: Float,
    density: Density,
): List<Float> {
    if (panels.isEmpty()) {
        return emptyList()
    }

    val ratios = MutableList<Float?>(panels.size) { null }
    var totalSpecified = 0f
    var emptyCount = 0

    panels.forEachIndexed { index, panel ->
        val ratio = resolveRatio(panel.size, panelAreaPx, density)
        if (ratio == null) {
            emptyCount += 1
        } else {
            ratios[index] = ratio.coerceAtLeast(0f)
            totalSpecified += ratio.coerceAtLeast(0f)
        }
    }

    val filled = if (totalSpecified > 1f || emptyCount == 0) {
        val scale = if (totalSpecified > 0f) 1f / totalSpecified else 0f
        ratios.map { ((it ?: 0f) * scale).coerceAtLeast(0f) }
    } else {
        val avg = (1f - totalSpecified) / emptyCount
        ratios.map { (it ?: avg).coerceAtLeast(0f) }
    }

    val constrained = filled.mapIndexed { index, ratio ->
        val minRatio = resolveRatio(panels[index].min, panelAreaPx, density) ?: 0f
        val maxRatio = resolveRatio(panels[index].max, panelAreaPx, density) ?: 1f
        ratio.coerceIn(min(minRatio, maxRatio), max(minRatio, maxRatio))
    }
    return normalizeRatios(constrained)
}

private fun resolveRatio(
    size: NexusSplitterPanelSize?,
    panelAreaPx: Float,
    density: Density,
): Float? {
    if (size == null || panelAreaPx <= 0f) {
        return null
    }
    return when (size) {
        is NexusSplitterPanelSize.Percent -> size.value / 100f
        is NexusSplitterPanelSize.Pixels -> size.value / panelAreaPx
        is NexusSplitterPanelSize.DpSize -> with(density) { size.value.toPx() / panelAreaPx }
    }
}

private fun normalizeRatios(ratios: List<Float>): List<Float> {
    if (ratios.isEmpty()) {
        return ratios
    }
    val safe = ratios.map { it.coerceAtLeast(0f) }
    val total = safe.sum()
    if (total <= 0f) {
        val each = 1f / safe.size.toFloat()
        return List(safe.size) { each }
    }
    return safe.map { it / total }
}
