package io.github.xingray.compose_nexus.controls

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import io.github.xingray.compose_nexus.theme.NexusTheme
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

enum class NexusColorFormat {
    Rgb,
    Prgb,
    Hex,
    Hex3,
    Hex4,
    Hex6,
    Hex8,
    Name,
    Hsl,
    Hsv,
}

private data class HsvColor(
    val h: Float, // 0..360
    val s: Float, // 0..1
    val v: Float, // 0..1
    val a: Float, // 0..1
)

@OptIn(ExperimentalLayoutApi::class, ExperimentalFoundationApi::class)
@Composable
fun NexusColorPickerPanel(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    border: Boolean = true,
    disabled: Boolean = false,
    showAlpha: Boolean = false,
    colorFormat: NexusColorFormat? = null,
    predefine: List<String> = emptyList(),
    validateEvent: Boolean = true,
    footer: (@Composable () -> Unit)? = null,
) {
    val colors = NexusTheme.colorScheme
    val shape = NexusTheme.shapes.base
    val density = LocalDensity.current
    @Suppress("UNUSED_VARIABLE")
    val triggerValidateEvent = validateEvent

    val resolvedFormat = colorFormat ?: if (showAlpha) NexusColorFormat.Rgb else NexusColorFormat.Hex
    val parsedColor = remember(value) { parseColor(value) ?: Color(0xFF409EFF) }

    var hsv by remember { mutableStateOf(colorToHsv(parsedColor)) }
    LaunchedEffect(parsedColor) {
        hsv = colorToHsv(parsedColor)
    }

    fun emit(hsvColor: HsvColor) {
        val output = formatColor(hsvToColor(hsvColor), resolvedFormat, showAlpha)
        if (output != value) {
            onValueChange(output)
        }
    }

    val panelModifier = Modifier
        .clip(shape)
        .then(if (border) Modifier.border(1.dp, colors.border.light, shape) else Modifier)
        .background(colors.fill.blank)
        .padding(12.dp)
        .alpha(if (disabled) 0.65f else 1f)

    Column(modifier = modifier.then(panelModifier), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(NexusTheme.shapes.small),
        ) {
            val widthPx = with(density) { maxWidth.toPx() }
            val heightPx = with(density) { maxHeight.toPx() }
            val hueColor = hsvToColor(HsvColor(h = hsv.h, s = 1f, v = 1f, a = 1f))

            fun updateFromPosition(position: Offset) {
                if (disabled) return
                val newS = (position.x / widthPx).coerceIn(0f, 1f)
                val newV = (1f - position.y / heightPx).coerceIn(0f, 1f)
                val next = hsv.copy(s = newS, v = newV)
                hsv = next
                emit(next)
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(hueColor)
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color.White, Color.Transparent),
                        )
                    )
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.Transparent, Color.Black),
                        )
                    )
                    .then(
                        if (!disabled) {
                            Modifier.pointerInput(hsv.h) {
                                detectDragGestures(
                                    onDragStart = { start -> updateFromPosition(start) },
                                    onDrag = { change, _ ->
                                        change.consumeAllChanges()
                                        updateFromPosition(change.position)
                                    },
                                )
                            }
                        } else {
                            Modifier
                        }
                    ),
            )

            val pointX = (hsv.s * widthPx).roundToInt()
            val pointY = ((1f - hsv.v) * heightPx).roundToInt()
            Box(
                modifier = Modifier
                    .offset { IntOffset(pointX - 6, pointY - 6) }
                    .size(12.dp)
                    .border(2.dp, Color.White, CircleShape)
                    .drawBehind {
                        drawCircle(
                            color = Color.Black.copy(alpha = 0.25f),
                            style = Stroke(width = 1.dp.toPx()),
                        )
                    },
            )
        }

        val sliderHeight = 14.dp
        ColorSlider(
            value = hsv.h / 360f,
            modifier = Modifier
                .fillMaxWidth()
                .height(sliderHeight),
            brush = Brush.horizontalGradient(
                listOf(
                    Color.Red,
                    Color.Yellow,
                    Color.Green,
                    Color.Cyan,
                    Color.Blue,
                    Color.Magenta,
                    Color.Red,
                ),
            ),
            disabled = disabled,
        ) { fraction ->
            val next = hsv.copy(h = (fraction * 360f).coerceIn(0f, 360f))
            hsv = next
            emit(next)
        }

        if (showAlpha) {
            val rgbOpaque = hsvToColor(hsv.copy(a = 1f))
            ColorSlider(
                value = hsv.a,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(sliderHeight)
                    .drawCheckerboard(),
                brush = Brush.horizontalGradient(
                    listOf(
                        rgbOpaque.copy(alpha = 0f),
                        rgbOpaque.copy(alpha = 1f),
                    ),
                ),
                disabled = disabled,
            ) { fraction ->
                val next = hsv.copy(a = fraction.coerceIn(0f, 1f))
                hsv = next
                emit(next)
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .width(30.dp)
                    .height(20.dp)
                    .clip(NexusTheme.shapes.small)
                    .drawCheckerboard()
                    .background(hsvToColor(hsv))
                    .border(1.dp, colors.border.light, NexusTheme.shapes.small),
            )
            NexusText(
                text = formatColor(hsvToColor(hsv), resolvedFormat, showAlpha),
                style = NexusTheme.typography.small,
                color = colors.text.regular,
                modifier = Modifier.fillMaxWidth(),
                truncated = true,
            )
        }

        if (predefine.isNotEmpty()) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                predefine.forEach { colorStr ->
                    val swatch = parseColor(colorStr)
                    val selected = swatch != null && areColorsClose(swatch, hsvToColor(hsv))
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(NexusTheme.shapes.small)
                            .drawCheckerboard()
                            .background(swatch ?: Color.Transparent)
                            .border(
                                width = if (selected) 2.dp else 1.dp,
                                color = if (selected) colors.primary.base else colors.border.light,
                                shape = NexusTheme.shapes.small,
                            )
                            .then(
                                if (!disabled && swatch != null) {
                                    Modifier
                                        .clickable { 
                                            val next = colorToHsv(swatch)
                                            hsv = next
                                            emit(next)
                                        }
                                } else {
                                    Modifier
                                }
                            ),
                    )
                }
            }
        }

        if (footer != null) {
            footer()
        }
    }
}

