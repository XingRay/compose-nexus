package io.github.xingray.compose_nexus.sample.demos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.xingray.compose_nexus.controls.*
import io.github.xingray.compose_nexus.theme.NexusTheme
import io.github.xingray.compose_nexus.theme.NexusType
import kotlinx.coroutines.launch

@Composable
fun AlertDemo() {
    DemoPage(title = "Alert", description = "Displays important alert messages.") {
        DemoSection("Basic usage") {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                NexusAlert(title = "Primary alert", type = NexusType.Primary)
                NexusAlert(title = "Success alert", type = NexusType.Success)
                NexusAlert(title = "Info alert", type = NexusType.Info)
                NexusAlert(title = "Warning alert", type = NexusType.Warning)
                NexusAlert(title = "Danger alert", type = NexusType.Danger)
            }
        }

        DemoSection("Theme") {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                NexusAlert(
                    title = "Light theme alert",
                    type = NexusType.Primary,
                    effect = AlertEffect.Light,
                )
                NexusAlert(
                    title = "Dark theme alert",
                    type = NexusType.Primary,
                    effect = AlertEffect.Dark,
                )
            }
        }

        DemoSection("Customizable close button") {
            var closeCount by remember { mutableIntStateOf(0) }
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                NexusAlert(
                    title = "Custom close text",
                    type = NexusType.Info,
                    closeText = "Got it",
                    onClose = { closeCount++ },
                )
                NexusAlert(
                    title = "Not closable alert",
                    type = NexusType.Success,
                    closable = false,
                )
                NexusText(
                    text = "close count = $closeCount",
                    style = NexusTheme.typography.small,
                    color = NexusTheme.colorScheme.text.secondary,
                )
            }
        }

        DemoSection("With icon") {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                NexusAlert(title = "Primary with icon", type = NexusType.Primary, showIcon = true)
                NexusAlert(title = "Success with icon", type = NexusType.Success, showIcon = true)
                NexusAlert(title = "Info with icon", type = NexusType.Info, showIcon = true)
                NexusAlert(title = "Warning with icon", type = NexusType.Warning, showIcon = true)
                NexusAlert(title = "Danger with icon", type = NexusType.Danger, showIcon = true)
            }
        }

        DemoSection("Centered text") {
            NexusAlert(
                title = "Centered alert message",
                type = NexusType.Warning,
                center = true,
            )
        }

        DemoSection("With description") {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                NexusAlert(
                    title = "Success alert",
                    type = NexusType.Success,
                    description = "Detailed description and advice about this success message.",
                )
                NexusAlert(
                    title = "Info alert",
                    type = NexusType.Info,
                    description = "Additional information for the current context.",
                )
                NexusAlert(
                    title = "Warning alert",
                    type = NexusType.Warning,
                    description = "Warning details can help users avoid mistakes.",
                )
                NexusAlert(
                    title = "Danger alert",
                    type = NexusType.Danger,
                    description = "Danger details explain why this action may fail.",
                )
            }
        }

        DemoSection("With icon and description") {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                NexusAlert(
                    title = "Warning with icon",
                    type = NexusType.Warning,
                    description = "This combines icon and descriptive text.",
                    showIcon = true,
                )
                NexusAlert(
                    title = "Custom slots",
                    type = NexusType.Primary,
                    showIcon = true,
                    titleContent = {
                        NexusText(
                            text = "Custom title slot",
                            style = NexusTheme.typography.small,
                            color = NexusTheme.colorScheme.primary.base,
                        )
                    },
                    descriptionContent = {
                        NexusText(
                            text = "Custom description slot content.",
                            style = NexusTheme.typography.extraSmall,
                            color = NexusTheme.colorScheme.text.secondary,
                        )
                    },
                    icon = {
                        NexusText(
                            text = "◆",
                            style = NexusTheme.typography.base,
                            color = NexusTheme.colorScheme.primary.base,
                        )
                    },
                )
            }
        }
    }
}

