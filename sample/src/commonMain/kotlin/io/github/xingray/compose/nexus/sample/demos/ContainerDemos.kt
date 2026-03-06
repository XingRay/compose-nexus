package io.github.xingray.compose.nexus.sample.demos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.xingray.compose.nexus.containers.CardShadow
import io.github.xingray.compose.nexus.containers.CarouselArrow
import io.github.xingray.compose.nexus.containers.CarouselDirection
import io.github.xingray.compose.nexus.containers.CarouselIndicatorPosition
import io.github.xingray.compose.nexus.containers.CarouselTrigger
import io.github.xingray.compose.nexus.containers.CarouselType
import io.github.xingray.compose.nexus.containers.CollapseExpandIconPosition
import io.github.xingray.compose.nexus.containers.DialogTransition
import io.github.xingray.compose.nexus.containers.DrawerDirection
import io.github.xingray.compose.nexus.containers.NexusCard
import io.github.xingray.compose.nexus.containers.NexusCarousel
import io.github.xingray.compose.nexus.containers.NexusCollapse
import io.github.xingray.compose.nexus.containers.NexusCollapseItem
import io.github.xingray.compose.nexus.containers.NexusDialog
import io.github.xingray.compose.nexus.containers.NexusDrawer
import io.github.xingray.compose.nexus.containers.rememberCarouselState
import io.github.xingray.compose.nexus.containers.rememberCollapseState
import io.github.xingray.compose.nexus.containers.rememberDialogState
import io.github.xingray.compose.nexus.controls.NexusButton
import io.github.xingray.compose.nexus.controls.NexusText
import io.github.xingray.compose.nexus.theme.ComponentSize
import io.github.xingray.compose.nexus.theme.NexusTheme
import io.github.xingray.compose.nexus.theme.NexusType

@Composable
fun CardDemo() {
    DemoPage(title = "Card", description = "Integrate information in a card container.") {
        DemoSection("Basic usage") {
            NexusCard(
                header = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        NexusText(text = "Card name")
                        NexusButton(
                            text = "Operation",
                            onClick = {},
                            type = NexusType.Primary,
                            size = ComponentSize.Small,
                            textButton = true,
                        )
                    }
                },
                footer = {
                    NexusText(
                        text = "Footer content",
                        color = NexusTheme.colorScheme.text.secondary,
                        style = NexusTheme.typography.small,
                    )
                },
            ) {
                NexusText(text = "Card content")
            }
        }

        DemoSection("Simple card") {
            NexusCard {
                NexusText(text = "This is a simple card.")
            }
        }

        DemoSection("With images") {
            NexusCard(
                bodyPadding = 0.dp,
            ) {
                Column {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .background(NexusTheme.colorScheme.primary.light8),
                        contentAlignment = Alignment.Center,
                    ) {
                        NexusText(
                            text = "Image Area",
                            color = NexusTheme.colorScheme.primary.base,
                        )
                    }
                    Column(modifier = Modifier.padding(16.dp)) {
                        NexusText(text = "Yummy hamburger")
                        Spacer(modifier = Modifier.height(8.dp))
                        NexusText(
                            text = "A rich card example using body-style like zero body padding.",
                            color = NexusTheme.colorScheme.text.secondary,
                            style = NexusTheme.typography.small,
                        )
                    }
                }
            }
        }

        DemoSection("Shadow") {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                NexusCard(
                    header = { NexusText(text = "Always") },
                    shadow = CardShadow.Always,
                    modifier = Modifier.weight(1f),
                ) { NexusText(text = "Shadow always visible") }

                NexusCard(
                    header = { NexusText(text = "Hover") },
                    shadow = CardShadow.Hover,
                    modifier = Modifier.weight(1f),
                ) { NexusText(text = "Shadow on hover") }

                NexusCard(
                    header = { NexusText(text = "Never") },
                    shadow = CardShadow.Never,
                    modifier = Modifier.weight(1f),
                ) { NexusText(text = "No shadow") }
            }
        }
    }
}