@Composable
private fun ColorSlider(
    value: Float,
    brush: Brush,
    modifier: Modifier = Modifier,
    disabled: Boolean = false,
    onValueChange: (Float) -> Unit,
) {
    val density = LocalDensity.current
    BoxWithConstraints(
        modifier = modifier
            .clip(NexusTheme.shapes.small)
            .background(brush),
    ) {
        val widthPx = with(density) { maxWidth.toPx().coerceAtLeast(1f) }
        if (!disabled) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(widthPx) {
                        detectDragGestures(
                            onDragStart = { start ->
                                onValueChange((start.x / widthPx).coerceIn(0f, 1f))
                            },
                            onDrag = { change, _ ->
                                change.consumeAllChanges()
                                onValueChange((change.position.x / widthPx).coerceIn(0f, 1f))
                            },
                        )
                    },
            )
        }
        Box(
            modifier = Modifier
                .offset { IntOffset((value.coerceIn(0f, 1f) * widthPx).roundToInt() - 6, -2) }
                .size(12.dp, maxHeight + 4.dp)
                .border(2.dp, Color.White, NexusTheme.shapes.small),
        )
    }
}

private fun Modifier.drawCheckerboard(cell: Dp = 6.dp): Modifier = drawBehind {
    val cellPx = cell.toPx().coerceAtLeast(2f)
    val light = Color(0xFFF3F3F3)
    val dark = Color(0xFFE2E2E2)
    var y = 0f
    var row = 0
    while (y < size.height) {
        var x = 0f
        var col = row % 2
        while (x < size.width) {
            drawRect(
                color = if (col % 2 == 0) light else dark,
                topLeft = Offset(x, y),
                size = androidx.compose.ui.geometry.Size(cellPx, cellPx),
            )
            col += 1
            x += cellPx
        }
        row += 1
        y += cellPx
    }
}

private fun parseColor(raw: String): Color? {
    val text = raw.trim().lowercase()
    if (text.isEmpty()) return null

    parseHex(text)?.let { return it }
    parseRgb(text)?.let { return it }
    parseHsl(text)?.let { return it }
    parseHsv(text)?.let { return it }
    namedColor(text)?.let { return it }
    return null
}

