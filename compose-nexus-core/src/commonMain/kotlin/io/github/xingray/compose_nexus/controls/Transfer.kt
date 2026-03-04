package io.github.xingray.compose_nexus.controls

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
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
import io.github.xingray.compose_nexus.theme.NexusType

data class TransferItem<T>(
    val key: T,
    val label: String,
    val disabled: Boolean = false,
    val payload: Map<String, Any?> = emptyMap(),
)

data class TransferPropsAlias(
    val key: String = "key",
    val label: String = "label",
    val disabled: String = "disabled",
)

data class TransferFormat(
    val noChecked: String = "{checked}/{total}",
    val hasChecked: String = "{checked}/{total}",
)

enum class TransferDirection { Left, Right }

enum class TransferTargetOrder { Original, Push, Unshift }

@Stable
class TransferState<T>(
    sourceItems: List<TransferItem<T>>,
    targetItems: List<TransferItem<T>> = emptyList(),
    leftDefaultChecked: List<T> = emptyList(),
    rightDefaultChecked: List<T> = emptyList(),
) {
    val sourceList = mutableStateListOf<TransferItem<T>>().apply { addAll(sourceItems) }
    val targetList = mutableStateListOf<TransferItem<T>>().apply { addAll(targetItems) }
    val sourceChecked = mutableStateListOf<T>().apply { addAll(leftDefaultChecked) }
    val targetChecked = mutableStateListOf<T>().apply { addAll(rightDefaultChecked) }

    private val originalOrder: Map<T, Int> = (sourceItems + targetItems).mapIndexed { index, item ->
        item.key to index
    }.toMap()

    fun moveToTarget(order: TransferTargetOrder): List<T> {
        val toMove = sourceList.filter { it.key in sourceChecked && !it.disabled }
        if (toMove.isEmpty()) {
            sourceChecked.clear()
            return emptyList()
        }
        sourceList.removeAll(toMove.toSet())
        when (order) {
            TransferTargetOrder.Push -> targetList.addAll(toMove)
            TransferTargetOrder.Unshift -> targetList.addAll(0, toMove)
            TransferTargetOrder.Original -> {
                targetList.addAll(toMove)
                val sorted = targetList.sortedBy { originalOrder[it.key] ?: Int.MAX_VALUE }
                targetList.clear()
                targetList.addAll(sorted)
            }
        }
        val moved = toMove.map { it.key }
        sourceChecked.clear()
        return moved
    }

    fun moveToSource(order: TransferTargetOrder): List<T> {
        val toMove = targetList.filter { it.key in targetChecked && !it.disabled }
        if (toMove.isEmpty()) {
            targetChecked.clear()
            return emptyList()
        }
        targetList.removeAll(toMove.toSet())
        when (order) {
            TransferTargetOrder.Push -> sourceList.addAll(toMove)
            TransferTargetOrder.Unshift -> sourceList.addAll(0, toMove)
            TransferTargetOrder.Original -> {
                sourceList.addAll(toMove)
                val sorted = sourceList.sortedBy { originalOrder[it.key] ?: Int.MAX_VALUE }
                sourceList.clear()
                sourceList.addAll(sorted)
            }
        }
        val moved = toMove.map { it.key }
        targetChecked.clear()
        return moved
    }

    fun toggleSourceCheck(key: T, disabled: Boolean = false): List<T> {
        if (disabled) return sourceChecked.toList()
        if (key in sourceChecked) sourceChecked.remove(key) else sourceChecked.add(key)
        return sourceChecked.toList()
    }

    fun toggleTargetCheck(key: T, disabled: Boolean = false): List<T> {
        if (disabled) return targetChecked.toList()
        if (key in targetChecked) targetChecked.remove(key) else targetChecked.add(key)
        return targetChecked.toList()
    }

    fun targetKeys(): List<T> = targetList.map { it.key }
}

@Composable
fun <T> rememberTransferState(
    sourceItems: List<TransferItem<T>>,
    targetItems: List<TransferItem<T>> = emptyList(),
    leftDefaultChecked: List<T> = emptyList(),
    rightDefaultChecked: List<T> = emptyList(),
): TransferState<T> = remember {
    TransferState(
        sourceItems = sourceItems,
        targetItems = targetItems,
        leftDefaultChecked = leftDefaultChecked,
        rightDefaultChecked = rightDefaultChecked,
    )
}