@Composable
fun CollapseDemo() {
    DemoPage(title = "Collapse", description = "Use Collapse to store contents.") {
        DemoSection("Basic usage") {
            val state = rememberCollapseState()
            NexusCollapse(state = state) {
                NexusCollapseItem(name = "1", titleText = "Consistency") {
                    NexusText(text = "Consistent with real life: in line with the process and logic of real life, and comply with languages and habits that the users are used to.")
                }
                NexusCollapseItem(name = "2", titleText = "Feedback") {
                    NexusText(text = "Operation feedback: enable the users to clearly perceive their operations by style updates and interactive effects.")
                }
                NexusCollapseItem(name = "3", titleText = "Efficiency") {
                    NexusText(text = "Simplify the process: keep operating process simple and intuitive.")
                }
            }
        }
        DemoSection("Accordion") {
            val state = rememberCollapseState(accordion = true)
            NexusCollapse(state = state) {
                NexusCollapseItem(name = "a", titleText = "Panel A") { NexusText(text = "Content A") }
                NexusCollapseItem(name = "b", titleText = "Panel B") { NexusText(text = "Content B") }
                NexusCollapseItem(name = "c", titleText = "Panel C") { NexusText(text = "Content C") }
            }
        }

        DemoSection("Custom title") {
            val state = rememberCollapseState(initialExpanded = setOf("x"))
            NexusCollapse(state = state) {
                NexusCollapseItem(
                    name = "x",
                    titleSlot = { isActive ->
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            NexusText(text = "Custom title slot")
                            NexusText(
                                text = if (isActive) "Opened" else "Closed",
                                color = if (isActive) NexusTheme.colorScheme.primary.base else NexusTheme.colorScheme.text.secondary,
                                style = NexusTheme.typography.small,
                            )
                        }
                    },
                ) {
                    NexusText(text = "Title slot receives current active status.")
                }
                NexusCollapseItem(name = "y", titleText = "Normal item") {
                    NexusText(text = "Another panel")
                }
            }
        }

        DemoSection("Custom icon") {
            val state = rememberCollapseState()
            NexusCollapse(state = state) {
                NexusCollapseItem(
                    name = "i1",
                    titleText = "Custom icon panel",
                    iconSlot = { isActive ->
                        NexusText(
                            text = if (isActive) "−" else "+",
                            color = NexusTheme.colorScheme.primary.base,
                            style = NexusTheme.typography.base,
                        )
                    },
                ) {
                    NexusText(text = "Custom icon slot controls icon rendering.")
                }
                NexusCollapseItem(name = "i2", titleText = "Default icon panel") {
                    NexusText(text = "Default icon on the right side.")
                }
            }
        }

        DemoSection("Custom icon position") {
            val state = rememberCollapseState(initialExpanded = setOf("p1"))
            NexusCollapse(
                state = state,
                expandIconPosition = CollapseExpandIconPosition.Left,
            ) {
                NexusCollapseItem(name = "p1", titleText = "Icon on the left") {
                    NexusText(text = "Expand icon position is controlled by collapse attribute.")
                }
                NexusCollapseItem(name = "p2", titleText = "Still left") {
                    NexusText(text = "Second panel with left icon.")
                }
            }
        }

        DemoSection("Prevent collapsing") {
            var allowCollapse by remember { mutableStateOf(false) }
            val state = rememberCollapseState(initialExpanded = setOf("guard"))
            NexusButton(
                text = if (allowCollapse) "Lock Collapse" else "Allow Collapse",
                onClick = { allowCollapse = !allowCollapse },
                size = ComponentSize.Small,
            )
            Spacer(modifier = Modifier.height(8.dp))
            NexusText(
                text = if (allowCollapse) "Current state: allow collapsing" else "Current state: prevent collapsing",
                color = NexusTheme.colorScheme.text.secondary,
                style = NexusTheme.typography.small,
            )
            Spacer(modifier = Modifier.height(8.dp))
            NexusCollapse(
                state = state,
                beforeCollapse = { allowCollapse },
            ) {
                NexusCollapseItem(name = "guard", titleText = "Guarded panel") {
                    NexusText(text = "This panel can be toggled only when allowCollapse=true.")
                }
            }
        }
    }
}

