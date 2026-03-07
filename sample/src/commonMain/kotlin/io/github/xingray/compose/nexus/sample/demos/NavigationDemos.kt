package io.github.xingray.compose.nexus.sample.demos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import io.github.xingray.compose.nexus.controls.AffixPosition
import io.github.xingray.compose.nexus.controls.AnchorDirection
import io.github.xingray.compose.nexus.controls.AnchorLink
import io.github.xingray.compose.nexus.controls.AnchorType
import io.github.xingray.compose.nexus.controls.DropdownPlacement
import io.github.xingray.compose.nexus.controls.DropdownTrigger
import io.github.xingray.compose.nexus.controls.MenuItem
import io.github.xingray.compose.nexus.controls.MenuMode
import io.github.xingray.compose.nexus.controls.MenuTrigger
import io.github.xingray.compose.nexus.controls.NexusAffix
import io.github.xingray.compose.nexus.controls.NexusAnchor
import io.github.xingray.compose.nexus.controls.NexusBacktop
import io.github.xingray.compose.nexus.controls.NexusBreadcrumb
import io.github.xingray.compose.nexus.controls.NexusBreadcrumbItem
import io.github.xingray.compose.nexus.controls.NexusButton
import io.github.xingray.compose.nexus.controls.NexusDropdown
import io.github.xingray.compose.nexus.controls.NexusDropdownItem
import io.github.xingray.compose.nexus.controls.NexusMenu
import io.github.xingray.compose.nexus.controls.NexusPageHeader
import io.github.xingray.compose.nexus.controls.NexusSteps
import io.github.xingray.compose.nexus.controls.NexusTabs
import io.github.xingray.compose.nexus.controls.NexusTag
import io.github.xingray.compose.nexus.controls.NexusText
import io.github.xingray.compose.nexus.controls.StepItem
import io.github.xingray.compose.nexus.controls.StepStatus
import io.github.xingray.compose.nexus.controls.StepsDirection
import io.github.xingray.compose.nexus.controls.TabItem
import io.github.xingray.compose.nexus.controls.TabsPosition
import io.github.xingray.compose.nexus.controls.TabsType
import io.github.xingray.compose.nexus.controls.rememberAnchorState
import io.github.xingray.compose.nexus.controls.rememberDropdownState
import io.github.xingray.compose.nexus.controls.rememberMenuState
import io.github.xingray.compose.nexus.controls.rememberStepsState
import io.github.xingray.compose.nexus.controls.rememberTabsState
import io.github.xingray.compose.nexus.theme.ComponentSize
import io.github.xingray.compose.nexus.theme.NexusTheme
import io.github.xingray.compose.nexus.theme.NexusType
import kotlinx.coroutines.launch

@Composable
fun BacktopDemo() {
    DemoPage(title = "Backtop", description = "A button to back to top.") {
        DemoSection("Basic usage") {
            val scrollState = rememberScrollState()
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .background(NexusTheme.colorScheme.fill.light, NexusTheme.shapes.base)
                    .padding(12.dp)
                    .verticalScroll(scrollState),
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    repeat(24) { index ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(NexusTheme.colorScheme.fill.blank, NexusTheme.shapes.base)
                                .padding(10.dp),
                        ) {
                            NexusText(text = "Scroll item ${index + 1}")
                        }
                    }
                }
                NexusBacktop(
                    scrollState = scrollState,
                    visibilityHeight = 120,
                    right = 18,
                    bottom = 18,
                )
            }
        }

        DemoSection("Customizations") {
            val scrollState = rememberScrollState()
            var clickedCount by remember { mutableStateOf(0) }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .background(NexusTheme.colorScheme.fill.light, NexusTheme.shapes.base)
                    .padding(12.dp)
                    .verticalScroll(scrollState),
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    repeat(26) { index ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(NexusTheme.colorScheme.fill.blank, NexusTheme.shapes.base)
                                .padding(10.dp),
                        ) {
                            NexusText(text = "Custom block ${index + 1}")
                        }
                    }
                }
                NexusBacktop(
                    scrollState = scrollState,
                    visibilityHeight = 80,
                    right = 24,
                    bottom = 24,
                    onClick = { clickedCount++ },
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                NexusTheme.colorScheme.primary.base,
                                NexusTheme.shapes.base
                            )
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        NexusText(
                            text = "TOP",
                            color = NexusTheme.colorScheme.white,
                            style = NexusTheme.typography.small,
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            NexusText(
                text = "click count = $clickedCount",
                style = NexusTheme.typography.small,
                color = NexusTheme.colorScheme.text.secondary,
            )
        }
    }
}

