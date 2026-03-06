package io.github.xingray.compose.nexus.controls

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import io.github.xingray.compose.nexus.theme.NexusTheme
import io.github.xingray.compose.nexus.theme.NexusType
import io.github.xingray.compose.nexus.theme.typeColor

/**
 * Tree node model.
 */
data class TreeNode<T>(
    val key: T,
    val label: String,
    val children: List<TreeNode<T>> = emptyList(),
    val disabled: Boolean = false,
)

/**
 * Tree state holder.
 */
@Stable
class TreeState<T> {
    internal val expandedKeyMap = mutableStateMapOf<T, Boolean>()
    internal val checkedKeyMap = mutableStateMapOf<T, Boolean>()
    internal val halfCheckedKeyMap = mutableStateMapOf<T, Boolean>()

    var currentKey by mutableStateOf<T?>(null)
        private set
    var filterKeyword by mutableStateOf("")
        private set

    fun isExpanded(key: T): Boolean = expandedKeyMap[key] == true

    fun toggleExpand(key: T) {
        expandedKeyMap[key] = !(expandedKeyMap[key] ?: false)
    }

    fun expand(key: T) {
        expandedKeyMap[key] = true
    }

    fun collapse(key: T) {
        expandedKeyMap[key] = false
    }

    fun collapseAll() {
        expandedKeyMap.clear()
    }

    fun selectedKey(): T? = currentKey

    fun select(key: T?) {
        currentKey = key
    }

    fun isSelected(key: T): Boolean = currentKey == key

    fun filter(keyword: String) {
        filterKeyword = keyword
    }

    fun isChecked(key: T): Boolean = checkedKeyMap[key] == true

    fun isHalfChecked(key: T): Boolean = halfCheckedKeyMap[key] == true

    fun checkedKeys(): List<T> = checkedKeyMap.filterValues { it }.keys.toList()

    fun halfCheckedKeys(): List<T> = halfCheckedKeyMap.filterValues { it }.keys.toList()

    fun setChecked(key: T, checked: Boolean) {
        if (checked) checkedKeyMap[key] = true else checkedKeyMap.remove(key)
    }

    fun setHalfChecked(key: T, halfChecked: Boolean) {
        if (halfChecked) halfCheckedKeyMap[key] = true else halfCheckedKeyMap.remove(key)
    }

    fun clearChecked() {
        checkedKeyMap.clear()
        halfCheckedKeyMap.clear()
    }

    fun setCheckedKeys(keys: Collection<T>) {
        checkedKeyMap.clear()
        keys.forEach { checkedKeyMap[it] = true }
        halfCheckedKeyMap.clear()
    }
}

@Composable
fun <T> rememberTreeState(): io.github.xingray.compose.nexus.controls.TreeState<T> = remember { _root_ide_package_.io.github.xingray.compose.nexus.controls.TreeState() }

private data class TreeRelations<T>(
    val nodeByKey: Map<T, io.github.xingray.compose.nexus.controls.TreeNode<T>>,
    val parentByKey: Map<T, T?>,
    val childrenByKey: Map<T, List<T>>,
    val rootKeys: List<T>,
)

/**
 * Element Plus Tree (core subset aligned to docs).
 */
