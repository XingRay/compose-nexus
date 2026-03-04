package io.github.xingray.compose_nexus.controls

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.xingray.compose_nexus.theme.NexusTheme
import io.github.xingray.compose_nexus.theme.NexusType
import io.github.xingray.compose_nexus.theme.typeColor

@Stable
class NexusVirtualizedTableColumn<T>(
    val key: String,
    val title: String,
    val width: Dp,
    val alignment: Alignment = Alignment.CenterStart,
    val headerAlignment: Alignment = alignment,
    val cellRenderer: @Composable (row: T, rowIndex: Int) -> Unit,
)

@Composable
fun <T> NexusVirtualizedTable(
    data: List<T>,
    columns: List<NexusVirtualizedTableColumn<T>>,
    modifier: Modifier = Modifier,
    width: Dp? = null,
    height: Dp = 420.dp,
    headerHeight: Dp = 48.dp,
    rowHeight: Dp = 44.dp,
    stripe: Boolean = false,
    border: Boolean = true,
    fixedData: List<T> = emptyList(),
    rowClassName: ((rowIndex: Int, row: T) -> NexusType?)? = null,
    listState: LazyListState = rememberLazyListState(),
    onEndReached: ((remainDistance: Int) -> Unit)? = null,
    onScroll: ((scrollTop: Int) -> Unit)? = null,
    empty: (@Composable () -> Unit)? = null,
    footer: (@Composable () -> Unit)? = null,
    overlay: (@Composable () -> Unit)? = null,
) {
    val colorScheme = NexusTheme.colorScheme
    val borderColor = colorScheme.border.lighter
    val baseModifier = Modifier.then(if (width != null) Modifier.width(width) else Modifier.fillMaxWidth())
    val shouldNotifyEnd by remember(data.size, listState) {
        derivedStateOf {
            val info = listState.layoutInfo
            val last = info.visibleItemsInfo.lastOrNull()?.index ?: -1
            val remain = (data.size - 1 - last).coerceAtLeast(0)
            remain <= 3
        }
    }

    LaunchedEffect(listState.firstVisibleItemScrollOffset, listState.firstVisibleItemIndex) {
        val top = listState.firstVisibleItemIndex * rowHeight.value.toInt() + listState.firstVisibleItemScrollOffset
        onScroll?.invoke(top)
    }

    LaunchedEffect(shouldNotifyEnd, data.size) {
        if (shouldNotifyEnd) {
            val last = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1
            val remain = (data.size - 1 - last).coerceAtLeast(0)
            onEndReached?.invoke(remain)
        }
    }

    Box(
        modifier = modifier
            .then(baseModifier)
            .height(height)
            .then(if (border) Modifier.border(1.dp, borderColor, NexusTheme.shapes.base) else Modifier),
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(headerHeight)
                    .background(colorScheme.fill.light),
            ) {
                columns.forEach { column ->
                    Box(
                        modifier = Modifier
                            .width(column.width)
                            .height(headerHeight)
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        contentAlignment = column.headerAlignment,
                    ) {
                        NexusText(
                            text = column.title,
                            style = NexusTheme.typography.small,
                            color = colorScheme.text.primary,
                        )
                    }
                }
            }

            // Fixed rows below header.
            if (fixedData.isNotEmpty()) {
                ColumnRows(
                    rows = fixedData,
                    columns = columns,
                    rowHeight = rowHeight,
                    startY = headerHeight,
                    stripe = stripe,
                    rowClassName = rowClassName,
                )
            }

            val bodyTop = headerHeight + rowHeight * fixedData.size
            val bodyHeight = (height - bodyTop).coerceAtLeast(40.dp)

            if (data.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(bodyHeight)
                        .padding(top = bodyTop),
                    contentAlignment = Alignment.Center,
                ) {
                    if (empty != null) {
                        empty()
                    } else {
                        NexusText(text = "No Data", color = colorScheme.text.secondary)
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(bodyHeight)
                        .padding(top = bodyTop),
                    state = listState,
                ) {
                    itemsIndexed(data, key = { index, _ -> index }) { rowIndex, row ->
                        val status = rowClassName?.invoke(rowIndex, row)
                        val statusBg = status?.let { colorScheme.typeColor(it)?.light9 }
                        val bg = when {
                            statusBg != null -> statusBg
                            stripe && rowIndex % 2 == 1 -> colorScheme.fill.lighter
                            else -> colorScheme.fill.blank
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(rowHeight)
                                .background(bg),
                        ) {
                            columns.forEach { column ->
                                Box(
                                    modifier = Modifier
                                        .width(column.width)
                                        .height(rowHeight)
                                        .padding(horizontal = 12.dp, vertical = 8.dp),
                                    contentAlignment = column.alignment,
                                ) {
                                    column.cellRenderer(row, rowIndex)
                                }
                            }
                        }
                    }
                    if (footer != null) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 10.dp),
                            ) {
                                footer()
                            }
                        }
                    }
                }
            }
        }

        if (overlay != null) {
            Box(
                modifier = Modifier.fillMaxWidth().height(height),
                contentAlignment = Alignment.Center,
            ) {
                overlay()
            }
        }
    }
}

@Composable
private fun <T> ColumnRows(
    rows: List<T>,
    columns: List<NexusVirtualizedTableColumn<T>>,
    rowHeight: Dp,
    startY: Dp,
    stripe: Boolean,
    rowClassName: ((rowIndex: Int, row: T) -> NexusType?)?,
) {
    val colorScheme = NexusTheme.colorScheme
    Box(modifier = Modifier.padding(top = startY)) {
        rows.forEachIndexed { rowIndex, row ->
            val status = rowClassName?.invoke(rowIndex, row)
            val statusBg = status?.let { colorScheme.typeColor(it)?.light9 }
            val bg = when {
                statusBg != null -> statusBg
                stripe && rowIndex % 2 == 1 -> colorScheme.fill.lighter
                else -> colorScheme.fill.blank
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(rowHeight)
                    .padding(top = rowHeight * rowIndex)
                    .background(bg),
            ) {
                columns.forEach { column ->
                    Box(
                        modifier = Modifier
                            .width(column.width)
                            .height(rowHeight)
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        contentAlignment = column.alignment,
                    ) {
                        column.cellRenderer(row, rowIndex)
                    }
                }
            }
        }
    }
}
