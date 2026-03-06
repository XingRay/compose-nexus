package io.github.xingray.compose.nexus.foundation

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.text.TextStyle

/**
 * CompositionLocal for the default [TextStyle] at a given point in the hierarchy.
 * Components use this as their default text style.
 */
val LocalTextStyle = compositionLocalOf { TextStyle.Default }