private fun parseHex(text: String): Color? {
    if (!text.startsWith("#")) return null
    val hex = text.removePrefix("#")
    return when (hex.length) {
        3 -> {
            val r = hex[0].toString().repeat(2).toIntOrNull(16) ?: return null
            val g = hex[1].toString().repeat(2).toIntOrNull(16) ?: return null
            val b = hex[2].toString().repeat(2).toIntOrNull(16) ?: return null
            Color(r / 255f, g / 255f, b / 255f, 1f)
        }
        4 -> {
            val r = hex[0].toString().repeat(2).toIntOrNull(16) ?: return null
            val g = hex[1].toString().repeat(2).toIntOrNull(16) ?: return null
            val b = hex[2].toString().repeat(2).toIntOrNull(16) ?: return null
            val a = hex[3].toString().repeat(2).toIntOrNull(16) ?: return null
            Color(r / 255f, g / 255f, b / 255f, a / 255f)
        }
        6 -> {
            val r = hex.substring(0, 2).toIntOrNull(16) ?: return null
            val g = hex.substring(2, 4).toIntOrNull(16) ?: return null
            val b = hex.substring(4, 6).toIntOrNull(16) ?: return null
            Color(r / 255f, g / 255f, b / 255f, 1f)
        }
        8 -> {
            val r = hex.substring(0, 2).toIntOrNull(16) ?: return null
            val g = hex.substring(2, 4).toIntOrNull(16) ?: return null
            val b = hex.substring(4, 6).toIntOrNull(16) ?: return null
            val a = hex.substring(6, 8).toIntOrNull(16) ?: return null
            Color(r / 255f, g / 255f, b / 255f, a / 255f)
        }
        else -> null
    }
}

private fun parseRgb(text: String): Color? {
    val normalized = text.replace(" ", "")
    if (!normalized.startsWith("rgb(") && !normalized.startsWith("rgba(")) return null
    val body = normalized.substringAfter("(").substringBeforeLast(")")
    val parts = body.split(",")
    if (parts.size !in 3..4) return null
    val channels = parts.take(3).map { parseChannel(it) ?: return null }
    val alpha = parts.getOrNull(3)?.toFloatOrNull()?.coerceIn(0f, 1f) ?: 1f
    return Color(channels[0], channels[1], channels[2], alpha)
}

private fun parseHsl(text: String): Color? {
    val normalized = text.replace(" ", "")
    if (!normalized.startsWith("hsl(") && !normalized.startsWith("hsla(")) return null
    val body = normalized.substringAfter("(").substringBeforeLast(")")
    val parts = body.split(",")
    if (parts.size !in 3..4) return null
    val h = parts[0].toFloatOrNull() ?: return null
    val s = parsePercent(parts[1]) ?: return null
    val l = parsePercent(parts[2]) ?: return null
    val alpha = parts.getOrNull(3)?.toFloatOrNull()?.coerceIn(0f, 1f) ?: 1f
    return hslToColor(h, s, l, alpha)
}

private fun parseHsv(text: String): Color? {
    val normalized = text.replace(" ", "")
    if (!normalized.startsWith("hsv(") && !normalized.startsWith("hsva(")) return null
    val body = normalized.substringAfter("(").substringBeforeLast(")")
    val parts = body.split(",")
    if (parts.size !in 3..4) return null
    val h = parts[0].toFloatOrNull() ?: return null
    val s = parsePercent(parts[1]) ?: return null
    val v = parsePercent(parts[2]) ?: return null
    val alpha = parts.getOrNull(3)?.toFloatOrNull()?.coerceIn(0f, 1f) ?: 1f
    return hsvToColor(HsvColor(h = h, s = s, v = v, a = alpha))
}

private fun parseChannel(input: String): Float? {
    return if (input.endsWith("%")) {
        val p = parsePercent(input) ?: return null
        p.coerceIn(0f, 1f)
    } else {
        (input.toFloatOrNull() ?: return null).coerceIn(0f, 255f) / 255f
    }
}

private fun parsePercent(input: String): Float? {
    val raw = input.removeSuffix("%").toFloatOrNull() ?: return null
    return (raw / 100f).coerceIn(0f, 1f)
}

private fun namedColor(name: String): Color? = when (name) {
    "red" -> Color.Red
    "green" -> Color.Green
    "blue" -> Color.Blue
    "black" -> Color.Black
    "white" -> Color.White
    "yellow" -> Color.Yellow
    "cyan" -> Color.Cyan
    "magenta" -> Color.Magenta
    "gray", "grey" -> Color.Gray
    else -> null
}