@Composable
fun MessageDemo() {
    DemoPage(title = "Message", description = "Used to show feedback after an activity.") {
        val messageState = rememberMessageState()
        NexusMessageHost(state = messageState)

        DemoSection("Basic usage") {
            NexusButton(
                text = "Show message",
                onClick = { messageState.show("This is a basic message.") },
                type = NexusType.Primary,
            )
        }

        DemoSection("Types") {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                NexusButton(onClick = { messageState.primary("Primary message") }, type = NexusType.Primary) { NexusText(text = "Primary") }
                NexusButton(onClick = { messageState.success("Success!") }, type = NexusType.Success) { NexusText(text = "Success") }
                NexusButton(onClick = { messageState.warning("Warning!") }, type = NexusType.Warning) { NexusText(text = "Warning") }
                NexusButton(onClick = { messageState.error("Error!") }, type = NexusType.Danger) { NexusText(text = "Error") }
                NexusButton(onClick = { messageState.info("Info!") }, type = NexusType.Info) { NexusText(text = "Info") }
            }
        }

        DemoSection("Plain") {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                NexusButton(
                    text = "Plain Success",
                    onClick = {
                        messageState.show(
                            text = "Plain success message",
                            type = NexusType.Success,
                            plain = true,
                        )
                    },
                )
                NexusButton(
                    text = "Plain Warning",
                    onClick = {
                        messageState.show(
                            text = "Plain warning message",
                            type = NexusType.Warning,
                            plain = true,
                        )
                    },
                )
            }
        }

        DemoSection("Closable") {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                NexusButton(
                    text = "Closable message",
                    onClick = {
                        messageState.show(
                            text = "Click close icon or wait 5s",
                            showClose = true,
                            duration = 5000L,
                        )
                    },
                )
                NexusButton(
                    text = "Duration = 0",
                    onClick = {
                        messageState.show(
                            text = "Persistent message (duration=0)",
                            showClose = true,
                            duration = 0L,
                        )
                    },
                )
                NexusButton(text = "Close all", onClick = { messageState.closeAll() }, type = NexusType.Danger)
            }
        }

        DemoSection("Grouping") {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                NexusButton(
                    text = "Send grouped x1",
                    onClick = {
                        messageState.show(
                            text = "Repeated message",
                            type = NexusType.Info,
                            grouping = true,
                            repeatNum = 1,
                        )
                    },
                )
                NexusButton(
                    text = "Send grouped x3",
                    onClick = {
                        messageState.show(
                            text = "Repeated message",
                            type = NexusType.Info,
                            grouping = true,
                            repeatNum = 3,
                        )
                    },
                )
            }
        }

        DemoSection("Placement") {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                NexusButton(text = "Top", onClick = {
                    messageState.show("Top message", placement = MessagePlacement.Top)
                })
                NexusButton(text = "Top Left", onClick = {
                    messageState.show("Top-left message", placement = MessagePlacement.TopLeft)
                })
                NexusButton(text = "Top Right", onClick = {
                    messageState.show("Top-right message", placement = MessagePlacement.TopRight)
                })
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                NexusButton(text = "Bottom", onClick = {
                    messageState.show("Bottom message", placement = MessagePlacement.Bottom)
                })
                NexusButton(text = "Bottom Left", onClick = {
                    messageState.show("Bottom-left message", placement = MessagePlacement.BottomLeft)
                })
                NexusButton(text = "Bottom Right", onClick = {
                    messageState.show("Bottom-right message", placement = MessagePlacement.BottomRight)
                })
            }
        }
    }
}

