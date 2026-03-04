package io.github.xingray.compose_nexus.sample.demos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.xingray.compose_nexus.containers.NexusCard
import io.github.xingray.compose_nexus.controls.*
import io.github.xingray.compose_nexus.theme.NexusTheme
import io.github.xingray.compose_nexus.theme.NexusType

@Composable
fun TableDemo() {
    data class User(val name: String, val age: Int, val city: String, val status: NexusType)
    val data = listOf(
        User("Alice", 28, "New York", NexusType.Success),
        User("Bob", 32, "London", NexusType.Warning),
        User("Charlie", 25, "Tokyo", NexusType.Info),
        User("Diana", 30, "Paris", NexusType.Danger),
        User("Evan", 29, "Berlin", NexusType.Primary),
        User("Frank", 31, "Sydney", NexusType.Success),
        User("Grace", 27, "Toronto", NexusType.Warning),
        User("Helen", 33, "Singapore", NexusType.Info),
    )
    val columns = listOf(
        NexusTableColumn<User>("Name", tooltipText = { it.name }) { NexusText(text = it.name) },
        NexusTableColumn<User>("Age", width = 80.dp, alignment = Alignment.Center) { NexusText(text = it.age.toString()) },
        NexusTableColumn<User>("City", showOverflowTooltip = true, tooltipText = { "${it.city} - long tooltip demo content" }) {
            NexusText(text = "${it.city} city with long content")
        },
        NexusTableColumn<User>("Status", width = 110.dp, alignment = Alignment.Center) {
            NexusTag(text = it.status.name.lowercase(), type = it.status, size = io.github.xingray.compose_nexus.theme.ComponentSize.Small)
        },
    )

    DemoPage(title = "Table", description = "For displaying multiple similar data sets.") {
        DemoSection("Basic table") {
            NexusTable(
                data = data,
                columns = columns,
            )
        }

        DemoSection("Striped Table") {
            NexusTable(
                data = data,
                columns = columns,
                stripe = true,
            )
        }

        DemoSection("Table with border") {
            NexusTable(
                data = data.take(4),
                columns = columns,
                border = true,
            )
        }

        DemoSection("Table with status") {
            NexusTable(
                data = data,
                columns = columns,
                rowClassName = { _, row -> row.status },
                stripe = true,
            )
        }

        DemoSection("Table with show overflow tooltip") {
            NexusTable(
                data = data.take(4),
                columns = columns,
                showOverflowTooltip = true,
            )
        }

        DemoSection("Table with fixed header") {
            val state = rememberTableState()
            var currentName by remember { mutableStateOf("none") }
            NexusTable(
                data = data + data + data,
                columns = columns,
                state = state,
                height = 260.dp,
                stripe = true,
                highlightCurrentRow = true,
                onCurrentChange = { current, _ ->
                    currentName = current?.name ?: "none"
                },
            )
            Spacer(modifier = Modifier.height(8.dp))
            NexusText(
                text = "current row: $currentName",
                color = NexusTheme.colorScheme.text.secondary,
                style = NexusTheme.typography.small,
            )
        }
        DemoSection("Empty table") {
            NexusTable(
                data = emptyList<String>(),
                columns = listOf(
                    NexusTableColumn<String>("Name") { NexusText(text = it) },
                    NexusTableColumn<String>("Value") { NexusText(text = it) },
                ),
            )
        }
    }
}

@Composable
fun VirtualizedTableDemo() {
    data class Record(val id: Int, val name: String, val age: Int, val city: String, val status: NexusType)
    val rows = remember {
        (1..1000).map { i ->
            val status = when {
                i % 10 == 0 -> NexusType.Danger
                i % 7 == 0 -> NexusType.Warning
                i % 5 == 0 -> NexusType.Success
                else -> NexusType.Info
            }
            Record(
                id = i,
                name = "User-$i",
                age = 18 + (i % 40),
                city = "City-${(i % 30) + 1}",
                status = status,
            )
        }
    }
    val columns = listOf(
        NexusVirtualizedTableColumn<Record>(
            key = "id",
            title = "ID",
            width = 80.dp,
            alignment = Alignment.Center,
        ) { row, _ ->
            NexusText(text = row.id.toString())
        },
        NexusVirtualizedTableColumn<Record>(
            key = "name",
            title = "Name",
            width = 180.dp,
        ) { row, _ ->
            NexusText(text = row.name)
        },
        NexusVirtualizedTableColumn<Record>(
            key = "age",
            title = "Age",
            width = 100.dp,
            alignment = Alignment.Center,
        ) { row, _ ->
            NexusText(text = row.age.toString())
        },
        NexusVirtualizedTableColumn<Record>(
            key = "city",
            title = "City",
            width = 180.dp,
        ) { row, _ ->
            NexusText(text = row.city)
        },
        NexusVirtualizedTableColumn<Record>(
            key = "status",
            title = "Status",
            width = 140.dp,
            alignment = Alignment.Center,
        ) { row, _ ->
            NexusTag(
                text = row.status.name.lowercase(),
                type = row.status,
                size = io.github.xingray.compose_nexus.theme.ComponentSize.Small,
            )
        },
    )

    DemoPage(title = "Virtualized Table", description = "Render massive chunks of data with virtual scrolling.") {
        DemoSection("Basic usage") {
            var remainDistance by remember { mutableStateOf("-") }
            NexusVirtualizedTable(
                data = rows,
                columns = columns,
                width = 700.dp,
                height = 320.dp,
                onEndReached = { remainDistance = it.toString() },
                footer = {
                    NexusText(
                        text = "end-reached remainDistance: $remainDistance",
                        color = NexusTheme.colorScheme.text.secondary,
                        style = NexusTheme.typography.small,
                    )
                },
            )
        }

        DemoSection("Table with status") {
            NexusVirtualizedTable(
                data = rows.take(300),
                columns = columns,
                width = 700.dp,
                height = 280.dp,
                stripe = true,
                rowClassName = { _, row -> row.status },
            )
        }

        DemoSection("Table with sticky rows") {
            NexusVirtualizedTable(
                data = rows.take(200),
                fixedData = rows.take(2),
                columns = columns,
                width = 700.dp,
                height = 280.dp,
                stripe = true,
            )
        }

        DemoSection("Customized Empty Renderer") {
            NexusVirtualizedTable(
                data = emptyList<Record>(),
                columns = columns,
                width = 700.dp,
                height = 220.dp,
                empty = {
                    NexusEmpty(description = "No virtualized data")
                },
            )
        }
    }
}

