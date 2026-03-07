package io.github.xingray.compose.nexus.controls

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.xingray.compose.nexus.theme.NexusTheme
import io.github.xingray.compose.nexus.theme.NexusType
import io.github.xingray.compose.nexus.theme.typeColor

private data class VirtualizedTreeFlatNode<T>(
    val node: TreeNode<T>,
    val depth: Int,
    val hasChildren: Boolean,
    val expanded: Boolean,
)

private data class VirtualizedTreeRelations<T>(
    val nodeByKey: Map<T, TreeNode<T>>,
    val parentByKey: Map<T, T?>,
    val childrenByKey: Map<T, List<T>>,
)

@Composable
fun <T> NexusVirtualizedTree(
    nodes: List<TreeNode<T>>,
    modifier: Modifier = Modifier,
    state: TreeState<T> = rememberTreeState(),
    emptyText: String = "No Data",
    height: Dp = 200.dp,
    itemSize: Dp = 26.dp,
    scrollbarAlwaysOn: Boolean = false,
    highlightCurrent: Boolean = false,
    expandOnClickNode: Boolean = true,
    checkOnClickNode: Boolean = false,
    checkOnClickLeaf: Boolean = true,
    showCheckbox: Boolean = false,
    checkStrictly: Boolean = false,
    defaultExpandedKeys: List<T> = emptyList(),
    defaultCheckedKeys: List<T> = emptyList(),
    currentNodeKey: T? = null,
    filterMethod: ((query: String, node: TreeNode<T>) -> Boolean)? = null,
    indent: Int = 16,
    icon: (@Composable (expanded: Boolean, isLeaf: Boolean) -> Unit)? = null,
    nodeClassName: ((TreeNode<T>) -> NexusType?)? = null,
    nodeContent: (@Composable (TreeNode<T>) -> Unit)? = null,
    onNodeClick: ((TreeNode<T>) -> Unit)? = null,
    onCheckChange: ((TreeNode<T>, Boolean) -> Unit)? = null,
    onCurrentChange: ((TreeNode<T>?) -> Unit)? = null,
    onNodeExpand: ((TreeNode<T>) -> Unit)? = null,
    onNodeCollapse: ((TreeNode<T>) -> Unit)? = null,
    empty: (@Composable () -> Unit)? = null,
) {
    // Keep API parity. Current implementation does not force a scrollbar style.
    @Suppress("UNUSED_VARIABLE")
    val _unusedScrollbarAlwaysOn = scrollbarAlwaysOn

    val relations = remember(nodes) { buildVirtualizedTreeRelations(nodes) }

    remember(nodes, defaultExpandedKeys, defaultCheckedKeys, currentNodeKey, checkStrictly) {
        defaultExpandedKeys.forEach { state.expand(it) }
        if (defaultCheckedKeys.isNotEmpty()) {
            state.clearChecked()
            defaultCheckedKeys.forEach { key ->
                if (relations.nodeByKey.containsKey(key)) {
                    setVirtualizedCheckedWithCascade(
                        key = key,
                        checked = true,
                        state = state,
                        relations = relations,
                        checkStrictly = checkStrictly,
                    )
                }
            }
        }
        if (currentNodeKey != null) {
            state.select(currentNodeKey)
        }
        true
    }

    if (nodes.isEmpty()) {
        if (empty != null) {
            empty()
        } else {
            NexusText(
                text = emptyText,
                color = NexusTheme.colorScheme.text.secondary,
                style = NexusTheme.typography.small,
                modifier = modifier.padding(8.dp),
            )
        }
        return
    }

    val keyword = state.filterKeyword.trim()
    val filtering = keyword.isNotEmpty()
    val visibleMap = remember(nodes, keyword, filterMethod) {
        buildVirtualizedVisibleMap(nodes, keyword, filterMethod)
    }

    val flatNodes = remember(nodes, visibleMap, filtering, state.expandedKeyMap.toMap()) {
        flattenVirtualizedTree(
            nodes = nodes,
            visibleMap = visibleMap,
            filtering = filtering,
            state = state,
        )
    }

    if (flatNodes.isEmpty()) {
        if (empty != null) {
            empty()
        } else {
            NexusText(
                text = emptyText,
                color = NexusTheme.colorScheme.text.secondary,
                style = NexusTheme.typography.small,
                modifier = modifier.padding(8.dp),
            )
        }
        return
    }

    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .height(height),
    ) {
        items(
            items = flatNodes,
            key = { item -> item.node.key as Any },
        ) { item ->
            val node = item.node
            val hasChildren = item.hasChildren
            val isExpanded = item.expanded
            val isCurrent = state.isSelected(node.key)
            val isChecked = state.isChecked(node.key)
            val isHalfChecked = state.isHalfChecked(node.key)
            val classColor = nodeClassName?.invoke(node)?.let { type -> NexusTheme.colorScheme.typeColor(type)?.base }

            val rowBackground = when {
                highlightCurrent && isCurrent -> NexusTheme.colorScheme.primary.light9
                else -> NexusTheme.colorScheme.fill.blank
            }
            val textColor = when {
                node.disabled -> NexusTheme.colorScheme.text.disabled
                isCurrent -> NexusTheme.colorScheme.primary.base
                classColor != null -> classColor
                else -> NexusTheme.colorScheme.text.regular
            }

            fun toggleExpand() {
                val nextExpanded = !state.isExpanded(node.key)
                if (nextExpanded) {
                    state.expand(node.key)
                    onNodeExpand?.invoke(node)
                } else {
                    state.collapse(node.key)
                    onNodeCollapse?.invoke(node)
                }
            }

            fun toggleChecked(next: Boolean) {
                setVirtualizedCheckedWithCascade(
                    key = node.key,
                    checked = next,
                    state = state,
                    relations = relations,
                    checkStrictly = checkStrictly,
                )
                onCheckChange?.invoke(node, state.isChecked(node.key))
            }

            fun onRowClick() {
                if (node.disabled) return
                if (hasChildren && expandOnClickNode) {
                    toggleExpand()
                }
                if (showCheckbox) {
                    val clickableForCheck = checkOnClickNode || (checkOnClickLeaf && !hasChildren)
                    if (clickableForCheck) {
                        toggleChecked(!isChecked)
                    }
                }
                state.select(node.key)
                onCurrentChange?.invoke(node)
                onNodeClick?.invoke(node)
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(itemSize)
                    .background(rowBackground, NexusTheme.shapes.base)
                    .then(
                        if (!node.disabled) {
                            Modifier
                                .clickable { onRowClick() }
                                .pointerHoverIcon(PointerIcon.Hand)
                        } else {
                            Modifier
                        }
                    )
                    .padding(start = (item.depth * indent + 8).dp, end = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Box(
                    modifier = Modifier
                        .then(
                            if (hasChildren && !node.disabled) {
                                Modifier
                                    .clickable { toggleExpand() }
                                    .pointerHoverIcon(PointerIcon.Hand)
                            } else {
                                Modifier
                            }
                        ),
                ) {
                    if (icon != null) {
                        icon(isExpanded, !hasChildren)
                    } else {
                        NexusText(
                            text = when {
                                !hasChildren -> "  "
                                isExpanded -> "▾"
                                else -> "▸"
                            },
                            color = NexusTheme.colorScheme.text.placeholder,
                            style = NexusTheme.typography.extraSmall,
                        )
                    }
                }

                if (showCheckbox) {
                    NexusCheckbox(
                        checked = isChecked,
                        onCheckedChange = { checked -> toggleChecked(checked) },
                        indeterminate = !checkStrictly && isHalfChecked,
                        disabled = node.disabled,
                        size = io.github.xingray.compose.nexus.theme.ComponentSize.Small,
                    )
                }

                if (nodeContent != null) {
                    nodeContent(node)
                } else {
                    NexusText(
                        text = node.label,
                        color = textColor,
                        style = NexusTheme.typography.base,
                    )
                }
            }
        }
    }
}