@Composable
fun DialogDemo() {
    DemoPage(title = "Dialog", description = "Informs users while preserving the current page state.") {
        DemoSection("Basic usage") {
            val state = rememberDialogState()
            var allowClose by remember { mutableStateOf(false) }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                NexusButton(
                    text = "Open Dialog",
                    onClick = { state.open() },
                    type = NexusType.Primary
                )
                NexusButton(
                    text = if (allowClose) "Block Before-Close" else "Allow Before-Close",
                    onClick = { allowClose = !allowClose },
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            NexusText(
                text = "beforeClose when clicking close icon or overlay: $allowClose",
                style = NexusTheme.typography.small,
                color = NexusTheme.colorScheme.text.secondary,
            )
            NexusDialog(
                state = state,
                title = "Dialog Title",
                beforeClose = { done -> if (allowClose) done() },
                footer = {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        NexusButton(text = "Cancel", onClick = { state.close() })
                        NexusButton(
                            text = "Confirm",
                            onClick = { state.close() },
                            type = NexusType.Primary
                        )
                    }
                },
            ) {
                NexusText(text = "This is the dialog content. You can put any composable here.")
            }
        }

        DemoSection("Customized content") {
            val state = rememberDialogState()
            NexusButton(
                text = "Open Content Dialog",
                onClick = { state.open() },
                type = NexusType.Primary
            )
            NexusDialog(
                state = state,
                title = "Edit Profile",
                width = 560.dp,
                footer = {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        NexusButton(text = "Reset", onClick = { })
                        NexusButton(
                            text = "Save",
                            onClick = { state.close() },
                            type = NexusType.Primary
                        )
                    }
                },
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    NexusText(text = "Name: Compose Nexus")
                    NexusText(text = "Role: UI Engineer")
                    NexusText(text = "Description: Dialog content can host forms, tables and any composables.")
                }
            }
        }

        DemoSection("Customized header") {
            val state = rememberDialogState()
            NexusButton(
                text = "Open Custom Header",
                onClick = { state.open() },
                type = NexusType.Primary
            )
            NexusDialog(
                state = state,
                title = "Custom Header",
                header = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        NexusText(
                            text = "Header Slot Content",
                            color = NexusTheme.colorScheme.primary.base
                        )
                        NexusText(
                            text = "v2.0",
                            style = NexusTheme.typography.small,
                            color = NexusTheme.colorScheme.text.secondary
                        )
                    }
                },
                closeIcon = {
                    NexusText(
                        text = "◉",
                        color = NexusTheme.colorScheme.primary.base
                    )
                },
                footer = {
                    NexusButton(
                        text = "Done",
                        onClick = { state.close() },
                        type = NexusType.Primary
                    )
                },
            ) {
                NexusText(text = "Header and close icon are both customized.")
            }
        }

        DemoSection("Nested dialog") {
            val outer = rememberDialogState()
            val inner = rememberDialogState()
            NexusButton(
                text = "Open Outer Dialog",
                onClick = { outer.open() },
                type = NexusType.Primary
            )
            NexusDialog(
                state = outer,
                title = "Outer Dialog",
                width = 560.dp,
                footer = {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        NexusButton(text = "Close Outer", onClick = { outer.close() })
                        NexusButton(
                            text = "Open Inner",
                            onClick = { inner.open() },
                            type = NexusType.Primary
                        )
                    }
                },
            ) {
                NexusText(text = "Open another dialog from here to simulate nested usage.")
            }
            NexusDialog(
                state = inner,
                title = "Inner Dialog",
                width = 420.dp,
                footer = {
                    NexusButton(
                        text = "Close Inner",
                        onClick = { inner.close() },
                        type = NexusType.Primary
                    )
                },
            ) {
                NexusText(text = "Nested dialog content.")
            }
        }

        DemoSection("Centered content") {
            val state = rememberDialogState()
            NexusButton(
                text = "Open Centered Dialog",
                onClick = { state.open() },
                type = NexusType.Primary
            )
            NexusDialog(
                state = state,
                title = "Centered Header/Footer",
                center = true,
                footer = {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        NexusButton(text = "Cancel", onClick = { state.close() })
                        NexusButton(
                            text = "Confirm",
                            onClick = { state.close() },
                            type = NexusType.Primary
                        )
                    }
                },
            ) {
                NexusText(text = "The body remains freely composable.")
            }
        }

        DemoSection("Align center dialog") {
            val state = rememberDialogState()
            NexusButton(
                text = "Open Align-Center Dialog",
                onClick = { state.open() },
                type = NexusType.Primary
            )
            NexusDialog(
                state = state,
                title = "Aligned Center",
                alignCenter = true,
                footer = {
                    NexusButton(
                        text = "Close",
                        onClick = { state.close() },
                        type = NexusType.Primary
                    )
                },
            ) {
                NexusText(text = "Dialog is centered both horizontally and vertically.")
            }
        }

        DemoSection("Destroy on close") {
            val state = rememberDialogState()
            var openedCount by remember { mutableIntStateOf(0) }
            NexusButton(
                text = "Open Destroy Dialog",
                onClick = {
                    openedCount += 1
                    state.open()
                },
                type = NexusType.Primary,
            )
            NexusDialog(
                state = state,
                title = "Destroy On Close",
                destroyOnClose = true,
                footer = {
                    NexusButton(
                        text = "Close",
                        onClick = { state.close() },
                        type = NexusType.Primary
                    )
                },
            ) {
                var localCounter by remember { mutableIntStateOf(0) }
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    NexusText(text = "open count = $openedCount")
                    NexusText(text = "local counter = $localCounter")
                    NexusButton(text = "Increase local counter", onClick = { localCounter += 1 })
                }
            }
        }

        DemoSection("Draggable dialog") {
            val state = rememberDialogState()
            var overflow by remember { mutableStateOf(false) }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                NexusButton(
                    text = "Open Draggable",
                    onClick = { state.open() },
                    type = NexusType.Primary
                )
                NexusButton(
                    text = if (overflow) "Disable Overflow" else "Enable Overflow",
                    onClick = { overflow = !overflow },
                )
                NexusButton(text = "Reset Position", onClick = { state.resetPosition() })
            }
            NexusDialog(
                state = state,
                title = "Drag me by header",
                draggable = true,
                overflow = overflow,
                footer = {
                    NexusButton(
                        text = "Close",
                        onClick = { state.close() },
                        type = NexusType.Primary
                    )
                },
            ) {
                NexusText(text = "overflow=$overflow")
            }
        }

        DemoSection("Fullscreen / Modal / Events") {
            val fullscreenState = rememberDialogState()
            val nonModalState = rememberDialogState()
            val eventsState = rememberDialogState()
            val events = remember { mutableStateListOf<String>() }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                NexusButton(
                    text = "Open Fullscreen",
                    onClick = { fullscreenState.open() },
                    type = NexusType.Primary
                )
                NexusButton(text = "Open Non-Modal", onClick = { nonModalState.open() })
                NexusButton(text = "Open Events Dialog", onClick = { eventsState.open() })
            }

            NexusDialog(
                state = fullscreenState,
                title = "Fullscreen Dialog",
                fullscreen = true,
                footer = {
                    NexusButton(
                        text = "Close",
                        onClick = { fullscreenState.close() },
                        type = NexusType.Primary
                    )
                },
            ) {
                NexusText(text = "When fullscreen=true, width/top/draggable are ignored.")
            }

            NexusDialog(
                state = nonModalState,
                title = "Non-Modal Dialog",
                modal = false,
                modalPenetrable = true,
                closeOnClickModal = false,
                footer = {
                    NexusButton(
                        text = "Close",
                        onClick = { nonModalState.close() },
                        type = NexusType.Primary
                    )
                },
            ) {
                NexusText(text = "No overlay. Background remains visible and penetrable.")
            }

            NexusDialog(
                state = eventsState,
                title = "Events + Transition",
                transition = DialogTransition.SlideUp,
                onOpen = { events.add("open") },
                onOpened = { events.add("opened") },
                onClose = { events.add("close") },
                onClosed = { events.add("closed") },
                onOpenAutoFocus = { events.add("open-auto-focus") },
                onCloseAutoFocus = { events.add("close-auto-focus") },
                footer = {
                    NexusButton(
                        text = "Close",
                        onClick = { eventsState.close() },
                        type = NexusType.Primary
                    )
                },
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    NexusText(text = "Open and close the dialog to inspect callback order.")
                    NexusText(
                        text = "Events: ${events.takeLast(8).joinToString(", ")}",
                        style = NexusTheme.typography.small,
                        color = NexusTheme.colorScheme.text.secondary,
                    )
                }
            }
        }
    }
}

