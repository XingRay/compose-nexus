package io.github.xingray.compose_nexus.controls

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import io.github.xingray.compose_nexus.theme.NexusTheme
import io.github.xingray.compose_nexus.theme.NexusType

/**
 * Transfer data item.
 */
data class TransferItem<T>(
    val key: T,
    val label: String,
    val disabled: Boolean = false,
)

/**
 * Transfer state holder.
 */
@Stable
class TransferState<T>(
    sourceItems: List<TransferItem<T>>,
    targetItems: List<TransferItem<T>> = emptyList(),
) {
    val sourceList = mutableStateListOf<TransferItem<T>>().apply { addAll(sourceItems) }
    val targetList = mutableStateListOf<TransferItem<T>>().apply { addAll(targetItems) }
    val sourceChecked = mutableStateListOf<T>()
    val targetChecked = mutableStateListOf<T>()

    fun moveToTarget() {
        val toMove = sourceList.filter { it.key in sourceChecked && !it.disabled }
        sourceList.removeAll(toMove)
        targetList.addAll(toMove)
        sourceChecked.clear()
    }

    fun moveToSource() {
        val toMove = targetList.filter { it.key in targetChecked && !it.disabled }
        targetList.removeAll(toMove)
        sourceList.addAll(toMove)
        targetChecked.clear()
    }

    fun toggleSourceCheck(key: T) {
        if (key in sourceChecked) sourceChecked.remove(key)
        else sourceChecked.add(key)
    }

    fun toggleTargetCheck(key: T) {
        if (key in targetChecked) targetChecked.remove(key)
        else targetChecked.add(key)
    }
}

@Composable
fun <T> rememberTransferState(
    sourceItems: List<TransferItem<T>>,
    targetItems: List<TransferItem<T>> = emptyList(),
): TransferState<T> = remember { TransferState(sourceItems, targetItems) }

/**
 * Element Plus Transfer — a dual-list shuttle component for moving items between two panels.
 *
 * @param T Item key type.
 * @param state Transfer state.
 * @param modifier Modifier.
 * @param sourceTitle Title for the source (left) panel.
 * @param targetTitle Title for the target (right) panel.
 * @param filterable Whether to show a filter input in each panel.
 */
@Composable
fun <T> NexusTransfer(
    state: TransferState<T>,
    modifier: Modifier = Modifier,
    sourceTitle: String = "Source",
    targetTitle: String = "Target",
    filterable: Boolean = false,
) {
    val colorScheme = NexusTheme.colorScheme
    val typography = NexusTheme.typography
    val shapes = NexusTheme.shapes

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(0.dp),
    ) {
        // Source panel
        TransferPanel(
            title = sourceTitle,
            items = state.sourceList,
            checkedKeys = state.sourceChecked,
            onToggle = { state.toggleSourceCheck(it) },
            filterable = filterable,
            modifier = Modifier.weight(1f),
        )

        // Action buttons
        Column(
            modifier = Modifier.padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            NexusButton(
                onClick = { state.moveToTarget() },
                type = NexusType.Primary,
                disabled = state.sourceChecked.isEmpty(),
            ) {
                NexusText(text = "→")
            }
            NexusButton(
                onClick = { state.moveToSource() },
                type = NexusType.Primary,
                disabled = state.targetChecked.isEmpty(),
            ) {
                NexusText(text = "←")
            }
        }

        // Target panel
        TransferPanel(
            title = targetTitle,
            items = state.targetList,
            checkedKeys = state.targetChecked,
            onToggle = { state.toggleTargetCheck(it) },
            filterable = filterable,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun <T> TransferPanel(
    title: String,
    items: List<TransferItem<T>>,
    checkedKeys: List<T>,
    onToggle: (T) -> Unit,
    filterable: Boolean,
    modifier: Modifier = Modifier,
) {
    val colorScheme = NexusTheme.colorScheme
    val typography = NexusTheme.typography
    val shapes = NexusTheme.shapes

    var filterText by remember { mutableStateOf("") }
    val filteredItems = if (filterText.isNotEmpty()) {
        items.filter { it.label.contains(filterText, ignoreCase = true) }
    } else {
        items
    }

    Column(
        modifier = modifier
            .clip(shapes.base)
            .border(1.dp, colorScheme.border.lighter, shapes.base)
            .background(colorScheme.fill.blank),
    ) {
        // Header
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
                    text = "${checkedKeys.size}/${items.size}",
                    color = colorScheme.text.secondary,
                    style = typography.extraSmall,
                )
            }
        }

        // Filter input
        if (filterable) {
            NexusInput(
                value = filterText,
                onValueChange = { filterText = it },
                placeholder = "Filter",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                clearable = true,
                size = io.github.xingray.compose_nexus.theme.ComponentSize.Small,
            )
        }

        // Items
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
                    NexusText(
                        text = "No Data",
                        color = colorScheme.text.placeholder,
                        style = typography.small,
                    )
                }
            } else {
                filteredItems.forEach { item ->
                    val isChecked = item.key in checkedKeys
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .then(
                                if (!item.disabled) {
                                    Modifier
                                        .clickable { onToggle(item.key) }
                                        .pointerHoverIcon(PointerIcon.Hand)
                                } else Modifier
                            )
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        NexusCheckbox(
                            checked = isChecked,
                            onCheckedChange = { if (!item.disabled) onToggle(item.key) },
                            disabled = item.disabled,
                        )
                        NexusText(
                            text = item.label,
                            color = if (item.disabled) colorScheme.text.disabled
                            else colorScheme.text.regular,
                            style = typography.base,
                        )
                    }
                }
            }
        }
    }
}
