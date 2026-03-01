package io.github.xingray.compose_nexus.sample.demos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.xingray.compose_nexus.controls.*
import io.github.xingray.compose_nexus.theme.NexusType

@Composable
fun AlertDemo() {
    DemoPage(title = "Alert", description = "Displays important alert messages.") {
        DemoSection("Basic usage") {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                NexusAlert(title = "Success alert", type = NexusType.Success)
                NexusAlert(title = "Info alert", type = NexusType.Info)
                NexusAlert(title = "Warning alert", type = NexusType.Warning)
                NexusAlert(title = "Danger alert", type = NexusType.Danger)
            }
        }
        DemoSection("With description & icon") {
            NexusAlert(
                title = "Important notice",
                type = NexusType.Warning,
                description = "This is a detailed description of the alert message.",
                showIcon = true,
            )
        }
        DemoSection("Not closable") {
            NexusAlert(title = "Permanent alert", type = NexusType.Info, closable = false)
        }
    }
}

@Composable
fun MessageDemo() {
    DemoPage(title = "Message", description = "Used to show feedback after an activity.") {
        DemoSection("Trigger messages", "Click buttons to trigger messages at the top of the page.") {
            val messageState = rememberMessageState()
            NexusMessageHost(state = messageState)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                NexusButton(onClick = { messageState.success("Success!") }, type = NexusType.Success) { NexusText(text = "Success") }
                NexusButton(onClick = { messageState.warning("Warning!") }, type = NexusType.Warning) { NexusText(text = "Warning") }
                NexusButton(onClick = { messageState.error("Error!") }, type = NexusType.Danger) { NexusText(text = "Error") }
                NexusButton(onClick = { messageState.info("Info!") }, type = NexusType.Info) { NexusText(text = "Info") }
            }
        }
    }
}

@Composable
fun NotificationDemo() {
    DemoPage(title = "Notification", description = "Displays a global notification message at a corner of the page.") {
        DemoSection("Trigger notifications") {
            val state = rememberNotificationState()
            NexusNotificationHost(state = state)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                NexusButton(onClick = {
                    state.success("Success", "This is a success notification")
                }, type = NexusType.Success) { NexusText(text = "Success") }
                NexusButton(onClick = {
                    state.error("Error", "Something went wrong")
                }, type = NexusType.Danger) { NexusText(text = "Error") }
                NexusButton(onClick = {
                    state.info("Info", "Here is some information")
                }, type = NexusType.Info) { NexusText(text = "Info") }
            }
        }
    }
}

@Composable
fun LoadingDemo() {
    DemoPage(title = "Loading", description = "Show animation while loading data.") {
        DemoSection("Inline spinner") {
            NexusLoading(loading = true, text = "Loading...")
        }
        DemoSection("Fullscreen overlay") {
            NexusLoading(
                loading = true,
                fullscreen = true,
                text = "Please wait...",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    NexusText(text = "Content behind loading overlay")
                    NexusText(text = "This text is covered by the loading mask")
                }
            }
        }
    }
}

@Composable
fun TooltipDemo() {
    DemoPage(title = "Tooltip", description = "Display prompt information for mouse hover.") {
        DemoSection("Basic usage") {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                NexusTooltip(text = "Top tooltip", placement = TooltipPlacement.Top) {
                    NexusButton(onClick = {}) { NexusText(text = "Top") }
                }
                NexusTooltip(text = "Bottom tooltip", placement = TooltipPlacement.Bottom) {
                    NexusButton(onClick = {}) { NexusText(text = "Bottom") }
                }
            }
        }
        DemoSection("Light theme") {
            NexusTooltip(text = "Light tooltip", theme = TooltipTheme.Light) {
                NexusButton(onClick = {}) { NexusText(text = "Hover me") }
            }
        }
    }
}

@Composable
fun TourDemo() {
    DemoPage(title = "Tour", description = "A popup component for guiding users.") {
        DemoSection("Basic usage", "Click 'Start Tour' to begin.") {
            val tourState = rememberTourState(
                steps = listOf(
                    TourStep("Welcome", "This is the Compose Nexus component library."),
                    TourStep("Components", "Browse 50+ components in the sidebar."),
                    TourStep("Done!", "You're all set. Enjoy building UIs!"),
                ),
            )
            NexusTour(state = tourState)
            NexusButton(onClick = { tourState.start() }, type = NexusType.Primary) {
                NexusText(text = "Start Tour")
            }
        }
    }
}