@Composable
fun MessageBoxDemo() {
    DemoPage(title = "Message Box", description = "A set of modal boxes for alert, confirm and prompt.") {
        val boxState = rememberMessageBoxState()
        var lastResult by remember { mutableStateOf("No action yet") }
        val scope = rememberCoroutineScope()
        NexusMessageBoxHost(state = boxState)

        DemoSection("Alert / Confirm / Prompt") {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                NexusButton(
                    text = "Alert",
                    onClick = {
                        boxState.alert("This is an alert message.", "Alert") { action, _ ->
                            lastResult = "alert action = $action"
                        }
                    },
                    type = NexusType.Primary,
                )
                NexusButton(
                    text = "Confirm",
                    onClick = {
                        boxState.confirm("Do you confirm this operation?", "Confirm", NexusType.Warning) { action, _ ->
                            lastResult = "confirm action = $action"
                        }
                    },
                )
                NexusButton(
                    text = "Prompt",
                    onClick = {
                        boxState.prompt(
                            message = "Please input your email:",
                            title = "Prompt",
                            inputPlaceholder = "example@domain.com",
                            inputPattern = Regex("^\\S+@\\S+\\.\\S+$"),
                            inputValidator = { value ->
                                if (value.length < 6) "Input is too short" else null
                            },
                        ) { action, value ->
                            lastResult = "prompt action = $action, value = ${value ?: ""}"
                        }
                    },
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            NexusText(
                text = lastResult,
                style = NexusTheme.typography.small,
                color = NexusTheme.colorScheme.text.secondary,
            )
        }

        DemoSection("Customization") {
            NexusButton(
                text = "Open customized MessageBox",
                onClick = {
                    boxState.show(
                        MessageBoxRequest(
                            title = "Custom MessageBox",
                            message = "This box uses custom buttons, beforeClose and draggable behavior.",
                            type = NexusType.Primary,
                            showCancelButton = true,
                            center = true,
                            draggable = true,
                            overflow = true,
                            confirmButtonText = "Submit",
                            cancelButtonText = "Close",
                            roundButton = true,
                            beforeClose = { _, _, done ->
                                scope.launch {
                                    kotlinx.coroutines.delay(300)
                                    done()
                                }
                            },
                            callback = { action, _ ->
                                lastResult = "custom action = $action"
                            },
                        ),
                    )
                },
                type = NexusType.Primary,
            )
        }

        DemoSection("Distinguish cancel and close") {
            NexusButton(
                text = "Open distinguish box",
                onClick = {
                    boxState.show(
                        MessageBoxRequest(
                            title = "Distinguish",
                            message = "Click cancel button or close icon to compare returned action.",
                            showCancelButton = true,
                            distinguishCancelAndClose = true,
                            callback = { action, _ ->
                                lastResult = "distinguish action = $action"
                            },
                        ),
                    )
                },
            )
        }
    }
}

@Composable
fun NotificationDemo() {
    DemoPage(title = "Notification", description = "Displays a global notification message at a corner of the page.") {
        val state = rememberNotificationState()
        NexusNotificationHost(state = state)

        DemoSection("Basic usage") {
            NexusButton(
                text = "Open Notification",
                onClick = {
                    state.show(
                        title = "Title",
                        message = "This is a basic notification.",
                        duration = 4500L,
                    )
                },
                type = NexusType.Primary,
            )
        }

        DemoSection("With types") {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                NexusButton(text = "Primary", onClick = { state.primary("Primary", "Primary notification") }, type = NexusType.Primary)
                NexusButton(text = "Success", onClick = { state.success("Success", "Operation succeeded") }, type = NexusType.Success)
                NexusButton(text = "Warning", onClick = { state.warning("Warning", "Please double check") }, type = NexusType.Warning)
                NexusButton(text = "Info", onClick = { state.info("Info", "Useful information") }, type = NexusType.Info)
                NexusButton(text = "Error", onClick = { state.error("Error", "Something went wrong") }, type = NexusType.Danger)
            }
        }

        DemoSection("Custom position") {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                NexusButton(text = "Top Right", onClick = {
                    state.show("Top Right", "Position top-right", position = NotificationPosition.TopRight)
                })
                NexusButton(text = "Top Left", onClick = {
                    state.show("Top Left", "Position top-left", position = NotificationPosition.TopLeft)
                })
                NexusButton(text = "Bottom Right", onClick = {
                    state.show("Bottom Right", "Position bottom-right", position = NotificationPosition.BottomRight)
                })
                NexusButton(text = "Bottom Left", onClick = {
                    state.show("Bottom Left", "Position bottom-left", position = NotificationPosition.BottomLeft)
                })
            }
        }

        DemoSection("Offset / hide close / custom icon") {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                NexusButton(text = "Offset 40", onClick = {
                    state.show(
                        title = "Offset",
                        message = "Offset from screen edge is 40dp.",
                        offset = 40.dp,
                    )
                })
                NexusButton(text = "No close button", onClick = {
                    state.show(
                        title = "No Close",
                        message = "showClose=false",
                        showClose = false,
                    )
                })
                NexusButton(text = "Custom close icon", onClick = {
                    state.show(
                        title = "Custom Icon",
                        message = "Custom close and click callback.",
                        onClick = { },
                        closeIcon = { NexusText(text = "◉", color = NexusTheme.colorScheme.primary.base) },
                    )
                })
                NexusButton(text = "Close All", onClick = { state.closeAll() }, type = NexusType.Danger)
            }
        }
    }
}

