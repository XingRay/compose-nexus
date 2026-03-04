package io.github.xingray.compose_nexus.controls

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.xingray.compose_nexus.theme.NexusTheme
import io.github.xingray.compose_nexus.theme.NexusType
import io.github.xingray.compose_nexus.theme.typeColor

class NexusTableColumn<T>(
    val header: String,
    val width: Dp = Dp.Unspecified,
    val weight: Float = 1f,
    val alignment: Alignment = Alignment.CenterStart,
    val headerAlignment: Alignment = alignment,
    val showOverflowTooltip: Boolean = false,
    val tooltipText: ((item: T) -> String)? = null,
    val content: @Composable (item: T) -> Unit,
)

@Stable
class TableState(
    initialCurrentRowIndex: Int? = null,
) {
    var currentRowIndex by mutableStateOf(initialCurrentRowIndex)
        internal set
}

@Composable
fun rememberTableState(
    initialCurrentRowIndex: Int? = null,
): TableState = remember { TableState(initialCurrentRowIndex) }

@Composable
fun <T> NexusTable(
    data: List<T>,
    columns: List<NexusTableColumn<T>>,
    modifier: Modifier = Modifier,
    stripe: Boolean = false,
    border: Boolean = true,
    emptyText: String = "No Data",
    height: Dp? = null,
    maxHeight: Dp? = null,
    showHeader: Boolean = true,
    highlightCurrentRow: Boolean = false,
    showOverflowTooltip: Boolean = false,
    currentRowIndex: Int? = null,
    state: TableState = rememberTableState(initialCurrentRowIndex = currentRowIndex),
    rowClassName: ((index: Int, row: T) -> NexusType?)? = null,
    onCurrentChange: ((currentRow: T?, oldCurrentRow: T?) -> Unit)? = null,
    onRowClick: ((index: Int, row: T) -> Unit)? = null,
) {
    val colorScheme = NexusTheme.colorScheme
    val typography = NexusTheme.typography
    val shapes = NexusTheme.shapes
    val borderColor = colorScheme.border.lighter
    val headerBg = colorScheme.fill.light
    val rowBg = colorScheme.fill.blank
    val stripeBg = colorScheme.fill.lighter
    val currentBg = colorScheme.primary.light9
    val verticalScrollState = rememberScrollState()

    LaunchedEffect(currentRowIndex) {
        if (currentRowIndex != null) {
            state.currentRowIndex = currentRowIndex
        }
    }

    fun rowStatusColor(type: NexusType?): androidx.compose.ui.graphics.Color? {
        if (type == null) return null
        return colorScheme.typeColor(type)?.light9
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(shapes.base)
            .then(if (border) Modifier.border(1.dp, borderColor, shapes.base) else Modifier),
    ) {
        if (showHeader) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
                    .background(headerBg)
                    .then(if (border) Modifier.border(0.5.dp, borderColor) else Modifier),
            ) {
                columns.forEach { column ->
                    TableCell(
                        width = column.width,
                        weight = column.weight,
                        alignment = column.headerAlignment,
                    ) {
                        NexusText(
                            text = column.header,
                            color = colorScheme.text.primary,
                            style = typography.small,
                        )
                    }
                }
            }
        }

        val bodyModifier = Modifier
            .fillMaxWidth()
            .then(
                when {
                    height != null -> Modifier.height(height).verticalScroll(verticalScrollState)
                    maxHeight != null -> Modifier.heightIn(max = maxHeight).verticalScroll(verticalScrollState)
                    else -> Modifier
                },
            )

        Column(modifier = bodyModifier) {
            if (data.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 40.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    NexusText(
                        text = emptyText,
                        color = colorScheme.text.secondary,
                        style = typography.base,
                    )
                }
            } else {
                data.forEachIndexed { rowIndex, item ->
                    val isCurrent = state.currentRowIndex == rowIndex
                    val statusBg = rowStatusColor(rowClassName?.invoke(rowIndex, item))
                    val bg = when {
                        isCurrent && highlightCurrentRow -> currentBg
                        statusBg != null -> statusBg
                        stripe && rowIndex % 2 == 1 -> stripeBg
                        else -> rowBg
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Min)
                            .background(bg)
                            .then(if (border) Modifier.border(0.5.dp, borderColor) else Modifier)
                            .then(
                                if (highlightCurrentRow || onRowClick != null) {
                                    Modifier
                                        .clickable {
                                            val oldIndex = state.currentRowIndex
                                            if (highlightCurrentRow) {
                                                state.currentRowIndex = rowIndex
                                                if (oldIndex != rowIndex) {
                                                    onCurrentChange?.invoke(
                                                        data.getOrNull(rowIndex),
                                                        oldIndex?.let { data.getOrNull(it) },
                                                    )
                                                }
                                            }
                                            onRowClick?.invoke(rowIndex, item)
                                        }
                                        .pointerHoverIcon(PointerIcon.Hand)
                                } else {
                                    Modifier
                                }
                            ),
                    ) {
                        columns.forEach { column ->
                            TableCell(
                                width = column.width,
                                weight = column.weight,
                                alignment = column.alignment,
                            ) {
                                val enableTooltip = showOverflowTooltip || column.showOverflowTooltip
                                val tooltip = if (enableTooltip) column.tooltipText?.invoke(item) else null
                                if (enableTooltip && !tooltip.isNullOrBlank()) {
                                    NexusTooltip(text = tooltip) {
                                        column.content(item)
                                    }
                                } else {
                                    column.content(item)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RowScope.TableCell(
    width: Dp,
    weight: Float,
    alignment: Alignment,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = Modifier
            .then(
                if (width != Dp.Unspecified) Modifier.width(width)
                else Modifier.weight(weight),
            )
            .padding(horizontal = 12.dp, vertical = 10.dp),
        contentAlignment = alignment,
    ) {
        content()
    }
}
