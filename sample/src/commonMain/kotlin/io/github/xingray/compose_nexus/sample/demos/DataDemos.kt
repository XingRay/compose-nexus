package io.github.xingray.compose_nexus.sample.demos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.xingray.compose_nexus.controls.*
import io.github.xingray.compose_nexus.theme.NexusTheme
import io.github.xingray.compose_nexus.theme.NexusType

@Composable
fun TableDemo() {
    DemoPage(title = "Table", description = "For displaying multiple similar data sets.") {
        DemoSection("Basic table") {
            data class User(val name: String, val age: Int, val city: String)
            val data = listOf(
                User("Alice", 28, "New York"),
                User("Bob", 32, "London"),
                User("Charlie", 25, "Tokyo"),
                User("Diana", 30, "Paris"),
            )
            NexusTable(
                data = data,
                columns = listOf(
                    NexusTableColumn("Name") { NexusText(text = it.name) },
                    NexusTableColumn("Age") { NexusText(text = it.age.toString()) },
                    NexusTableColumn("City") { NexusText(text = it.city) },
                ),
                stripe = true,
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
fun TreeDemo() {
    DemoPage(title = "Tree", description = "Display a set of data with hierarchies.") {
        DemoSection("Basic usage") {
            val nodes = listOf(
                TreeNode("1", "Level 1 - Node 1", listOf(
                    TreeNode("1-1", "Level 2 - Node 1"),
                    TreeNode("1-2", "Level 2 - Node 2"),
                )),
                TreeNode("2", "Level 1 - Node 2", listOf(
                    TreeNode("2-1", "Level 2 - Node 1", listOf(
                        TreeNode("2-1-1", "Level 3 - Node 1"),
                    )),
                )),
                TreeNode("3", "Level 1 - Node 3"),
            )
            NexusTree(nodes = nodes, defaultExpandAll = true)
        }
    }
}

@Composable
fun PaginationDemo() {
    DemoPage(title = "Pagination", description = "Divide data into pages when there's too much to show.") {
        DemoSection("Basic usage") {
            val state = rememberPaginationState(pageCount = 20)
            NexusPagination(state = state)
        }
        DemoSection("Small size") {
            val state = rememberPaginationState(pageCount = 10, initialPage = 3)
            NexusPagination(state = state, size = io.github.xingray.compose_nexus.theme.ComponentSize.Small)
        }
    }
}

@Composable
fun ProgressDemo() {
    DemoPage(title = "Progress", description = "Progress is used to show the progress of current operation.") {
        DemoSection("Line progress") {
            NexusProgress(percentage = 70f)
        }
        DemoSection("Type colors") {
            NexusProgress(percentage = 100f, status = NexusType.Success)
            NexusProgress(percentage = 80f, status = NexusType.Warning)
            NexusProgress(percentage = 50f, status = NexusType.Danger)
        }
        DemoSection("Circle progress") {
            Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                NexusProgress(percentage = 75f, type = ProgressType.Circle)
                NexusProgress(percentage = 100f, type = ProgressType.Circle, status = NexusType.Success)
                NexusProgress(percentage = 50f, type = ProgressType.Circle, status = NexusType.Danger)
            }
        }
    }
}

@Composable
fun AvatarDemo() {
    DemoPage(title = "Avatar", description = "Avatars can be used to represent people or objects.") {
        DemoSection("Basic usage") {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.CenterVertically) {
                NexusAvatar(size = 32.dp) { NexusText(text = "A") }
                NexusAvatar(size = 40.dp) { NexusText(text = "B") }
                NexusAvatar(size = 56.dp) { NexusText(text = "C") }
            }
        }
        DemoSection("Shape") {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                NexusAvatar(shape = AvatarShape.Circle) { NexusText(text = "C") }
                NexusAvatar(shape = AvatarShape.Square) { NexusText(text = "S") }
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
        DemoSection("With avatar") {
            NexusSkeleton(loading = true, avatar = true, rows = 2)
        }
        DemoSection("Not animated") {
            NexusSkeleton(loading = true, animated = false, rows = 2)
        }
    }
}

@Composable
fun EmptyDemo() {
    DemoPage(title = "Empty", description = "Placeholder for empty states.") {
        DemoSection("Basic usage") {
            NexusEmpty()
        }
        DemoSection("Custom description & actions") {
            NexusEmpty(
                description = "No matching results",
                actions = {
                    NexusButton(onClick = {}, type = NexusType.Primary) { NexusText(text = "Refresh") }
                },
            )
        }
    }
}

@Composable
fun CalendarDemo() {
    DemoPage(title = "Calendar", description = "Display date information.") {
        DemoSection("Basic usage") {
            val state = rememberCalendarState(initialYear = 2026, initialMonth = 3)
            NexusCalendar(state = state)
        }
    }
}
