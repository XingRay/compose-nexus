package io.github.xingray.compose_nexus.sample.demos

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import io.github.xingray.compose_nexus.containers.*
import io.github.xingray.compose_nexus.controls.*
import io.github.xingray.compose_nexus.foundation.LocalContentColor
import io.github.xingray.compose_nexus.theme.ComponentSize
import io.github.xingray.compose_nexus.theme.NexusTheme
import io.github.xingray.compose_nexus.theme.NexusType
import io.github.xingray.compose_nexus.theme.TypeColor

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
fun BorderDemo() {
    DemoPage(
        title = "Border",
        description = "We standardize the borders that can be used in buttons, cards, pop-ups and other components.",
    ) {
        val colors = NexusTheme.colorScheme
        val shapes = NexusTheme.shapes
        val shadows = NexusTheme.shadows

        DemoSection("Border style") {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    NexusText(text = "Solid 1px")
                    Box(
                        modifier = Modifier
                            .width(320.dp)
                            .height(1.dp)
                            .border(1.dp, colors.border.base),
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    NexusText(text = "Dashed 2px")
                    Box(
                        modifier = Modifier
                            .width(320.dp)
                            .height(2.dp)
                            .drawBehind {
                                drawLine(
                                    color = colors.border.base,
                                    start = Offset(0f, size.height / 2f),
                                    end = Offset(size.width, size.height / 2f),
                                    strokeWidth = size.height,
                                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 6f)),
                                )
                            },
                    )
                }
            }
        }

        DemoSection("Radius") {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                BorderRadiusItem("No Radius", null)
                BorderRadiusItem("Small Radius", shapes.small)
                BorderRadiusItem("Large Radius", shapes.base)
                BorderRadiusItem("Round Radius", shapes.round)
            }
        }

        DemoSection("Shadow") {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                ShadowItem("Basic Shadow", shadows.default.elevation)
                ShadowItem("Light Shadow", shadows.light.elevation)
                ShadowItem("Lighter Shadow", shadows.lighter.elevation)
                ShadowItem("Dark Shadow", shadows.dark.elevation)
            }
        }
    }
}

@Composable
private fun BorderRadiusItem(name: String, shape: androidx.compose.ui.graphics.Shape?) {
    val borderColor = NexusTheme.colorScheme.border.base
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        NexusText(text = name)
        Box(
            modifier = Modifier
                .size(width = 120.dp, height = 40.dp)
                .then(if (shape != null) Modifier.clip(shape) else Modifier)
                .border(1.dp, borderColor, shape ?: RectangleShape),
        )
    }
}

@Composable
private fun ShadowItem(name: String, elevation: androidx.compose.ui.unit.Dp) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .shadow(elevation = elevation, shape = NexusTheme.shapes.base, clip = false)
                .clip(NexusTheme.shapes.base)
                .border(1.dp, NexusTheme.colorScheme.border.lighter, NexusTheme.shapes.base),
        )
        NexusText(text = name, style = NexusTheme.typography.small)
    }
}

@Composable
fun ColorDemo() {
    val colors = NexusTheme.colorScheme

    DemoPage(
        title = "Color",
        description = "Element Plus uses a specific set of palettes to specify colors to provide a consistent look and feel for the products you build.",
    ) {
        DemoSection("Main Color") {
            ColorPalette(
                title = "Primary",
                typeColor = colors.primary,
            )
        }

        DemoSection("Secondary Color") {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                ColorPalette(title = "Success", typeColor = colors.success)
                ColorPalette(title = "Warning", typeColor = colors.warning)
                ColorPalette(title = "Danger", typeColor = colors.danger)
                ColorPalette(title = "Info", typeColor = colors.info)
            }
        }

        DemoSection("Neutral Color") {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                ColorColumn(
                    title = "Text",
                    items = listOf(
                        "Primary Text" to colors.text.primary,
                        "Regular Text" to colors.text.regular,
                        "Secondary Text" to colors.text.secondary,
                        "Placeholder Text" to colors.text.placeholder,
                        "Disabled Text" to colors.text.disabled,
                    ),
                    darkText = false,
                )
                ColorColumn(
                    title = "Border",
                    items = listOf(
                        "Darker Border" to colors.border.darker,
                        "Dark Border" to colors.border.dark,
                        "Base Border" to colors.border.base,
                        "Light Border" to colors.border.light,
                        "Lighter Border" to colors.border.lighter,
                        "Extra Light Border" to colors.border.extraLight,
                    ),
                )
                ColorColumn(
                    title = "Fill",
                    items = listOf(
                        "Darker Fill" to colors.fill.darker,
                        "Dark Fill" to colors.fill.dark,
                        "Base Fill" to colors.fill.base,
                        "Light Fill" to colors.fill.light,
                        "Lighter Fill" to colors.fill.lighter,
                        "Extra Light Fill" to colors.fill.extraLight,
                        "Blank Fill" to colors.fill.blank,
                    ),
                )
                ColorColumn(
                    title = "Background",
                    items = listOf(
                        "Base Background" to colors.background.base,
                        "Page Background" to colors.background.page,
                        "Overlay Background" to colors.background.overlay,
                    ),
                )
            }
        }
    }
}

@Composable
private fun ColorPalette(title: String, typeColor: TypeColor) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        ColorBlock(name = title, color = typeColor.base, darkText = false)
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            val levels = listOf(
                "dark-2" to typeColor.dark2,
                "light-3" to typeColor.light3,
                "light-5" to typeColor.light5,
                "light-7" to typeColor.light7,
                "light-8" to typeColor.light8,
                "light-9" to typeColor.light9,
            )
            levels.forEach { (_, color) ->
                Box(
                    modifier = Modifier
                        .size(width = 36.dp, height = 24.dp)
                        .clip(NexusTheme.shapes.small)
                        .border(1.dp, NexusTheme.colorScheme.border.light, NexusTheme.shapes.small)
                        .drawBehind { drawRect(color) },
                ) {
                    // keep small blocks visual only
                }
            }
        }
    }
}

