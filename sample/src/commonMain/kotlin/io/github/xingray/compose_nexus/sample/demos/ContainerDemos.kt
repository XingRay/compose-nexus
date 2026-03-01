package io.github.xingray.compose_nexus.sample.demos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.xingray.compose_nexus.containers.*
import io.github.xingray.compose_nexus.controls.NexusButton
import io.github.xingray.compose_nexus.controls.NexusText
import io.github.xingray.compose_nexus.theme.NexusTheme
import io.github.xingray.compose_nexus.theme.NexusType

@Composable
fun CardDemo() {
    DemoPage(title = "Card", description = "Integrate information in a card container.") {
        DemoSection("Basic usage") {
            NexusCard(
                header = { NexusText(text = "Card title") },
            ) {
                NexusText(text = "Card body content goes here. This is a simple card with header and body.")
            }
        }
        DemoSection("Shadow variants") {
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
                NexusCollapseItem(name = "1", title = { NexusText(text = "Consistency") }) {
                    NexusText(text = "Consistent with real life: in line with the process and logic of real life, and comply with languages and habits that the users are used to.")
                }
                NexusCollapseItem(name = "2", title = { NexusText(text = "Feedback") }) {
                    NexusText(text = "Operation feedback: enable the users to clearly perceive their operations by style updates and interactive effects.")
                }
                NexusCollapseItem(name = "3", title = { NexusText(text = "Efficiency") }) {
                    NexusText(text = "Simplify the process: keep operating process simple and intuitive.")
                }
            }
        }
        DemoSection("Accordion") {
            val state = rememberCollapseState(accordion = true)
            NexusCollapse(state = state) {
                NexusCollapseItem(name = "a", title = { NexusText(text = "Panel A") }) { NexusText(text = "Content A") }
                NexusCollapseItem(name = "b", title = { NexusText(text = "Panel B") }) { NexusText(text = "Content B") }
                NexusCollapseItem(name = "c", title = { NexusText(text = "Panel C") }) { NexusText(text = "Content C") }
            }
        }
    }
}

@Composable
fun DialogDemo() {
    DemoPage(title = "Dialog", description = "Informs users while preserving the current page state.") {
        DemoSection("Basic usage") {
            val state = rememberDialogState()
            NexusButton(onClick = { state.open() }, type = NexusType.Primary) {
                NexusText(text = "Open Dialog")
            }
            NexusDialog(
                state = state,
                title = "Dialog Title",
                footer = {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        NexusButton(onClick = { state.close() }) { NexusText(text = "Cancel") }
                        NexusButton(onClick = { state.close() }, type = NexusType.Primary) { NexusText(text = "Confirm") }
                    }
                },
            ) {
                NexusText(text = "This is the dialog content. You can put any composable here.")
            }
        }
    }
}

@Composable
fun DrawerDemo() {
    DemoPage(title = "Drawer", description = "A panel which slides in from the edge of the screen.") {
        DemoSection("Basic usage") {
            val state = rememberDialogState()
            NexusButton(onClick = { state.open() }, type = NexusType.Primary) {
                NexusText(text = "Open Drawer")
            }
            NexusDrawer(state = state, title = "Drawer Title") {
                NexusText(text = "Drawer content goes here. Slide in from the right edge.")
            }
        }
    }
}

@Composable
fun CarouselDemo() {
    DemoPage(title = "Carousel", description = "Loop a series of images or texts in a limited space.") {
        DemoSection("Basic usage") {
            val colors = listOf(
                NexusTheme.colorScheme.primary.light7,
                NexusTheme.colorScheme.success.light7,
                NexusTheme.colorScheme.warning.light7,
                NexusTheme.colorScheme.danger.light7,
            )
            val state = rememberCarouselState(itemCount = 4)
            NexusCarousel(state = state, height = 200.dp) { index ->
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
    }
}
