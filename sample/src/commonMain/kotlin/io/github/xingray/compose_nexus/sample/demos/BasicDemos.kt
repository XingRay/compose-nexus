package io.github.xingray.compose_nexus.sample.demos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.xingray.compose_nexus.controls.*
import io.github.xingray.compose_nexus.theme.ComponentSize
import io.github.xingray.compose_nexus.theme.NexusTheme
import io.github.xingray.compose_nexus.theme.NexusType

@Composable
fun ButtonDemo() {
    DemoPage(title = "Button", description = "Commonly used button.") {
        DemoSection("Basic usage") {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                NexusButton(onClick = {}) { NexusText(text = "Default") }
                NexusButton(onClick = {}, type = NexusType.Primary) { NexusText(text = "Primary") }
                NexusButton(onClick = {}, type = NexusType.Success) { NexusText(text = "Success") }
                NexusButton(onClick = {}, type = NexusType.Warning) { NexusText(text = "Warning") }
                NexusButton(onClick = {}, type = NexusType.Danger) { NexusText(text = "Danger") }
                NexusButton(onClick = {}, type = NexusType.Info) { NexusText(text = "Info") }
            }
        }
        DemoSection("Plain buttons") {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                NexusButton(onClick = {}, plain = true) { NexusText(text = "Default") }
                NexusButton(onClick = {}, type = NexusType.Primary, plain = true) { NexusText(text = "Primary") }
                NexusButton(onClick = {}, type = NexusType.Success, plain = true) { NexusText(text = "Success") }
                NexusButton(onClick = {}, type = NexusType.Warning, plain = true) { NexusText(text = "Warning") }
                NexusButton(onClick = {}, type = NexusType.Danger, plain = true) { NexusText(text = "Danger") }
            }
        }
        DemoSection("Round buttons") {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                NexusButton(onClick = {}, type = NexusType.Primary, round = true) { NexusText(text = "Primary") }
                NexusButton(onClick = {}, type = NexusType.Success, round = true) { NexusText(text = "Success") }
                NexusButton(onClick = {}, type = NexusType.Info, round = true) { NexusText(text = "Info") }
            }
        }
        DemoSection("Sizes") {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                NexusButton(onClick = {}, type = NexusType.Primary, size = ComponentSize.Large) { NexusText(text = "Large") }
                NexusButton(onClick = {}, type = NexusType.Primary, size = ComponentSize.Default) { NexusText(text = "Default") }
                NexusButton(onClick = {}, type = NexusType.Primary, size = ComponentSize.Small) { NexusText(text = "Small") }
            }
        }
        DemoSection("Disabled & Loading") {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                NexusButton(onClick = {}, type = NexusType.Primary, disabled = true) { NexusText(text = "Disabled") }
                NexusButton(onClick = {}, type = NexusType.Primary, loading = true) { NexusText(text = "Loading") }
            }
        }
    }
}

@Composable
fun TextDemo() {
    DemoPage(title = "Text", description = "Semantic text with type colors.") {
        DemoSection("Type colors") {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                NexusText(text = "Default text")
                NexusText(text = "Primary text", type = NexusType.Primary)
                NexusText(text = "Success text", type = NexusType.Success)
                NexusText(text = "Warning text", type = NexusType.Warning)
                NexusText(text = "Danger text", type = NexusType.Danger)
                NexusText(text = "Info text", type = NexusType.Info)
            }
        }
        DemoSection("Typography sizes") {
            val typography = NexusTheme.typography
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                NexusText(text = "Extra Large (20sp)", style = typography.extraLarge)
                NexusText(text = "Large (18sp)", style = typography.large)
                NexusText(text = "Medium (16sp)", style = typography.medium)
                NexusText(text = "Base (14sp)", style = typography.base)
                NexusText(text = "Small (13sp)", style = typography.small)
                NexusText(text = "Extra Small (12sp)", style = typography.extraSmall)
            }
        }
    }
}

@Composable
fun LinkDemo() {
    DemoPage(title = "Link", description = "Text hyperlink.") {
        DemoSection("Basic usage") {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                NexusLink(text = "Default link", onClick = {})
                NexusLink(text = "Primary", onClick = {}, type = NexusType.Primary)
                NexusLink(text = "Success", onClick = {}, type = NexusType.Success)
                NexusLink(text = "Warning", onClick = {}, type = NexusType.Warning)
                NexusLink(text = "Danger", onClick = {}, type = NexusType.Danger)
                NexusLink(text = "Info", onClick = {}, type = NexusType.Info)
            }
        }
        DemoSection("Disabled") {
            NexusLink(text = "Disabled link", onClick = {}, disabled = true)
        }
    }
}

@Composable
fun TagDemo() {
    DemoPage(title = "Tag", description = "Used for marking and selection.") {
        DemoSection("Basic usage") {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                NexusTag(text = "Default")
                NexusTag(text = "Primary", type = NexusType.Primary)
                NexusTag(text = "Success", type = NexusType.Success)
                NexusTag(text = "Warning", type = NexusType.Warning)
                NexusTag(text = "Danger", type = NexusType.Danger)
                NexusTag(text = "Info", type = NexusType.Info)
            }
        }
        DemoSection("Effects") {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                NexusTag(text = "Light", type = NexusType.Primary, effect = TagEffect.Light)
                NexusTag(text = "Dark", type = NexusType.Primary, effect = TagEffect.Dark)
                NexusTag(text = "Plain", type = NexusType.Primary, effect = TagEffect.Plain)
            }
        }
        DemoSection("Closable & Round") {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                NexusTag(text = "Closable", type = NexusType.Primary, closable = true, onClose = {})
                NexusTag(text = "Round", type = NexusType.Success, round = true)
            }
        }
    }
}

@Composable
fun DividerDemo() {
    DemoPage(title = "Divider", description = "The dividing line that separates the content.") {
        DemoSection("Basic usage") {
            Column {
                NexusText(text = "Content above")
                NexusDivider()
                NexusText(text = "Content below")
            }
        }
        DemoSection("With text") {
            Column {
                NexusDivider(contentPosition = DividerContentPosition.Center) { NexusText(text = "Center") }
                Spacer(modifier = Modifier.height(12.dp))
                NexusDivider(contentPosition = DividerContentPosition.Left) { NexusText(text = "Left") }
                Spacer(modifier = Modifier.height(12.dp))
                NexusDivider(contentPosition = DividerContentPosition.Right) { NexusText(text = "Right") }
            }
        }
    }
}

@Composable
fun SpaceDemo() {
    DemoPage(title = "Space", description = "Set a spacing between components.") {
        DemoSection("Horizontal space") {
            NexusSpace {
                NexusButton(onClick = {}, type = NexusType.Primary) { NexusText(text = "Button 1") }
                NexusButton(onClick = {}) { NexusText(text = "Button 2") }
                NexusButton(onClick = {}) { NexusText(text = "Button 3") }
            }
        }
        DemoSection("Vertical space") {
            NexusSpace(direction = SpaceDirection.Vertical) {
                NexusButton(onClick = {}, type = NexusType.Primary) { NexusText(text = "Button 1") }
                NexusButton(onClick = {}) { NexusText(text = "Button 2") }
                NexusButton(onClick = {}) { NexusText(text = "Button 3") }
            }
        }
    }
}
