package io.github.xingray.compose_nexus.controls

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.xingray.compose_nexus.theme.NexusTheme

/**
 * Column definition for [NexusTable].
 *
 * @param T Row data type.
 * @param header Header text.
 * @param width Column width. `Dp.Unspecified` means the column will use weight.
 * @param weight Flex weight when [width] is `Dp.Unspecified`.
 * @param alignment Content alignment within the cell.
 * @param content Cell content composable receiving the row data item.
 */
@Immutable
class NexusTableColumn<T>(
    val header: String,
    val width: Dp = Dp.Unspecified,
    val weight: Float = 1f,
    val alignment: Alignment = Alignment.CenterStart,
    val content: @Composable (item: T) -> Unit,
)

/**
 * Element Plus Table — a data table with columns and rows.
 *
 * @param T Row data type.
 * @param data List of row data items.
 * @param columns Column definitions.
 * @param modifier Modifier.
 * @param stripe Whether to use alternating row background colors.
 * @param border Whether to show cell borders.
 * @param emptyText Text displayed when data is empty.
 */
@Composable
fun <T> NexusTable(
    data: List<T>,
    columns: List<NexusTableColumn<T>>,
    modifier: Modifier = Modifier,
    stripe: Boolean = false,
    border: Boolean = true,
    emptyText: String = "No Data",
) {
    val colorScheme = NexusTheme.colorScheme
    val typography = NexusTheme.typography
    val shapes = NexusTheme.shapes

    val borderColor = colorScheme.border.lighter
    val headerBg = colorScheme.fill.light
    val rowBg = colorScheme.fill.blank
    val stripeBg = colorScheme.fill.lighter

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(shapes.base)
            .then(
                if (border) Modifier.border(1.dp, borderColor, shapes.base)
                else Modifier
            ),
    ) {
        // Header row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .background(headerBg)
                .then(
                    if (border) Modifier.border(width = 0.5.dp, color = borderColor)
                    else Modifier
                ),
        ) {
            columns.forEachIndexed { index, column ->
                TableCell(
                    column = column,
                    border = border,
                    borderColor = borderColor,
                    isLast = index == columns.lastIndex,
                ) {
                    NexusText(
                        text = column.header,
                        color = colorScheme.text.primary,
                        style = typography.small,
                    )
                }
            }
        }

        // Data rows
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
                val bg = when {
                    stripe && rowIndex % 2 == 1 -> stripeBg
                    else -> rowBg
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min)
                        .background(bg)
                        .then(
                            if (border) Modifier.border(width = 0.5.dp, color = borderColor)
                            else Modifier
                        ),
                ) {
                    columns.forEachIndexed { colIndex, column ->
                        TableCell(
                            column = column,
                            border = border,
                            borderColor = borderColor,
                            isLast = colIndex == columns.lastIndex,
                        ) {
                            column.content(item)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun <T> RowScope.TableCell(
    column: NexusTableColumn<T>,
    border: Boolean,
    borderColor: Color,
    isLast: Boolean,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = Modifier
            .then(
                if (column.width != Dp.Unspecified) {
                    Modifier.width(column.width)
                } else {
                    Modifier.weight(column.weight)
                }
            )
            .padding(horizontal = 12.dp, vertical = 10.dp)
            .then(
                if (border && !isLast) {
                    Modifier.border(width = 0.dp, color = Color.Transparent) // spacing handled by Row border
                } else Modifier
            ),
        contentAlignment = column.alignment,
    ) {
        content()
    }
}