@Composable
fun DrawerDemo() {
    DemoPage(title = "Drawer", description = "A panel which slides in from the edge of the screen.") {
        DemoSection("Basic usage") {
            val state = rememberDialogState()
            var direction by remember { mutableStateOf(DrawerDirection.Right) }
            var allowClose by remember { mutableStateOf(false) }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                NexusButton(
                    text = "Open Drawer",
                    onClick = { state.open() },
                    type = NexusType.Primary
                )
                NexusButton(
                    text = "Direction: ${direction.name}",
                    onClick = {
                        direction = when (direction) {
                            DrawerDirection.Right -> DrawerDirection.Left
                            DrawerDirection.Left -> DrawerDirection.Top
                            DrawerDirection.Top -> DrawerDirection.Bottom
                            DrawerDirection.Bottom -> DrawerDirection.Right
                        }
                    },
                )
                NexusButton(
                    text = if (allowClose) "Block Before-Close" else "Allow Before-Close",
                    onClick = { allowClose = !allowClose },
                )
            }
            NexusDrawer(
                state = state,
                title = "Drawer Title",
                direction = direction,
                beforeClose = { done -> done(!allowClose) },
                footer = {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        NexusButton(text = "Cancel", onClick = { state.close() })
                        NexusButton(
                            text = "Confirm",
                            onClick = { state.close() },
                            type = NexusType.Primary
                        )
                    }
                },
            ) {
                NexusText(text = "Drawer content goes here. Current direction=${direction.name}")
            }
        }

        DemoSection("No title") {
            val state = rememberDialogState()
            NexusButton(
                text = "Open Headerless Drawer",
                onClick = { state.open() },
                type = NexusType.Primary
            )
            NexusDrawer(
                state = state,
                withHeader = false,
                size = 360.dp,
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    NexusText(text = "withHeader=false")
                    NexusText(text = "Title and header area are hidden.")
                    NexusButton(
                        text = "Close",
                        onClick = { state.close() },
                        type = NexusType.Primary
                    )
                }
            }
        }

        DemoSection("Customized content") {
            val state = rememberDialogState()
            NexusButton(
                text = "Open Custom Content",
                onClick = { state.open() },
                type = NexusType.Primary
            )
            NexusDrawer(
                state = state,
                title = "Custom Form",
                size = 420.dp,
                footer = {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        NexusButton(text = "Reset", onClick = { })
                        NexusButton(
                            text = "Submit",
                            onClick = { state.close() },
                            type = NexusType.Primary
                        )
                    }
                },
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    NexusText(text = "Name: Compose Nexus")
                    NexusText(text = "Email: demo@example.com")
                    NexusText(text = "Drawer can hold forms and complex content.")
                }
            }
        }

        DemoSection("Customized header") {
            val state = rememberDialogState()
            NexusButton(
                text = "Open Custom Header",
                onClick = { state.open() },
                type = NexusType.Primary
            )
            NexusDrawer(
                state = state,
                title = "Custom Header",
                header = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        NexusText(
                            text = "Header Slot",
                            color = NexusTheme.colorScheme.primary.base
                        )
                        NexusText(
                            text = "Beta",
                            style = NexusTheme.typography.small,
                            color = NexusTheme.colorScheme.text.secondary
                        )
                    }
                },
                closeIcon = {
                    NexusText(
                        text = "◉",
                        color = NexusTheme.colorScheme.primary.base
                    )
                },
            ) {
                NexusText(text = "Header and close icon have been customized.")
            }
        }

        DemoSection("Resizable drawer") {
            val state = rememberDialogState()
            val logs = remember { mutableStateListOf<String>() }
            NexusButton(
                text = "Open Resizable Drawer",
                onClick = { state.open() },
                type = NexusType.Primary
            )
            NexusDrawer(
                state = state,
                title = "Resizable",
                resizable = true,
                onResizeStart = { logs.add("resize-start: $it") },
                onResize = { logs.add("resize: $it") },
                onResizeEnd = { logs.add("resize-end: $it") },
                footer = {
                    NexusButton(
                        text = "Close",
                        onClick = { state.close() },
                        type = NexusType.Primary
                    )
                },
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    NexusText(text = "Try dragging the edge handle to resize.")
                    NexusText(
                        text = logs.takeLast(3).joinToString(" | "),
                        style = NexusTheme.typography.small,
                        color = NexusTheme.colorScheme.text.secondary,
                    )
                }
            }
        }

        DemoSection("Nested drawer") {
            val outer = rememberDialogState()
            val inner = rememberDialogState()
            NexusButton(
                text = "Open Outer Drawer",
                onClick = { outer.open() },
                type = NexusType.Primary
            )
            NexusDrawer(
                state = outer,
                title = "Outer Drawer",
                size = 420.dp,
                footer = {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        NexusButton(text = "Close Outer", onClick = { outer.close() })
                        NexusButton(
                            text = "Open Inner",
                            onClick = { inner.open() },
                            type = NexusType.Primary
                        )
                    }
                },
            ) {
                NexusText(text = "Open inner drawer to simulate nested layers.")
            }
            NexusDrawer(
                state = inner,
                title = "Inner Drawer",
                size = 320.dp,
                footer = {
                    NexusButton(
                        text = "Close Inner",
                        onClick = { inner.close() },
                        type = NexusType.Primary
                    )
                },
            ) {
                NexusText(text = "Nested drawer content.")
            }
        }

        DemoSection("Modal / destroy-on-close / events") {
            val modalState = rememberDialogState()
            val nonModalState = rememberDialogState()
            val eventState = rememberDialogState()
            val events = remember { mutableStateListOf<String>() }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                NexusButton(
                    text = "Open Modal Drawer",
                    onClick = { modalState.open() },
                    type = NexusType.Primary
                )
                NexusButton(text = "Open Non-Modal", onClick = { nonModalState.open() })
                NexusButton(text = "Open Events Drawer", onClick = { eventState.open() })
            }

            NexusDrawer(
                state = modalState,
                title = "Destroy On Close",
                destroyOnClose = true,
                footer = {
                    NexusButton(
                        text = "Close",
                        onClick = { modalState.close() },
                        type = NexusType.Primary
                    )
                },
            ) {
                var localCounter by remember { mutableIntStateOf(0) }
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    NexusText(text = "local counter = $localCounter")
                    NexusButton(text = "Increase", onClick = { localCounter += 1 })
                }
            }

            NexusDrawer(
                state = nonModalState,
                title = "Non-Modal Drawer",
                modal = false,
                modalPenetrable = true,
                closeOnClickModal = false,
                footer = {
                    NexusButton(
                        text = "Close",
                        onClick = { nonModalState.close() },
                        type = NexusType.Primary
                    )
                },
            ) {
                NexusText(text = "No overlay mask. Background remains visible.")
            }

            NexusDrawer(
                state = eventState,
                title = "Events",
                onOpen = { events.add("open") },
                onOpened = { events.add("opened") },
                onClose = { events.add("close") },
                onClosed = { events.add("closed") },
                onOpenAutoFocus = { events.add("open-auto-focus") },
                onCloseAutoFocus = { events.add("close-auto-focus") },
                footer = {
                    NexusButton(
                        text = "Close",
                        onClick = { eventState.close() },
                        type = NexusType.Primary
                    )
                },
            ) {
                NexusText(
                    text = "Events: ${events.takeLast(8).joinToString(", ")}",
                    style = NexusTheme.typography.small,
                    color = NexusTheme.colorScheme.text.secondary,
                )
            }
        }
    }
}