@Composable
private fun ColorColumn(
    title: String,
    items: List<Pair<String, Color>>,
    darkText: Boolean = true,
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        NexusText(text = title, style = NexusTheme.typography.small)
        items.forEach { (name, color) ->
            ColorBlock(name = name, color = color, darkText = darkText)
        }
    }
}

@Composable
private fun ColorBlock(
    name: String,
    color: Color,
    darkText: Boolean = true,
) {
    val textColor = if (darkText) NexusTheme.colorScheme.text.primary else NexusTheme.colorScheme.white
    Box(
        modifier = Modifier
            .size(width = 180.dp, height = 52.dp)
            .clip(NexusTheme.shapes.base)
            .drawBehind { drawRect(color) }
            .border(1.dp, NexusTheme.colorScheme.border.light, NexusTheme.shapes.base),
    ) {
        Column(
            modifier = Modifier
                .align(androidx.compose.ui.Alignment.CenterStart)
                .padding(start = 8.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            NexusText(
                text = name,
                color = textColor,
                style = NexusTheme.typography.small,
                modifier = Modifier.width(170.dp),
            )
            NexusText(
                text = color.toHexString(),
                color = textColor,
                style = NexusTheme.typography.extraSmall,
            )
        }
    }
}

private fun Color.toHexString(): String {
    fun toChannel(v: Float): String {
        val i = (v * 255f).toInt().coerceIn(0, 255)
        return i.toString(16).padStart(2, '0').uppercase()
    }
    return "#${toChannel(red)}${toChannel(green)}${toChannel(blue)}"
}

@Composable
fun LayoutContainerDemo() {
    DemoPage(
        title = "Layout Container",
        description = "Container components for scaffolding basic structure of the page.",
    ) {
        DemoSection("Common layouts") {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                LayoutCase("Header + Main") {
                    NexusContainer(direction = NexusContainerDirection.Vertical) {
                        NexusHeader(height = 40.dp) { NexusText(text = "Header") }
                        NexusMain(modifier = Modifier.height(92.dp)) { NexusText(text = "Main") }
                    }
                }
                LayoutCase("Header + Main + Footer") {
                    NexusContainer(direction = NexusContainerDirection.Vertical) {
                        NexusHeader(height = 34.dp) { NexusText(text = "Header") }
                        NexusMain(modifier = Modifier.height(64.dp)) { NexusText(text = "Main") }
                        NexusFooter(height = 34.dp) { NexusText(text = "Footer") }
                    }
                }
                LayoutCase("Aside + Main") {
                    NexusContainer(
                        direction = NexusContainerDirection.Horizontal,
                        modifier = Modifier.height(130.dp),
                    ) {
                        NexusAside(width = 120.dp) { NexusText(text = "Aside") }
                        NexusMain(modifier = Modifier.width(350.dp)) { NexusText(text = "Main") }
                    }
                }
                LayoutCase("Header + Aside + Main") {
                    NexusContainer(direction = NexusContainerDirection.Vertical) {
                        NexusHeader(height = 34.dp) { NexusText(text = "Header") }
                        NexusContainer(
                            direction = NexusContainerDirection.Horizontal,
                            modifier = Modifier.height(90.dp),
                        ) {
                            NexusAside(width = 110.dp) { NexusText(text = "Aside") }
                            NexusMain(modifier = Modifier.width(360.dp)) { NexusText(text = "Main") }
                        }
                    }
                }
                LayoutCase("Header + Aside + Main + Footer") {
                    NexusContainer(direction = NexusContainerDirection.Vertical) {
                        NexusHeader(height = 30.dp) { NexusText(text = "Header") }
                        NexusContainer(
                            direction = NexusContainerDirection.Horizontal,
                            modifier = Modifier.height(68.dp),
                        ) {
                            NexusAside(width = 110.dp) { NexusText(text = "Aside") }
                            NexusMain(modifier = Modifier.width(360.dp)) { NexusText(text = "Main") }
                        }
                        NexusFooter(height = 30.dp) { NexusText(text = "Footer") }
                    }
                }
                LayoutCase("Aside + Header + Main") {
                    NexusContainer(
                        direction = NexusContainerDirection.Horizontal,
                        modifier = Modifier.height(130.dp),
                    ) {
                        NexusAside(width = 110.dp) { NexusText(text = "Aside") }
                        NexusContainer(
                            direction = NexusContainerDirection.Vertical,
                            modifier = Modifier.width(360.dp),
                        ) {
                            NexusHeader(height = 34.dp) { NexusText(text = "Header") }
                            NexusMain(modifier = Modifier.height(86.dp)) { NexusText(text = "Main") }
                        }
                    }
                }
                LayoutCase("Aside + Header + Main + Footer") {
                    NexusContainer(
                        direction = NexusContainerDirection.Horizontal,
                        modifier = Modifier.height(130.dp),
                    ) {
                        NexusAside(width = 110.dp) { NexusText(text = "Aside") }
                        NexusContainer(
                            direction = NexusContainerDirection.Vertical,
                            modifier = Modifier.width(360.dp),
                        ) {
                            NexusHeader(height = 30.dp) { NexusText(text = "Header") }
                            NexusMain(modifier = Modifier.height(70.dp)) { NexusText(text = "Main") }
                            NexusFooter(height = 30.dp) { NexusText(text = "Footer") }
                        }
                    }
                }
            }
        }

        DemoSection("Example") {
            LayoutCase("Admin dashboard layout") {
                NexusContainer(
                    direction = NexusContainerDirection.Horizontal,
                    modifier = Modifier.height(160.dp),
                ) {
                    NexusAside(width = 120.dp) {
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            NexusText(text = "Navigation")
                            NexusText(text = "Overview")
                            NexusText(text = "Users")
                            NexusText(text = "Settings")
                        }
                    }
                    NexusContainer(
                        direction = NexusContainerDirection.Vertical,
                        modifier = Modifier.width(350.dp),
                    ) {
                        NexusHeader(height = 36.dp) { NexusText(text = "Top Bar") }
                        NexusMain(modifier = Modifier.height(88.dp)) { NexusText(text = "Content Area") }
                        NexusFooter(height = 36.dp) { NexusText(text = "Footer") }
                    }
                }
            }
        }
    }
}