@Composable
fun <T> NexusTree(
    nodes: List<io.github.xingray.compose.nexus.controls.TreeNode<T>>,
    modifier: Modifier = Modifier,
    state: io.github.xingray.compose.nexus.controls.TreeState<T> = _root_ide_package_.io.github.xingray.compose.nexus.controls.rememberTreeState(),
    emptyText: String = "No Data",
    defaultExpandAll: Boolean = false,
    defaultExpandedKeys: List<T> = emptyList(),
    highlightCurrent: Boolean = false,
    expandOnClickNode: Boolean = true,
    checkOnClickNode: Boolean = false,
    checkOnClickLeaf: Boolean = true,
    autoExpandParent: Boolean = true,
    showCheckbox: Boolean = false,
    checkStrictly: Boolean = false,
    defaultCheckedKeys: List<T> = emptyList(),
    currentNodeKey: T? = null,
    filterNodeMethod: ((String, io.github.xingray.compose.nexus.controls.TreeNode<T>) -> Boolean)? = null,
    accordion: Boolean = false,
    indent: Int = 18,
    icon: (@Composable (expanded: Boolean, hasChildren: Boolean) -> Unit)? = null,
    onNodeClick: ((io.github.xingray.compose.nexus.controls.TreeNode<T>) -> Unit)? = null,
    onCheckChange: ((node: io.github.xingray.compose.nexus.controls.TreeNode<T>, checked: Boolean, indeterminate: Boolean) -> Unit)? = null,
    onCurrentChange: ((currentNode: io.github.xingray.compose.nexus.controls.TreeNode<T>?) -> Unit)? = null,
    onNodeExpand: ((io.github.xingray.compose.nexus.controls.TreeNode<T>) -> Unit)? = null,
    onNodeCollapse: ((io.github.xingray.compose.nexus.controls.TreeNode<T>) -> Unit)? = null,
    nodeClassName: ((io.github.xingray.compose.nexus.controls.TreeNode<T>) -> io.github.xingray.compose.nexus.theme.NexusType?)? = null,
    nodeContent: (@Composable (io.github.xingray.compose.nexus.controls.TreeNode<T>) -> Unit)? = null,
    empty: (@Composable () -> Unit)? = null,
) {
    val relations = remember(nodes) { _root_ide_package_.io.github.xingray.compose.nexus.controls.buildTreeRelations(nodes) }

    // Apply default state when input data/defaults change.
    remember(nodes, defaultExpandAll, defaultExpandedKeys, defaultCheckedKeys, currentNodeKey, checkStrictly) {
        if (defaultExpandAll) {
            relations.nodeByKey.keys.forEach { state.expand(it) }
        }
        defaultExpandedKeys.forEach { state.expand(it) }

        if (defaultCheckedKeys.isNotEmpty()) {
            state.clearChecked()
            defaultCheckedKeys.forEach { key ->
                if (relations.nodeByKey.containsKey(key)) {
                    _root_ide_package_.io.github.xingray.compose.nexus.controls.setCheckedWithCascade(
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
            _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
                text = emptyText,
                color = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.colorScheme.text.secondary,
                style = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.typography.small,
                modifier = modifier.padding(8.dp),
            )
        }
        return
    }

    val keyword = state.filterKeyword.trim()
    val filtering = keyword.isNotEmpty()
    val visibleMap = remember(nodes, keyword, filterNodeMethod) {
        _root_ide_package_.io.github.xingray.compose.nexus.controls.buildVisibleMap(nodes, keyword, filterNodeMethod)
    }

    Column(modifier = modifier.fillMaxWidth()) {
        nodes.forEach { node ->
            _root_ide_package_.io.github.xingray.compose.nexus.controls.TreeNodeItem(
                node = node,
                state = state,
                relations = relations,
                visibleMap = visibleMap,
                depth = 0,
                filtering = filtering,
                showCheckbox = showCheckbox,
                checkStrictly = checkStrictly,
                highlightCurrent = highlightCurrent,
                expandOnClickNode = expandOnClickNode,
                checkOnClickNode = checkOnClickNode,
                checkOnClickLeaf = checkOnClickLeaf,
                autoExpandParent = autoExpandParent,
                accordion = accordion,
                indent = indent,
                icon = icon,
                onNodeClick = onNodeClick,
                onCheckChange = onCheckChange,
                onCurrentChange = onCurrentChange,
                onNodeExpand = onNodeExpand,
                onNodeCollapse = onNodeCollapse,
                nodeClassName = nodeClassName,
                nodeContent = nodeContent,
            )
        }
    }
}

private fun <T> buildTreeRelations(nodes: List<io.github.xingray.compose.nexus.controls.TreeNode<T>>): io.github.xingray.compose.nexus.controls.TreeRelations<T> {
    val nodeByKey = LinkedHashMap<T, io.github.xingray.compose.nexus.controls.TreeNode<T>>()
    val parentByKey = LinkedHashMap<T, T?>()
    val childrenByKey = LinkedHashMap<T, List<T>>()
    val rootKeys = nodes.map { it.key }

    fun walk(node: io.github.xingray.compose.nexus.controls.TreeNode<T>, parent: T?) {
        nodeByKey[node.key] = node
        parentByKey[node.key] = parent
        childrenByKey[node.key] = node.children.map { it.key }
        node.children.forEach { child -> walk(child, node.key) }
    }
    nodes.forEach { walk(it, null) }

    return _root_ide_package_.io.github.xingray.compose.nexus.controls.TreeRelations(
        nodeByKey = nodeByKey,
        parentByKey = parentByKey,
        childrenByKey = childrenByKey,
        rootKeys = rootKeys,
    )
}

private fun <T> buildVisibleMap(
    nodes: List<io.github.xingray.compose.nexus.controls.TreeNode<T>>,
    keyword: String,
    filterNodeMethod: ((String, io.github.xingray.compose.nexus.controls.TreeNode<T>) -> Boolean)?,
): Map<T, Boolean> {
    val visible = mutableMapOf<T, Boolean>()

    if (keyword.isBlank()) {
        fun markAll(list: List<io.github.xingray.compose.nexus.controls.TreeNode<T>>) {
            list.forEach { node ->
                visible[node.key] = true
                markAll(node.children)
            }
        }
        markAll(nodes)
        return visible
    }

    fun matches(node: io.github.xingray.compose.nexus.controls.TreeNode<T>): Boolean {
        return filterNodeMethod?.invoke(keyword, node) ?: node.label.contains(keyword, ignoreCase = true)
    }

    fun walk(node: io.github.xingray.compose.nexus.controls.TreeNode<T>): Boolean {
        val selfMatch = matches(node)
        val childMatch = node.children.any { walk(it) }
        val isVisible = selfMatch || childMatch
        visible[node.key] = isVisible
        return isVisible
    }
    nodes.forEach { walk(it) }

    return visible
}

@Composable
private fun <T> TreeNodeItem(
    node: io.github.xingray.compose.nexus.controls.TreeNode<T>,
    state: io.github.xingray.compose.nexus.controls.TreeState<T>,
    relations: io.github.xingray.compose.nexus.controls.TreeRelations<T>,
    visibleMap: Map<T, Boolean>,
    depth: Int,
    filtering: Boolean,
    showCheckbox: Boolean,
    checkStrictly: Boolean,
    highlightCurrent: Boolean,
    expandOnClickNode: Boolean,
    checkOnClickNode: Boolean,
    checkOnClickLeaf: Boolean,
    autoExpandParent: Boolean,
    accordion: Boolean,
    indent: Int,
    icon: (@Composable (expanded: Boolean, hasChildren: Boolean) -> Unit)?,
    onNodeClick: ((io.github.xingray.compose.nexus.controls.TreeNode<T>) -> Unit)?,
    onCheckChange: ((node: io.github.xingray.compose.nexus.controls.TreeNode<T>, checked: Boolean, indeterminate: Boolean) -> Unit)?,
    onCurrentChange: ((currentNode: io.github.xingray.compose.nexus.controls.TreeNode<T>?) -> Unit)?,
    onNodeExpand: ((io.github.xingray.compose.nexus.controls.TreeNode<T>) -> Unit)?,
    onNodeCollapse: ((io.github.xingray.compose.nexus.controls.TreeNode<T>) -> Unit)?,
    nodeClassName: ((io.github.xingray.compose.nexus.controls.TreeNode<T>) -> io.github.xingray.compose.nexus.theme.NexusType?)?,
    nodeContent: (@Composable (io.github.xingray.compose.nexus.controls.TreeNode<T>) -> Unit)?,
) {
    if (visibleMap[node.key] != true) return

    val colorScheme = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.colorScheme
    val typography = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.typography

    val hasChildren = node.children.isNotEmpty()
    val isExpanded = state.isExpanded(node.key)
    val isCurrent = state.isSelected(node.key)
    val isChecked = state.isChecked(node.key)
    val isHalfChecked = state.isHalfChecked(node.key)

    val classType = nodeClassName?.invoke(node)
    val classColor = classType?.let { colorScheme.typeColor(it)?.base }

    val rowBackground = when {
        highlightCurrent && isCurrent -> colorScheme.primary.light9
        else -> colorScheme.fill.blank
    }
    val textColor = when {
        node.disabled -> colorScheme.text.disabled
        isCurrent -> colorScheme.primary.base
        classColor != null -> classColor
        else -> colorScheme.text.regular
    }

    fun toggleExpand() {
        val nextExpanded = !state.isExpanded(node.key)
        if (nextExpanded && accordion) {
            val parentKey = relations.parentByKey[node.key]
            val siblingKeys = if (parentKey == null) relations.rootKeys else relations.childrenByKey[parentKey].orEmpty()
            siblingKeys.forEach { sibling ->
                if (sibling != node.key) state.collapse(sibling)
            }
        }
        if (nextExpanded) state.expand(node.key) else state.collapse(node.key)
        if (nextExpanded) onNodeExpand?.invoke(node) else onNodeCollapse?.invoke(node)
    }

    fun toggleCheckedByInput(next: Boolean) {
        _root_ide_package_.io.github.xingray.compose.nexus.controls.setCheckedWithCascade(
            key = node.key,
            checked = next,
            state = state,
            relations = relations,
            checkStrictly = checkStrictly,
        )
        onCheckChange?.invoke(node, state.isChecked(node.key), state.isHalfChecked(node.key))
    }

    fun onRowClick() {
        if (node.disabled) return

        if (hasChildren && expandOnClickNode) {
            toggleExpand()
        }

        if (showCheckbox) {
            val clickableForCheck = checkOnClickNode || (checkOnClickLeaf && !hasChildren)
            if (clickableForCheck) {
                toggleCheckedByInput(!isChecked)
            }
        }

        state.select(node.key)
        if (autoExpandParent && hasChildren && !state.isExpanded(node.key)) {
            state.expand(node.key)
        }
        onCurrentChange?.invoke(node)
        onNodeClick?.invoke(node)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(rowBackground, _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.shapes.base)
            .then(
                if (!node.disabled) {
                    Modifier
                        .clickable { onRowClick() }
                        .pointerHoverIcon(PointerIcon.Hand)
                } else {
                    Modifier
                }
            )
            .padding(
                start = (depth * indent + 8).dp,
                top = 6.dp,
                bottom = 6.dp,
                end = 8.dp,
            ),
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
                icon(isExpanded, hasChildren)
            } else {
                _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
                    text = when {
                        !hasChildren -> "  "
                        isExpanded -> "▾"
                        else -> "▸"
                    },
                    color = colorScheme.text.placeholder,
                    style = typography.extraSmall,
                )
            }
        }

        if (showCheckbox) {
            _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusCheckbox(
                checked = isChecked,
                onCheckedChange = { checked -> toggleCheckedByInput(checked) },
                indeterminate = !checkStrictly && isHalfChecked,
                disabled = node.disabled,
                size = _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Small,
            )
        }

        if (nodeContent != null) {
            nodeContent(node)
        } else {
            _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
                text = node.label,
                color = textColor,
                style = typography.base,
            )
        }
    }

    if (hasChildren) {
        val showChildren = if (filtering) true else isExpanded
        AnimatedVisibility(
            visible = showChildren,
            enter = expandVertically(),
            exit = shrinkVertically(),
        ) {
            Column {
                node.children.forEach { child ->
                    TreeNodeItem(
                        node = child,
                        state = state,
                        relations = relations,
                        visibleMap = visibleMap,
                        depth = depth + 1,
                        filtering = filtering,
                        showCheckbox = showCheckbox,
                        checkStrictly = checkStrictly,
                        highlightCurrent = highlightCurrent,
                        expandOnClickNode = expandOnClickNode,
                        checkOnClickNode = checkOnClickNode,
                        checkOnClickLeaf = checkOnClickLeaf,
                        autoExpandParent = autoExpandParent,
                        accordion = accordion,
                        indent = indent,
                        icon = icon,
                        onNodeClick = onNodeClick,
                        onCheckChange = onCheckChange,
                        onCurrentChange = onCurrentChange,
                        onNodeExpand = onNodeExpand,
                        onNodeCollapse = onNodeCollapse,
                        nodeClassName = nodeClassName,
                        nodeContent = nodeContent,
                    )
                }
            }
        }
    }
}

private fun <T> setCheckedWithCascade(
    key: T,
    checked: Boolean,
    state: io.github.xingray.compose.nexus.controls.TreeState<T>,
    relations: io.github.xingray.compose.nexus.controls.TreeRelations<T>,
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
    _root_ide_package_.io.github.xingray.compose.nexus.controls.updateAncestors(key, state, relations)
}

private fun <T> updateAncestors(
    key: T,
    state: io.github.xingray.compose.nexus.controls.TreeState<T>,
    relations: io.github.xingray.compose.nexus.controls.TreeRelations<T>,
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

