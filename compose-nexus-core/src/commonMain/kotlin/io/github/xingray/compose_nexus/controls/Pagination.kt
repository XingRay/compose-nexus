package io.github.xingray.compose_nexus.controls

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import io.github.xingray.compose_nexus.theme.ComponentSize
import io.github.xingray.compose_nexus.theme.NexusTheme

/**
 * Pagination state holder.
 */
@Stable
class PaginationState(
    initialPage: Int = 1,
    val pageCount: Int,
) {
    var currentPage by mutableIntStateOf(initialPage.coerceIn(1, pageCount.coerceAtLeast(1)))
        private set

    fun goTo(page: Int) {
        currentPage = page.coerceIn(1, pageCount.coerceAtLeast(1))
    }

    fun next() = goTo(currentPage + 1)
    fun prev() = goTo(currentPage - 1)
}

@Composable
fun rememberPaginationState(
    initialPage: Int = 1,
    pageCount: Int,
): PaginationState = remember(pageCount) {
    PaginationState(initialPage, pageCount)
}

/**
 * Element Plus Pagination — a page navigation component.
 *
 * @param state Pagination state.
 * @param modifier Modifier.
 * @param size Component size.
 * @param pagerCount Maximum number of page buttons to show (odd number, ≥5).
 * @param onPageChange Callback when page changes.
 */
@Composable
fun NexusPagination(
    state: PaginationState,
    modifier: Modifier = Modifier,
    size: ComponentSize = ComponentSize.Default,
    pagerCount: Int = 7,
    onPageChange: ((Int) -> Unit)? = null,
) {
    val colorScheme = NexusTheme.colorScheme
    val typography = NexusTheme.typography
    val shapes = NexusTheme.shapes

    val effectivePagerCount = pagerCount.coerceAtLeast(5).let { if (it % 2 == 0) it + 1 else it }
    val pageCount = state.pageCount
    val current = state.currentPage

    val textStyle = when (size) {
        ComponentSize.Large -> typography.base
        ComponentSize.Default -> typography.small
        ComponentSize.Small -> typography.extraSmall
    }
    val itemSize = when (size) {
        ComponentSize.Large -> 40.dp
        ComponentSize.Default -> 32.dp
        ComponentSize.Small -> 24.dp
    }

    val pages = buildPageList(current, pageCount, effectivePagerCount)

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Prev button
        PaginationItem(
            text = "‹",
            isActive = false,
            enabled = current > 1,
            itemSize = itemSize,
            onClick = {
                state.prev()
                onPageChange?.invoke(state.currentPage)
            },
        )

        // Page buttons
        pages.forEach { page ->
            when (page) {
                -1 -> {
                    // Ellipsis
                    Box(
                        modifier = Modifier.defaultMinSize(minWidth = itemSize, minHeight = itemSize),
                        contentAlignment = Alignment.Center,
                    ) {
                        NexusText(
                            text = "···",
                            color = colorScheme.text.placeholder,
                            style = textStyle,
                        )
                    }
                }
                else -> {
                    PaginationItem(
                        text = page.toString(),
                        isActive = page == current,
                        enabled = true,
                        itemSize = itemSize,
                        onClick = {
                            state.goTo(page)
                            onPageChange?.invoke(page)
                        },
                    )
                }
            }
        }

        // Next button
        PaginationItem(
            text = "›",
            isActive = false,
            enabled = current < pageCount,
            itemSize = itemSize,
            onClick = {
                state.next()
                onPageChange?.invoke(state.currentPage)
            },
        )
    }
}

@Composable
private fun PaginationItem(
    text: String,
    isActive: Boolean,
    enabled: Boolean,
    itemSize: androidx.compose.ui.unit.Dp,
    onClick: () -> Unit,
) {
    val colorScheme = NexusTheme.colorScheme
    val typography = NexusTheme.typography
    val shapes = NexusTheme.shapes

    val bgColor = if (isActive) colorScheme.primary.base else colorScheme.fill.blank
    val textColor = when {
        !enabled -> colorScheme.text.disabled
        isActive -> colorScheme.white
        else -> colorScheme.text.regular
    }

    Box(
        modifier = Modifier
            .defaultMinSize(minWidth = itemSize, minHeight = itemSize)
            .clip(shapes.base)
            .background(bgColor)
            .then(
                if (!isActive) Modifier.border(1.dp, colorScheme.border.lighter, shapes.base)
                else Modifier
            )
            .then(
                if (enabled && !isActive) {
                    Modifier
                        .clickable(onClick = onClick)
                        .pointerHoverIcon(PointerIcon.Hand)
                } else if (enabled && isActive) {
                    Modifier
                } else {
                    Modifier
                }
            ),
        contentAlignment = Alignment.Center,
    ) {
        NexusText(
            text = text,
            color = textColor,
            style = typography.extraSmall,
        )
    }
}

/**
 * Build list of page numbers to display, using -1 as ellipsis marker.
 */
private fun buildPageList(current: Int, total: Int, pagerCount: Int): List<Int> {
    if (total <= pagerCount) {
        return (1..total).toList()
    }

    val half = pagerCount / 2
    val pages = mutableListOf<Int>()

    // Always show page 1
    pages.add(1)

    val rangeStart: Int
    val rangeEnd: Int

    when {
        current <= half + 1 -> {
            rangeStart = 2
            rangeEnd = pagerCount - 1
        }
        current >= total - half -> {
            rangeStart = total - pagerCount + 2
            rangeEnd = total - 1
        }
        else -> {
            rangeStart = current - half + 1
            rangeEnd = current + half - 1
        }
    }

    if (rangeStart > 2) pages.add(-1) // left ellipsis
    for (i in rangeStart..rangeEnd) pages.add(i)
    if (rangeEnd < total - 1) pages.add(-1) // right ellipsis

    // Always show last page
    pages.add(total)

    return pages
}
