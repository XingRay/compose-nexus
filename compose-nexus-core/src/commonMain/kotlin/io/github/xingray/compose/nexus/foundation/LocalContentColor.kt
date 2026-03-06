package io.github.xingray.compose.nexus.foundation

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * CompositionLocal for the preferred content color at a given point in the hierarchy.
 * Components use this to default their text/icon color.
 */
val LocalContentColor = compositionLocalOf { Color(0xFF303133) }