@Composable
fun TreeDemo() {
    DemoPage(title = "Tree", description = "Display a set of data with hierarchies.") {
        val nodes = remember {
            listOf(
                TreeNode(
                    key = "1",
                    label = "Level one 1",
                    children = listOf(
                        TreeNode("1-1", "Level two 1-1"),
                        TreeNode("1-2", "Level two 1-2"),
                    ),
                ),
                TreeNode(
                    key = "2",
                    label = "Level one 2",
                    children = listOf(
                        TreeNode(
                            key = "2-1",
                            label = "Level two 2-1",
                            children = listOf(
                                TreeNode("2-1-1", "Level three 2-1-1"),
                            ),
                        ),
                        TreeNode("2-2", "Level two 2-2"),
                    ),
                ),
                TreeNode("3", "Level one 3"),
            )
        }

        DemoSection("Basic usage") {
            val state = rememberTreeState<String>()
            NexusTree(
                nodes = nodes,
                state = state,
                defaultExpandAll = true,
                highlightCurrent = true,
            )
        }

        DemoSection("Selectable") {
            val state = rememberTreeState<String>()
            var checkedInfo by remember { mutableStateOf("No checked keys") }
            NexusTree(
                nodes = nodes,
                state = state,
                showCheckbox = true,
                defaultExpandAll = true,
                onCheckChange = { _, _, _ ->
                    checkedInfo = state.checkedKeys().joinToString(", ").ifBlank { "No checked keys" }
                },
            )
            Spacer(modifier = Modifier.height(8.dp))
            NexusText(
                text = "Checked: $checkedInfo",
                style = NexusTheme.typography.small,
                color = NexusTheme.colorScheme.text.secondary,
            )
        }

        DemoSection("Disabled checkbox") {
            val disabledNodes = remember {
                listOf(
                    TreeNode(
                        key = "d-1",
                        label = "Parent A",
                        children = listOf(
                            TreeNode("d-1-1", "A-1", disabled = true),
                            TreeNode("d-1-2", "A-2"),
                        ),
                    ),
                    TreeNode(
                        key = "d-2",
                        label = "Parent B",
                        disabled = true,
                        children = listOf(
                            TreeNode("d-2-1", "B-1"),
                        ),
                    ),
                )
            }
            NexusTree(
                nodes = disabledNodes,
                showCheckbox = true,
                defaultExpandAll = true,
            )
        }

        DemoSection("Default expanded and default checked") {
            NexusTree(
                nodes = nodes,
                showCheckbox = true,
                defaultExpandedKeys = listOf("1", "2"),
                defaultCheckedKeys = listOf("1-1", "2-1-1"),
                highlightCurrent = true,
                currentNodeKey = "2-1",
            )
        }

        DemoSection("Checking tree nodes") {
            val state = rememberTreeState<String>()
            var checkedText by remember { mutableStateOf("[]") }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                NexusButton(
                    text = "Get Checked Keys",
                    onClick = {
                        checkedText = state.checkedKeys().joinToString(prefix = "[", postfix = "]")
                    },
                    size = io.github.xingray.compose_nexus.theme.ComponentSize.Small,
                )
                NexusButton(
                    text = "Set Checked Keys",
                    onClick = {
                        state.clearChecked()
                        state.setCheckedKeys(listOf("1-2", "2-1-1"))
                    },
                    size = io.github.xingray.compose_nexus.theme.ComponentSize.Small,
                    type = NexusType.Primary,
                )
                NexusButton(
                    text = "Clear",
                    onClick = {
                        state.clearChecked()
                        checkedText = "[]"
                    },
                    size = io.github.xingray.compose_nexus.theme.ComponentSize.Small,
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            NexusText(
                text = "checkedKeys = $checkedText",
                style = NexusTheme.typography.small,
                color = NexusTheme.colorScheme.text.secondary,
            )
            Spacer(modifier = Modifier.height(8.dp))
            NexusTree(
                nodes = nodes,
                state = state,
                showCheckbox = true,
                defaultExpandAll = true,
            )
        }

        DemoSection("Custom node content") {
            val state = rememberTreeState<String>()
            NexusTree(
                nodes = nodes,
                state = state,
                defaultExpandAll = true,
                nodeContent = { node ->
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                        NexusText(text = node.label)
                        NexusTag(
                            text = node.key,
                            type = NexusType.Info,
                            size = io.github.xingray.compose_nexus.theme.ComponentSize.Small,
                        )
                    }
                },
            )
        }

        DemoSection("Custom node class") {
            val classMap = remember {
                mapOf(
                    "1" to NexusType.Primary,
                    "1-1" to NexusType.Success,
                    "2" to NexusType.Warning,
                    "2-1-1" to NexusType.Danger,
                )
            }
            NexusTree(
                nodes = nodes,
                defaultExpandAll = true,
                nodeClassName = { node -> classMap[node.key] },
            )
        }

        DemoSection("Tree node filtering") {
            val state = rememberTreeState<String>()
            var keyword by remember { mutableStateOf("") }
            NexusInput(
                value = keyword,
                onValueChange = {
                    keyword = it
                    state.filter(it)
                },
                placeholder = "Type to filter tree nodes",
                clearable = true,
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(8.dp))
            NexusTree(
                nodes = nodes,
                state = state,
                defaultExpandAll = true,
                filterNodeMethod = { value, node ->
                    node.label.contains(value, ignoreCase = true) || node.key.contains(value, ignoreCase = true)
                },
            )
        }

        DemoSection("Accordion") {
            NexusTree(
                nodes = nodes,
                accordion = true,
                expandOnClickNode = true,
                highlightCurrent = true,
            )
        }
    }
}

@Composable
fun VirtualizedTreeDemo() {
    val nodes = remember {
        (1..24).map { group ->
            val parentKey = "group-$group"
            TreeNode(
                key = parentKey,
                label = "Group $group",
                children = (1..16).map { item ->
                    val itemKey = "$parentKey-item-$item"
                    TreeNode(
                        key = itemKey,
                        label = "Node $group-$item",
                        disabled = item % 11 == 0,
                        children = (1..4).map { leaf ->
                            TreeNode(
                                key = "$itemKey-leaf-$leaf",
                                label = "Leaf $group-$item-$leaf",
                                disabled = leaf == 4 && item % 7 == 0,
                            )
                        },
                    )
                },
            )
        }
    }

    DemoPage(title = "Virtualized Tree", description = "Tree view with blazing fast scrolling performance for large datasets.") {
        DemoSection("Basic usage") {
            val state = rememberTreeState<String>()
            NexusVirtualizedTree(
                nodes = nodes,
                state = state,
                height = 280.dp,
                itemSize = 28.dp,
                defaultExpandedKeys = listOf("group-1", "group-2", "group-3"),
                highlightCurrent = true,
            )
        }

        DemoSection("Selectable") {
            val state = rememberTreeState<String>()
            var checkedInfo by remember { mutableStateOf("No checked keys") }
            NexusVirtualizedTree(
                nodes = nodes,
                state = state,
                height = 280.dp,
                showCheckbox = true,
                defaultExpandedKeys = listOf("group-1", "group-2"),
                onCheckChange = { _, _ ->
                    checkedInfo = state.checkedKeys().take(10).joinToString(", ").ifBlank { "No checked keys" }
                },
            )
            Spacer(modifier = Modifier.height(8.dp))
            NexusText(
                text = "Checked sample: $checkedInfo",
                style = NexusTheme.typography.small,
                color = NexusTheme.colorScheme.text.secondary,
            )
        }

        DemoSection("Disabled checkbox") {
            NexusVirtualizedTree(
                nodes = nodes,
                height = 260.dp,
                showCheckbox = true,
                defaultExpandedKeys = listOf("group-1"),
            )
        }

        DemoSection("Default expanded and default checked") {
            NexusVirtualizedTree(
                nodes = nodes,
                height = 260.dp,
                showCheckbox = true,
                defaultExpandedKeys = listOf("group-1", "group-2"),
                defaultCheckedKeys = listOf("group-1-item-1", "group-2-item-3-leaf-2"),
                currentNodeKey = "group-2-item-3",
                highlightCurrent = true,
            )
        }

        DemoSection("Custom node content") {
            NexusVirtualizedTree(
                nodes = nodes,
                height = 260.dp,
                defaultExpandedKeys = listOf("group-1"),
                nodeContent = { node ->
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                        NexusText(text = node.label)
                        NexusTag(
                            text = node.key,
                            type = NexusType.Info,
                            size = io.github.xingray.compose_nexus.theme.ComponentSize.Small,
                        )
                    }
                },
            )
        }

        DemoSection("Custom node class") {
            NexusVirtualizedTree(
                nodes = nodes,
                height = 260.dp,
                defaultExpandedKeys = listOf("group-1", "group-2"),
                nodeClassName = { node ->
                    when {
                        node.key.endsWith("-1") -> NexusType.Success
                        node.key.endsWith("-2") -> NexusType.Warning
                        node.key.endsWith("-3") -> NexusType.Danger
                        else -> null
                    }
                },
            )
        }

        DemoSection("Custom node icon") {
            NexusVirtualizedTree(
                nodes = nodes,
                height = 260.dp,
                defaultExpandedKeys = listOf("group-1", "group-2"),
                icon = { expanded, isLeaf ->
                    val text = when {
                        isLeaf -> "•"
                        expanded -> "−"
                        else -> "+"
                    }
                    NexusText(
                        text = text,
                        color = NexusTheme.colorScheme.primary.base,
                        style = NexusTheme.typography.extraSmall,
                    )
                },
            )
        }

        DemoSection("Tree node filtering") {
            val state = rememberTreeState<String>()
            var keyword by remember { mutableStateOf("") }
            NexusInput(
                value = keyword,
                onValueChange = {
                    keyword = it
                    state.filter(it)
                },
                placeholder = "Type to filter virtualized tree nodes",
                clearable = true,
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(8.dp))
            NexusVirtualizedTree(
                nodes = nodes,
                state = state,
                height = 280.dp,
                filterMethod = { query, node ->
                    node.label.contains(query, ignoreCase = true) || node.key.contains(query, ignoreCase = true)
                },
                defaultExpandedKeys = listOf("group-1"),
            )
        }
    }
}