@Composable
fun CarouselDemo() {
    DemoPage(title = "Carousel", description = "Loop a series of images or texts in a limited space.") {
        val colors = listOf(
            NexusTheme.colorScheme.primary.light7,
            NexusTheme.colorScheme.success.light7,
            NexusTheme.colorScheme.warning.light7,
            NexusTheme.colorScheme.danger.light7,
        )

        DemoSection("Basic usage") {
            val state = rememberCarouselState(itemCount = 4)
            NexusCarousel(
                state = state,
                height = 200.dp,
                trigger = CarouselTrigger.Click,
            ) { index ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(colors[index]),
                    contentAlignment = Alignment.Center,
                ) {
                    NexusText(
                        text = "Slide ${index + 1}",
                        style = NexusTheme.typography.extraLarge,
                    )
                }
            }
        }

        DemoSection("Motion blur") {
            val state = rememberCarouselState(itemCount = 4)
            NexusCarousel(
                state = state,
                height = 180.dp,
                motionBlur = true,
                interval = 1600L,
            ) { index ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .background(colors[index]),
                    contentAlignment = Alignment.Center,
                ) {
                    NexusText(text = "Motion ${index + 1}")
                }
            }
        }

        DemoSection("Indicators") {
            val outside = rememberCarouselState(itemCount = 4)
            NexusCarousel(
                state = outside,
                height = 170.dp,
                indicatorPosition = CarouselIndicatorPosition.Outside,
                arrow = CarouselArrow.Never,
            ) { index ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(170.dp)
                        .background(colors[index]),
                    contentAlignment = Alignment.Center,
                ) {
                    NexusText(text = "Outside ${index + 1}")
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            val none = rememberCarouselState(itemCount = 4)
            NexusCarousel(
                state = none,
                height = 170.dp,
                indicatorPosition = CarouselIndicatorPosition.None,
            ) { index ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(170.dp)
                        .background(colors[index]),
                    contentAlignment = Alignment.Center,
                ) {
                    NexusText(text = "None ${index + 1}")
                }
            }
        }

        DemoSection("Arrows") {
            val always = rememberCarouselState(itemCount = 4)
            NexusCarousel(
                state = always,
                height = 160.dp,
                arrow = CarouselArrow.Always,
            ) { index ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .background(colors[index]),
                    contentAlignment = Alignment.Center,
                ) {
                    NexusText(text = "Always ${index + 1}")
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            val hover = rememberCarouselState(itemCount = 4)
            NexusCarousel(
                state = hover,
                height = 160.dp,
                arrow = CarouselArrow.Hover,
            ) { index ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .background(colors[index]),
                    contentAlignment = Alignment.Center,
                ) {
                    NexusText(text = "Hover ${index + 1}")
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            val never = rememberCarouselState(itemCount = 4)
            NexusCarousel(
                state = never,
                height = 160.dp,
                arrow = CarouselArrow.Never,
            ) { index ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .background(colors[index]),
                    contentAlignment = Alignment.Center,
                ) {
                    NexusText(text = "Never ${index + 1}")
                }
            }
        }

        DemoSection("Auto height") {
            val state = rememberCarouselState(itemCount = 3)
            NexusCarousel(
                state = state,
                height = null,
                indicatorPosition = CarouselIndicatorPosition.Outside,
            ) { index ->
                val slideHeight = when (index) {
                    0 -> 120.dp
                    1 -> 180.dp
                    else -> 240.dp
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(slideHeight)
                        .background(colors[index]),
                    contentAlignment = Alignment.Center,
                ) {
                    NexusText(text = "Auto Height ${index + 1}")
                }
            }
        }

        DemoSection("Card mode") {
            val state = rememberCarouselState(itemCount = 4)
            NexusCarousel(
                state = state,
                height = 180.dp,
                type = CarouselType.Card,
                autoplay = false,
                trigger = CarouselTrigger.Click,
            ) { index ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .background(colors[index]),
                    contentAlignment = Alignment.Center,
                ) {
                    NexusText(text = "Card ${index + 1}")
                }
            }
        }

        DemoSection("Vertical") {
            val state = rememberCarouselState(itemCount = 4)
            NexusCarousel(
                state = state,
                height = 220.dp,
                direction = CarouselDirection.Vertical,
                arrow = CarouselArrow.Always,
            ) { index ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .background(colors[index]),
                    contentAlignment = Alignment.Center,
                ) {
                    NexusText(text = "Vertical ${index + 1}")
                }
            }
        }
    }
}