private fun <T> flattenVirtualizedTree(
    nodes: List<TreeNode<T>>,
    visibleMap: Map<T, Boolean>,
    filtering: Boolean,
    state: TreeState<T>,
): List<VirtualizedTreeFlatNode<T>> {
    val list = mutableListOf<VirtualizedTreeFlatNode<T>>()

    fun walk(node: TreeNode<T>, depth: Int) {
        if (visibleMap[node.key] != true) return

        val hasChildren = node.children.isNotEmpty()
        val expanded = state.isExpanded(node.key)
        list += VirtualizedTreeFlatNode(
            node = node,
            depth = depth,
            hasChildren = hasChildren,
            expanded = expanded,
        )

        if (hasChildren && (expanded || filtering)) {
            node.children.forEach { child ->
                walk(child, depth + 1)
            }
        }
    }

    nodes.forEach { node -> walk(node, 0) }
    return list
}

private fun <T> buildVirtualizedTreeRelations(nodes: List<TreeNode<T>>): VirtualizedTreeRelations<T> {
    val nodeByKey = LinkedHashMap<T, TreeNode<T>>()
    val parentByKey = LinkedHashMap<T, T?>()
    val childrenByKey = LinkedHashMap<T, List<T>>()

    fun walk(node: TreeNode<T>, parent: T?) {
        nodeByKey[node.key] = node
        parentByKey[node.key] = parent
        childrenByKey[node.key] = node.children.map { it.key }
        node.children.forEach { child -> walk(child, node.key) }
    }
    nodes.forEach { walk(it, null) }

    return VirtualizedTreeRelations(
        nodeByKey = nodeByKey,
        parentByKey = parentByKey,
        childrenByKey = childrenByKey,
    )
}