@Composable
fun StatisticDemo() {
    DemoPage(title = "Statistic", description = "Display statistics and countdown values.") {
        DemoSection("Basic usage") {
            Row(horizontalArrangement = Arrangement.spacedBy(28.dp), verticalAlignment = Alignment.CenterVertically) {
                NexusStatistic(
                    title = "Daily Active Users",
                    value = 268500,
                )
                NexusStatistic(
                    title = "Transactions",
                    value = 1388.56,
                    precision = 2,
                    prefix = "$",
                    valueColor = NexusTheme.colorScheme.success.base,
                )
                NexusStatistic(
                    title = "Growth",
                    value = 93.27,
                    precision = 2,
                    suffix = "%",
                    formatter = { v -> "${v.toInt()} pts" },
                )
            }
        }

        DemoSection("Countdown") {
            var remainMillis by remember { mutableStateOf(90_000L) }
            var remainText by remember { mutableStateOf("counting...") }
            NexusCountdown(
                value = remainMillis,
                title = "Event Countdown",
                format = "mm:ss",
                suffix = "remaining",
                onChange = { remain ->
                    remainText = "${remain / 1000}s"
                },
                onFinish = {
                    remainText = "finished"
                },
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                NexusButton(
                    text = "Restart 90s",
                    onClick = { remainMillis = 90_000L },
                    type = NexusType.Primary,
                    size = io.github.xingray.compose_nexus.theme.ComponentSize.Small,
                )
                NexusButton(
                    text = "Finish Now",
                    onClick = { remainMillis = 0L },
                    size = io.github.xingray.compose_nexus.theme.ComponentSize.Small,
                )
                NexusText(
                    text = "status: $remainText",
                    style = NexusTheme.typography.small,
                    color = NexusTheme.colorScheme.text.secondary,
                )
            }
        }

        DemoSection("Card usage") {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.Top) {
                NexusCard(
                    modifier = Modifier.weight(1f),
                    headerText = "Total Revenue",
                ) {
                    NexusStatistic(
                        value = 1265600.12,
                        precision = 2,
                        prefix = "$",
                        valueColor = NexusTheme.colorScheme.primary.base,
                    )
                }
                NexusCard(
                    modifier = Modifier.weight(1f),
                    headerText = "Orders",
                ) {
                    NexusStatistic(
                        value = 82456,
                        suffix = "orders",
                        valueColor = NexusTheme.colorScheme.warning.base,
                    )
                }
                NexusCard(
                    modifier = Modifier.weight(1f),
                    headerText = "Server Refresh",
                ) {
                    NexusCountdown(
                        value = 3_600_000L,
                        format = "HH:mm:ss",
                    )
                }
            }
        }
    }
}