@Composable
private fun LayoutCase(
    title: String,
    content: @Composable () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        NexusText(text = title, style = NexusTheme.typography.small)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, NexusTheme.colorScheme.border.light, NexusTheme.shapes.base)
                .padding(8.dp),
        ) {
            content()
        }
    }
}

@Composable
fun IconDemo() {
    val colors = NexusTheme.colorScheme
    DemoPage(
        title = "Icon",
        description = "Element Plus provides a set of common icons.",
    ) {
        DemoSection("Simple usage") {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                NexusIcon(contentDescription = "Edit", size = 24.dp) { EditIcon() }
                NexusIcon(contentDescription = "Search", size = 24.dp) { SearchIcon() }
                NexusIcon(contentDescription = "Delete", size = 24.dp) { DeleteIcon() }
                NexusIcon(contentDescription = "Star", size = 24.dp) { StarIcon() }
            }
        }

        DemoSection("Combined with button") {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                NexusButton(onClick = {}, type = NexusType.Primary) {
                    NexusIcon(contentDescription = "Search", size = 16.dp, color = colors.white) { SearchIcon() }
                    NexusText(text = "Search")
                }
                NexusButton(onClick = {}, type = NexusType.Success, plain = true) {
                    NexusIcon(contentDescription = "Check", size = 16.dp, color = colors.success.base) { CheckIcon() }
                    NexusText(text = "Confirm")
                }
            }
        }

        DemoSection("Size and color") {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                NexusIcon(contentDescription = "Warning", size = 14.dp, color = colors.warning.base) { StarIcon() }
                NexusIcon(contentDescription = "Info", size = 20.dp, color = colors.info.base) { SearchIcon() }
                NexusIcon(contentDescription = "Danger", size = 28.dp, color = colors.danger.base) { DeleteIcon() }
            }
        }

        DemoSection("Loading icon") {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                NexusIcon(
                    contentDescription = "Loading",
                    size = 20.dp,
                    color = colors.primary.base,
                    spinning = true,
                ) { SearchIcon() }
                NexusIcon(
                    contentDescription = "Loading",
                    size = 24.dp,
                    color = colors.success.base,
                    spinning = true,
                ) { StarIcon() }
            }
        }
    }
}

@Composable
fun LayoutDemo() {
    DemoPage(
        title = "Layout",
        description = "Quickly and easily create layouts with the basic 24-column.",
    ) {
        DemoSection("Basic layout") {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                NexusRow {
                    NexusCol(span = 24) { GridCell(0) }
                }
                NexusRow {
                    NexusCol(span = 12) { GridCell(1) }
                    NexusCol(span = 12) { GridCell(2) }
                }
                NexusRow {
                    NexusCol(span = 8) { GridCell(3) }
                    NexusCol(span = 8) { GridCell(4) }
                    NexusCol(span = 8) { GridCell(5) }
                }
                NexusRow {
                    NexusCol(span = 6) { GridCell(6) }
                    NexusCol(span = 6) { GridCell(7) }
                    NexusCol(span = 6) { GridCell(8) }
                    NexusCol(span = 6) { GridCell(9) }
                }
            }
        }

        DemoSection("Column spacing") {
            NexusRow(gutter = 20.dp) {
                NexusCol(span = 6) { GridCell(10) }
                NexusCol(span = 6) { GridCell(11) }
                NexusCol(span = 6) { GridCell(12) }
                NexusCol(span = 6) { GridCell(13) }
            }
        }

        DemoSection("Hybrid layout") {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                NexusRow(gutter = 20.dp) {
                    NexusCol(span = 16) { GridCell(14) }
                    NexusCol(span = 8) { GridCell(15) }
                }
                NexusRow(gutter = 20.dp) {
                    NexusCol(span = 8) { GridCell(16) }
                    NexusCol(span = 8) { GridCell(17) }
                    NexusCol(span = 4) { GridCell(18) }
                    NexusCol(span = 4) { GridCell(19) }
                }
                NexusRow(gutter = 20.dp) {
                    NexusCol(span = 4) { GridCell(20) }
                    NexusCol(span = 16) { GridCell(21) }
                    NexusCol(span = 4) { GridCell(22) }
                }
            }
        }

        DemoSection("Column offset") {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                NexusRow(gutter = 20.dp) {
                    NexusCol(span = 6) { GridCell(23) }
                    NexusCol(span = 6, offset = 6) { GridCell(24) }
                }
                NexusRow(gutter = 20.dp) {
                    NexusCol(span = 6, offset = 6) { GridCell(25) }
                    NexusCol(span = 6, offset = 6) { GridCell(26) }
                }
                NexusRow(gutter = 20.dp) {
                    NexusCol(span = 12, offset = 6) { GridCell(27) }
                }
            }
        }

        DemoSection("Alignment") {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                NexusRow(justify = NexusRowJustify.Start) {
                    NexusCol(span = 6) { GridCell(28) }
                    NexusCol(span = 6) { GridCell(29) }
                    NexusCol(span = 6) { GridCell(30) }
                }
                NexusRow(justify = NexusRowJustify.Center) {
                    NexusCol(span = 6) { GridCell(31) }
                    NexusCol(span = 6) { GridCell(32) }
                    NexusCol(span = 6) { GridCell(33) }
                }
                NexusRow(justify = NexusRowJustify.End) {
                    NexusCol(span = 6) { GridCell(34) }
                    NexusCol(span = 6) { GridCell(35) }
                    NexusCol(span = 6) { GridCell(36) }
                }
                NexusRow(justify = NexusRowJustify.SpaceBetween) {
                    NexusCol(span = 6) { GridCell(37) }
                    NexusCol(span = 6) { GridCell(38) }
                    NexusCol(span = 6) { GridCell(39) }
                }
                NexusRow(justify = NexusRowJustify.SpaceAround) {
                    NexusCol(span = 6) { GridCell(40) }
                    NexusCol(span = 6) { GridCell(41) }
                    NexusCol(span = 6) { GridCell(42) }
                }
                NexusRow(justify = NexusRowJustify.SpaceEvenly) {
                    NexusCol(span = 6) { GridCell(43) }
                    NexusCol(span = 6) { GridCell(44) }
                    NexusCol(span = 6) { GridCell(45) }
                }
            }
        }

        DemoSection("Responsive layout") {
            NexusRow(gutter = 10.dp) {
                NexusCol(
                    xs = NexusColSize(8),
                    sm = NexusColSize(6),
                    md = NexusColSize(4),
                    lg = NexusColSize(3),
                    xl = NexusColSize(1),
                ) { GridCell(46) }
                NexusCol(
                    xs = NexusColSize(4),
                    sm = NexusColSize(6),
                    md = NexusColSize(8),
                    lg = NexusColSize(9),
                    xl = NexusColSize(11),
                ) { GridCell(47) }
                NexusCol(
                    xs = NexusColSize(4),
                    sm = NexusColSize(6),
                    md = NexusColSize(8),
                    lg = NexusColSize(9),
                    xl = NexusColSize(11),
                ) { GridCell(48) }
                NexusCol(
                    xs = NexusColSize(8),
                    sm = NexusColSize(6),
                    md = NexusColSize(4),
                    lg = NexusColSize(3),
                    xl = NexusColSize(1),
                ) { GridCell(49) }
            }
        }
    }
}

