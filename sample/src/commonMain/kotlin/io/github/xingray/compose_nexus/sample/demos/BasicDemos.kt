package io.github.xingray.compose_nexus.sample.demos

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import io.github.xingray.compose_nexus.controls.*
import io.github.xingray.compose_nexus.foundation.LocalContentColor
import io.github.xingray.compose_nexus.theme.ComponentSize
import io.github.xingray.compose_nexus.theme.NexusTheme
import io.github.xingray.compose_nexus.theme.NexusType

// Simple icon composables for button demo
@Composable
private fun SearchIcon() {
    val color = LocalContentColor.current
    Canvas(modifier = Modifier.size(14.dp)) {
        val sw = 1.5.dp.toPx()
        val r = size.minDimension * 0.28f
        val cx = size.width * 0.4f
        val cy = size.height * 0.38f
        drawCircle(color, r, Offset(cx, cy), style = Stroke(sw))
        drawLine(
            color,
            Offset(cx + r * 0.7f, cy + r * 0.7f),
            Offset(size.width * 0.88f, size.height * 0.88f),
            sw, cap = StrokeCap.Round
        )
    }
}

@Composable
private fun EditIcon() {
    val color = LocalContentColor.current
    Canvas(modifier = Modifier.size(14.dp)) {
        val sw = 1.5.dp.toPx()
        drawLine(color, Offset(size.width * 0.15f, size.height * 0.85f), Offset(size.width * 0.75f, size.height * 0.25f), sw, cap = StrokeCap.Round)
        drawLine(color, Offset(size.width * 0.75f, size.height * 0.25f), Offset(size.width * 0.85f, size.height * 0.15f), sw, cap = StrokeCap.Round)
        drawLine(color, Offset(size.width * 0.15f, size.height * 0.85f), Offset(size.width * 0.35f, size.height * 0.85f), sw, cap = StrokeCap.Round)
    }
}

@Composable
private fun CheckIcon() {
    val color = LocalContentColor.current
    Canvas(modifier = Modifier.size(14.dp)) {
        val sw = 1.8.dp.toPx()
        drawLine(color, Offset(size.width * 0.18f, size.height * 0.5f), Offset(size.width * 0.42f, size.height * 0.75f), sw, cap = StrokeCap.Round)
        drawLine(color, Offset(size.width * 0.42f, size.height * 0.75f), Offset(size.width * 0.82f, size.height * 0.28f), sw, cap = StrokeCap.Round)
    }
}

@Composable
private fun StarIcon() {
    val color = LocalContentColor.current
    Canvas(modifier = Modifier.size(14.dp)) {
        val sw = 1.5.dp.toPx()
        val cx = size.width / 2f
        val cy = size.height / 2f
        val outerR = size.minDimension * 0.44f
        val innerR = outerR * 0.4f
        val path = androidx.compose.ui.graphics.Path()
        for (i in 0 until 5) {
            val outerAngle = (-90.0 + i * 72.0) * kotlin.math.PI / 180.0
            val innerAngle = (-90.0 + i * 72.0 + 36.0) * kotlin.math.PI / 180.0
            val ox = cx + outerR * kotlin.math.cos(outerAngle).toFloat()
            val oy = cy + outerR * kotlin.math.sin(outerAngle).toFloat()
            val ix = cx + innerR * kotlin.math.cos(innerAngle).toFloat()
            val iy = cy + innerR * kotlin.math.sin(innerAngle).toFloat()
            if (i == 0) path.moveTo(ox, oy) else path.lineTo(ox, oy)
            path.lineTo(ix, iy)
        }
        path.close()
        drawPath(path, color, style = Stroke(sw, cap = StrokeCap.Round, join = androidx.compose.ui.graphics.StrokeJoin.Round))
    }
}

@Composable
private fun DeleteIcon() {
    val color = LocalContentColor.current
    Canvas(modifier = Modifier.size(14.dp)) {
        val sw = 1.8.dp.toPx()
        val p = size.minDimension * 0.22f
        drawLine(color, Offset(p, p), Offset(size.width - p, size.height - p), sw, cap = StrokeCap.Round)
        drawLine(color, Offset(size.width - p, p), Offset(p, size.height - p), sw, cap = StrokeCap.Round)
    }
}