@Composable
fun SegmentedDemo() {
    DemoPage(title = "Segmented", description = "Display multiple options and allow users to select a single option.") {
        DemoSection("Basic usage") {
            val options = listOf(
                NexusSegmentedOption("Daily", "Daily"),
                NexusSegmentedOption("Weekly", "Weekly"),
                NexusSegmentedOption("Monthly", "Monthly"),
                NexusSegmentedOption("Yearly", "Yearly"),
            )
            var value by remember { mutableStateOf("Weekly") }
            NexusSegmented<String>(
                modelValue = value,
                onValueChange = { value = it },
                options = options,
            )
            Spacer(modifier = Modifier.height(8.dp))
            NexusText(
                text = "selected = $value",
                style = NexusTheme.typography.small,
                color = NexusTheme.colorScheme.text.secondary,
            )
        }

        DemoSection("Direction usage") {
            val options = listOf(
                NexusSegmentedOption("iOS", "iOS"),
                NexusSegmentedOption("Android", "Android"),
                NexusSegmentedOption("Desktop", "Desktop"),
            )
            var horizontal by remember { mutableStateOf("Android") }
            var vertical by remember { mutableStateOf("iOS") }
            Row(horizontalArrangement = Arrangement.spacedBy(20.dp), verticalAlignment = Alignment.Top) {
                NexusSegmented<String>(
                    modelValue = horizontal,
                    onValueChange = { horizontal = it },
                    options = options,
                )
                NexusSegmented<String>(
                    modelValue = vertical,
                    onValueChange = { vertical = it },
                    options = options,
                    direction = SegmentedDirection.Vertical,
                    size = io.github.xingray.compose_nexus.theme.ComponentSize.Small,
                )
            }
        }

        DemoSection("Disabled") {
            val options = listOf(
                NexusSegmentedOption("A", "Option A"),
                NexusSegmentedOption("B", "Option B", disabled = true),
                NexusSegmentedOption("C", "Option C"),
            )
            var value by remember { mutableStateOf("A") }
            NexusSegmented<String>(
                modelValue = value,
                onValueChange = { value = it },
                options = options,
            )
            Spacer(modifier = Modifier.height(8.dp))
            NexusSegmented<String>(
                modelValue = value,
                onValueChange = { value = it },
                options = options,
                disabled = true,
            )
        }

        DemoSection("Aliases for custom options") {
            val options = listOf<Any>(
                mapOf("id" to "draft", "name" to "Draft", "locked" to false),
                mapOf("id" to "review", "name" to "In Review", "locked" to false),
                mapOf("id" to "published", "name" to "Published", "locked" to true),
            )
            var value: Any? by remember { mutableStateOf("review") }
            NexusSegmented(
                modelValue = value,
                onValueChange = { value = it },
                options = options,
                props = SegmentedProps(
                    value = "id",
                    label = "name",
                    disabled = "locked",
                ),
            )
            Spacer(modifier = Modifier.height(8.dp))
            NexusText(
                text = "selected = $value",
                style = NexusTheme.typography.small,
                color = NexusTheme.colorScheme.text.secondary,
            )
        }

        DemoSection("Block") {
            val options = listOf(
                NexusSegmentedOption("left", "Left"),
                NexusSegmentedOption("center", "Center"),
                NexusSegmentedOption("right", "Right"),
            )
            var value by remember { mutableStateOf("center") }
            NexusSegmented<String>(
                modelValue = value,
                onValueChange = { value = it },
                options = options,
                block = true,
                modifier = Modifier.fillMaxWidth(),
            )
        }

        DemoSection("Custom content") {
            val options = listOf(
                NexusSegmentedOption("mail", "Mail"),
                NexusSegmentedOption("calendar", "Calendar"),
                NexusSegmentedOption("cloud", "Cloud"),
            )
            var value by remember { mutableStateOf("mail") }
            NexusSegmented<String>(
                modelValue = value,
                onValueChange = { value = it },
                options = options,
                optionContent = { item, selected ->
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                        NexusText(
                            text = if (selected) "●" else "○",
                            color = if (selected) NexusTheme.colorScheme.primary.base else NexusTheme.colorScheme.text.placeholder,
                            style = NexusTheme.typography.extraSmall,
                        )
                        NexusText(text = item.label)
                    }
                },
            )
        }

        DemoSection("Custom style") {
            val options = listOf(
                NexusSegmentedOption("low", "Low"),
                NexusSegmentedOption("medium", "Medium"),
                NexusSegmentedOption("high", "High"),
            )
            var value by remember { mutableStateOf("medium") }
            NexusSegmented<String>(
                modelValue = value,
                onValueChange = { value = it },
                options = options,
                size = io.github.xingray.compose_nexus.theme.ComponentSize.Large,
                optionContent = { item, selected ->
                    NexusTag(
                        text = item.label,
                        type = when {
                            !selected -> NexusType.Info
                            item.value == "low" -> NexusType.Success
                            item.value == "medium" -> NexusType.Warning
                            else -> NexusType.Danger
                        },
                        size = io.github.xingray.compose_nexus.theme.ComponentSize.Small,
                    )
                },
            )
        }
    }
}

@Composable
fun PaginationDemo() {
    DemoPage(title = "Pagination", description = "Divide data into pages when there's too much to show.") {
        DemoSection("Basic usage") {
            val state = rememberPaginationState(pageCount = 20, total = 200, pageSize = 10)
            NexusPagination(
                state = state,
                layout = "prev, pager, next, jumper, ->, total",
            )
        }

        DemoSection("Number of pagers") {
            val state = rememberPaginationState(pageCount = 40, initialPage = 20)
            NexusPagination(
                state = state,
                pagerCount = 11,
            )
        }

        DemoSection("Buttons with background color") {
            val state = rememberPaginationState(pageCount = 15, initialPage = 8)
            NexusPagination(
                state = state,
                background = true,
            )
        }

        DemoSection("Small Pagination") {
            val state = rememberPaginationState(pageCount = 10, initialPage = 3)
            NexusPagination(state = state, size = io.github.xingray.compose_nexus.theme.ComponentSize.Small)
        }

        DemoSection("Hide pagination when there is only one page") {
            val state = rememberPaginationState(pageCount = 1)
            NexusPagination(
                state = state,
                hideOnSinglePage = true,
            )
            NexusText(
                text = "When pageCount = 1, pagination is hidden.",
                color = NexusTheme.colorScheme.text.secondary,
                style = NexusTheme.typography.small,
            )
        }

        DemoSection("More elements") {
            val state = rememberPaginationState(pageCount = 0, total = 240, pageSize = 10, initialPage = 2)
            var eventLog by remember { mutableStateOf("No events yet") }
            NexusPagination(
                state = state,
                background = true,
                layout = "total, sizes, prev, pager, next, jumper, ->, slot",
                pageSizes = listOf(5, 10, 20, 40),
                onCurrentChange = { page ->
                    eventLog = "current-change: page=$page"
                },
                onSizeChange = { size ->
                    eventLog = "size-change: pageSize=$size"
                },
                onPrevClick = { page ->
                    eventLog = "prev-click: page=$page"
                },
                onNextClick = { page ->
                    eventLog = "next-click: page=$page"
                },
                slot = {
                    NexusTag(
                        text = eventLog,
                        type = NexusType.Info,
                        size = io.github.xingray.compose_nexus.theme.ComponentSize.Small,
                    )
                },
            )
        }
    }
}

@Composable
fun ProgressDemo() {
    DemoPage(title = "Progress", description = "Progress is used to show the progress of current operation.") {
        DemoSection("Linear progress bar") {
            NexusProgress(percentage = 30f)
            NexusProgress(percentage = 55f, format = { p -> "${p.toInt()} pts" })
            NexusProgress(percentage = 95f, status = NexusType.Success)
        }

        DemoSection("Internal percentage") {
            NexusProgress(
                percentage = 65f,
                textInside = true,
                strokeWidth = 18.dp,
            )
        }

        DemoSection("Custom color") {
            val dangerColor = NexusTheme.colorScheme.danger.base
            val warningColor = NexusTheme.colorScheme.warning.base
            val successColor = NexusTheme.colorScheme.success.base

            NexusProgress(percentage = 20f, color = dangerColor)
            NexusProgress(
                percentage = 45f,
                colorFunction = { p ->
                    when {
                        p < 30f -> dangerColor
                        p < 70f -> warningColor
                        else -> successColor
                    }
                },
            )
            NexusProgress(
                percentage = 78f,
                colorStops = listOf(
                    ProgressColorStop(dangerColor, 25f),
                    ProgressColorStop(warningColor, 60f),
                    ProgressColorStop(successColor, 100f),
                ),
            )
        }

        DemoSection("Circular progress bar") {
            Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                NexusProgress(percentage = 75f, type = ProgressType.Circle)
                NexusProgress(percentage = 100f, type = ProgressType.Circle, status = NexusType.Success)
                NexusProgress(percentage = 50f, type = ProgressType.Circle, status = NexusType.Danger)
            }
        }

        DemoSection("Dashboard progress bar") {
            Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                NexusProgress(percentage = 35f, type = ProgressType.Dashboard)
                NexusProgress(percentage = 76f, type = ProgressType.Dashboard, status = NexusType.Warning)
            }
        }

        DemoSection("Customized content") {
            NexusProgress(
                percentage = 68f,
                content = { p ->
                    NexusTag(
                        text = "Custom ${p.toInt()}%",
                        type = NexusType.Success,
                        size = io.github.xingray.compose_nexus.theme.ComponentSize.Small,
                    )
                },
            )
        }

        DemoSection("Indeterminate progress") {
            NexusProgress(
                percentage = 40f,
                indeterminate = true,
                duration = 2.2f,
            )
        }

        DemoSection("Striped progress") {
            NexusProgress(
                percentage = 62f,
                striped = true,
                textInside = true,
                strokeWidth = 16.dp,
            )
            NexusProgress(
                percentage = 48f,
                striped = true,
                stripedFlow = true,
                duration = 1.6f,
                textInside = true,
                strokeWidth = 16.dp,
            )
        }
    }
}