@Composable
private fun GridCell(index: Int) {
    val colors = NexusTheme.colorScheme
    val background = if (index % 2 == 0) colors.primary.light5 else colors.primary.light7
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(36.dp)
            .clip(NexusTheme.shapes.small)
            .drawBehind { drawRect(background) },
    )
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
                NexusButton(
                    text = "Custom",
                    onClick = {},
                    type = NexusType.Warning,
                    loading = true,
                    loadingContent = { StarIcon() },
                )
            }
        }
        DemoSection("Tag") {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                NexusButton(text = "Button", onClick = {}, tag = "button")
                NexusButton(text = "Div", onClick = {}, tag = "div", type = NexusType.Primary)
                NexusButton(text = "Anchor", onClick = {}, tag = "a", type = NexusType.Success)
            }
        }
        DemoSection("Auto insert space") {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                NexusButton(text = "提交", onClick = {}, type = NexusType.Primary, autoInsertSpace = true)
                NexusButton(text = "提交", onClick = {}, type = NexusType.Default, autoInsertSpace = false)
                NexusButton(text = "OK", onClick = {}, type = NexusType.Success, autoInsertSpace = true)
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
                NexusButtonGroup(type = NexusType.Success, size = ComponentSize.Small) {
                    NexusButton(onClick = {}) { NexusText(text = "Small 1") }
                    NexusButton(onClick = {}) { NexusText(text = "Small 2") }
                    NexusButton(onClick = {}) { NexusText(text = "Small 3") }
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
    DemoPage(title = "Text", description = "Used for text.") {
        DemoSection("Basic") {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                NexusText(text = "Default")
                NexusText(text = "Primary", type = NexusType.Primary)
                NexusText(text = "Success", type = NexusType.Success)
                NexusText(text = "Info", type = NexusType.Info)
                NexusText(text = "Warning", type = NexusType.Warning)
                NexusText(text = "Danger", type = NexusType.Danger)
            }
        }
        DemoSection("Sizes") {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                NexusText(text = "Large", size = ComponentSize.Large)
                NexusText(text = "Default", size = ComponentSize.Default)
                NexusText(text = "Small", size = ComponentSize.Small)
            }
        }
        DemoSection("Ellipsis") {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                NexusText(
                    text = "Self element set width 150px and truncated text content",
                    modifier = Modifier.width(150.dp),
                    truncated = true,
                )
                Box(modifier = Modifier.width(150.dp)) {
                    NexusText(
                        text = "Squeezed by parent element with truncated mode",
                        truncated = true,
                    )
                }
                NexusText(
                    text = "The line-clamp prop allows limiting of the contents of a block to the specified number of lines.",
                    lineClamp = 2,
                    modifier = Modifier.width(240.dp),
                )
            }
        }
        DemoSection("Override") {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                NexusText(text = "span", tag = "span")
                NexusText(text = "This is a paragraph.", tag = "p")
                NexusText(text = "Bold", tag = "b")
                NexusText(text = "Italic", tag = "i")
                NexusText(text = "Inserted", tag = "ins")
                NexusText(text = "Deleted", tag = "del")
                NexusText(text = "Marked", tag = "mark")
            }
        }
        DemoSection("Mixed") {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                NexusText {
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        NexusIcon(contentDescription = "logo", size = 14.dp) { StarIcon() }
                        NexusText(text = "Element-Plus")
                    }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    NexusText(text = "Rate")
                    NexusText(text = "★★★★★", type = NexusType.Warning)
                }
                NexusText {
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        NexusText(text = "Text mixed icon")
                        NexusIcon(contentDescription = "bell", size = 14.dp) { SearchIcon() }
                        NexusText(text = "and component")
                        NexusButton(text = "Button", onClick = {}, size = ComponentSize.Small)
                    }
                }
            }
        }
    }
}

