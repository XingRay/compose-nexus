package io.github.xingray.compose.nexus.controls

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.xingray.compose.nexus.theme.NexusTheme
import kotlinx.coroutines.delay

/**
 * InfiniteScroll state holder.
 */
@Stable
class InfiniteScrollState(
    val listState: LazyListState = LazyListState(),
) {
    var isLoading by mutableStateOf(false)
    var hasMore by mutableStateOf(true)
    var error by mutableStateOf(false)
    var disabled by mutableStateOf(false)
}

@Composable
fun rememberInfiniteScrollState(): InfiniteScrollState {
    val listState = rememberLazyListState()
    return remember { InfiniteScrollState(listState) }
}

/**
 * Element Plus InfiniteScroll — a list that automatically loads more items when scrolled to the bottom.
 *
 * @param T Item data type.
 * @param items Current list of items.
 * @param state InfiniteScroll state.
 * @param modifier Modifier.
 * @param threshold Number of items from the end to trigger loading.
 * @param onLoadMore Suspending callback to load more items.
 * @param loadingContent Composable shown while loading.
 * @param noMoreContent Composable shown when no more items.
 * @param itemContent Composable for each item.
 */
@Composable
fun <T> NexusInfiniteScroll(
    items: List<T>,
    state: InfiniteScrollState = rememberInfiniteScrollState(),
    modifier: Modifier = Modifier,
    threshold: Int = 3,
    infiniteScrollDisabled: Boolean = false,
    infiniteScrollDelay: Long = 200L,
    infiniteScrollDistance: Int = 0,
    infiniteScrollImmediate: Boolean = true,
    onLoadMore: suspend () -> Unit = {},
    loadingContent: (@Composable () -> Unit)? = null,
    noMoreContent: (@Composable () -> Unit)? = null,
    itemContent: @Composable (index: Int, item: T) -> Unit,
) {
    val colorScheme = NexusTheme.colorScheme
    val typography = NexusTheme.typography

    state.disabled = infiniteScrollDisabled

    // Detect when scrolled near the bottom.
    val shouldLoadMore by remember {
        derivedStateOf {
            val layoutInfo = state.listState.layoutInfo
            val lastVisible = layoutInfo.visibleItemsInfo.lastOrNull()
            val lastVisibleIndex = lastVisible?.index ?: -1
            val nearEndByIndex = lastVisibleIndex >= items.lastIndex - threshold
            val distanceToBottom = if (lastVisible == null) Int.MAX_VALUE else {
                layoutInfo.viewportEndOffset - (lastVisible.offset + lastVisible.size)
            }
            val nearEndByDistance = distanceToBottom <= infiniteScrollDistance
            state.hasMore &&
                !state.isLoading &&
                !state.disabled &&
                (nearEndByIndex || nearEndByDistance)
        }
    }

    val shouldImmediateLoad by remember {
        derivedStateOf {
            if (!infiniteScrollImmediate) return@derivedStateOf false
            val layoutInfo = state.listState.layoutInfo
            val contentNotFilled = layoutInfo.visibleItemsInfo.size >= items.size
            state.hasMore &&
                !state.isLoading &&
                !state.disabled &&
                contentNotFilled
        }
    }

    // Trigger loading with throttle delay.
    val triggerKey = remember(
        shouldLoadMore,
        shouldImmediateLoad,
        items.size,
        state.disabled,
        state.hasMore,
        state.isLoading,
        infiniteScrollDelay,
    ) {
        Triple(shouldLoadMore, shouldImmediateLoad, items.size)
    }
    LaunchedEffect(triggerKey) {
        val shouldTrigger = shouldLoadMore || shouldImmediateLoad
        if (shouldTrigger && state.hasMore && !state.isLoading && !state.disabled) {
            if (infiniteScrollDelay > 0) delay(infiniteScrollDelay)
            if (!state.hasMore || state.isLoading || state.disabled) return@LaunchedEffect
            state.isLoading = true
            try {
                onLoadMore()
            } catch (_: Exception) {
                state.error = true
            } finally {
                state.isLoading = false
            }
        }
    }

    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        state = state.listState,
    ) {
        items(
            count = items.size,
            key = { it },
        ) { index ->
            itemContent(index, items[index])
        }

        // Loading indicator
        if (state.isLoading) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    if (loadingContent != null) {
                        loadingContent()
                    } else {
                        NexusLoading(
                            loading = true,
                            text = "Loading...",
                            spinnerSize = 24.dp,
                        )
                    }
                }
            }
        }

        // No more items
        if (!state.hasMore && !state.isLoading) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    if (noMoreContent != null) {
                        noMoreContent()
                    } else {
                        NexusText(
                            text = "No more data",
                            color = colorScheme.text.placeholder,
                            style = typography.small,
                        )
                    }
                }
            }
        }
    }
}