@Composable
fun ResultDemo() {
    DemoPage(title = "Result", description = "Used to give feedback on the result of operations or access exceptions.") {
        DemoSection("Basic usage") {
            Row(horizontalArrangement = Arrangement.spacedBy(18.dp), verticalAlignment = Alignment.Top) {
                NexusResult(
                    icon = NexusResultIcon.Primary,
                    title = "Primary",
                    subTitle = "This is a primary result.",
                    modifier = Modifier.weight(1f),
                )
                NexusResult(
                    icon = NexusResultIcon.Success,
                    title = "Success Tip",
                    subTitle = "Please follow the instruction.",
                    modifier = Modifier.weight(1f),
                )
                NexusResult(
                    icon = NexusResultIcon.Warning,
                    title = "Warning Tip",
                    subTitle = "Careful with this operation.",
                    modifier = Modifier.weight(1f),
                )
                NexusResult(
                    icon = NexusResultIcon.Error,
                    title = "Error Tip",
                    subTitle = "Please try again later.",
                    modifier = Modifier.weight(1f),
                )
            }
        }

        DemoSection("Customized content") {
            NexusResult(
                icon = NexusResultIcon.Info,
                title = "404",
                subTitle = "The requested resource could not be found.",
                iconSlot = {
                    NexusTag(text = "404", type = NexusType.Info)
                },
                titleSlot = {
                    NexusText(
                        text = "Custom Title Slot",
                        style = NexusTheme.typography.large,
                        color = NexusTheme.colorScheme.primary.base,
                    )
                },
                subTitleSlot = {
                    NexusText(
                        text = "Custom subtitle slot with richer style.",
                        style = NexusTheme.typography.small,
                        color = NexusTheme.colorScheme.text.secondary,
                    )
                },
                extra = {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        NexusButton(text = "Back", onClick = {})
                        NexusButton(text = "Retry", onClick = {}, type = NexusType.Primary)
                    }
                },
            )
        }
    }
}

@Composable
fun AvatarDemo() {
    DemoPage(title = "Avatar", description = "Avatars can be used to represent people or objects. It supports images, icons, or characters.") {
        DemoSection("Basic usage") {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.CenterVertically) {
                NexusAvatar(size = 32.dp, shape = AvatarShape.Circle, alt = "A")
                NexusAvatar(size = 40.dp, shape = AvatarShape.Square, alt = "B")
                NexusAvatar(size = 56.dp, shape = AvatarShape.Circle, alt = "C")
            }
        }
        DemoSection("Types") {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.CenterVertically) {
                NexusAvatar(src = "https://example.com/avatar.png", alt = "img")
                NexusAvatar(icon = { NexusText(text = "👤") })
                NexusAvatar(alt = "LX")
            }
        }
        DemoSection("Fallback") {
            var log by remember { mutableStateOf("No error") }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                NexusAvatar(
                    src = "error://broken-image",
                    alt = "FB",
                    onError = {
                        log = "image load error -> fallback content"
                    },
                ) {
                    NexusText(text = "FB")
                }
                NexusText(
                    text = log,
                    style = NexusTheme.typography.extraSmall,
                    color = NexusTheme.colorScheme.text.secondary,
                )
            }
        }
        DemoSection("Fit Container") {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                NexusAvatar(src = "img://fill", fit = AvatarFit.Fill, alt = "FILL")
                NexusAvatar(src = "img://contain", fit = AvatarFit.Contain, alt = "CON")
                NexusAvatar(src = "img://cover", fit = AvatarFit.Cover, alt = "COV")
                NexusAvatar(src = "img://none", fit = AvatarFit.None, alt = "NONE")
                NexusAvatar(src = "img://scale", fit = AvatarFit.ScaleDown, alt = "SD")
            }
        }
        DemoSection("Avatar Group") {
            NexusAvatarGroup(
                items = listOf(
                    AvatarGroupItem(alt = "A", text = "A"),
                    AvatarGroupItem(alt = "B", text = "B"),
                    AvatarGroupItem(alt = "C", text = "C"),
                    AvatarGroupItem(alt = "D", text = "D"),
                ),
                size = 36.dp,
                shape = AvatarShape.Circle,
                collapseAvatars = true,
                collapseAvatarsTooltip = true,
                maxCollapseAvatars = 2,
            )
        }
    }
}

@Composable
fun BadgeDemo() {
    DemoPage(title = "Badge", description = "A number or status mark on buttons and icons.") {
        DemoSection("Basic Usage") {
            Row(horizontalArrangement = Arrangement.spacedBy(18.dp), verticalAlignment = Alignment.CenterVertically) {
                NexusBadge(value = 12) {
                    NexusButton(text = "Comments", onClick = {}, size = io.github.xingray.compose_nexus.theme.ComponentSize.Small)
                }
                NexusBadge(value = "new") {
                    NexusButton(text = "Replies", onClick = {}, size = io.github.xingray.compose_nexus.theme.ComponentSize.Small)
                }
            }
        }

        DemoSection("Max Value") {
            Row(horizontalArrangement = Arrangement.spacedBy(18.dp), verticalAlignment = Alignment.CenterVertically) {
                NexusBadge(value = 9, max = 99) { NexusTag(text = "9", size = io.github.xingray.compose_nexus.theme.ComponentSize.Small) }
                NexusBadge(value = 120, max = 99) { NexusTag(text = "120", size = io.github.xingray.compose_nexus.theme.ComponentSize.Small) }
            }
        }

        DemoSection("Customizations") {
            Row(horizontalArrangement = Arrangement.spacedBy(18.dp), verticalAlignment = Alignment.CenterVertically) {
                NexusBadge(value = "HOT", type = NexusType.Warning) {
                    NexusText(text = "Topic A")
                }
                NexusBadge(value = 7, color = NexusTheme.colorScheme.success.base, contentSlot = { v ->
                    NexusText(text = "No.$v", color = NexusTheme.colorScheme.white, style = NexusTheme.typography.extraSmall)
                }) {
                    NexusText(text = "Ranking")
                }
            }
        }

        DemoSection("Red Dot") {
            Row(horizontalArrangement = Arrangement.spacedBy(18.dp), verticalAlignment = Alignment.CenterVertically) {
                NexusBadge(isDot = true) { NexusText(text = "Node status") }
                NexusBadge(value = 0, showZero = false, isDot = true) { NexusText(text = "Hidden zero") }
            }
        }

        DemoSection("Offset") {
            Row(horizontalArrangement = Arrangement.spacedBy(18.dp), verticalAlignment = Alignment.CenterVertically) {
                NexusBadge(value = 8, offset = -6 to 6) {
                    NexusButton(text = "Inbox", onClick = {}, size = io.github.xingray.compose_nexus.theme.ComponentSize.Small)
                }
                NexusBadge(value = 20, offset = 8 to -4, type = NexusType.Success) {
                    NexusButton(text = "Tasks", onClick = {}, size = io.github.xingray.compose_nexus.theme.ComponentSize.Small)
                }
            }
        }
    }
}