@Composable
fun TypographyDemo() {
    val typography = NexusTheme.typography
    val colors = NexusTheme.colorScheme

    DemoPage(
        title = "Typography",
        description = "Font convention, size scale and line-height system.",
    ) {
        DemoSection("Font") {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                NexusText(
                    text = "The quick brown fox jumps over the lazy dog",
                    style = typography.base.copy(color = colors.text.primary),
                )
                NexusText(
                    text = "0123456789 ~!@#$%^&*()_+",
                    style = typography.base.copy(color = colors.text.regular),
                )
            }
        }

        DemoSection("Font convention") {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                NexusText(text = "Extra Large 20 / 28", style = typography.extraLarge)
                NexusText(text = "Large 18 / 26", style = typography.large)
                NexusText(text = "Medium 16 / 24", style = typography.medium)
                NexusText(text = "Base 14 / 22", style = typography.base)
                NexusText(text = "Small 13 / 20", style = typography.small)
                NexusText(text = "Extra Small 12 / 18", style = typography.extraSmall)
            }
        }

        DemoSection("Font line height") {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(NexusTheme.shapes.small)
                        .drawBehind { drawRect(colors.fill.extraLight) }
                        .padding(8.dp),
                ) {
                    NexusText(text = "Line height 28", style = typography.extraLarge)
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(NexusTheme.shapes.small)
                        .drawBehind { drawRect(colors.fill.extraLight) }
                        .padding(8.dp),
                ) {
                    NexusText(text = "Line height 22", style = typography.base)
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(NexusTheme.shapes.small)
                        .drawBehind { drawRect(colors.fill.extraLight) }
                        .padding(8.dp),
                ) {
                    NexusText(text = "Line height 18", style = typography.extraSmall)
                }
            }
        }

        DemoSection("Font family") {
            NexusText(
                text = "Inter, Helvetica Neue, Helvetica, PingFang SC, Hiragino Sans GB, Microsoft YaHei, Arial, sans-serif",
                style = typography.small,
                modifier = Modifier.fillMaxWidth(),
                lineClamp = 2,
            )
        }
    }
}

@Composable
fun ConfigProviderDemo() {
    var globalSize by remember { mutableStateOf(ComponentSize.Default) }
    var dashedDefault by remember { mutableStateOf(false) }

    DemoPage(
        title = "Config Provider",
        description = "Provide global configuration for the current composition scope.",
    ) {
        DemoSection("Global size") {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                NexusButton(text = "Large", onClick = { globalSize = ComponentSize.Large }, size = ComponentSize.Small)
                NexusButton(text = "Default", onClick = { globalSize = ComponentSize.Default }, size = ComponentSize.Small)
                NexusButton(text = "Small", onClick = { globalSize = ComponentSize.Small }, size = ComponentSize.Small)
            }
            Spacer(modifier = Modifier.height(8.dp))
            NexusConfigProvider(size = globalSize) {
                NexusSpace {
                    NexusButton(text = "Action", onClick = {})
                    NexusButton(text = "提交", onClick = {})
                    NexusButton(text = "Danger", onClick = {}, type = NexusType.Danger)
                }
            }
        }

        DemoSection("Button configurations") {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                NexusButton(
                    text = if (dashedDefault) "Disable dashed default" else "Enable dashed default",
                    onClick = { dashedDefault = !dashedDefault },
                    size = ComponentSize.Small,
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            NexusConfigProvider(
                button = NexusButtonConfig(
                    autoInsertSpace = true,
                    type = NexusType.Success,
                    plain = true,
                    dashed = if (dashedDefault) true else null,
                ),
            ) {
                NexusSpace {
                    NexusButton(text = "提交", onClick = {})
                    NexusButton(text = "确定", onClick = {})
                    NexusButton(text = "Override", onClick = {}, type = NexusType.Warning, plain = false)
                }
            }
        }

        DemoSection("Link configurations") {
            NexusConfigProvider(
                link = NexusLinkConfig(
                    type = NexusType.Primary,
                    underline = NexusLinkUnderline.Always,
                ),
            ) {
                NexusSpace {
                    NexusLink(text = "Default from config", onClick = {})
                    NexusLink(text = "Override type", onClick = {}, type = NexusType.Danger)
                    NexusLink(text = "Override underline", onClick = {}, underline = NexusLinkUnderline.Never)
                }
            }
        }

        DemoSection("Card configurations") {
            NexusConfigProvider(
                card = NexusCardConfig(shadow = NexusCardShadowMode.Hover),
            ) {
                NexusCard(
                    header = { NexusText(text = "Config Card") },
                    modifier = Modifier.width(280.dp),
                ) {
                    NexusText(text = "Shadow mode comes from Config Provider.")
                }
            }
        }

        DemoSection("Nested providers") {
            NexusConfigProvider(
                size = ComponentSize.Small,
                button = NexusButtonConfig(type = NexusType.Primary),
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    NexusSpace {
                        NexusButton(text = "Parent Config", onClick = {})
                        NexusButton(text = "Parent Config", onClick = {})
                    }
                    NexusConfigProvider(
                        button = NexusButtonConfig(type = NexusType.Danger, plain = true),
                    ) {
                        NexusSpace {
                            NexusButton(text = "Child Override", onClick = {})
                            NexusButton(text = "Child Override", onClick = {})
                        }
                    }
                }
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
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                NexusLink(text = "Default", onClick = {}, disabled = true)
                NexusLink(text = "Primary", onClick = {}, type = NexusType.Primary, disabled = true)
                NexusLink(text = "Success", onClick = {}, type = NexusType.Success, disabled = true)
            }
        }
        DemoSection("Underline") {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                NexusLink(text = "Always", onClick = {}, underline = NexusLinkUnderline.Always)
                NexusLink(text = "Hover", onClick = {}, underline = NexusLinkUnderline.Hover)
                NexusLink(text = "Never", onClick = {}, underline = NexusLinkUnderline.Never)
            }
        }
        DemoSection("Icon") {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                NexusLink(
                    text = "Edit",
                    onClick = {},
                    type = NexusType.Primary,
                    icon = { NexusIcon(contentDescription = "edit", size = 14.dp) { EditIcon() } },
                )
                NexusLink(
                    text = "Search",
                    onClick = {},
                    type = NexusType.Success,
                    icon = { NexusIcon(contentDescription = "search", size = 14.dp) { SearchIcon() } },
                )
            }
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
                NexusTag(text = "Custom", color = Color(0xFF7C5CFC))
            }
        }

        DemoSection("Removable Tag") {
            var removed by remember { mutableStateOf(false) }
            if (!removed) {
                NexusTag(
                    text = "Closable",
                    type = NexusType.Primary,
                    closable = true,
                    onClose = { removed = true },
                )
            } else {
                NexusText(text = "Tag removed")
            }
        }

        DemoSection("Edit Dynamically") {
            var tags by remember { mutableStateOf(listOf("Tag 1", "Tag 2", "Tag 3")) }
            var counter by remember { mutableStateOf(4) }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                tags.forEach { item ->
                    NexusTag(
                        text = item,
                        type = NexusType.Info,
                        closable = true,
                        onClose = { tags = tags.filterNot { it == item }.ifEmpty { emptyList() } },
                    )
                }
                NexusButton(
                    text = "+ New Tag",
                    onClick = {
                        tags = tags + "Tag ${counter++}"
                    },
                    size = ComponentSize.Small,
                )
            }
        }

        DemoSection("Sizes") {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                NexusTag(text = "Large", size = ComponentSize.Large, type = NexusType.Primary)
                NexusTag(text = "Default", size = ComponentSize.Default, type = NexusType.Success)
                NexusTag(text = "Small", size = ComponentSize.Small, type = NexusType.Warning)
            }
        }

        DemoSection("Theme") {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                NexusTag(text = "Light", type = NexusType.Primary, effect = TagEffect.Light)
                NexusTag(text = "Dark", type = NexusType.Primary, effect = TagEffect.Dark)
                NexusTag(text = "Plain", type = NexusType.Primary, effect = TagEffect.Plain)
            }
        }

        DemoSection("Rounded") {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                NexusTag(text = "Round", type = NexusType.Success, round = true)
                NexusTag(text = "Round Hit", type = NexusType.Warning, round = true, hit = true)
            }
        }

        DemoSection("Checkable Tag") {
            var checkedA by remember { mutableStateOf(true) }
            var checkedB by remember { mutableStateOf(false) }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                NexusCheckTag(
                    text = "Option A",
                    checked = checkedA,
                    onCheckedChange = { checkedA = it },
                    type = NexusType.Success,
                )
                NexusCheckTag(
                    text = "Option B",
                    checked = checkedB,
                    onCheckedChange = { checkedB = it },
                    type = NexusType.Warning,
                )
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

        DemoSection("Custom content") {
            Column {
                NexusDivider(contentPosition = DividerContentPosition.Center) { NexusText(text = "Center") }
                Spacer(modifier = Modifier.height(12.dp))
                NexusDivider(contentPosition = DividerContentPosition.Left) { NexusText(text = "Left") }
                Spacer(modifier = Modifier.height(12.dp))
                NexusDivider(contentPosition = DividerContentPosition.Right) { NexusText(text = "Right") }
            }
        }

        DemoSection("Border styles") {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                NexusDivider(borderStyle = DividerBorderStyle.Solid) { NexusText(text = "Solid") }
                NexusDivider(borderStyle = DividerBorderStyle.Dashed) { NexusText(text = "Dashed") }
                NexusDivider(borderStyle = DividerBorderStyle.Dotted) { NexusText(text = "Dotted") }
                NexusDivider(borderStyle = DividerBorderStyle.Double) { NexusText(text = "Double") }
            }
        }

        DemoSection("Vertical divider") {
            Row(
                modifier = Modifier.height(36.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                NexusText(text = "A")
                NexusDivider(direction = DividerDirection.Vertical)
                NexusText(text = "B")
                NexusDivider(direction = DividerDirection.Vertical, borderStyle = DividerBorderStyle.Dashed)
                NexusText(text = "C")
            }
        }
    }
}

