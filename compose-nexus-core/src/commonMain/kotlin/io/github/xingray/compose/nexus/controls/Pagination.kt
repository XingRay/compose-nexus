package io.github.xingray.compose.nexus.controls

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import io.github.xingray.compose.nexus.theme.ComponentSize
import io.github.xingray.compose.nexus.theme.NexusTheme

@Stable
class PaginationState(
    initialPage: Int = 1,
    initialPageSize: Int = 10,
    initialTotal: Int = 0,
    initialPageCount: Int? = null,
) {
    var currentPage by mutableIntStateOf(initialPage.coerceAtLeast(1))
        private set
    var pageSize by mutableIntStateOf(initialPageSize.coerceAtLeast(1))
        private set
    var total by mutableIntStateOf(initialTotal.coerceAtLeast(0))
        private set
    private var pageCountOverride by mutableIntStateOf((initialPageCount ?: 0).coerceAtLeast(0))

    val pageCount: Int
        get() = if (pageCountOverride > 0) {
            pageCountOverride
        } else {
            ((total + pageSize - 1) / pageSize).coerceAtLeast(1)
        }

    fun goTo(page: Int) {
        currentPage = page.coerceIn(1, pageCount.coerceAtLeast(1))
    }

    fun next() = goTo(currentPage + 1)
    fun prev() = goTo(currentPage - 1)

    fun updatePageSize(size: Int) {
        pageSize = size.coerceAtLeast(1)
        goTo(currentPage)
    }

    fun updateTotal(total: Int) {
        this.total = total.coerceAtLeast(0)
        goTo(currentPage)
    }

    fun setPageCount(pageCount: Int?) {
        pageCountOverride = (pageCount ?: 0).coerceAtLeast(0)
        goTo(currentPage)
    }

    fun updateCurrentPage(page: Int) {
        goTo(page)
    }
}

@Composable
fun rememberPaginationState(
    initialPage: Int = 1,
    pageCount: Int = 1,
    pageSize: Int = 10,
    total: Int = 0,
): PaginationState = remember(pageCount, pageSize, total) {
    PaginationState(
        initialPage = initialPage,
        initialPageSize = pageSize,
        initialTotal = total,
        initialPageCount = if (pageCount > 0) pageCount else null,
    )
}