@Composable
fun AffixDemo() {
    DemoPage(title = "Affix", description = "Fix the element to a specific visible area.") {
        DemoSection("Basic usage") {
            val scrollState = rememberScrollState()
            var fixedState by remember { mutableStateOf(false) }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .background(NexusTheme.colorScheme.fill.light, NexusTheme.shapes.base)
                    .padding(12.dp)
                    .verticalScroll(scrollState),
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    NexusAffix(
                        scrollState = scrollState,
                        offset = 12.dp,
                        onChange = { fixedState = it },
                    ) {
                        NexusButton(
                            text = "Affix Top",
                            onClick = {},
                            type = NexusType.Primary
                        )
                    }
                    repeat(18) { index ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(NexusTheme.colorScheme.fill.blank, NexusTheme.shapes.base)
                                .padding(10.dp),
                        ) {
                            NexusText(text = "List item ${index + 1}")
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            NexusText(
                text = "fixed = $fixedState",
                style = NexusTheme.typography.small,
                color = NexusTheme.colorScheme.text.secondary,
            )
        }

        DemoSection("Fixed position") {
            val scrollState = rememberScrollState()
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .background(NexusTheme.colorScheme.fill.light, NexusTheme.shapes.base)
                    .padding(12.dp)
                    .verticalScroll(scrollState),
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    repeat(14) { index ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(NexusTheme.colorScheme.fill.blank, NexusTheme.shapes.base)
                                .padding(10.dp),
                        ) {
                            NexusText(text = "Scrollable content ${index + 1}")
                        }
                    }
                    NexusAffix(
                        scrollState = scrollState,
                        offset = 8.dp,
                        position = AffixPosition.Bottom,
                    ) {
                        NexusButton(
                            text = "Affix Bottom",
                            onClick = {},
                            type = NexusType.Warning
                        )
                    }
                    repeat(4) { index ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(NexusTheme.colorScheme.fill.blank, NexusTheme.shapes.base)
                                .padding(10.dp),
                        ) {
                            NexusText(text = "Tail content ${index + 1}")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AnchorDemo() {
    val links = remember {
        listOf(
            AnchorLink(
                title = "Part 1",
                href = "#part-1",
                subLinks = listOf(
                    AnchorLink("Part 1-1", "#part-1-1"),
                    AnchorLink("Part 1-2", "#part-1-2"),
                ),
            ),
            AnchorLink("Part 2", "#part-2"),
            AnchorLink("Part 3", "#part-3"),
        )
    }

    DemoPage(title = "Anchor", description = "Quickly navigate to content sections on the current page.") {
        DemoSection("Basic usage") {
            val state = rememberAnchorState(initialHref = "#part-1")
            NexusAnchor(
                links = links,
                state = state,
                onChange = {},
            )
        }

        DemoSection("Horizontal mode") {
            val state = rememberAnchorState(initialHref = "#part-2")
            NexusAnchor(
                links = links,
                state = state,
                direction = AnchorDirection.Horizontal,
                type = AnchorType.Underline,
            )
        }

        DemoSection("Scroll container") {
            val state = rememberAnchorState(initialHref = "#section-1")
            val scrollState = rememberScrollState()
            val scope = rememberCoroutineScope()
            val sectionIds = listOf("#section-1", "#section-2", "#section-3", "#section-4")
            val sectionLinks = sectionIds.mapIndexed { index, id -> AnchorLink("Section ${index + 1}", id) }
            val sectionOffsetMap = sectionIds.mapIndexed { index, id -> id to (index * 170) }.toMap()

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Box(
                    modifier = Modifier
                        .width(180.dp)
                        .background(NexusTheme.colorScheme.fill.blank, NexusTheme.shapes.base)
                        .padding(8.dp),
                ) {
                    NexusAnchor(
                        links = sectionLinks,
                        state = state,
                        onRequestScroll = { href ->
                            val target = sectionOffsetMap[href] ?: 0
                            scope.launch {
                                scrollState.animateScrollTo(target)
                            }
                        },
                    )
                }

                Box(
                    modifier = Modifier
                        .width(420.dp)
                        .height(280.dp)
                        .background(NexusTheme.colorScheme.fill.light, NexusTheme.shapes.base)
                        .padding(8.dp)
                        .verticalScroll(scrollState),
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        sectionIds.forEachIndexed { index, id ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(160.dp)
                                    .background(NexusTheme.colorScheme.fill.blank, NexusTheme.shapes.base)
                                    .padding(12.dp),
                            ) {
                                NexusText(text = "Content ${index + 1} ($id)")
                            }
                        }
                    }
                }
            }
        }

        DemoSection("Anchor link change") {
            val state = rememberAnchorState()
            var current by remember { mutableStateOf("-") }
            NexusAnchor(
                links = links,
                state = state,
                onChange = { href -> current = href },
            )
            Spacer(modifier = Modifier.height(8.dp))
            NexusText(
                text = "current link = $current",
                style = NexusTheme.typography.small,
                color = NexusTheme.colorScheme.text.secondary,
            )
        }

        DemoSection("Underline type") {
            val state = rememberAnchorState(initialHref = "#part-3")
            NexusAnchor(
                links = links,
                state = state,
                type = AnchorType.Underline,
                marker = true,
            )
        }

        DemoSection("Affix mode") {
            val scrollState = rememberScrollState()
            val state = rememberAnchorState(initialHref = "#part-1")
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .background(NexusTheme.colorScheme.fill.light, NexusTheme.shapes.base)
                    .padding(8.dp)
                    .verticalScroll(scrollState),
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    NexusAffix(
                        scrollState = scrollState,
                        offset = 8.dp,
                    ) {
                        Box(
                            modifier = Modifier
                                .width(220.dp)
                                .background(
                                    NexusTheme.colorScheme.fill.blank,
                                    NexusTheme.shapes.base
                                )
                                .padding(8.dp),
                        ) {
                            NexusAnchor(
                                links = links,
                                state = state,
                            )
                        }
                    }
                    repeat(14) { index ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(NexusTheme.colorScheme.fill.blank, NexusTheme.shapes.base)
                                .padding(10.dp),
                        ) {
                            NexusText(text = "Scrollable block ${index + 1}")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MenuDemo() {
    DemoPage(title = "Menu", description = "Menu provides navigation for your website.") {
        DemoSection("Top bar") {
            val items = listOf(
                MenuItem("home", "Home"),
                MenuItem(
                    "workspace", "Workspace", children = listOf(
                        MenuItem("workspace-team", "Team"),
                        MenuItem("workspace-project", "Projects"),
                        MenuItem("workspace-reports", "Reports"),
                    )
                ),
                MenuItem("docs", "Docs"),
                MenuItem("settings", "Settings"),
            )
            val state = rememberMenuState(initialActiveKey = "home")
            NexusMenu(
                items = items,
                state = state,
                mode = MenuMode.Horizontal,
                menuTrigger = MenuTrigger.Hover,
                onSelect = { index, _ ->
                    state.updateActiveIndex(index)
                },
            )
        }

        DemoSection("Left and right") {
            val items = listOf(
                MenuItem("left-home", "Home"),
                MenuItem("left-news", "News"),
                MenuItem("left-docs", "Docs"),
                MenuItem("right-login", "Login"),
                MenuItem("right-user", "User"),
            )
            val state = rememberMenuState(initialActiveKey = "left-home")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(NexusTheme.colorScheme.fill.blank, NexusTheme.shapes.base)
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                NexusMenu(
                    items = items.take(3),
                    state = state,
                    mode = MenuMode.Horizontal,
                    modifier = Modifier.weight(1f),
                )
                NexusMenu(
                    items = items.takeLast(2),
                    state = state,
                    mode = MenuMode.Horizontal,
                    modifier = Modifier.width(220.dp),
                )
            }
        }

        DemoSection("Side bar") {
            val items = listOf(
                MenuItem(
                    "guide", "Guide", children = listOf(
                        MenuItem("guide-install", "Install"),
                        MenuItem("guide-start", "Quick Start"),
                    )
                ),
                MenuItem(
                    "component", "Component", children = listOf(
                        MenuItem("component-basic", "Basic"),
                        MenuItem("component-form", "Form"),
                        MenuItem("component-data", "Data"),
                    )
                ),
                MenuItem("resources", "Resources"),
            )
            val state = rememberMenuState(initialActiveKey = "guide-install", defaultOpeneds = listOf("guide"))
            NexusMenu(
                items = items,
                state = state,
                mode = MenuMode.Vertical,
                defaultOpeneds = listOf("guide"),
                uniqueOpened = true,
            )
        }

        DemoSection("Collapse") {
            val items = listOf(
                MenuItem("dashboard", "Dashboard"),
                MenuItem(
                    "monitor", "Monitor", children = listOf(
                        MenuItem("monitor-log", "Logs"),
                        MenuItem("monitor-metrics", "Metrics"),
                    )
                ),
                MenuItem(
                    "system", "System", children = listOf(
                        MenuItem("system-user", "User"),
                        MenuItem("system-role", "Role"),
                    )
                ),
            )
            val state = rememberMenuState(initialActiveKey = "dashboard")
            var collapse by remember { mutableStateOf(false) }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                NexusButton(
                    text = if (collapse) "Expand" else "Collapse",
                    onClick = { collapse = !collapse },
                    size = ComponentSize.Small,
                    type = NexusType.Primary,
                )
                NexusText(
                    text = "collapse=$collapse",
                    style = NexusTheme.typography.small,
                    color = NexusTheme.colorScheme.text.secondary,
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            NexusMenu(
                items = items,
                state = state,
                mode = MenuMode.Vertical,
                collapse = collapse,
                uniqueOpened = true,
            )
        }
    }
}

@Composable
fun PageHeaderDemo() {
    DemoPage(title = "Page Header", description = "If path of the page is simple, use PageHeader instead of Breadcrumb.") {
        DemoSection("Complete example") {
            NexusPageHeader(
                title = "Back",
                content = "Detail Page",
                onBack = {},
                breadcrumb = {
                    NexusBreadcrumb {
                        item { NexusBreadcrumbItem(text = "Home", to = "/home", onNavigate = { _, _ -> }) }
                        item { NexusBreadcrumbItem(text = "List", to = "/list", onNavigate = { _, _ -> }) }
                        item { NexusBreadcrumbItem(text = "Detail", isCurrent = true) }
                    }
                },
                extra = {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        NexusButton(
                            text = "Edit",
                            onClick = {},
                            size = ComponentSize.Small
                        )
                        NexusButton(
                            text = "Publish",
                            onClick = {},
                            type = NexusType.Primary,
                            size = ComponentSize.Small
                        )
                    }
                },
                main = {
                    NexusText(
                        text = "This is the main content under page header.",
                        color = NexusTheme.colorScheme.text.secondary,
                    )
                },
            )
        }

        DemoSection("Basic usage") {
            NexusPageHeader(
                title = "Back",
                content = "Page Header",
                onBack = {},
            )
        }

        DemoSection("Custom icon") {
            NexusPageHeader(
                title = "Back",
                content = "Custom Icon",
                onBack = {},
                iconSlot = {
                    NexusTag(
                        text = "←",
                        type = NexusType.Primary,
                        size = ComponentSize.Small,
                    )
                },
            )
        }

        DemoSection("No icon") {
            NexusPageHeader(
                icon = "",
                title = "No Icon",
                content = "Only text is shown",
            )
        }

        DemoSection("Breadcrumbs") {
            NexusPageHeader(
                title = "Order",
                content = "Order #A-1024",
                breadcrumb = {
                    NexusBreadcrumb(separator = ">") {
                        item { NexusBreadcrumbItem(text = "Home", onClick = {}) }
                        item { NexusBreadcrumbItem(text = "Order", onClick = {}) }
                        item { NexusBreadcrumbItem(text = "Detail", isCurrent = true) }
                    }
                },
            )
        }

        DemoSection("Additional operation section") {
            NexusPageHeader(
                title = "Back",
                content = "Operation Header",
                onBack = {},
                extra = {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        NexusButton(
                            text = "Cancel",
                            onClick = {},
                            size = ComponentSize.Small
                        )
                        NexusButton(
                            text = "Save",
                            onClick = {},
                            type = NexusType.Primary,
                            size = ComponentSize.Small
                        )
                    }
                },
            )
        }

        DemoSection("Main content") {
            NexusPageHeader(
                title = "Back",
                content = "Page Content",
                onBack = {},
                main = {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        NexusText(text = "Summary")
                        NexusText(
                            text = "Here is the main content slot for description and additional information.",
                            color = NexusTheme.colorScheme.text.secondary,
                            style = NexusTheme.typography.small,
                        )
                    }
                },
            )
        }
    }
}

@Composable
fun StepsDemo() {
    DemoPage(title = "Steps", description = "Guide the user to complete tasks in accordance with the process.") {
        DemoSection("Basic usage") {
            val state = rememberStepsState(
                initialActive = 1,
                steps = listOf(
                    StepItem("Step 1", "Select plan"),
                    StepItem("Step 2", "Fill information"),
                    StepItem("Step 3", "Payment"),
                    StepItem("Step 4", "Complete"),
                ),
            )
            NexusSteps(
                state = state,
                space = 180.dp,
                finishStatus = StepStatus.Success,
            )
        }

        DemoSection("Step bar that contains status") {
            val state = rememberStepsState(
                initialActive = 2,
                steps = listOf(
                    StepItem("Done", status = StepStatus.Success),
                    StepItem("Error", status = StepStatus.Error),
                    StepItem("In Progress", status = StepStatus.Process),
                    StepItem("Waiting", status = StepStatus.Wait),
                ),
            )
            NexusSteps(
                state = state,
                processStatus = StepStatus.Process,
                finishStatus = StepStatus.Success,
            )
        }

        DemoSection("Center") {
            val state = rememberStepsState(
                initialActive = 1,
                steps = listOf(
                    StepItem("Step 1", "Start"),
                    StepItem("Step 2", "Run"),
                    StepItem("Step 3", "Finish"),
                ),
            )
            NexusSteps(state = state, alignCenter = true)
        }

        DemoSection("Step bar with description") {
            val state = rememberStepsState(
                initialActive = 2,
                steps = listOf(
                    StepItem("Create", "Create an account"),
                    StepItem("Verify", "Verify identity"),
                    StepItem("Finish", "Activate account"),
                ),
            )
            NexusSteps(state = state)
        }

        DemoSection("Step bar with icon") {
            val state = rememberStepsState(
                initialActive = 1,
                steps = listOf(
                    StepItem("Upload", icon = {
                        NexusText(
                            text = "↑",
                            color = NexusTheme.colorScheme.white,
                            style = NexusTheme.typography.extraSmall
                        )
                    }),
                    StepItem("Review", icon = {
                        NexusText(
                            text = "✓",
                            color = NexusTheme.colorScheme.white,
                            style = NexusTheme.typography.extraSmall
                        )
                    }),
                    StepItem("Publish", icon = {
                        NexusText(
                            text = "★",
                            color = NexusTheme.colorScheme.white,
                            style = NexusTheme.typography.extraSmall
                        )
                    }),
                ),
            )
            NexusSteps(state = state)
        }

        DemoSection("Vertical") {
            val state = rememberStepsState(
                initialActive = 2,
                steps = listOf(
                    StepItem("Register", "Create account"),
                    StepItem("Verify email", "Verify mailbox"),
                    StepItem("Setup profile", "Complete profile"),
                ),
            )
            NexusSteps(state = state, direction = StepsDirection.Vertical)
        }

        DemoSection("Simple step bar") {
            val state = rememberStepsState(
                initialActive = 1,
                steps = listOf(
                    StepItem("Step 1"),
                    StepItem("Step 2"),
                    StepItem("Step 3"),
                ),
            )
            NexusSteps(state = state, simple = true)
        }
    }
}

@Composable
fun BreadcrumbDemo() {
    DemoPage(title = "Breadcrumb", description = "Displays the current page's location within a navigational hierarchy.") {
        DemoSection("Basic usage") {
            NexusBreadcrumb {
                item { NexusBreadcrumbItem(text = "Home", to = "/home", onNavigate = { _, _ -> }) }
                item { NexusBreadcrumbItem(text = "Components", to = "/components", onNavigate = { _, _ -> }) }
                item { NexusBreadcrumbItem(text = "Breadcrumb", isCurrent = true) }
            }
        }
        DemoSection("Custom separator") {
            NexusBreadcrumb(separator = ">") {
                item { NexusBreadcrumbItem(text = "Home", onClick = {}) }
                item { NexusBreadcrumbItem(text = "Settings", onClick = {}) }
                item { NexusBreadcrumbItem(text = "Profile", isCurrent = true) }
            }
        }
        DemoSection("Icon separator") {
            NexusBreadcrumb(
                separatorIcon = {
                    NexusText(
                        text = "→",
                        color = NexusTheme.colorScheme.text.placeholder,
                        style = NexusTheme.typography.small,
                    )
                },
            ) {
                item { NexusBreadcrumbItem(text = "Home", to = "/home", onNavigate = { _, _ -> }) }
                item { NexusBreadcrumbItem(text = "Workspace", to = "/workspace", replace = true, onNavigate = { _, _ -> }) }
                item {
                    NexusBreadcrumbItem(
                        text = "Current",
                        isCurrent = true,
                        content = {
                            NexusTag(
                                text = "Current",
                                type = NexusType.Primary,
                                size = ComponentSize.Small,
                            )
                        },
                    )
                }
            }
        }
    }
}

@Composable
fun TabsDemo() {
    DemoPage(title = "Tabs", description = "Divide data collections which are related yet belong to different types.") {
        DemoSection("Basic usage") {
            val items = listOf(
                TabItem("User"),
                TabItem("Role"),
                TabItem("Config"),
            )
            val state = rememberTabsState()
            NexusTabs(state = state, items = items) { index ->
                NexusText(text = "Content of tab ${index + 1}")
            }
        }

        DemoSection("Card style") {
            val items = listOf(
                TabItem("Tab 1"),
                TabItem("Tab 2"),
                TabItem("Tab 3"),
            )
            val state = rememberTabsState()
            NexusTabs(
                state = state,
                items = items,
                type = TabsType.Card
            ) { index ->
                NexusText(text = "Card content: tab ${index + 1}")
            }
        }

        DemoSection("Border card") {
            val items = listOf(
                TabItem("Overview"),
                TabItem("Detail"),
                TabItem("History"),
            )
            val state = rememberTabsState()
            NexusTabs(
                state = state,
                items = items,
                type = TabsType.BorderCard
            ) { index ->
                NexusText(text = "Border-card content: pane ${index + 1}")
            }
        }

        DemoSection("Tab position") {
            val items = listOf(
                TabItem("Profile"),
                TabItem("Security"),
                TabItem("Billing"),
            )
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.Top) {
                val leftState = rememberTabsState()
                NexusTabs(
                    state = leftState,
                    items = items,
                    tabPosition = TabsPosition.Left,
                    modifier = Modifier.width(420.dp),
                ) { index ->
                    NexusText(text = "Left tabs content ${index + 1}")
                }

                val rightState = rememberTabsState()
                NexusTabs(
                    state = rightState,
                    items = items,
                    tabPosition = TabsPosition.Right,
                    modifier = Modifier.width(420.dp),
                ) { index ->
                    NexusText(text = "Right tabs content ${index + 1}")
                }
            }
        }

        DemoSection("Add & close tab") {
            val items = remember {
                mutableStateListOf(
                    TabItem("Tab 1"),
                    TabItem("Tab 2", closable = true),
                    TabItem("Tab 3", closable = true),
                )
            }
            val state = rememberTabsState()
            var counter by remember { mutableStateOf(3) }
            NexusTabs(
                state = state,
                items = items,
                type = TabsType.Card,
                editable = true,
                onTabAdd = {
                    counter += 1
                    items.add(TabItem("Tab $counter", closable = true))
                    state.selectedIndex = items.lastIndex
                },
                onTabRemove = { index ->
                    if (index in items.indices) {
                        items.removeAt(index)
                        if (state.selectedIndex >= items.size) {
                            state.selectedIndex = (items.lastIndex).coerceAtLeast(0)
                        }
                    }
                },
            ) { index ->
                if (items.isNotEmpty() && index in items.indices) {
                    NexusText(text = "Dynamic content of ${items[index].label}")
                }
            }
        }

        DemoSection("Stretch and before-leave") {
            val items = listOf(
                TabItem("Info"),
                TabItem("Runtime"),
                TabItem("Danger"),
            )
            val state = rememberTabsState()
            var allowLeave by remember { mutableStateOf(false) }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                NexusButton(
                    text = if (allowLeave) "Block Leaving" else "Allow Leaving",
                    onClick = { allowLeave = !allowLeave },
                    size = ComponentSize.Small,
                )
                NexusText(
                    text = "allowLeave=$allowLeave",
                    style = NexusTheme.typography.small,
                    color = NexusTheme.colorScheme.text.secondary,
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            NexusTabs(
                state = state,
                items = items,
                stretch = true,
                beforeLeave = { newIndex, oldIndex ->
                    !(oldIndex == 2 && !allowLeave && newIndex != oldIndex)
                },
            ) { index ->
                NexusText(text = "Stretch tab content ${index + 1}")
            }
        }
    }
}

@Composable
fun DropdownDemo() {
    DemoPage(title = "Dropdown", description = "Toggleable menu for displaying a list of links and actions.") {
        DemoSection("Basic usage") {
            val state = rememberDropdownState()
            NexusDropdown(
                state = state,
                trigger = {
                    NexusButton(text = "Dropdown Menu ▾", onClick = { })
                },
            ) {
                NexusDropdownItem(text = "Action 1", command = "action-1")
                NexusDropdownItem(text = "Action 2", command = "action-2")
                NexusDropdownItem(text = "Action 3", command = "action-3")
            }
        }

        DemoSection("Placement") {
            val placements = listOf(
                DropdownPlacement.TopStart,
                DropdownPlacement.Top,
                DropdownPlacement.TopEnd,
                DropdownPlacement.BottomStart,
                DropdownPlacement.Bottom,
                DropdownPlacement.BottomEnd,
            )
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                placements.forEach { placement ->
                    val state = rememberDropdownState()
                    NexusDropdown(
                        state = state,
                        placement = placement,
                        triggerMode = DropdownTrigger.Click,
                        trigger = {
                            NexusButton(
                                text = placement.name,
                                onClick = {},
                                size = ComponentSize.Small,
                            )
                        },
                    ) {
                        NexusDropdownItem(text = "A")
                        NexusDropdownItem(text = "B")
                        NexusDropdownItem(text = "C")
                    }
                }
            }
        }

        DemoSection("Triggering element") {
            val state = rememberDropdownState()
            var clickLog by remember { mutableStateOf("none") }
            NexusDropdown(
                state = state,
                splitButton = true,
                buttonText = "Actions",
                type = NexusType.Primary,
                onClick = { clickLog = "left button clicked" },
                onCommand = { clickLog = "command=$it" },
                trigger = {},
            ) {
                NexusDropdownItem(text = "Edit", command = "edit")
                NexusDropdownItem(text = "Copy", command = "copy")
                NexusDropdownItem(text = "Delete", command = "delete", divided = true)
            }
            Spacer(modifier = Modifier.height(8.dp))
            NexusText(
                text = "event: $clickLog",
                style = NexusTheme.typography.small,
                color = NexusTheme.colorScheme.text.secondary,
            )
        }

        DemoSection("How to trigger") {
            val hoverState = rememberDropdownState()
            val clickState = rememberDropdownState()
            Row(horizontalArrangement = Arrangement.spacedBy(14.dp), verticalAlignment = Alignment.CenterVertically) {
                NexusDropdown(
                    state = hoverState,
                    triggerMode = DropdownTrigger.Hover,
                    trigger = { NexusButton(text = "Hover Trigger", onClick = {}) },
                ) {
                    NexusDropdownItem(text = "Hover Item 1")
                    NexusDropdownItem(text = "Hover Item 2")
                }
                NexusDropdown(
                    state = clickState,
                    triggerMode = DropdownTrigger.Click,
                    trigger = { NexusButton(text = "Click Trigger", onClick = {}) },
                ) {
                    NexusDropdownItem(text = "Click Item 1")
                    NexusDropdownItem(text = "Click Item 2")
                }
            }
        }

        DemoSection("Menu hiding behavior") {
            val state = rememberDropdownState()
            var eventLog by remember { mutableStateOf("none") }
            NexusDropdown(
                state = state,
                triggerMode = DropdownTrigger.Click,
                hideOnClick = false,
                onCommand = { eventLog = "command=$it (menu remains open)" },
                trigger = { NexusButton(text = "hide-on-click = false", onClick = {}) },
            ) {
                NexusDropdownItem(text = "Stay 1", command = 1)
                NexusDropdownItem(text = "Stay 2", command = 2)
                NexusDropdownItem(text = "Close manually", command = "close", onClick = { state.close() }, divided = true)
            }
            Spacer(modifier = Modifier.height(8.dp))
            NexusText(
                text = eventLog,
                style = NexusTheme.typography.small,
                color = NexusTheme.colorScheme.text.secondary,
            )
        }

        DemoSection("Dropdown methods") {
            val state = rememberDropdownState()
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                NexusButton(
                    text = "Open",
                    onClick = { state.handleOpen() },
                    size = ComponentSize.Small
                )
                NexusButton(
                    text = "Close",
                    onClick = { state.handleClose() },
                    size = ComponentSize.Small
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            NexusDropdown(
                state = state,
                triggerMode = DropdownTrigger.Click,
                trigger = { NexusButton(text = "Manual Controlled Dropdown", onClick = {}) },
            ) {
                NexusDropdownItem(text = "Manual 1")
                NexusDropdownItem(text = "Manual 2")
            }
        }

        DemoSection("Sizes") {
            val largeState = rememberDropdownState()
            val defaultState = rememberDropdownState()
            val smallState = rememberDropdownState()
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                NexusDropdown(
                    state = largeState,
                    size = ComponentSize.Large,
                    triggerMode = DropdownTrigger.Click,
                    trigger = {
                        NexusButton(
                            text = "Large",
                            onClick = {},
                            size = ComponentSize.Large
                        )
                    },
                ) {
                    NexusDropdownItem(text = "Large Item 1")
                    NexusDropdownItem(text = "Large Item 2")
                }
                NexusDropdown(
                    state = defaultState,
                    size = ComponentSize.Default,
                    triggerMode = DropdownTrigger.Click,
                    trigger = { NexusButton(text = "Default", onClick = {}) },
                ) {
                    NexusDropdownItem(text = "Default Item 1")
                    NexusDropdownItem(text = "Default Item 2")
                }
                NexusDropdown(
                    state = smallState,
                    size = ComponentSize.Small,
                    triggerMode = DropdownTrigger.Click,
                    trigger = {
                        NexusButton(
                            text = "Small",
                            onClick = {},
                            size = ComponentSize.Small
                        )
                    },
                ) {
                    NexusDropdownItem(text = "Small Item 1")
                    NexusDropdownItem(text = "Small Item 2")
                }
            }
        }
    }
}
