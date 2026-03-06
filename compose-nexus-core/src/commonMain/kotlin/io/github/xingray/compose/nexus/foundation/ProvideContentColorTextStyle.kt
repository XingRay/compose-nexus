package io.github.xingray.compose.nexus.foundation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

/**
 * Provide both [io.github.xingray.compose.nexus.foundation.LocalContentColor] and [io.github.xingray.compose.nexus.foundation.LocalTextStyle] to the content hierarchy.
 */
@Composable
fun ProvideContentColorTextStyle(
    contentColor: Color,
    textStyle: TextStyle,
    content: @Composable () -> Unit,
) {
    val mergedStyle = _root_ide_package_.io.github.xingray.compose.nexus.foundation.LocalTextStyle.current.merge(textStyle)
    CompositionLocalProvider(
        _root_ide_package_.io.github.xingray.compose.nexus.foundation.LocalContentColor provides contentColor,
        _root_ide_package_.io.github.xingray.compose.nexus.foundation.LocalTextStyle provides mergedStyle,
        content = content,
    )
}

/**
 * Provide [io.github.xingray.compose.nexus.foundation.LocalContentColor] to the content hierarchy.
 */
@Composable
fun ProvideContentColor(
    contentColor: Color,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        _root_ide_package_.io.github.xingray.compose.nexus.foundation.LocalContentColor provides contentColor,
        content = content,
    )
}

/**
 * Provide [io.github.xingray.compose.nexus.foundation.LocalTextStyle] to the content hierarchy.
 */
@Composable
fun ProvideTextStyle(
    textStyle: TextStyle,
    content: @Composable () -> Unit,
) {
    val mergedStyle = _root_ide_package_.io.github.xingray.compose.nexus.foundation.LocalTextStyle.current.merge(textStyle)
    CompositionLocalProvider(
        _root_ide_package_.io.github.xingray.compose.nexus.foundation.LocalTextStyle provides mergedStyle,
        content = content,
    )
}