@Composable
fun LoadingDemo() {
    DemoPage(title = "Loading", description = "Show animation while loading data.") {
        DemoSection("Loading inside a container") {
            var loading by remember { mutableStateOf(true) }
            NexusButton(
                text = if (loading) "Stop Loading" else "Start Loading",
                onClick = { loading = !loading },
                type = NexusType.Primary,
            )
            Spacer(modifier = Modifier.height(8.dp))
            NexusLoading(
                loading = loading,
                text = "Loading table data...",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(NexusTheme.colorScheme.fill.light, NexusTheme.shapes.base),
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    repeat(4) { index ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(NexusTheme.colorScheme.fill.blank, NexusTheme.shapes.base)
                                .padding(10.dp),
                        ) {
                            NexusText(text = "Row ${index + 1}")
                        }
                    }
                }
            }
        }

        DemoSection("Customization") {
            NexusLoading(
                loading = true,
                text = "Custom spinner and background",
                backgroundColor = NexusTheme.colorScheme.primary.light9.copy(alpha = 0.78f),
                spinnerSize = 44.dp,
                spinner = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(NexusTheme.colorScheme.primary.base, NexusTheme.shapes.circle),
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .background(NexusTheme.colorScheme.fill.light, NexusTheme.shapes.base),
            ) {
                Box(modifier = Modifier.fillMaxSize())
            }
        }

        DemoSection("Full screen loading") {
            var fullscreenLoading by remember { mutableStateOf(false) }
            NexusButton(
                text = "Toggle Fullscreen Loading",
                onClick = { fullscreenLoading = !fullscreenLoading },
                type = NexusType.Primary,
            )
            Spacer(modifier = Modifier.height(8.dp))
            NexusLoading(
                loading = fullscreenLoading,
                fullscreen = true,
                text = "Please wait...",
                lock = true,
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

        DemoSection("Service") {
            val service = rememberLoadingServiceState()
            val serviceBackground = NexusTheme.colorScheme.overlay.base
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                NexusButton(
                    text = "Open Service Loading",
                    onClick = {
                        service.open(
                            text = "Loading by service...",
                            backgroundColor = serviceBackground,
                            onClosed = { },
                        )
                    },
                    type = NexusType.Primary,
                )
                NexusButton(text = "Close Service Loading", onClick = { service.close() })
            }
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(NexusTheme.colorScheme.fill.light, NexusTheme.shapes.base)
                    .padding(12.dp),
            ) {
                NexusText(
                    text = "Service host area (simulates app-level loading).",
                    style = NexusTheme.typography.small,
                    color = NexusTheme.colorScheme.text.secondary,
                )
                NexusLoadingServiceHost(state = service, modifier = Modifier.fillMaxSize())
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
                NexusTooltip(text = "Right-start tooltip", placement = TooltipPlacement.RightStart) {
                    NexusButton(onClick = {}) { NexusText(text = "Right Start") }
                }
            }
        }

        DemoSection("Theme and content slot") {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                NexusTooltip(text = "Dark tooltip", theme = TooltipTheme.Dark) {
                    NexusButton(onClick = {}) { NexusText(text = "Dark") }
                }
                NexusTooltip(
                    text = "",
                    theme = TooltipTheme.Light,
                    popupContent = {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            NexusText(text = "Rich content")
                            NexusText(
                                text = "Line 2",
                                style = NexusTheme.typography.extraSmall,
                                color = NexusTheme.colorScheme.text.secondary,
                            )
                        }
                    },
                ) {
                    NexusButton(onClick = {}) { NexusText(text = "Rich") }
                }
            }
        }

        DemoSection("Advanced usage") {
            var disabled by remember { mutableStateOf(false) }
            val controlledState = rememberTooltipState()
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                NexusButton(
                    text = if (disabled) "Enable Tooltip" else "Disable Tooltip",
                    onClick = { disabled = !disabled },
                )
                NexusButton(
                    text = if (controlledState.visible) "Hide Controlled" else "Show Controlled",
                    onClick = { controlledState.visible = !controlledState.visible },
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                NexusTooltip(
                    text = "Click trigger tooltip",
                    trigger = TooltipTrigger.Click,
                    disabled = disabled,
                ) {
                    NexusButton(onClick = {}) { NexusText(text = "Click Trigger") }
                }
                NexusTooltip(
                    text = "Controlled tooltip",
                    state = controlledState,
                    visible = controlledState.visible,
                    trigger = TooltipTrigger.Hover,
                ) {
                    NexusButton(onClick = {}) { NexusText(text = "Controlled") }
                }
            }
        }
    }
}