@Composable
fun WatermarkDemo() {
    DemoPage(title = "Watermark", description = "Add specific text or patterns to the page.") {
        DemoSection("Basic usage") {
            NexusWatermark(
                content = listOf("Compose Nexus"),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                ) {
                    NexusText(text = "Watermark basic content area")
                }
            }
        }

        DemoSection("Multi-line watermark") {
            NexusWatermark(
                content = listOf("Compose Nexus", "Confidential"),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
            ) {
                Box(modifier = Modifier.fillMaxSize().padding(12.dp)) {
                    NexusText(text = "Multi-line text watermark")
                }
            }
        }

        DemoSection("Image watermark") {
            NexusWatermark(
                image = {
                    Box(
                        modifier = Modifier
                            .size(26.dp)
                            .clip(NexusTheme.shapes.circle)
                            .background(NexusTheme.colorScheme.primary.base),
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
            ) {
                Box(modifier = Modifier.fillMaxSize().padding(12.dp)) {
                    NexusText(text = "Image watermark (custom composable)")
                }
            }
        }

        DemoSection("Custom configuration") {
            NexusWatermark(
                content = listOf("Custom", "Watermark"),
                width = 160,
                height = 80,
                rotate = -30f,
                gap = 70 to 80,
                offset = 24 to 24,
                font = WatermarkFont(
                    color = NexusTheme.colorScheme.primary.base.copy(alpha = 0.22f),
                    fontSize = 14,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Medium,
                    fontGap = 2,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
            ) {
                Box(modifier = Modifier.fillMaxSize().padding(12.dp)) {
                    NexusText(text = "Custom width/height/rotate/gap/offset/font")
                }
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
        DemoSection("Control size") {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                NexusSpace(size = NexusSpaceSize.Small.horizontal) {
                    NexusButton(text = "Small", onClick = {}, size = ComponentSize.Small)
                    NexusButton(text = "Small", onClick = {}, size = ComponentSize.Small)
                    NexusButton(text = "Small", onClick = {}, size = ComponentSize.Small)
                }
                NexusSpace(size = NexusSpaceSize.Default.horizontal) {
                    NexusButton(text = "Default", onClick = {}, size = ComponentSize.Small)
                    NexusButton(text = "Default", onClick = {}, size = ComponentSize.Small)
                    NexusButton(text = "Default", onClick = {}, size = ComponentSize.Small)
                }
                NexusSpace(size = NexusSpaceSize.Large.horizontal) {
                    NexusButton(text = "Large", onClick = {}, size = ComponentSize.Small)
                    NexusButton(text = "Large", onClick = {}, size = ComponentSize.Small)
                    NexusButton(text = "Large", onClick = {}, size = ComponentSize.Small)
                }
            }
        }
        DemoSection("Customized size") {
            NexusSpace(size = 20.dp, wrap = true) {
                repeat(8) { index ->
                    NexusButton(text = "Item ${index + 1}", onClick = {}, size = ComponentSize.Small)
                }
            }
        }
        DemoSection("Auto wrapping") {
            NexusSpace(size = 8.dp, wrap = true) {
                repeat(20) { index ->
                    NexusButton(onClick = {}, text = true) {
                        NexusText(text = "Text ${index + 1}")
                    }
                }
            }
        }
        DemoSection("Spacer (literal)") {
            NexusSpace(
                items = listOf(
                    { NexusButton(text = "Button 1", onClick = {}, size = ComponentSize.Small) },
                    { NexusButton(text = "Button 2", onClick = {}, size = ComponentSize.Small) },
                    { NexusButton(text = "Button 3", onClick = {}, size = ComponentSize.Small) },
                ),
                spaceSize = NexusSpaceSize.Default,
                spacer = NexusSpaceSpacer.Literal("|"),
            )
        }
        DemoSection("Spacer (VNode)") {
            NexusSpace(
                items = listOf(
                    { NexusButton(text = "Primary", onClick = {}, type = NexusType.Primary, size = ComponentSize.Small) },
                    { NexusButton(text = "Success", onClick = {}, type = NexusType.Success, size = ComponentSize.Small) },
                    { NexusButton(text = "Danger", onClick = {}, type = NexusType.Danger, size = ComponentSize.Small) },
                ),
                spaceSize = NexusSpaceSize.Default,
                spacer = NexusSpaceSpacer.Content {
                    NexusDivider(
                        direction = DividerDirection.Vertical,
                        modifier = Modifier.height(20.dp),
                    )
                },
            )
        }
        DemoSection("Alignment") {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                NexusSpace(alignment = androidx.compose.ui.Alignment.Top, size = 8.dp) {
                    NexusText(text = "string")
                    NexusButton(text = "button", onClick = {}, size = ComponentSize.Small)
                    NexusCard(
                        header = { NexusText(text = "header") },
                        modifier = Modifier.width(120.dp),
                    ) { NexusText(text = "body") }
                }
                NexusSpace(alignment = androidx.compose.ui.Alignment.Bottom, size = 8.dp) {
                    NexusText(text = "string")
                    NexusButton(text = "button", onClick = {}, size = ComponentSize.Small)
                    NexusCard(
                        header = { NexusText(text = "header") },
                        modifier = Modifier.width(120.dp),
                    ) { NexusText(text = "body") }
                }
            }
        }
        DemoSection("Fill container") {
            NexusSpace(
                modifier = Modifier.fillMaxWidth(),
                items = listOf(
                    { NexusButton(text = "Item 1", onClick = {}) },
                    { NexusButton(text = "Item 2", onClick = {}) },
                    { NexusButton(text = "Item 3", onClick = {}) },
                ),
                fill = true,
                spaceSize = NexusSpaceSize.Default,
            )
        }
        DemoSection("Fill ratio") {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                NexusSpace(
                    modifier = Modifier.fillMaxWidth(),
                    items = listOf(
                        { NexusButton(text = "50%", onClick = {}) },
                        { NexusButton(text = "50%", onClick = {}) },
                    ),
                    fill = true,
                    fillRatio = 50,
                    spaceSize = NexusSpaceSize.Default,
                )
                NexusSpace(
                    modifier = Modifier.fillMaxWidth(),
                    items = listOf(
                        { NexusButton(text = "30%", onClick = {}) },
                        { NexusButton(text = "30%", onClick = {}) },
                        { NexusButton(text = "30%", onClick = {}) },
                    ),
                    wrap = true,
                    fill = true,
                    fillRatio = 30,
                    spaceSize = NexusSpaceSize.Default,
                )
            }
        }
    }
}

@Composable
private fun SplitterPanelItem(
    text: String,
    color: Color,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .clip(NexusTheme.shapes.small)
            .drawBehind { drawRect(color) },
    ) {
        NexusText(
            text = text,
            color = NexusTheme.colorScheme.text.primary,
            modifier = Modifier.align(androidx.compose.ui.Alignment.Center),
        )
    }
}

