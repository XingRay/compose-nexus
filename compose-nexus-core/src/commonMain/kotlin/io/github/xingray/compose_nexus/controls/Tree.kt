package io.github.xingray.compose_nexus.controls

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import io.github.xingray.compose_nexus.theme.NexusTheme

/**
 * A tree node data model.
 *
 * @param T The type of the node key.
 * @param key Unique identifier for this node.
 * @param label Display label.
 * @param children Child nodes.
 * @param disabled Whether this node is disabled.
 */
data class TreeNode<T>(
    val key: T,
    val label: String,
    val children: List<TreeNode<T>> = emptyList(),
    val disabled: Boolean = false,
)

/**
 * Tree state holder managing expanded and selected nodes.
 */
@Stable
class TreeState<T> {
    internal val expandedKeys = mutableStateMapOf<T, Boolean>()
    internal val selectedKey = mutableStateMapOf<String, T?>() // using "selected" as single key

    fun isExpanded(key: T): Boolean = expandedKeys[key] == true

    fun toggleExpand(key: T) {
        expandedKeys[key] = !(expandedKeys[key] ?: false)
    }

    fun expand(key: T) {
        expandedKeys[key] = true
    }

    fun collapse(key: T) {
        expandedKeys[key] = false
    }

    fun selectedKey(): T? = selectedKey["selected"]

    fun select(key: T) {
        selectedKey["selected"] = key
    }

    fun isSelected(key: T): Boolean = selectedKey["selected"] == key
}

@Composable
fun <T> rememberTreeState(): TreeState<T> = remember { TreeState() }

/**
 * Element Plus Tree — a hierarchical tree view.
 *
 * @param T Node key type.
 * @param nodes Root-level tree nodes.
 * @param modifier Modifier.
 * @param state Tree expand/select state.
 * @param defaultExpandAll Whether to expand all nodes by default.
 * @param onNodeClick Callback when a node is clicked.
 * @param nodeContent Custom content composable for each node. Defaults to label text.
 */
@Composable
fun <T> NexusTree(
    nodes: List<TreeNode<T>>,
    modifier: Modifier = Modifier,
    state: TreeState<T> = rememberTreeState(),
    defaultExpandAll: Boolean = false,
    onNodeClick: ((TreeNode<T>) -> Unit)? = null,
    nodeContent: (@Composable (TreeNode<T>) -> Unit)? = null,
) {
    // Initialize expansion state for all nodes when defaultExpandAll
    if (defaultExpandAll) {
        remember(nodes) {
            expandAllNodes(nodes, state)
            true
        }
    }

    Column(modifier = modifier.fillMaxWidth()) {
        nodes.forEach { node ->
            TreeNodeItem(
                node = node,
                state = state,
                depth = 0,
                onNodeClick = onNodeClick,
                nodeContent = nodeContent,
            )
        }
    }
}

private fun <T> expandAllNodes(nodes: List<TreeNode<T>>, state: TreeState<T>) {
    nodes.forEach { node ->
        if (node.children.isNotEmpty()) {
            state.expand(node.key)
            expandAllNodes(node.children, state)
        }
    }
}

@Composable
private fun <T> TreeNodeItem(
    node: TreeNode<T>,
    state: TreeState<T>,
    depth: Int,
    onNodeClick: ((TreeNode<T>) -> Unit)?,
    nodeContent: (@Composable (TreeNode<T>) -> Unit)?,
) {
    val colorScheme = NexusTheme.colorScheme
    val typography = NexusTheme.typography

    val hasChildren = node.children.isNotEmpty()
    val isExpanded = state.isExpanded(node.key)
    val isSelected = state.isSelected(node.key)

    val textColor = when {
        node.disabled -> colorScheme.text.disabled
        isSelected -> colorScheme.primary.base
        else -> colorScheme.text.regular
    }

    val expandIcon = when {
        !hasChildren -> "  " // indentation spacer
        isExpanded -> "▾"
        else -> "▸"
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (!node.disabled) {
                    Modifier
                        .clickable {
                            if (hasChildren) state.toggleExpand(node.key)
                            state.select(node.key)
                            onNodeClick?.invoke(node)
                        }
                        .pointerHoverIcon(PointerIcon.Hand)
                } else Modifier
            )
            .padding(
                start = (depth * 18 + 8).dp,
                top = 6.dp,
                bottom = 6.dp,
                end = 8.dp,
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        NexusText(
            text = expandIcon,
            color = colorScheme.text.placeholder,
            style = typography.extraSmall,
        )

        if (nodeContent != null) {
            nodeContent(node)
        } else {
            NexusText(
                text = node.label,
                color = textColor,
                style = typography.base,
            )
        }
    }

    // Children (animated)
    if (hasChildren) {
        AnimatedVisibility(
            visible = isExpanded,
            enter = expandVertically(),
            exit = shrinkVertically(),
        ) {
            Column {
                node.children.forEach { child ->
                    TreeNodeItem(
                        node = child,
                        state = state,
                        depth = depth + 1,
                        onNodeClick = onNodeClick,
                        nodeContent = nodeContent,
                    )
                }
            }
        }
    }
}