@Composable
fun PopoverDemo() {
    DemoPage(title = "Popover", description = "A popup card shown by hover/click/focus/contextmenu trigger.") {
        DemoSection("Placement") {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                NexusPopover(title = "Top", content = "Popover top", placement = PopoverPlacement.Top) {
                    NexusButton(onClick = {}) { NexusText(text = "Top") }
                }
                NexusPopover(title = "Left End", content = "Popover left-end", placement = PopoverPlacement.LeftEnd) {
                    NexusButton(onClick = {}) { NexusText(text = "Left End") }
                }
                NexusPopover(title = "Right Start", content = "Popover right-start", placement = PopoverPlacement.RightStart) {
                    NexusButton(onClick = {}) { NexusText(text = "Right Start") }
                }
            }
        }

        DemoSection("Basic usage and trigger") {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                NexusPopover(
                    title = "Hover trigger",
                    content = "Default hover trigger.",
                    trigger = PopoverTrigger.Hover,
                ) {
                    NexusButton(onClick = {}) { NexusText(text = "Hover") }
                }
                NexusPopover(
                    title = "Click trigger",
                    content = "Click to toggle visibility.",
                    trigger = PopoverTrigger.Click,
                ) {
                    NexusButton(onClick = {}) { NexusText(text = "Click") }
                }
            }
        }

        DemoSection("Rich content and controlled") {
            val state = rememberPopoverState()
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                NexusPopover(
                    width = 260.dp,
                    title = "Nested operation",
                    popoverContent = {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            NexusText(text = "You can place forms/tables/actions here.")
                            NexusButton(text = "Close", onClick = { state.hide() }, size = io.github.xingray.compose_nexus.theme.ComponentSize.Small)
                        }
                    },
                    state = state,
                    visible = state.visible,
                    trigger = PopoverTrigger.Click,
                ) {
                    NexusButton(text = "Controlled Popover", onClick = { state.toggle() })
                }
            }
        }
    }
}

