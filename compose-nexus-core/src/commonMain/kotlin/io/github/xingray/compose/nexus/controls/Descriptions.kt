package io.github.xingray.compose.nexus.controls

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.xingray.compose.nexus.foundation.ProvideContentColorTextStyle
import io.github.xingray.compose.nexus.theme.ComponentSize
import io.github.xingray.compose.nexus.theme.NexusTheme

enum class DescriptionsDirection {
    Horizontal,
    Vertical,
}

enum class DescriptionsAlign {
    Left,
    Center,
    Right,
}

data class NexusDescriptionsItem(
    val label: String = "",
    val span: Int = 1,
    val rowspan: Int = 1,
    val width: Dp? = null,
    val minWidth: Dp? = null,
    val labelWidth: Dp? = null,
    val align: io.github.xingray.compose.nexus.controls.DescriptionsAlign = _root_ide_package_.io.github.xingray.compose.nexus.controls.DescriptionsAlign.Left,
    val labelAlign: io.github.xingray.compose.nexus.controls.DescriptionsAlign? = null,
    val className: String? = null,
    val labelClassName: String? = null,
    val labelSlot: (@Composable () -> Unit)? = null,
    val content: @Composable () -> Unit,
)

@Stable
class DescriptionsScope internal constructor() {
    internal val items = mutableListOf<io.github.xingray.compose.nexus.controls.NexusDescriptionsItem>()

    fun item(
        label: String = "",
        span: Int = 1,
        rowspan: Int = 1,
        width: Dp? = null,
        minWidth: Dp? = null,
        labelWidth: Dp? = null,
        align: io.github.xingray.compose.nexus.controls.DescriptionsAlign = _root_ide_package_.io.github.xingray.compose.nexus.controls.DescriptionsAlign.Left,
        labelAlign: io.github.xingray.compose.nexus.controls.DescriptionsAlign? = null,
        className: String? = null,
        labelClassName: String? = null,
        labelSlot: (@Composable () -> Unit)? = null,
        content: @Composable () -> Unit,
    ) {
        items += _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusDescriptionsItem(
            label = label,
            span = span,
            rowspan = rowspan,
            width = width,
            minWidth = minWidth,
            labelWidth = labelWidth,
            align = align,
            labelAlign = labelAlign,
            className = className,
            labelClassName = labelClassName,
            labelSlot = labelSlot,
            content = content,
        )
    }
}

@Composable
fun NexusDescriptions(
    modifier: Modifier = Modifier,
    border: Boolean = false,
    column: Int = 3,
    direction: io.github.xingray.compose.nexus.controls.DescriptionsDirection = _root_ide_package_.io.github.xingray.compose.nexus.controls.DescriptionsDirection.Horizontal,
    size: io.github.xingray.compose.nexus.theme.ComponentSize = _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Default,
    title: String = "",
    extra: String = "",
    labelWidth: Dp? = null,
    titleSlot: (@Composable () -> Unit)? = null,
    extraSlot: (@Composable () -> Unit)? = null,
    content: io.github.xingray.compose.nexus.controls.DescriptionsScope.() -> Unit,
) {
    val colorScheme = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.colorScheme
    val typography = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.typography
    val headerStyle = when (size) {
        _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Large -> typography.large
        _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Default -> typography.base
        _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Small -> typography.small
    }
    val labelStyle = when (size) {
        _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Large -> typography.base
        _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Default -> typography.small
        _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Small -> typography.extraSmall
    }
    val valueStyle = when (size) {
        _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Large -> typography.base
        _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Default -> typography.base
        _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Small -> typography.small
    }
    val rowHeight = when (size) {
        _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Large -> 48.dp
        _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Default -> 42.dp
        _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Small -> 36.dp
    }

    val scope = _root_ide_package_.io.github.xingray.compose.nexus.controls.DescriptionsScope().also(content)
    val rows = _root_ide_package_.io.github.xingray.compose.nexus.controls.buildDescriptionRows(scope.items, column.coerceAtLeast(1))

    Column(modifier = modifier.fillMaxWidth()) {
        if (titleSlot != null || extraSlot != null || title.isNotBlank() || extra.isNotBlank()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (titleSlot != null) {
                    titleSlot()
                } else {
                    _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
                        text = title,
                        style = headerStyle,
                        color = colorScheme.text.primary,
                    )
                }
                if (extraSlot != null) {
                    extraSlot()
                } else {
                    _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
                        text = extra,
                        style = labelStyle,
                        color = colorScheme.text.secondary,
                    )
                }
            }
        }

        if (border) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, colorScheme.border.lighter, _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.shapes.base),
            ) {
                _root_ide_package_.io.github.xingray.compose.nexus.controls.renderRows(
                    rows = rows,
                    direction = direction,
                    border = true,
                    rowHeight = rowHeight,
                    defaultLabelWidth = labelWidth,
                    labelStyle = labelStyle,
                    valueStyle = valueStyle,
                )
            }
        } else {
            _root_ide_package_.io.github.xingray.compose.nexus.controls.renderRows(
                rows = rows,
                direction = direction,
                border = false,
                rowHeight = rowHeight,
                defaultLabelWidth = labelWidth,
                labelStyle = labelStyle,
                valueStyle = valueStyle,
            )
        }
    }
}