@Composable
fun <T> NexusTransfer(
    state: TransferState<T>,
    modifier: Modifier = Modifier,
    sourceTitle: String = "Source",
    targetTitle: String = "Target",
    filterable: Boolean = false,
    filterPlaceholder: String = "Filter keyword",
    filterMethod: ((String, TransferItem<T>) -> Boolean)? = null,
    targetOrder: TransferTargetOrder = TransferTargetOrder.Original,
    titles: Pair<String, String>? = null,
    buttonTexts: Pair<String, String>? = null,
    format: TransferFormat = TransferFormat(),
    props: TransferPropsAlias = TransferPropsAlias(),
    renderContent: (@Composable (TransferItem<T>) -> Unit)? = null,
    leftFooter: (@Composable () -> Unit)? = null,
    rightFooter: (@Composable () -> Unit)? = null,
    leftEmpty: (@Composable () -> Unit)? = null,
    rightEmpty: (@Composable () -> Unit)? = null,
    onChange: ((value: List<T>, direction: TransferDirection, movedKeys: List<T>) -> Unit)? = null,
    onLeftCheckChange: ((value: List<T>, movedKeys: List<T>) -> Unit)? = null,
    onRightCheckChange: ((value: List<T>, movedKeys: List<T>) -> Unit)? = null,
) {
    val finalTitles = titles ?: (sourceTitle to targetTitle)
    val finalButtonTexts = buttonTexts ?: ("→" to "←")

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(0.dp),
    ) {
        TransferPanel(
            title = finalTitles.first,
            items = state.sourceList,
            checkedKeys = state.sourceChecked,
            filterable = filterable,
            filterPlaceholder = filterPlaceholder,
            filterMethod = filterMethod,
            format = format,
            props = props,
            renderContent = renderContent,
            footer = leftFooter,
            empty = leftEmpty,
            onToggle = { item ->
                val checked = state.toggleSourceCheck(item.key, resolveDisabled(item, props))
                onLeftCheckChange?.invoke(checked, listOf(item.key))
            },
            modifier = Modifier.weight(1f),
        )

        Column(
            modifier = Modifier.padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            NexusButton(
                onClick = {
                    val moved = state.moveToTarget(targetOrder)
                    if (moved.isNotEmpty()) {
                        onChange?.invoke(state.targetKeys(), TransferDirection.Right, moved)
                    }
                },
                type = NexusType.Primary,
                disabled = state.sourceChecked.isEmpty(),
                size = ComponentSize.Small,
            ) {
                NexusText(text = finalButtonTexts.first)
            }
            NexusButton(
                onClick = {
                    val moved = state.moveToSource(targetOrder)
                    if (moved.isNotEmpty()) {
                        onChange?.invoke(state.targetKeys(), TransferDirection.Left, moved)
                    }
                },
                type = NexusType.Primary,
                disabled = state.targetChecked.isEmpty(),
                size = ComponentSize.Small,
            ) {
                NexusText(text = finalButtonTexts.second)
            }
        }

        TransferPanel(
            title = finalTitles.second,
            items = state.targetList,
            checkedKeys = state.targetChecked,
            filterable = filterable,
            filterPlaceholder = filterPlaceholder,
            filterMethod = filterMethod,
            format = format,
            props = props,
            renderContent = renderContent,
            footer = rightFooter,
            empty = rightEmpty,
            onToggle = { item ->
                val checked = state.toggleTargetCheck(item.key, resolveDisabled(item, props))
                onRightCheckChange?.invoke(checked, listOf(item.key))
            },
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun <T> TransferPanel(
    title: String,
    items: List<TransferItem<T>>,
    checkedKeys: List<T>,
    filterable: Boolean,
    filterPlaceholder: String,
    filterMethod: ((String, TransferItem<T>) -> Boolean)?,
    format: TransferFormat,
    props: TransferPropsAlias,
    renderContent: (@Composable (TransferItem<T>) -> Unit)?,
    footer: (@Composable () -> Unit)?,
    empty: (@Composable () -> Unit)?,
    onToggle: (TransferItem<T>) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colorScheme = NexusTheme.colorScheme
    val typography = NexusTheme.typography
    val shapes = NexusTheme.shapes

    var filterText by remember { mutableStateOf("") }
    val filteredItems = if (filterText.isNotEmpty()) {
        items.filter { item ->
            filterMethod?.invoke(filterText, item)
                ?: resolveLabel(item, props).contains(filterText, ignoreCase = true)
        }
    } else {
        items
    }

    val statusTemplate = if (checkedKeys.isEmpty()) format.noChecked else format.hasChecked
    val statusText = statusTemplate
        .replace("{checked}", checkedKeys.size.toString())
        .replace("{total}", items.size.toString())

    Column(
        modifier = modifier
            .clip(shapes.base)
            .border(1.dp, colorScheme.border.lighter, shapes.base)
            .background(colorScheme.fill.blank),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(colorScheme.fill.light)
                .padding(horizontal = 12.dp, vertical = 10.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                NexusText(
                    text = title,
                    color = colorScheme.text.primary,
                    style = typography.base,
                    modifier = Modifier.weight(1f),
                )
                NexusText(
                    text = statusText,
                    color = colorScheme.text.secondary,
                    style = typography.extraSmall,
                )
            }
        }

        if (filterable) {
            NexusInput(
                value = filterText,
                onValueChange = { filterText = it },
                placeholder = filterPlaceholder,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                clearable = true,
                size = ComponentSize.Small,
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 100.dp, max = 260.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            if (filteredItems.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    if (empty != null) {
                        empty()
                    } else {
                        NexusText(
                            text = "No Data",
                            color = colorScheme.text.placeholder,
                            style = typography.small,
                        )
                    }
                }
            } else {
                filteredItems.forEach { item ->
                    val disabled = resolveDisabled(item, props)
                    val isChecked = item.key in checkedKeys
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .then(
                                if (!disabled) {
                                    Modifier
                                        .clickable { onToggle(item) }
                                        .pointerHoverIcon(PointerIcon.Hand)
                                } else {
                                    Modifier
                                }
                            )
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        NexusCheckbox(
                            checked = isChecked,
                            onCheckedChange = { if (!disabled) onToggle(item) },
                            disabled = disabled,
                        )
                        Box(modifier = Modifier.weight(1f)) {
                            if (renderContent != null) {
                                renderContent(item)
                            } else {
                                NexusText(
                                    text = resolveLabel(item, props),
                                    color = if (disabled) colorScheme.text.disabled else colorScheme.text.regular,
                                    style = typography.base,
                                )
                            }
                        }
                    }
                }
            }
        }

        if (footer != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 8.dp),
            ) {
                footer()
            }
        }
    }
}

private fun <T> resolveLabel(item: TransferItem<T>, props: TransferPropsAlias): String {
    return item.payload[props.label]?.toString() ?: item.label
}

private fun <T> resolveDisabled(item: TransferItem<T>, props: TransferPropsAlias): Boolean {
    return when (val value = item.payload[props.disabled]) {
        is Boolean -> value
        is String -> value.equals("true", ignoreCase = true)
        else -> item.disabled
    }
}