@Composable
private fun ArrowLeftIcon() {
    val color = LocalContentColor.current
    Canvas(modifier = Modifier.size(14.dp)) {
        val sw = 1.8.dp.toPx()
        val cx = size.width / 2f
        val cy = size.height / 2f
        val d = size.minDimension * 0.25f
        drawLine(color, Offset(cx + d * 0.3f, cy - d), Offset(cx - d * 0.7f, cy), sw, cap = StrokeCap.Round)
        drawLine(color, Offset(cx - d * 0.7f, cy), Offset(cx + d * 0.3f, cy + d), sw, cap = StrokeCap.Round)
    }
}

@Composable
private fun ArrowRightIcon() {
    val color = LocalContentColor.current
    Canvas(modifier = Modifier.size(14.dp)) {
        val sw = 1.8.dp.toPx()
        val cx = size.width / 2f
        val cy = size.height / 2f
        val d = size.minDimension * 0.25f
        drawLine(color, Offset(cx - d * 0.3f, cy - d), Offset(cx + d * 0.7f, cy), sw, cap = StrokeCap.Round)
        drawLine(color, Offset(cx + d * 0.7f, cy), Offset(cx - d * 0.3f, cy + d), sw, cap = StrokeCap.Round)
    }
}

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
                NexusButton(onClick = {}, plain = true) { NexusText(text = "Plain") }
                NexusButton(onClick = {}, type = NexusType.Primary, plain = true) { NexusText(text = "Primary") }
                NexusButton(onClick = {}, type = NexusType.Success, plain = true) { NexusText(text = "Success") }
                NexusButton(onClick = {}, type = NexusType.Warning, plain = true) { NexusText(text = "Warning") }
                NexusButton(onClick = {}, type = NexusType.Danger, plain = true) { NexusText(text = "Danger") }
                NexusButton(onClick = {}, type = NexusType.Info, plain = true) { NexusText(text = "Info") }
            }
        }
        DemoSection("Round buttons") {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                NexusButton(onClick = {}, round = true) { NexusText(text = "Default") }
                NexusButton(onClick = {}, type = NexusType.Primary, round = true) { NexusText(text = "Primary") }
                NexusButton(onClick = {}, type = NexusType.Success, round = true) { NexusText(text = "Success") }
                NexusButton(onClick = {}, type = NexusType.Warning, round = true) { NexusText(text = "Warning") }
                NexusButton(onClick = {}, type = NexusType.Danger, round = true) { NexusText(text = "Danger") }
                NexusButton(onClick = {}, type = NexusType.Info, round = true) { NexusText(text = "Info") }
            }
        }
        DemoSection("Dashed buttons") {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                NexusButton(onClick = {}, dashed = true) { NexusText(text = "Dashed") }
                NexusButton(onClick = {}, type = NexusType.Primary, dashed = true) { NexusText(text = "Primary") }
                NexusButton(onClick = {}, type = NexusType.Success, dashed = true) { NexusText(text = "Success") }
                NexusButton(onClick = {}, type = NexusType.Warning, dashed = true) { NexusText(text = "Warning") }
                NexusButton(onClick = {}, type = NexusType.Danger, dashed = true) { NexusText(text = "Danger") }
                NexusButton(onClick = {}, type = NexusType.Info, dashed = true) { NexusText(text = "Info") }
            }
        }
        DemoSection("Icon buttons") {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    NexusButton(onClick = {}, circle = true) { SearchIcon() }
                    NexusButton(onClick = {}, type = NexusType.Primary, circle = true) { EditIcon() }
                    NexusButton(onClick = {}, type = NexusType.Success, circle = true) { CheckIcon() }
                    NexusButton(onClick = {}, type = NexusType.Warning, circle = true) { StarIcon() }
                    NexusButton(onClick = {}, type = NexusType.Danger, circle = true) { DeleteIcon() }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    NexusButton(onClick = {}, type = NexusType.Primary) { SearchIcon(); NexusText(text = "Search") }
                    NexusButton(onClick = {}, type = NexusType.Primary) { NexusText(text = "Upload"); ArrowRightIcon() }
                    NexusButton(onClick = {}, type = NexusType.Success) { CheckIcon(); NexusText(text = "OK") }
                    NexusButton(onClick = {}, type = NexusType.Danger) { DeleteIcon(); NexusText(text = "Delete") }
                }
            }
        }
        DemoSection("Disabled") {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    NexusButton(onClick = {}, disabled = true) { NexusText(text = "Default") }
                    NexusButton(onClick = {}, type = NexusType.Primary, disabled = true) { NexusText(text = "Primary") }
                    NexusButton(onClick = {}, type = NexusType.Success, disabled = true) { NexusText(text = "Success") }
                    NexusButton(onClick = {}, type = NexusType.Warning, disabled = true) { NexusText(text = "Warning") }
                    NexusButton(onClick = {}, type = NexusType.Danger, disabled = true) { NexusText(text = "Danger") }
                    NexusButton(onClick = {}, type = NexusType.Info, disabled = true) { NexusText(text = "Info") }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    NexusButton(onClick = {}, plain = true, disabled = true) { NexusText(text = "Default") }
                    NexusButton(onClick = {}, type = NexusType.Primary, plain = true, disabled = true) { NexusText(text = "Primary") }
                    NexusButton(onClick = {}, type = NexusType.Success, plain = true, disabled = true) { NexusText(text = "Success") }
                    NexusButton(onClick = {}, type = NexusType.Warning, plain = true, disabled = true) { NexusText(text = "Warning") }
                    NexusButton(onClick = {}, type = NexusType.Danger, plain = true, disabled = true) { NexusText(text = "Danger") }
                    NexusButton(onClick = {}, type = NexusType.Info, plain = true, disabled = true) { NexusText(text = "Info") }
                }
            }
        }
        DemoSection("Text buttons") {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                NexusButton(onClick = {}, text = true) { NexusText(text = "Default") }
                NexusButton(onClick = {}, text = true, type = NexusType.Primary) { NexusText(text = "Primary") }
                NexusButton(onClick = {}, text = true, type = NexusType.Success) { NexusText(text = "Success") }
                NexusButton(onClick = {}, text = true, type = NexusType.Warning) { NexusText(text = "Warning") }
                NexusButton(onClick = {}, text = true, type = NexusType.Danger) { NexusText(text = "Danger") }
                NexusButton(onClick = {}, text = true, type = NexusType.Info) { NexusText(text = "Info") }
            }
        }
        DemoSection("Text buttons with background") {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                NexusButton(onClick = {}, text = true, bg = true) { NexusText(text = "Default") }
                NexusButton(onClick = {}, text = true, bg = true, type = NexusType.Primary) { NexusText(text = "Primary") }
                NexusButton(onClick = {}, text = true, bg = true, type = NexusType.Success) { NexusText(text = "Success") }
                NexusButton(onClick = {}, text = true, bg = true, type = NexusType.Warning) { NexusText(text = "Warning") }
                NexusButton(onClick = {}, text = true, bg = true, type = NexusType.Danger) { NexusText(text = "Danger") }
                NexusButton(onClick = {}, text = true, bg = true, type = NexusType.Info) { NexusText(text = "Info") }
            }
        }
        DemoSection("Text buttons disabled") {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                NexusButton(onClick = {}, text = true, disabled = true) { NexusText(text = "Default") }
                NexusButton(onClick = {}, text = true, type = NexusType.Primary, disabled = true) { NexusText(text = "Primary") }
                NexusButton(onClick = {}, text = true, bg = true, disabled = true) { NexusText(text = "With BG") }
                NexusButton(onClick = {}, text = true, bg = true, type = NexusType.Primary, disabled = true) { NexusText(text = "Primary BG") }
            }
        }
        DemoSection("Link buttons") {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    NexusButton(onClick = {}, link = true) { NexusText(text = "Default") }
                    NexusButton(onClick = {}, link = true, type = NexusType.Primary) { NexusText(text = "Primary") }
                    NexusButton(onClick = {}, link = true, type = NexusType.Success) { NexusText(text = "Success") }
                    NexusButton(onClick = {}, link = true, type = NexusType.Warning) { NexusText(text = "Warning") }
                    NexusButton(onClick = {}, link = true, type = NexusType.Danger) { NexusText(text = "Danger") }
                    NexusButton(onClick = {}, link = true, type = NexusType.Info) { NexusText(text = "Info") }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    NexusButton(onClick = {}, link = true, disabled = true) { NexusText(text = "Default") }
                    NexusButton(onClick = {}, link = true, type = NexusType.Primary, disabled = true) { NexusText(text = "Primary") }
                    NexusButton(onClick = {}, link = true, type = NexusType.Success, disabled = true) { NexusText(text = "Success") }
                    NexusButton(onClick = {}, link = true, type = NexusType.Warning, disabled = true) { NexusText(text = "Warning") }
                    NexusButton(onClick = {}, link = true, type = NexusType.Danger, disabled = true) { NexusText(text = "Danger") }
                    NexusButton(onClick = {}, link = true, type = NexusType.Info, disabled = true) { NexusText(text = "Info") }
                }
            }
        }
        DemoSection("Sizes") {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                NexusButton(onClick = {}, type = NexusType.Primary, size = ComponentSize.Large) { NexusText(text = "Large") }
                NexusButton(onClick = {}, type = NexusType.Primary, size = ComponentSize.Default) { NexusText(text = "Default") }
                NexusButton(onClick = {}, type = NexusType.Primary, size = ComponentSize.Small) { NexusText(text = "Small") }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                NexusButton(onClick = {}, size = ComponentSize.Large, round = true) { NexusText(text = "Large") }
                NexusButton(onClick = {}, size = ComponentSize.Default, round = true) { NexusText(text = "Default") }
                NexusButton(onClick = {}, size = ComponentSize.Small, round = true) { NexusText(text = "Small") }
            }
        }
        DemoSection("Loading") {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                NexusButton(onClick = {}, type = NexusType.Primary, loading = true) { NexusText(text = "Loading") }
                NexusButton(onClick = {}, type = NexusType.Success, loading = true, plain = true) { NexusText(text = "Loading") }
                NexusButton(onClick = {}, loading = true) { NexusText(text = "Loading") }
            }
        }
        DemoSection("Custom color") {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    NexusButton(onClick = {}, color = Color(0xFF626AEF)) { NexusText(text = "Default") }
                    NexusButton(onClick = {}, color = Color(0xFF626AEF), plain = true) { NexusText(text = "Plain") }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    NexusButton(onClick = {}, color = Color(0xFF626AEF), disabled = true) { NexusText(text = "Disabled") }
                    NexusButton(onClick = {}, color = Color(0xFF626AEF), disabled = true, plain = true) { NexusText(text = "Disabled Plain") }
                }
            }
        }
        DemoSection("Button group") {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                NexusButtonGroup {
                    NexusButton(onClick = {}, type = NexusType.Primary) { NexusText(text = "Previous") }
                    NexusButton(onClick = {}, type = NexusType.Primary) { NexusText(text = "Next") }
                }
                NexusButtonGroup {
                    NexusButton(onClick = {}, type = NexusType.Primary) { NexusText(text = "Home") }
                    NexusButton(onClick = {}, type = NexusType.Primary) { NexusText(text = "Settings") }
                    NexusButton(onClick = {}, type = NexusType.Primary) { NexusText(text = "Messages") }
                }
                NexusButtonGroup(direction = ButtonGroupDirection.Vertical) {
                    NexusButton(onClick = {}, type = NexusType.Primary) { NexusText(text = "Top") }
                    NexusButton(onClick = {}, type = NexusType.Primary) { NexusText(text = "Middle") }
                    NexusButton(onClick = {}, type = NexusType.Primary) { NexusText(text = "Bottom") }
                }
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
