package io.github.xingray.compose_nexus.controls

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private const val GRID_COLUMNS = 24

enum class NexusRowJustify {
    Start,
    End,
    Center,
    SpaceAround,
    SpaceBetween,
    SpaceEvenly,
}

enum class NexusRowAlign {
    Top,
    Middle,
    Bottom,
}

data class NexusColSize(
    val span: Int? = null,
    val offset: Int = 0,
    val push: Int = 0,
    val pull: Int = 0,
) {
    constructor(span: Int) : this(span = span, offset = 0, push = 0, pull = 0)
}

private data class NexusRowLayoutConfig(
    val unitWidth: Dp,
    val rowWidth: Dp,
)

private val LocalNexusRowLayoutConfig = compositionLocalOf<NexusRowLayoutConfig?> { null }

@Composable
fun NexusRow(
    modifier: Modifier = Modifier,
    gutter: Dp = 0.dp,
    justify: NexusRowJustify = NexusRowJustify.Start,
    align: NexusRowAlign = NexusRowAlign.Top,
    content: @Composable () -> Unit,
) {
    BoxWithConstraints(modifier = Modifier.fillMaxWidth().then(modifier)) {
        val rowWidth = maxWidth
        val unitWidth = rowWidth / GRID_COLUMNS
        val arrangement = when (justify) {
            NexusRowJustify.Start -> Arrangement.spacedBy(gutter, Alignment.Start)
            NexusRowJustify.End -> Arrangement.spacedBy(gutter, Alignment.End)
            NexusRowJustify.Center -> Arrangement.spacedBy(gutter, Alignment.CenterHorizontally)
            NexusRowJustify.SpaceAround -> Arrangement.SpaceAround
            NexusRowJustify.SpaceBetween -> Arrangement.SpaceBetween
            NexusRowJustify.SpaceEvenly -> Arrangement.SpaceEvenly
        }
        val verticalAlignment = when (align) {
            NexusRowAlign.Top -> Alignment.Top
            NexusRowAlign.Middle -> Alignment.CenterVertically
            NexusRowAlign.Bottom -> Alignment.Bottom
        }
        CompositionLocalProvider(
            LocalNexusRowLayoutConfig provides NexusRowLayoutConfig(
                unitWidth = unitWidth,
                rowWidth = rowWidth,
            )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = arrangement,
                verticalAlignment = verticalAlignment,
            ) {
                content()
            }
        }
    }
}

@Composable
fun NexusCol(
    modifier: Modifier = Modifier,
    span: Int = GRID_COLUMNS,
    offset: Int = 0,
    push: Int = 0,
    pull: Int = 0,
    xs: NexusColSize? = null,
    sm: NexusColSize? = null,
    md: NexusColSize? = null,
    lg: NexusColSize? = null,
    xl: NexusColSize? = null,
    content: @Composable () -> Unit,
) {
    val rowConfig = LocalNexusRowLayoutConfig.current
    if (rowConfig == null) {
        Box(modifier = modifier) {
            content()
        }
        return
    }

    val responsive = resolveResponsiveSize(
        rowWidth = rowConfig.rowWidth,
        xs = xs,
        sm = sm,
        md = md,
        lg = lg,
        xl = xl,
    )
    val resolvedSpan = (responsive?.span ?: span).coerceIn(0, GRID_COLUMNS)
    val resolvedOffset = (responsive?.offset ?: offset).coerceIn(0, GRID_COLUMNS)
    val resolvedPush = (responsive?.push ?: push).coerceIn(-GRID_COLUMNS, GRID_COLUMNS)
    val resolvedPull = (responsive?.pull ?: pull).coerceIn(-GRID_COLUMNS, GRID_COLUMNS)
    val shift = resolvedPush - resolvedPull

    Row(
        modifier = modifier.width(rowConfig.unitWidth * (resolvedOffset + resolvedSpan)),
    ) {
        if (resolvedOffset > 0) {
            Spacer(modifier = Modifier.width(rowConfig.unitWidth * resolvedOffset))
        }
        Box(
            modifier = Modifier
                .width(rowConfig.unitWidth * resolvedSpan)
                .offset(x = rowConfig.unitWidth * shift),
        ) {
            content()
        }
    }
}

private fun resolveResponsiveSize(
    rowWidth: Dp,
    xs: NexusColSize?,
    sm: NexusColSize?,
    md: NexusColSize?,
    lg: NexusColSize?,
    xl: NexusColSize?,
): NexusColSize? {
    return when {
        rowWidth >= 1920.dp -> xl ?: lg ?: md ?: sm ?: xs
        rowWidth >= 1200.dp -> lg ?: md ?: sm ?: xs
        rowWidth >= 992.dp -> md ?: sm ?: xs
        rowWidth >= 768.dp -> sm ?: xs
        else -> xs
    }
}