@Composable
fun SkeletonDemo() {
    DemoPage(title = "Skeleton", description = "When loading data, show a loading placeholder.") {
        DemoSection("Basic usage") {
            NexusSkeleton(loading = true)
        }

        DemoSection("Configurable Rows") {
            NexusSkeleton(loading = true, rows = 5)
        }

        DemoSection("Animation") {
            NexusSkeleton(loading = true, animated = true, rows = 3, avatar = true)
            Spacer(modifier = Modifier.height(8.dp))
            NexusSkeleton(loading = true, animated = false, rows = 3, avatar = true)
        }

        DemoSection("Customized Template") {
            NexusSkeleton(
                loading = true,
                template = {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        NexusSkeletonItem(variant = SkeletonVariant.Circle)
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                            NexusSkeletonItem(variant = SkeletonVariant.H1)
                            NexusSkeletonItem(variant = SkeletonVariant.Text)
                            NexusSkeletonItem(variant = SkeletonVariant.Caption)
                        }
                    }
                },
            )
        }

        DemoSection("Loading state") {
            var loading by remember { mutableStateOf(true) }
            NexusButton(
                text = if (loading) "Show Content" else "Show Skeleton",
                onClick = { loading = !loading },
                size = io.github.xingray.compose_nexus.theme.ComponentSize.Small,
            )
            Spacer(modifier = Modifier.height(8.dp))
            NexusSkeleton(
                loading = loading,
                rows = 2,
                avatar = true,
                content = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(NexusTheme.colorScheme.fill.light, NexusTheme.shapes.base)
                            .padding(12.dp),
                    ) {
                        NexusText(text = "This is real content after loading completes.")
                    }
                },
            )
        }

        DemoSection("Rendering a list of data") {
            NexusSkeleton(
                loading = true,
                count = 3,
                rows = 2,
                avatar = true,
            )
        }

        DemoSection("Avoiding rendering bouncing") {
            var loading by remember { mutableStateOf(false) }
            NexusButton(
                text = "Toggle Loading Quickly",
                onClick = { loading = !loading },
                size = io.github.xingray.compose_nexus.theme.ComponentSize.Small,
                type = NexusType.Primary,
            )
            Spacer(modifier = Modifier.height(8.dp))
            NexusSkeleton(
                loading = loading,
                rows = 2,
                throttleConfig = SkeletonThrottle(initVal = true, leading = 300, trailing = 300),
                content = {
                    NexusText(text = "Stable content with throttle")
                },
            )
        }
    }
}

@Composable
fun EmptyDemo() {
    DemoPage(title = "Empty", description = "Placeholder for empty states.") {
        DemoSection("Basic usage") {
            NexusEmpty()
        }

        DemoSection("Custom image") {
            Row(horizontalArrangement = Arrangement.spacedBy(24.dp), verticalAlignment = Alignment.CenterVertically) {
                NexusEmpty(
                    imageUrl = "https://example.com/empty.png",
                    description = "Image URL placeholder",
                    modifier = Modifier.weight(1f),
                )
                NexusEmpty(
                    image = {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                                .background(NexusTheme.colorScheme.primary.light8, NexusTheme.shapes.base),
                            contentAlignment = Alignment.Center,
                        ) {
                            NexusText(text = "Custom Image", color = NexusTheme.colorScheme.primary.base)
                        }
                    },
                    description = "Image slot",
                    modifier = Modifier.weight(1f),
                )
            }
        }

        DemoSection("Image size") {
            Row(horizontalArrangement = Arrangement.spacedBy(24.dp), verticalAlignment = Alignment.CenterVertically) {
                NexusEmpty(description = "Small", imageSize = 60, modifier = Modifier.weight(1f))
                NexusEmpty(description = "Default", imageSize = 96, modifier = Modifier.weight(1f))
                NexusEmpty(description = "Large", imageSize = 140, modifier = Modifier.weight(1f))
            }
        }

        DemoSection("Bottom content") {
            NexusEmpty(
                description = "No matching results",
                actions = {
                    NexusButton(onClick = {}, type = NexusType.Primary) { NexusText(text = "Refresh") }
                },
            )
        }

        DemoSection("Description slot") {
            NexusEmpty(
                imageSize = 84,
                description = "Ignored by slot",
                descriptionSlot = {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                        NexusTag(text = "CUSTOM", type = NexusType.Warning, size = io.github.xingray.compose_nexus.theme.ComponentSize.Small)
                        NexusText(text = "Customized description")
                    }
                },
            )
        }
    }
}

@Composable
fun ImageDemo() {
    val previewList = listOf(
        "img://photo-1",
        "img://photo-2",
        "img://photo-3",
    )
    DemoPage(title = "Image", description = "Support placeholder, error state, lazy load and preview.") {
        DemoSection("Basic usage") {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                NexusImage(
                    src = "img://cover",
                    fit = NexusImageFit.Cover,
                    modifier = Modifier.weight(1f).height(96.dp),
                    alt = "cover",
                )
                NexusImage(
                    src = "img://contain",
                    fit = NexusImageFit.Contain,
                    modifier = Modifier.weight(1f).height(96.dp),
                    alt = "contain",
                )
                NexusImage(
                    src = "img://fill",
                    fit = NexusImageFit.Fill,
                    modifier = Modifier.weight(1f).height(96.dp),
                    alt = "fill",
                )
            }
        }

        DemoSection("Placeholder") {
            NexusImage(
                src = "img://lazy-placeholder",
                loading = NexusImageLoading.Lazy,
                modifier = Modifier.fillMaxWidth().height(120.dp),
                placeholder = {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        NexusText(text = "Custom placeholder: click to load")
                    }
                },
            )
        }

        DemoSection("Load failed") {
            NexusImage(
                src = "error://broken",
                modifier = Modifier.fillMaxWidth().height(120.dp),
                error = {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        NexusTag(text = "Load Failed", type = NexusType.Danger)
                    }
                },
                viewerError = { activeIndex, src ->
                    NexusText(text = "Viewer error at $activeIndex: $src", color = NexusTheme.colorScheme.danger.base)
                },
            )
        }

        DemoSection("Lazy load") {
            NexusImage(
                src = "img://lazy-image",
                lazy = true,
                modifier = Modifier.fillMaxWidth().height(120.dp),
                alt = "click to trigger loading",
            )
        }

        DemoSection("Image preview") {
            NexusImage(
                src = "img://preview-entry",
                previewSrcList = previewList,
                showProgress = true,
                modifier = Modifier.fillMaxWidth().height(140.dp),
            )
        }

        DemoSection("Manually open preview") {
            val imageState = rememberNexusImageState(initialIndex = 1)
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                NexusButton(
                    text = "Open Preview",
                    onClick = { imageState.showPreview() },
                    type = NexusType.Primary,
                    size = io.github.xingray.compose_nexus.theme.ComponentSize.Small,
                )
                NexusText(text = "Open at initial index = 1", color = NexusTheme.colorScheme.text.secondary, style = NexusTheme.typography.small)
            }
            Spacer(modifier = Modifier.height(10.dp))
            NexusImage(
                src = "img://manual-open",
                state = imageState,
                previewSrcList = previewList,
                modifier = Modifier.fillMaxWidth().height(110.dp),
            )
        }

        DemoSection("Custom toolbar") {
            val toolbarState = rememberNexusImageState()
            NexusImage(
                src = "img://toolbar",
                state = toolbarState,
                previewSrcList = previewList,
                modifier = Modifier.fillMaxWidth().height(120.dp),
                toolbar = { activeIndex, total, setActiveItem, prev, next, reset, zoomIn, zoomOut ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        NexusButton(text = "Prev", onClick = prev, size = io.github.xingray.compose_nexus.theme.ComponentSize.Small)
                        NexusButton(text = "Next", onClick = next, size = io.github.xingray.compose_nexus.theme.ComponentSize.Small)
                        NexusButton(text = "Reset", onClick = reset, size = io.github.xingray.compose_nexus.theme.ComponentSize.Small)
                        NexusButton(text = "+", onClick = zoomIn, size = io.github.xingray.compose_nexus.theme.ComponentSize.Small)
                        NexusButton(text = "-", onClick = zoomOut, size = io.github.xingray.compose_nexus.theme.ComponentSize.Small)
                        NexusButton(
                            text = "First",
                            onClick = { setActiveItem(0) },
                            size = io.github.xingray.compose_nexus.theme.ComponentSize.Small,
                        )
                        NexusText(
                            text = "${activeIndex + 1}/$total",
                            color = NexusTheme.colorScheme.white,
                            style = NexusTheme.typography.small,
                        )
                    }
                },
            )
        }

        DemoSection("Custom progress") {
            NexusImage(
                src = "img://custom-progress",
                previewSrcList = previewList,
                showProgress = false,
                modifier = Modifier.fillMaxWidth().height(120.dp),
                progress = { activeIndex, total ->
                    NexusTag(
                        text = "Slide ${activeIndex + 1} of $total",
                        type = NexusType.Success,
                    )
                },
            )
        }
    }
}

