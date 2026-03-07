package io.github.xingray.compose.nexus.templates

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.xingray.compose.nexus.controls.NexusText
import io.github.xingray.compose.nexus.controls.NexusTree
import io.github.xingray.compose.nexus.controls.TreeNode
import io.github.xingray.compose.nexus.controls.TreeState
import io.github.xingray.compose.nexus.controls.rememberTreeState
import io.github.xingray.compose.nexus.theme.NexusTheme

/**
 * TreeListPage — a page with a tree navigation sidebar and a content/list area on the right.
 *
 * @param T Tree node key type.
 * @param nodes Tree node data.
 * @param modifier Modifier.
 * @param title Page title.
 * @param treeState Tree state for expand/select tracking.
 * @param sidebarWidth Sidebar width.
 * @param defaultExpandAll Whether to expand all tree nodes.
 * @param onNodeSelect Callback when a tree node is selected.
 * @param content Right-side content composable, receives the selected node key.
 */
@Composable
fun <T> NexusTreeListPage(
    nodes: List<TreeNode<T>>,
    modifier: Modifier = Modifier,
    title: String = "Tree List",
    treeState: TreeState<T> = rememberTreeState(),
    sidebarWidth: Dp = 260.dp,
    defaultExpandAll: Boolean = false,
    onNodeSelect: ((TreeNode<T>) -> Unit)? = null,
    content: @Composable (selectedKey: T?) -> Unit,
) {
    val colorScheme = NexusTheme.colorScheme
    val typography = NexusTheme.typography
    val shapes = NexusTheme.shapes

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colorScheme.background.page)
            .padding(20.dp),
    ) {
        // Title
        NexusText(
            text = title,
            color = colorScheme.text.primary,
            style = typography.extraLarge,
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Body
        Row(modifier = Modifier.weight(1f)) {
            // Tree sidebar
            Column(
                modifier = Modifier
                    .width(sidebarWidth)
                    .fillMaxHeight()
                    .clip(shapes.base)
                    .background(colorScheme.fill.blank)
                    .padding(8.dp)
                    .verticalScroll(rememberScrollState()),
            ) {
                NexusTree(
                    nodes = nodes,
                    state = treeState,
                    defaultExpandAll = defaultExpandAll,
                    onNodeClick = { node ->
                        onNodeSelect?.invoke(node)
                    },
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Content area
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clip(shapes.base)
                    .background(colorScheme.fill.blank)
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
            ) {
                content(treeState.selectedKey())
            }
        }
    }
}