@Composable
fun PopconfirmDemo() {
    DemoPage(title = "Popconfirm", description = "A simple confirmation popup for click action.") {
        var result by remember { mutableStateOf("No action") }

        DemoSection("Basic usage") {
            NexusPopconfirm(
                title = "Are you sure to delete this?",
                onConfirm = { result = "confirmed" },
                onCancel = { result = "canceled" },
            ) {
                NexusButton(text = "Delete", onClick = {}, type = NexusType.Danger)
            }
            Spacer(modifier = Modifier.height(8.dp))
            NexusText(
                text = "result = $result",
                style = NexusTheme.typography.small,
                color = NexusTheme.colorScheme.text.secondary,
            )
        }

        DemoSection("Placement") {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                NexusPopconfirm(title = "top-start", placement = PopoverPlacement.TopStart) {
                    NexusButton(text = "Top Start", onClick = {})
                }
                NexusPopconfirm(title = "left", placement = PopoverPlacement.Left) {
                    NexusButton(text = "Left", onClick = {})
                }
                NexusPopconfirm(title = "bottom-end", placement = PopoverPlacement.BottomEnd) {
                    NexusButton(text = "Bottom End", onClick = {})
                }
            }
        }

        DemoSection("Customize actions") {
            NexusPopconfirm(
                title = "Custom action footer?",
                hideIcon = false,
                actions = { confirm, cancel ->
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        NexusButton(text = "No", onClick = cancel, size = io.github.xingray.compose_nexus.theme.ComponentSize.Small, textButton = true)
                        NexusButton(text = "Yes", onClick = confirm, size = io.github.xingray.compose_nexus.theme.ComponentSize.Small, type = NexusType.Success)
                    }
                },
                onConfirm = { result = "custom confirmed" },
                onCancel = { result = "custom canceled" },
            ) {
                NexusButton(text = "Custom Popconfirm", onClick = {})
            }
        }
    }
}