private fun colorToHsv(color: Color): HsvColor {
    val r = color.red
    val g = color.green
    val b = color.blue
    val max = max(r, max(g, b))
    val min = min(r, min(g, b))
    val delta = max - min

    val h = when {
        delta == 0f -> 0f
        max == r -> (60f * ((g - b) / delta) + 360f) % 360f
        max == g -> (60f * ((b - r) / delta) + 120f) % 360f
        else -> (60f * ((r - g) / delta) + 240f) % 360f
    }
    val s = if (max == 0f) 0f else delta / max
    val v = max
    return HsvColor(h = h, s = s, v = v, a = color.alpha)
}

private fun hsvToColor(hsv: HsvColor): Color {
    val h = ((hsv.h % 360f) + 360f) % 360f
    val s = hsv.s.coerceIn(0f, 1f)
    val v = hsv.v.coerceIn(0f, 1f)
    val c = v * s
    val x = c * (1 - abs((h / 60f) % 2 - 1))
    val m = v - c

    val (r1, g1, b1) = when {
        h < 60f -> Triple(c, x, 0f)
        h < 120f -> Triple(x, c, 0f)
        h < 180f -> Triple(0f, c, x)
        h < 240f -> Triple(0f, x, c)
        h < 300f -> Triple(x, 0f, c)
        else -> Triple(c, 0f, x)
    }
    return Color(
        red = (r1 + m).coerceIn(0f, 1f),
        green = (g1 + m).coerceIn(0f, 1f),
        blue = (b1 + m).coerceIn(0f, 1f),
        alpha = hsv.a.coerceIn(0f, 1f),
    )
}

private fun hslToColor(h: Float, s: Float, l: Float, alpha: Float): Color {
    val c = (1f - abs(2f * l - 1f)) * s
    val x = c * (1f - abs(((h / 60f) % 2f) - 1f))
    val m = l - c / 2f
    val hh = ((h % 360f) + 360f) % 360f
    val (r1, g1, b1) = when {
        hh < 60f -> Triple(c, x, 0f)
        hh < 120f -> Triple(x, c, 0f)
        hh < 180f -> Triple(0f, c, x)
        hh < 240f -> Triple(0f, x, c)
        hh < 300f -> Triple(x, 0f, c)
        else -> Triple(c, 0f, x)
    }
    return Color(r1 + m, g1 + m, b1 + m, alpha.coerceIn(0f, 1f))
}

private fun formatColor(color: Color, format: NexusColorFormat, showAlpha: Boolean): String {
    val includeAlpha = showAlpha || color.alpha < 0.999f
    return when (format) {
        NexusColorFormat.Hex -> toHex(color, short = false, includeAlpha = includeAlpha)
        NexusColorFormat.Hex3 -> toHex(color, short = true, includeAlpha = false)
        NexusColorFormat.Hex4 -> toHex(color, short = true, includeAlpha = true)
        NexusColorFormat.Hex6 -> toHex(color, short = false, includeAlpha = false)
        NexusColorFormat.Hex8 -> toHex(color, short = false, includeAlpha = true)
        NexusColorFormat.Rgb -> toRgb(color, percent = false, includeAlpha = includeAlpha)
        NexusColorFormat.Prgb -> toRgb(color, percent = true, includeAlpha = includeAlpha)
        NexusColorFormat.Hsl -> toHsl(color, includeAlpha = includeAlpha)
        NexusColorFormat.Hsv -> toHsv(color, includeAlpha = includeAlpha)
        NexusColorFormat.Name -> toName(color) ?: toHex(color, short = false, includeAlpha = false)
    }
}

private fun toHex(color: Color, short: Boolean, includeAlpha: Boolean): String {
    val r = (color.red * 255f).roundToInt().coerceIn(0, 255)
    val g = (color.green * 255f).roundToInt().coerceIn(0, 255)
    val b = (color.blue * 255f).roundToInt().coerceIn(0, 255)
    val a = (color.alpha * 255f).roundToInt().coerceIn(0, 255)

    val long = if (includeAlpha) {
        "#${hex2(r)}${hex2(g)}${hex2(b)}${hex2(a)}"
    } else {
        "#${hex2(r)}${hex2(g)}${hex2(b)}"
    }
    if (!short) return long

    val compact = if (includeAlpha) {
        "#${hex1IfPossible(r)}${hex1IfPossible(g)}${hex1IfPossible(b)}${hex1IfPossible(a)}"
    } else {
        "#${hex1IfPossible(r)}${hex1IfPossible(g)}${hex1IfPossible(b)}"
    }
    return if (compact.contains('?')) long else compact
}