private fun <T> setVirtualizedCheckedWithCascade(
    key: T,
    checked: Boolean,
    state: TreeState<T>,
    relations: VirtualizedTreeRelations<T>,
    checkStrictly: Boolean,
) {
    if (checkStrictly) {
        state.setChecked(key, checked)
        state.setHalfChecked(key, false)
        return
    }

    fun setDescendants(nodeKey: T, value: Boolean) {
        state.setChecked(nodeKey, value)
        state.setHalfChecked(nodeKey, false)
        relations.childrenByKey[nodeKey].orEmpty().forEach { child ->
            setDescendants(child, value)
        }
    }

    setDescendants(key, checked)
    updateVirtualizedAncestors(key, state, relations)
}

private fun <T> updateVirtualizedAncestors(
    key: T,
    state: TreeState<T>,
    relations: VirtualizedTreeRelations<T>,
) {
    var parent = relations.parentByKey[key]
    while (parent != null) {
        val children = relations.childrenByKey[parent].orEmpty()
        val allChecked = children.isNotEmpty() && children.all { child -> state.isChecked(child) }
        val anyChecked = children.any { child -> state.isChecked(child) || state.isHalfChecked(child) }

        when {
            allChecked -> {
                state.setChecked(parent, true)
                state.setHalfChecked(parent, false)
            }
            anyChecked -> {
                state.setChecked(parent, false)
                state.setHalfChecked(parent, true)
            }
            else -> {
                state.setChecked(parent, false)
                state.setHalfChecked(parent, false)
            }
        }
        parent = relations.parentByKey[parent]
    }
}

private fun <T> buildVirtualizedVisibleMap(
    nodes: List<TreeNode<T>>,
    keyword: String,
    filterMethod: ((String, TreeNode<T>) -> Boolean)?,
): Map<T, Boolean> {
    val visible = mutableMapOf<T, Boolean>()

    if (keyword.isBlank()) {
        fun markAll(list: List<TreeNode<T>>) {
            list.forEach { node ->
                visible[node.key] = true
                markAll(node.children)
            }
        }
        markAll(nodes)
        return visible
    }

    fun matches(node: TreeNode<T>): Boolean {
        return filterMethod?.invoke(keyword, node) ?: node.label.contains(keyword, ignoreCase = true)
    }

    fun walk(node: TreeNode<T>): Boolean {
        val selfMatch = matches(node)
        val childMatch = node.children.any { walk(it) }
        val isVisible = selfMatch || childMatch
        visible[node.key] = isVisible
        return isVisible
    }

    nodes.forEach { walk(it) }
    return visible
}

