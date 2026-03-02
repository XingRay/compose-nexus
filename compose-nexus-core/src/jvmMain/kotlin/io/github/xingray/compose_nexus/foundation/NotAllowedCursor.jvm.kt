package io.github.xingray.compose_nexus.foundation

import androidx.compose.ui.input.pointer.PointerIcon
import java.awt.BasicStroke
import java.awt.Color
import java.awt.GraphicsEnvironment
import java.awt.Point
import java.awt.RenderingHints
import java.awt.Toolkit
import java.awt.image.BufferedImage

actual val NotAllowedPointerIcon: PointerIcon = run {
    val toolkit = Toolkit.getDefaultToolkit()
    val scale = GraphicsEnvironment.getLocalGraphicsEnvironment()
        .defaultScreenDevice.defaultConfiguration
        .defaultTransform.scaleX
    val logicalSize = 16
    val physicalSize = maxOf((logicalSize * scale).toInt(), 16)
    val bestSize = toolkit.getBestCursorSize(physicalSize, physicalSize)
    val w = maxOf(bestSize.width, 1)
    val h = maxOf(bestSize.height, 1)
    val img = BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB)
    val g = img.createGraphics()
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
    val r = (minOf(w, h) / 2 - 2) * 2 / 5
    val cx = w / 2
    val cy = h / 2
    val strokeWidth = maxOf(2f, (2f * scale).toFloat())
    // White fill inside circle
    g.color = Color.WHITE
    g.fillOval(cx - r, cy - r, r * 2, r * 2)
    // Red circle and diagonal line (top-left to bottom-right)
    g.color = Color(0xFF, 0x38, 0x38)
    g.stroke = BasicStroke(strokeWidth)
    g.drawOval(cx - r, cy - r, r * 2, r * 2)
    val d = (r * 0.7).toInt()
    g.drawLine(cx - d, cy - d, cx + d, cy + d)
    g.dispose()
    val cursor = toolkit.createCustomCursor(img, Point(cx, cy), "not-allowed")
    PointerIcon(cursor)
}