@Composable
private fun renderRows(
    rows: List<List<io.github.xingray.compose.nexus.controls.NexusDescriptionsItem>>,
    direction: io.github.xingray.compose.nexus.controls.DescriptionsDirection,
    border: Boolean,
    rowHeight: Dp,
    defaultLabelWidth: Dp?,
    labelStyle: androidx.compose.ui.text.TextStyle,
    valueStyle: androidx.compose.ui.text.TextStyle,
) {
    val colorScheme = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.colorScheme

    rows.forEach { rowItems ->
        when (direction) {
            _root_ide_package_.io.github.xingray.compose.nexus.controls.DescriptionsDirection.Horizontal -> {
                Row(modifier = Modifier.fillMaxWidth()) {
                    rowItems.forEach { item ->
                        val span = item.span.coerceAtLeast(1).toFloat()
                        val minHeight = rowHeight * item.rowspan.coerceAtLeast(1)
                        val itemModifier = if (item.width != null) {
                            Modifier.width(item.width)
                        } else {
                            Modifier.weight(span)
                        }

                        if (border) {
                            Row(
                                modifier = itemModifier
                                    .defaultMinSize(minWidth = item.minWidth ?: 0.dp)
                                    .heightIn(min = minHeight),
                            ) {
                                val currentLabelWidth = item.labelWidth ?: defaultLabelWidth
                                Box(
                                    modifier = Modifier
                                        .then(if (currentLabelWidth != null) Modifier.width(currentLabelWidth) else Modifier.weight(1f))
                                        .fillMaxHeight()
                                        .background(colorScheme.fill.light)
                                        .border(1.dp, colorScheme.border.lighter)
                                        .padding(horizontal = 10.dp, vertical = 8.dp),
                                    contentAlignment = _root_ide_package_.io.github.xingray.compose.nexus.controls.alignToContent(item.labelAlign ?: item.align),
                                ) {
                                    _root_ide_package_.io.github.xingray.compose.nexus.controls.DescriptionLabel(item, labelStyle)
                                }
                                Box(
                                    modifier = Modifier
                                        .weight(2f)
                                        .fillMaxHeight()
                                        .border(1.dp, colorScheme.border.lighter)
                                        .padding(horizontal = 10.dp, vertical = 8.dp),
                                    contentAlignment = _root_ide_package_.io.github.xingray.compose.nexus.controls.alignToContent(item.align),
                                ) {
                                    _root_ide_package_.io.github.xingray.compose.nexus.foundation.ProvideContentColorTextStyle(
                                        contentColor = colorScheme.text.regular,
                                        textStyle = valueStyle,
                                    ) {
                                        item.content()
                                    }
                                }
                            }
                        } else {
                            Column(
                                modifier = itemModifier
                                    .defaultMinSize(minWidth = item.minWidth ?: 0.dp)
                                    .heightIn(min = minHeight)
                                    .padding(horizontal = 8.dp, vertical = 8.dp),
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = _root_ide_package_.io.github.xingray.compose.nexus.controls.alignToContent(item.labelAlign ?: item.align),
                                ) {
                                    _root_ide_package_.io.github.xingray.compose.nexus.controls.DescriptionLabel(item, labelStyle)
                                }
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 4.dp),
                                    contentAlignment = _root_ide_package_.io.github.xingray.compose.nexus.controls.alignToContent(item.align),
                                ) {
                                    _root_ide_package_.io.github.xingray.compose.nexus.foundation.ProvideContentColorTextStyle(
                                        contentColor = colorScheme.text.regular,
                                        textStyle = valueStyle,
                                    ) {
                                        item.content()
                                    }
                                }
                            }
                        }
                    }
                }
            }

            _root_ide_package_.io.github.xingray.compose.nexus.controls.DescriptionsDirection.Vertical -> {
                Row(modifier = Modifier.fillMaxWidth()) {
                    rowItems.forEach { item ->
                        val span = item.span.coerceAtLeast(1).toFloat()
                        val itemModifier = if (item.width != null) {
                            Modifier.width(item.width)
                        } else {
                            Modifier.weight(span)
                        }
                        val minHeight = rowHeight * item.rowspan.coerceAtLeast(1)
                        Column(
                            modifier = itemModifier
                                .defaultMinSize(minWidth = item.minWidth ?: 0.dp)
                                .heightIn(min = minHeight),
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .then(
                                        if (border) {
                                            Modifier
                                                .background(colorScheme.fill.light)
                                                .border(1.dp, colorScheme.border.lighter)
                                        } else {
                                            Modifier
                                        },
                                    )
                                    .padding(horizontal = 10.dp, vertical = 8.dp),
                                contentAlignment = _root_ide_package_.io.github.xingray.compose.nexus.controls.alignToContent(item.labelAlign ?: item.align),
                            ) {
                                _root_ide_package_.io.github.xingray.compose.nexus.controls.DescriptionLabel(item, labelStyle)
                            }
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .then(
                                        if (border) {
                                            Modifier.border(1.dp, colorScheme.border.lighter)
                                        } else {
                                            Modifier
                                        },
                                    )
                                    .padding(horizontal = 10.dp, vertical = 8.dp),
                                contentAlignment = _root_ide_package_.io.github.xingray.compose.nexus.controls.alignToContent(item.align),
                            ) {
                                _root_ide_package_.io.github.xingray.compose.nexus.foundation.ProvideContentColorTextStyle(
                                    contentColor = colorScheme.text.regular,
                                    textStyle = valueStyle,
                                ) {
                                    item.content()
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
private fun DescriptionLabel(
    item: io.github.xingray.compose.nexus.controls.NexusDescriptionsItem,
    labelStyle: androidx.compose.ui.text.TextStyle,
) {
    val colorScheme = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.colorScheme
    if (item.labelSlot != null) {
        item.labelSlot.invoke()
    } else {
        _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
            text = item.label,
            style = labelStyle,
            color = colorScheme.text.secondary,
        )
    }
}

private fun alignToContent(align: io.github.xingray.compose.nexus.controls.DescriptionsAlign): Alignment {
    return when (align) {
        _root_ide_package_.io.github.xingray.compose.nexus.controls.DescriptionsAlign.Left -> Alignment.CenterStart
        _root_ide_package_.io.github.xingray.compose.nexus.controls.DescriptionsAlign.Center -> Alignment.Center
        _root_ide_package_.io.github.xingray.compose.nexus.controls.DescriptionsAlign.Right -> Alignment.CenterEnd
    }
}

private fun buildDescriptionRows(
    items: List<io.github.xingray.compose.nexus.controls.NexusDescriptionsItem>,
    column: Int,
): List<List<io.github.xingray.compose.nexus.controls.NexusDescriptionsItem>> {
    if (items.isEmpty()) return emptyList()

    val rows = mutableListOf<List<io.github.xingray.compose.nexus.controls.NexusDescriptionsItem>>()
    var current = mutableListOf<io.github.xingray.compose.nexus.controls.NexusDescriptionsItem>()
    var used = 0

    items.forEach { item ->
        val span = item.span.coerceAtLeast(1).coerceAtMost(column)
        if (used + span > column && current.isNotEmpty()) {
            rows += current
            current = mutableListOf()
            used = 0
        }
        current += item.copy(span = span)
        used += span
        if (used == column) {
            rows += current
            current = mutableListOf()
            used = 0
        }
    }

    if (current.isNotEmpty()) {
        rows += current
    }

    return rows
}