@Composable
fun TourDemo() {
    DemoPage(title = "Tour", description = "A popup component for guiding users through a product.") {
        DemoSection("Basic usage") {
            val basicState = rememberTourState(
                steps = listOf(
                    TourStep(
                        title = "Upload files",
                        description = "Use this action to upload resources.",
                        target = "tour-basic-upload",
                        placement = TourPlacement.BottomStart,
                    ),
                    TourStep(
                        title = "Run validation",
                        description = "Check content quality before submitting.",
                        target = "tour-basic-validate",
                        placement = TourPlacement.Bottom,
                    ),
                    TourStep(
                        title = "Submit",
                        description = "Everything is ready. Submit now.",
                        target = "tour-basic-submit",
                        placement = TourPlacement.BottomEnd,
                    ),
                ),
            )
            NexusTour(
                state = basicState,
                onFinish = {},
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                NexusTourTarget(state = basicState, target = "tour-basic-upload") {
                    NexusButton(text = "Upload", onClick = {})
                }
                NexusTourTarget(state = basicState, target = "tour-basic-validate") {
                    NexusButton(text = "Validate", onClick = {})
                }
                NexusTourTarget(state = basicState, target = "tour-basic-submit") {
                    NexusButton(text = "Submit", onClick = {}, type = NexusType.Primary)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            NexusButton(
                text = "Start Tour",
                onClick = { basicState.start() },
                type = NexusType.Primary,
            )
        }

        DemoSection("Non modal") {
            val nonModalState = rememberTourState(
                steps = listOf(
                    TourStep(
                        title = "Non modal guide",
                        description = "Mask disabled, keep interacting with the page.",
                        target = "tour-non-modal-btn",
                        placement = TourPlacement.Right,
                    ),
                ),
            )
            NexusTour(
                state = nonModalState,
                mask = false,
                type = TourType.Primary,
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                NexusTourTarget(state = nonModalState, target = "tour-non-modal-btn") {
                    NexusButton(text = "Target Button", onClick = {})
                }
                NexusButton(
                    text = "Start Non-modal Tour",
                    onClick = { nonModalState.start() },
                    type = NexusType.Primary,
                )
            }
        }

        DemoSection("Placement") {
            val placements = remember { TourPlacement.entries.toList() }
            var placementIndex by remember { mutableIntStateOf(0) }
            val currentPlacement = placements[placementIndex]
            val placementState = rememberTourState(
                steps = listOf(
                    TourStep(
                        title = "Current placement",
                        description = currentPlacement.name,
                        target = "tour-placement-target",
                        placement = currentPlacement,
                    ),
                ),
            )
            NexusTour(state = placementState)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                NexusTourTarget(state = placementState, target = "tour-placement-target") {
                    NexusButton(text = "Placement Target", onClick = {})
                }
                NexusButton(
                    text = "Change Placement",
                    onClick = {
                        placementIndex = (placementIndex + 1) % placements.size
                    },
                )
                NexusButton(
                    text = "Show",
                    onClick = { placementState.start() },
                    type = NexusType.Primary,
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            NexusText(
                text = "placement = ${currentPlacement.name}",
                color = NexusTheme.colorScheme.text.secondary,
                style = NexusTheme.typography.small,
            )
        }

        DemoSection("Custom mask style") {
            val maskState = rememberTourState(
                steps = listOf(
                    TourStep(
                        title = "Mask with custom style",
                        description = "Custom color and larger transparent gap.",
                        target = "tour-mask-target",
                        placement = TourPlacement.Top,
                    ),
                ),
            )
            NexusTour(
                state = maskState,
                maskColor = NexusTheme.colorScheme.primary.base.copy(alpha = 0.55f),
                gap = TourGap(offset = 12.dp, radius = 12.dp),
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                NexusTourTarget(state = maskState, target = "tour-mask-target") {
                    NexusButton(text = "Mask Target", onClick = {})
                }
                NexusButton(
                    text = "Show Mask Tour",
                    onClick = { maskState.start() },
                    type = NexusType.Primary,
                )
            }
        }

        DemoSection("Custom indicator") {
            val indicatorState = rememberTourState(
                steps = listOf(
                    TourStep("Step A", "Introduction", target = "tour-indicator-target", placement = TourPlacement.Bottom),
                    TourStep("Step B", "Details", target = "tour-indicator-target", placement = TourPlacement.Bottom),
                    TourStep("Step C", "Done", target = "tour-indicator-target", placement = TourPlacement.Bottom),
                ),
            )
            NexusTour(
                state = indicatorState,
                indicators = { current, total ->
                    NexusTag(
                        text = "Step ${current + 1} / $total",
                        type = NexusType.Primary,
                        size = io.github.xingray.compose_nexus.theme.ComponentSize.Small,
                    )
                },
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                NexusTourTarget(state = indicatorState, target = "tour-indicator-target") {
                    NexusButton(text = "Indicator Target", onClick = {})
                }
                NexusButton(
                    text = "Start Indicator Tour",
                    onClick = { indicatorState.start() },
                    type = NexusType.Primary,
                )
            }
        }

        DemoSection("Target") {
            val centerState = rememberTourState(
                steps = listOf(
                    TourStep(
                        title = "No target",
                        description = "When target is empty, card is centered.",
                    ),
                ),
            )
            val targetState = rememberTourState(
                steps = listOf(
                    TourStep(
                        title = "String target id",
                        description = "Bind by target id registered via NexusTourTarget.",
                        target = "tour-target-id",
                        placement = TourPlacement.RightStart,
                    ),
                ),
            )
            NexusTour(state = centerState)
            NexusTour(
                state = targetState,
                onClose = {},
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                NexusButton(text = "Show Center Tour", onClick = { centerState.start() })
                NexusTourTarget(state = targetState, target = "tour-target-id") {
                    Box(modifier = Modifier.padding(horizontal = 2.dp)) {
                        NexusButton(text = "Target By Id", onClick = {}, type = NexusType.Primary)
                    }
                }
                NexusButton(text = "Show Target Tour", onClick = { targetState.start() })
            }
        }
    }
}
