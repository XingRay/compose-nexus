package io.github.xingray.compose_nexus.controls

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Element Plus VirtualList — a virtualized scrolling list for large datasets.
 *
 * Uses LazyColumn internally to only compose visible items, providing
 * efficient rendering for thousands of items.
 *
 * @param T Item data type.
 * @param items Full data list.
 * @param modifier Modifier.
 * @param height List container height.
 * @param itemHeight Estimated item height (used for scrollbar calculations).
 * @param listState LazyListState for scroll control.
 * @param itemContent Composable for each item.
 */
@Composable
fun <T> NexusVirtualList(
    items: List<T>,
    modifier: Modifier = Modifier,
    height: Dp = 400.dp,
    itemHeight: Dp = 40.dp,
    listState: LazyListState = rememberLazyListState(),
    itemContent: @Composable (index: Int, item: T) -> Unit,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .height(height),
        state = listState,
    ) {
        items(
            count = items.size,
            key = { it },
        ) { index ->
            Box(modifier = Modifier.fillMaxWidth()) {
                itemContent(index, items[index])
            }
        }
    }
}