@Composable
fun InfiniteScrollDemo() {
    DemoPage(title = "Infinite Scroll", description = "Load more data while reach bottom of the page.") {
        DemoSection("Basic usage") {
            val list = remember { mutableStateListOf<Int>().apply { addAll(1..20) } }
            val state = rememberInfiniteScrollState()
            NexusInfiniteScroll(
                items = list,
                state = state,
                modifier = Modifier.fillMaxWidth().height(260.dp),
                infiniteScrollDistance = 8,
                onLoadMore = {
                    if (list.size >= 60) {
                        state.hasMore = false
                    } else {
                        val nextStart = list.size + 1
                        list.addAll(nextStart until (nextStart + 10))
                    }
                },
            ) { _, item ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                        .background(NexusTheme.colorScheme.fill.light, NexusTheme.shapes.base)
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                ) {
                    NexusText(text = "List Item $item")
                }
            }
        }

        DemoSection("Disable Loading") {
            val list = remember { mutableStateListOf<Int>().apply { addAll(1..12) } }
            val state = rememberInfiniteScrollState()
            var disabled by remember { mutableStateOf(false) }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                NexusButton(
                    text = if (disabled) "Enable Loading" else "Disable Loading",
                    onClick = { disabled = !disabled },
                    size = io.github.xingray.compose_nexus.theme.ComponentSize.Small,
                    type = NexusType.Primary,
                )
                NexusText(
                    text = if (disabled) "disabled=true" else "disabled=false",
                    style = NexusTheme.typography.small,
                    color = NexusTheme.colorScheme.text.secondary,
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            NexusInfiniteScroll(
                items = list,
                state = state,
                modifier = Modifier.fillMaxWidth().height(220.dp),
                infiniteScrollDisabled = disabled,
                infiniteScrollDelay = 300L,
                infiniteScrollImmediate = true,
                onLoadMore = {
                    if (list.size >= 40) {
                        state.hasMore = false
                    } else {
                        val nextStart = list.size + 1
                        list.addAll(nextStart until (nextStart + 8))
                    }
                },
            ) { _, item ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                        .background(NexusTheme.colorScheme.fill.light, NexusTheme.shapes.base)
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                ) {
                    NexusText(text = "Data #$item")
                }
            }
        }
    }
}

@Composable
fun TimelineDemo() {
    val records = listOf(
        "Create a services site 2015-09-01",
        "Solve initial network problems 2015-09-01",
        "Technical testing 2015-09-01",
        "Online operation 2015-09-01",
    )
    val warningColor = NexusTheme.colorScheme.warning.base

    fun TimelineScope.defaultItems(
        center: Boolean = false,
        placement: TimelinePlacement = TimelinePlacement.Bottom,
        hideLastTimestamp: Boolean = false,
    ) {
        records.forEachIndexed { index, text ->
            item(
                timestamp = "2018/4/${12 - index}",
                center = center,
                placement = placement,
                hideTimestamp = hideLastTimestamp && index == records.lastIndex,
            ) {
                NexusText(text = text)
            }
        }
    }

    DemoPage(title = "Timeline", description = "Visually display timeline.") {
        DemoSection("Basic usage") {
            NexusTimeline {
                defaultItems()
            }
        }

        DemoSection("Mode") {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                NexusText(text = "start", color = NexusTheme.colorScheme.text.secondary, style = NexusTheme.typography.small)
                NexusTimeline(mode = TimelineMode.Start) { defaultItems() }
                NexusText(text = "alternate", color = NexusTheme.colorScheme.text.secondary, style = NexusTheme.typography.small)
                NexusTimeline(mode = TimelineMode.Alternate) { defaultItems() }
                NexusText(text = "alternate-reverse", color = NexusTheme.colorScheme.text.secondary, style = NexusTheme.typography.small)
                NexusTimeline(mode = TimelineMode.AlternateReverse) { defaultItems() }
                NexusText(text = "end", color = NexusTheme.colorScheme.text.secondary, style = NexusTheme.typography.small)
                NexusTimeline(mode = TimelineMode.End) { defaultItems() }
            }
        }

        DemoSection("Custom node") {
            NexusTimeline {
                item(
                    timestamp = "2018/4/12",
                    type = NexusType.Primary,
                    size = TimelineItemSize.Large,
                    icon = {
                        NexusText(
                            text = "P",
                            style = NexusTheme.typography.extraSmall,
                            color = NexusTheme.colorScheme.white,
                        )
                    },
                ) {
                    NexusText(text = "Large primary node with icon")
                }
                item(
                    timestamp = "2018/4/11",
                    color = warningColor,
                    hollow = true,
                ) {
                    NexusText(text = "Custom color + hollow node")
                }
                item(
                    timestamp = "2018/4/10",
                    type = NexusType.Success,
                    dot = {
                        NexusTag(
                            text = "NEW",
                            type = NexusType.Success,
                            size = io.github.xingray.compose_nexus.theme.ComponentSize.Small,
                        )
                    },
                ) {
                    NexusText(text = "Custom dot slot")
                }
                item(
                    timestamp = "2018/4/09",
                    type = NexusType.Danger,
                ) {
                    NexusText(text = "Danger status node")
                }
            }
        }

        DemoSection("Custom timestamp") {
            NexusTimeline {
                item(
                    timestamp = "2018/4/12",
                    placement = TimelinePlacement.Top,
                ) {
                    NexusText(text = "Timestamp on top for long content block.")
                    NexusText(
                        text = "When content is higher, top placement keeps timestamp readable.",
                        color = NexusTheme.colorScheme.text.secondary,
                        style = NexusTheme.typography.small,
                    )
                }
                item(
                    timestamp = "2018/4/11",
                    placement = TimelinePlacement.Top,
                ) {
                    NexusText(text = "Another top timestamp item.")
                }
                item(
                    timestamp = "2018/4/10",
                    hideTimestamp = true,
                ) {
                    NexusText(text = "Hidden timestamp item")
                }
            }
        }

        DemoSection("Vertically centered") {
            NexusTimeline {
                defaultItems(center = true, hideLastTimestamp = true)
            }
        }

        DemoSection("Reverse") {
            var reverse by remember { mutableStateOf(true) }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                NexusButton(
                    text = if (reverse) "Disable Reverse" else "Enable Reverse",
                    onClick = { reverse = !reverse },
                    size = io.github.xingray.compose_nexus.theme.ComponentSize.Small,
                    type = NexusType.Primary,
                )
                NexusText(
                    text = "reverse=$reverse",
                    style = NexusTheme.typography.small,
                    color = NexusTheme.colorScheme.text.secondary,
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            NexusTimeline(reverse = reverse) {
                defaultItems()
            }
        }
    }
}