@Composable
fun NexusPagination(
    state: PaginationState,
    modifier: Modifier = Modifier,
    size: ComponentSize = ComponentSize.Default,
    pagerCount: Int = 7,
    background: Boolean = false,
    layout: String = "prev, pager, next, jumper, ->, total",
    pageSizes: List<Int> = listOf(10, 20, 30, 40, 50, 100),
    total: Int? = null,
    pageCount: Int? = null,
    currentPage: Int? = null,
    pageSize: Int? = null,
    prevText: String = "",
    nextText: String = "",
    disabled: Boolean = false,
    hideOnSinglePage: Boolean = false,
    onPageChange: ((Int) -> Unit)? = null,
    onCurrentChange: ((Int) -> Unit)? = null,
    onSizeChange: ((Int) -> Unit)? = null,
    onChange: ((currentPage: Int, pageSize: Int) -> Unit)? = null,
    onPrevClick: ((Int) -> Unit)? = null,
    onNextClick: ((Int) -> Unit)? = null,
    slot: (@Composable () -> Unit)? = null,
) {
    LaunchedEffect(total) {
        if (total != null) state.updateTotal(total)
    }
    LaunchedEffect(pageCount) {
        if (pageCount != null) state.setPageCount(pageCount)
    }
    LaunchedEffect(pageSize) {
        if (pageSize != null) state.updatePageSize(pageSize)
    }
    LaunchedEffect(currentPage) {
        if (currentPage != null) state.updateCurrentPage(currentPage)
    }

    val colorScheme = NexusTheme.colorScheme
    val typography = NexusTheme.typography
    val effectivePagerCount = pagerCount.coerceAtLeast(5).let { if (it % 2 == 0) it + 1 else it }
    val totalPageCount = state.pageCount.coerceAtLeast(1)
    if (hideOnSinglePage && totalPageCount <= 1) return

    val itemSize = when (size) {
        ComponentSize.Large -> 40.dp
        ComponentSize.Default -> 32.dp
        ComponentSize.Small -> 24.dp
    }
    val textStyle = when (size) {
        ComponentSize.Large -> typography.base
        ComponentSize.Default -> typography.small
        ComponentSize.Small -> typography.extraSmall
    }
    val pages = buildPageList(state.currentPage, totalPageCount, effectivePagerCount)
    val sections = layout.split(",").map { it.trim() }.filter { it.isNotEmpty() }
    val splitIndex = sections.indexOf("->")
    val leftTokens = if (splitIndex >= 0) sections.take(splitIndex) else sections
    val rightTokens = if (splitIndex >= 0) sections.drop(splitIndex + 1) else emptyList()
    val sizeSelectState = rememberSelectState(initialSelected = state.pageSize)

    LaunchedEffect(state.pageSize) {
        sizeSelectState.selected = state.pageSize
    }

    fun emitCurrentChanged() {
        onPageChange?.invoke(state.currentPage)
        onCurrentChange?.invoke(state.currentPage)
        onChange?.invoke(state.currentPage, state.pageSize)
    }

    fun emitSizeChanged() {
        onSizeChange?.invoke(state.pageSize)
        onChange?.invoke(state.currentPage, state.pageSize)
    }

    @Composable
    fun RenderTokens(tokens: List<String>) {
        tokens.forEach { token ->
            when (token) {
                "prev" -> PaginationItem(
                    text = if (prevText.isNotBlank()) prevText else "‹",
                    isActive = false,
                    enabled = !disabled && state.currentPage > 1,
                    itemSize = itemSize,
                    background = background,
                    textStyle = textStyle,
                    onClick = {
                        state.prev()
                        onPrevClick?.invoke(state.currentPage)
                        emitCurrentChanged()
                    },
                )

                "next" -> PaginationItem(
                    text = if (nextText.isNotBlank()) nextText else "›",
                    isActive = false,
                    enabled = !disabled && state.currentPage < totalPageCount,
                    itemSize = itemSize,
                    background = background,
                    textStyle = textStyle,
                    onClick = {
                        state.next()
                        onNextClick?.invoke(state.currentPage)
                        emitCurrentChanged()
                    },
                )

                "pager" -> {
                    pages.forEach { page ->
                        if (page == -1) {
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
                        } else {
                            PaginationItem(
                                text = page.toString(),
                                isActive = page == state.currentPage,
                                enabled = !disabled,
                                itemSize = itemSize,
                                background = background,
                                textStyle = textStyle,
                                onClick = {
                                    state.goTo(page)
                                    emitCurrentChanged()
                                },
                            )
                        }
                    }
                }

                "total" -> {
                    NexusText(
                        text = "Total ${state.total}",
                        color = colorScheme.text.secondary,
                        style = textStyle,
                        modifier = Modifier.padding(horizontal = 4.dp),
                    )
                }

                "sizes" -> {
                    val options = pageSizes.distinct().sorted().map {
                        SelectOption(value = it, label = "$it / page")
                    }
                    NexusSelect(
                        state = sizeSelectState,
                        options = options,
                        onSelect = {
                            state.updatePageSize(it)
                            emitSizeChanged()
                        },
                        modifier = Modifier.width(120.dp),
                        size = size,
                        disabled = disabled,
                        fitInputWidth = true,
                    )
                }

                "jumper" -> {
                    var pageInput by remember(state.currentPage) { mutableStateOf(state.currentPage.toString()) }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        NexusText(
                            text = "Go to",
                            color = colorScheme.text.secondary,
                            style = textStyle,
                        )
                        NexusInput(
                            value = pageInput,
                            onValueChange = { pageInput = it.filter(Char::isDigit) },
                            modifier = Modifier.width(58.dp),
                            size = size,
                            disabled = disabled,
                        )
                        NexusButton(
                            text = "Go",
                            onClick = {
                                val target = pageInput.toIntOrNull() ?: state.currentPage
                                state.goTo(target)
                                emitCurrentChanged()
                            },
                            size = size,
                            disabled = disabled,
                        )
                    }
                }

                "slot" -> {
                    slot?.invoke()
                }
            }
        }
    }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RenderTokens(leftTokens)
        if (rightTokens.isNotEmpty()) {
            Spacer(modifier = Modifier.weight(1f))
            RenderTokens(rightTokens)
        }
    }
}

@Composable
private fun PaginationItem(
    text: String,
    isActive: Boolean,
    enabled: Boolean,
    itemSize: Dp,
    background: Boolean,
    textStyle: androidx.compose.ui.text.TextStyle,
    onClick: () -> Unit,
) {
    val colorScheme = NexusTheme.colorScheme
    val shapes = NexusTheme.shapes

    val bgColor = when {
        isActive -> colorScheme.primary.base
        background -> colorScheme.fill.light
        else -> colorScheme.fill.blank
    }
    val textColor = when {
        !enabled -> colorScheme.text.disabled
        isActive -> colorScheme.white
        else -> colorScheme.text.regular
    }
    val borderModifier = if (background || isActive) {
        Modifier
    } else {
        Modifier.border(1.dp, colorScheme.border.lighter, shapes.base)
    }

    Box(
        modifier = Modifier
            .defaultMinSize(minWidth = itemSize, minHeight = itemSize)
            .clip(shapes.base)
            .background(bgColor)
            .then(borderModifier)
            .then(
                if (enabled) {
                    Modifier
                        .clickable(onClick = onClick)
                        .pointerHoverIcon(PointerIcon.Hand)
                } else {
                    Modifier
                }
            ),
        contentAlignment = Alignment.Center,
    ) {
        NexusText(
            text = text,
            color = textColor,
            style = textStyle,
        )
    }
}

private fun buildPageList(current: Int, total: Int, pagerCount: Int): List<Int> {
    if (total <= pagerCount) return (1..total).toList()

    val half = pagerCount / 2
    val pages = mutableListOf(1)

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

    if (rangeStart > 2) pages += -1
    for (i in rangeStart..rangeEnd) pages += i
    if (rangeEnd < total - 1) pages += -1
    pages += total
    return pages
}
