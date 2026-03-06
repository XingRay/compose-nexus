package io.github.xingray.compose.nexus.controls

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex

@Immutable
data class WatermarkFont(
    val color: Color = Color(0x26000000),
    val fontSize: Int = 16,
    val fontWeight: FontWeight = FontWeight.Normal,
    val fontFamily: FontFamily = FontFamily.SansSerif,
    val fontGap: Int = 3,
    val fontStyle: FontStyle = FontStyle.Normal,
    val textAlign: TextAlign = TextAlign.Center,
)

@Composable
fun NexusWatermark(
    modifier: Modifier = Modifier,
    width: Int = 120,
    height: Int = 64,
    rotate: Float = -22f,
    zIndex: Float = 9f,
    content: List<String> = listOf("Element Plus"),
    font: io.github.xingray.compose.nexus.controls.WatermarkFont = _root_ide_package_.io.github.xingray.compose.nexus.controls.WatermarkFont(),
    gap: Pair<Int, Int> = 100 to 100,
    offset: Pair<Int, Int>? = null,
    image: (@Composable () -> Unit)? = null,
    body: @Composable () -> Unit,
) {
    BoxWithConstraints(modifier = modifier) {
        val startX = (offset?.first ?: gap.first / 2).coerceAtLeast(0)
        val startY = (offset?.second ?: gap.second / 2).coerceAtLeast(0)
        val stepX = (width + gap.first).coerceAtLeast(1)
        val stepY = (height + gap.second).coerceAtLeast(1)
        val columns = ((maxWidth.value.toInt() - startX).coerceAtLeast(0) / stepX) + 2
        val rows = ((maxHeight.value.toInt() - startY).coerceAtLeast(0) / stepY) + 2

        Box(modifier = Modifier.fillMaxSize()) {
            body()
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .zIndex(zIndex)
                .drawWithContent {
                    drawContent()
                },
        ) {
            repeat(rows) { row ->
                repeat(columns) { col ->
                    Box(
                        modifier = Modifier
                            .offset(x = (startX + col * stepX).dp, y = (startY + row * stepY).dp)
                            .width(width.dp)
                            .height(height.dp)
                            .graphicsLayer {
                                rotationZ = rotate
                                alpha = font.color.alpha
                            },
                        contentAlignment = Alignment.Center,
                    ) {
                        if (image != null) {
                            image()
                        } else {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(font.fontGap.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                content.forEach { line ->
                                    _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
                                        text = line,
                                        color = font.color.copy(alpha = 1f),
                                        style = TextStyle(
                                            fontSize = font.fontSize.sp,
                                            fontWeight = font.fontWeight,
                                            fontFamily = font.fontFamily,
                                            fontStyle = font.fontStyle,
                                            textAlign = font.textAlign,
                                        ),
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