@Composable
fun CalendarDemo() {
    DemoPage(title = "Calendar", description = "Display date information.") {
        DemoSection("Basic") {
            var value by remember { mutableStateOf(NexusDate(2026, 3, 10)) }
            val state = rememberCalendarState(initialYear = value.year, initialMonth = value.month)
            NexusCalendar(
                state = state,
                modelValue = value,
                onModelValueChange = { value = it },
            )
        }

        DemoSection("Controller Type") {
            val state = rememberCalendarState(initialYear = 2026, initialMonth = 6)
            NexusCalendar(
                state = state,
                controllerType = CalendarControllerType.Select,
                formatter = { v, type ->
                    when (type) {
                        CalendarFormatterType.Year -> "${v}Y"
                        CalendarFormatterType.Month -> "${v.toString().padStart(2, '0')}M"
                    }
                },
            )
        }

        DemoSection("Custom Date Cell") {
            val state = rememberCalendarState(initialYear = 2026, initialMonth = 3)
            NexusCalendar(
                state = state,
                dateCellSlot = { data ->
                    NexusText(
                        text = "${data.date.day}\n${data.type.value}",
                        style = NexusTheme.typography.extraSmall,
                        color = if (data.isSelected) NexusTheme.colorScheme.primary.base else NexusTheme.colorScheme.text.secondary,
                    )
                },
            )
        }

        DemoSection("Range") {
            val state = rememberCalendarState(initialYear = 2026, initialMonth = 3)
            NexusCalendar(
                state = state,
                range = NexusDate(2026, 2, 23) to NexusDate(2026, 3, 29),
            )
        }

        DemoSection("Custom Header") {
            val state = rememberCalendarState(initialYear = 2026, initialMonth = 8)
            NexusCalendar(
                state = state,
                header = { date ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        NexusText(text = date, style = NexusTheme.typography.base)
                        NexusButton(
                            text = "Prev",
                            onClick = { state.selectDate(CalendarDateType.PrevMonth) },
                            size = io.github.xingray.compose_nexus.theme.ComponentSize.Small,
                        )
                        NexusButton(
                            text = "Today",
                            onClick = { state.selectDate(CalendarDateType.Today) },
                            size = io.github.xingray.compose_nexus.theme.ComponentSize.Small,
                        )
                        NexusButton(
                            text = "Next",
                            onClick = { state.selectDate(CalendarDateType.NextMonth) },
                            size = io.github.xingray.compose_nexus.theme.ComponentSize.Small,
                        )
                    }
                },
            )
        }
    }
}

@Composable
fun DescriptionsDemo() {
    DemoPage(title = "Descriptions", description = "Display multiple fields in list form.") {
        DemoSection("Basic usage") {
            NexusDescriptions(
                title = "User Info",
                extra = "Details",
                column = 3,
            ) {
                item(label = "Username") { NexusText(text = "xingray") }
                item(label = "Telephone") { NexusText(text = "18100000000") }
                item(label = "Place") { NexusText(text = "Shanghai") }
                item(label = "Address", span = 2) { NexusText(text = "No. 189, Grove St, Los Angeles") }
                item(label = "Remark") { NexusText(text = "Long-term user") }
            }
        }

        DemoSection("Sizes") {
            NexusDescriptions(
                border = true,
                size = io.github.xingray.compose_nexus.theme.ComponentSize.Large,
                column = 2,
                title = "Large",
            ) {
                item(label = "Status") { NexusTag(text = "Online", type = NexusType.Success) }
                item(label = "Role") { NexusText(text = "Admin") }
                item(label = "Company", span = 2) { NexusText(text = "Compose Nexus") }
            }
            Spacer(modifier = Modifier.height(12.dp))
            NexusDescriptions(
                border = true,
                size = io.github.xingray.compose_nexus.theme.ComponentSize.Small,
                column = 2,
                title = "Small",
            ) {
                item(label = "Status") { NexusTag(text = "Idle", type = NexusType.Warning, size = io.github.xingray.compose_nexus.theme.ComponentSize.Small) }
                item(label = "Role") { NexusText(text = "Viewer") }
                item(label = "Company", span = 2) { NexusText(text = "Compose Nexus") }
            }
        }

        DemoSection("Vertical List") {
            NexusDescriptions(
                border = true,
                column = 3,
                direction = DescriptionsDirection.Vertical,
                title = "Vertical",
            ) {
                item(label = "Username") { NexusText(text = "nexus_user") }
                item(label = "Phone") { NexusText(text = "19900000000") }
                item(label = "City") { NexusText(text = "Hangzhou") }
            }
        }

        DemoSection("Rowspan") {
            NexusDescriptions(
                border = true,
                column = 3,
                title = "Rowspan sample",
            ) {
                item(label = "Name", rowspan = 2) { NexusText(text = "Rowspan 2") }
                item(label = "Tag") { NexusTag(text = "A", size = io.github.xingray.compose_nexus.theme.ComponentSize.Small) }
                item(label = "Tag") { NexusTag(text = "B", size = io.github.xingray.compose_nexus.theme.ComponentSize.Small) }
                item(label = "Memo", span = 2) { NexusText(text = "This row demonstrates large cell height.") }
            }
        }

        DemoSection("Customized Style") {
            NexusDescriptions(
                border = true,
                column = 2,
                labelWidth = 100.dp,
                title = "Custom style",
                extraSlot = {
                    NexusButton(
                        text = "Edit",
                        onClick = {},
                        size = io.github.xingray.compose_nexus.theme.ComponentSize.Small,
                        type = NexusType.Primary,
                    )
                },
            ) {
                item(
                    label = "Progress",
                    align = DescriptionsAlign.Right,
                    labelSlot = {
                        NexusText(
                            text = "Progress",
                            color = NexusTheme.colorScheme.primary.base,
                            style = NexusTheme.typography.small,
                        )
                    },
                ) {
                    NexusProgress(percentage = 72f, status = NexusType.Warning)
                }
                item(label = "Description", span = 2) {
                    NexusText(
                        text = "Custom title/extra slots, label width and align are all supported.",
                        color = NexusTheme.colorScheme.text.secondary,
                    )
                }
            }
        }
    }
}
