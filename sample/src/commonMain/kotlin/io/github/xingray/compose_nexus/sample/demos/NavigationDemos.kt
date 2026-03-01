package io.github.xingray.compose_nexus.sample.demos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import io.github.xingray.compose_nexus.controls.*
import io.github.xingray.compose_nexus.theme.NexusTheme

@Composable
fun MenuDemo() {
    DemoPage(title = "Menu", description = "Menu provides navigation for your website.") {
        DemoSection("Vertical menu") {
            val items = listOf(
                MenuItem("home", "Home"),
                MenuItem("about", "About", children = listOf(
                    MenuItem("team", "Team"),
                    MenuItem("history", "History"),
                )),
                MenuItem("contact", "Contact"),
            )
            val state = rememberMenuState(initialActiveKey = "home")
            NexusMenu(items = items, state = state, mode = MenuMode.Vertical)
        }
        DemoSection("Horizontal menu") {
            val items = listOf(
                MenuItem("nav1", "Navigation 1"),
                MenuItem("nav2", "Navigation 2"),
                MenuItem("nav3", "Navigation 3"),
            )
            val state = rememberMenuState(initialActiveKey = "nav1")
            NexusMenu(items = items, state = state, mode = MenuMode.Horizontal)
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
            NexusSteps(state = state)
        }
        DemoSection("Vertical") {
            val state = rememberStepsState(
                initialActive = 2,
                steps = listOf(
                    StepItem("Register"),
                    StepItem("Verify email"),
                    StepItem("Setup profile"),
                ),
            )
            NexusSteps(state = state, direction = StepsDirection.Vertical)
        }
    }
}

@Composable
fun BreadcrumbDemo() {
    DemoPage(title = "Breadcrumb", description = "Displays the current page's location within a navigational hierarchy.") {
        DemoSection("Basic usage") {
            NexusBreadcrumb {
                item { NexusBreadcrumbItem(text = "Home", onClick = {}) }
                item { NexusBreadcrumbItem(text = "Components", onClick = {}) }
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
        DemoSection("Card type") {
            val items = listOf(
                TabItem("Tab 1"),
                TabItem("Tab 2"),
                TabItem("Tab 3"),
            )
            val state = rememberTabsState()
            NexusTabs(state = state, items = items, type = TabsType.Card) { index ->
                NexusText(text = "Card content: tab ${index + 1}")
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
                    NexusButton(onClick = { state.toggle() }) {
                        NexusText(text = "Dropdown Menu ▾")
                    }
                },
            ) {
                NexusDropdownItem(text = "Action 1", onClick = { state.close() })
                NexusDropdownItem(text = "Action 2", onClick = { state.close() })
                NexusDropdownItem(text = "Action 3", onClick = { state.close() })
            }
        }
    }
}