@Composable
fun SplitterDemo() {
    val colors = NexusTheme.colorScheme
    var collapsible by remember { mutableStateOf(true) }
    var resizable by remember { mutableStateOf(false) }
    var middleSize by remember { mutableStateOf(120f) }
    var lazyStatus by remember { mutableStateOf("Drag a splitter bar") }

    DemoPage(title = "Splitter", description = "Divide area and resize panels by dragging.") {
        DemoSection("Basic usage") {
            NexusSplitter(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
            ) {
                panel(size = splitterPercent(30)) {
                    SplitterPanelItem("1", colors.primary.light9)
                }
                panel(min = splitterPx(120)) {
                    SplitterPanelItem("2", colors.success.light9)
                }
            }
        }

        DemoSection("Vertical") {
            NexusSplitter(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                layout = NexusSplitterLayout.Vertical,
            ) {
                panel { SplitterPanelItem("Top", colors.warning.light9) }
                panel { SplitterPanelItem("Bottom", colors.info.light9) }
            }
        }

        DemoSection("Collapsible") {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                NexusButton(
                    text = if (collapsible) "Disable collapsible" else "Enable collapsible",
                    onClick = { collapsible = !collapsible },
                    size = ComponentSize.Small,
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            NexusSplitter(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
            ) {
                panel(collapsible = collapsible, min = splitterPx(50)) {
                    SplitterPanelItem("1", colors.primary.light9)
                }
                panel(collapsible = collapsible) {
                    SplitterPanelItem("2", colors.success.light9)
                }
                panel {
                    SplitterPanelItem("3", colors.danger.light9)
                }
            }
        }

        DemoSection("Disable drag") {
            NexusButton(
                text = if (resizable) "Disable drag" else "Enable drag",
                onClick = { resizable = !resizable },
                size = ComponentSize.Small,
            )
            Spacer(modifier = Modifier.height(8.dp))
            NexusSplitter(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
            ) {
                panel { SplitterPanelItem("1", colors.primary.light9) }
                panel(resizable = resizable) {
                    SplitterPanelItem("drag ${if (resizable) "enable" else "disable"}", colors.warning.light9)
                }
                panel { SplitterPanelItem("3", colors.success.light9) }
            }
        }

        DemoSection("Panel size") {
            NexusText(text = "Middle panel size: ${middleSize.toInt()}px")
            Spacer(modifier = Modifier.height(8.dp))
            NexusSplitter(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                onResizeStart = { index, sizes ->
                    lazyStatus = "resize-start: bar=$index sizes=${sizes.map { it.toInt() }}"
                },
                onResize = { index, sizes ->
                    lazyStatus = "resize: bar=$index sizes=${sizes.map { it.toInt() }}"
                },
                onResizeEnd = { index, sizes ->
                    lazyStatus = "resize-end: bar=$index sizes=${sizes.map { it.toInt() }}"
                },
            ) {
                panel { SplitterPanelItem("1", colors.primary.light9) }
                panel(
                    size = splitterPx(120),
                    min = splitterPx(50),
                    max = splitterPx(220),
                    onSizeChange = { middleSize = it },
                ) {
                    SplitterPanelItem("${middleSize.toInt()}px", colors.info.light9)
                }
                panel { SplitterPanelItem("3", colors.success.light9) }
            }
        }

        DemoSection("Lazy") {
            NexusText(text = lazyStatus, style = NexusTheme.typography.small)
            Spacer(modifier = Modifier.height(8.dp))
            NexusSplitter(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                lazy = true,
                onResizeStart = { index, _ -> lazyStatus = "lazy resize-start: bar=$index" },
                onResizeEnd = { index, sizes ->
                    lazyStatus = "lazy resize-end: bar=$index sizes=${sizes.map { it.toInt() }}"
                },
            ) {
                panel(collapsible = true, min = splitterPx(50)) {
                    SplitterPanelItem("1", colors.primary.light9)
                }
                panel(collapsible = true) {
                    SplitterPanelItem("2", colors.success.light9)
                }
                panel(collapsible = true) {
                    SplitterPanelItem("3", colors.warning.light9)
                }
            }
        }
    }
}

@Composable
fun ScrollbarDemo() {
    val colors = NexusTheme.colorScheme
    DemoPage(title = "Scrollbar", description = "Used to replace the browser's native scrollbar.") {
        DemoSection("Basic usage") {
            NexusScrollbar(height = 220.dp) {
                repeat(20) { index ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                            .padding(vertical = 4.dp)
                            .clip(NexusTheme.shapes.small)
                            .drawBehind { drawRect(colors.primary.light9) },
                    ) {
                        NexusText(
                            text = "${index + 1}",
                            color = colors.primary.base,
                            modifier = Modifier.align(androidx.compose.ui.Alignment.Center),
                        )
                    }
                }
            }
        }
        DemoSection("Horizontal scroll") {
            NexusScrollbar(height = 90.dp, always = true) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    repeat(20) { index ->
                        Box(
                            modifier = Modifier
                            .width(90.dp)
                            .height(50.dp)
                            .clip(NexusTheme.shapes.small)
                            .drawBehind { drawRect(colors.danger.light9) },
                        ) {
                            NexusText(
                                text = "${index + 1}",
                                color = colors.danger.base,
                                modifier = Modifier.align(androidx.compose.ui.Alignment.Center),
                            )
                        }
                    }
                }
            }
        }
        DemoSection("Max height") {
            var count by remember { mutableStateOf(3) }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                NexusButton(text = "Add Item", onClick = { count++ }, size = ComponentSize.Small)
                NexusButton(text = "Delete Item", onClick = { if (count > 0) count-- }, size = ComponentSize.Small)
            }
            Spacer(modifier = Modifier.height(8.dp))
            NexusScrollbar(maxHeight = 220.dp) {
                repeat(count) { index ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                            .padding(vertical = 4.dp)
                            .clip(NexusTheme.shapes.small)
                            .drawBehind { drawRect(colors.primary.light9) },
                    ) {
                        NexusText(
                            text = "${index + 1}",
                            color = colors.primary.base,
                            modifier = Modifier.align(androidx.compose.ui.Alignment.Center),
                        )
                    }
                }
            }
        }
        DemoSection("Manual scroll") {
            val state = rememberNexusScrollbarState()
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                NexusButton(text = "Top", onClick = { state.setScrollTop(0) }, size = ComponentSize.Small)
                NexusButton(text = "Middle", onClick = { state.setScrollTop(300) }, size = ComponentSize.Small)
                NexusButton(text = "Bottom", onClick = { state.setScrollTop(1000) }, size = ComponentSize.Small)
            }
            Spacer(modifier = Modifier.height(8.dp))
            NexusScrollbar(
                state = state,
                height = 220.dp,
                always = true,
            ) {
                repeat(20) { index ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                            .padding(vertical = 4.dp)
                            .clip(NexusTheme.shapes.small)
                            .drawBehind { drawRect(colors.primary.light9) },
                    ) {
                        NexusText(
                            text = "${index + 1}",
                            color = colors.primary.base,
                            modifier = Modifier.align(androidx.compose.ui.Alignment.Center),
                        )
                    }
                }
            }
        }
        DemoSection("Infinite scroll") {
            var count by remember { mutableStateOf(30) }
            NexusScrollbar(
                height = 220.dp,
                onEndReached = { direction ->
                    if (direction == NexusScrollbarDirection.Bottom) {
                        count += 5
                    }
                },
            ) {
                repeat(count) { index ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                            .padding(vertical = 4.dp)
                            .clip(NexusTheme.shapes.small)
                            .drawBehind { drawRect(colors.primary.light9) },
                    ) {
                        NexusText(
                            text = "${index + 1}",
                            color = colors.primary.base,
                            modifier = Modifier.align(androidx.compose.ui.Alignment.Center),
                        )
                    }
                }
            }
        }
    }
}