private fun hex2(value: Int): String = value.toString(16).padStart(2, '0').uppercase()

private fun hex1IfPossible(value: Int): Char {
    val high = value shr 4
    val low = value and 0xF
    return if (high == low) "0123456789ABCDEF"[high] else '?'
}

private fun toRgb(color: Color, percent: Boolean, includeAlpha: Boolean): String {
    val r = (color.red * 255f).roundToInt().coerceIn(0, 255)
    val g = (color.green * 255f).roundToInt().coerceIn(0, 255)
    val b = (color.blue * 255f).roundToInt().coerceIn(0, 255)
    return if (percent) {
        val rp = ((r / 255f) * 100f).roundToInt()
        val gp = ((g / 255f) * 100f).roundToInt()
        val bp = ((b / 255f) * 100f).roundToInt()
        if (includeAlpha) {
            "rgba(${rp}%, ${gp}%, ${bp}%, ${formatFloat2(color.alpha)})"
        } else {
            "rgb(${rp}%, ${gp}%, ${bp}%)"
        }
    } else {
        if (includeAlpha) {
            "rgba($r, $g, $b, ${formatFloat2(color.alpha)})"
        } else {
            "rgb($r, $g, $b)"
        }
    }
}

private fun toHsl(color: Color, includeAlpha: Boolean): String {
    val r = color.red
    val g = color.green
    val b = color.blue
    val max = max(r, max(g, b))
    val min = min(r, min(g, b))
    val delta = max - min
    val l = (max + min) / 2f
    val s = if (delta == 0f) 0f else delta / (1f - abs(2f * l - 1f))
    val h = when {
        delta == 0f -> 0f
        max == r -> (60f * ((g - b) / delta) + 360f) % 360f
        max == g -> (60f * ((b - r) / delta) + 120f) % 360f
        else -> (60f * ((r - g) / delta) + 240f) % 360f
    }
    val hs = h.roundToInt()
    val ss = (s * 100f).roundToInt()
    val ls = (l * 100f).roundToInt()
    return if (includeAlpha) {
        "hsla($hs, ${ss}%, ${ls}%, ${formatFloat2(color.alpha)})"
    } else {
        "hsl($hs, ${ss}%, ${ls}%)"
    }
}

private fun toHsv(color: Color, includeAlpha: Boolean): String {
    val hsv = colorToHsv(color)
    val h = hsv.h.roundToInt()
    val s = (hsv.s * 100f).roundToInt()
    val v = (hsv.v * 100f).roundToInt()
    return if (includeAlpha) {
        "hsva($h, ${s}%, ${v}%, ${formatFloat2(hsv.a)})"
    } else {
        "hsv($h, ${s}%, ${v}%)"
    }
}

private fun toName(color: Color): String? {
    val c = toHex(color.copy(alpha = 1f), short = false, includeAlpha = false).lowercase()
    return when (c) {
        "#ff0000" -> "red"
        "#00ff00" -> "green"
        "#0000ff" -> "blue"
        "#000000" -> "black"
        "#ffffff" -> "white"
        "#ffff00" -> "yellow"
        "#00ffff" -> "cyan"
        "#ff00ff" -> "magenta"
        "#808080" -> "gray"
        else -> null
    }
}

private fun areColorsClose(left: Color, right: Color): Boolean {
    fun close(a: Float, b: Float): Boolean = abs(a - b) <= 0.01f
    return close(left.red, right.red) &&
        close(left.green, right.green) &&
        close(left.blue, right.blue) &&
        close(left.alpha, right.alpha)
}

private fun formatFloat2(value: Float): String {
    val scaled = (value.coerceIn(0f, 1f) * 100f).roundToInt() / 100f
    val text = scaled.toString()
    return if (text.contains('.')) {
        text.padEnd(text.indexOf('.') + 3, '0')
    } else {
        "$text.00"
    }
}
