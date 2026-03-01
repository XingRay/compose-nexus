package io.github.xingray.compose_nexus.foundation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

/**
 * Provide both [LocalContentColor] and [LocalTextStyle] to the content hierarchy.
 */
@Composable
fun ProvideContentColorTextStyle(
    contentColor: Color,
    textStyle: TextStyle,
    content: @Composable () -> Unit,
) {
    val mergedStyle = LocalTextStyle.current.merge(textStyle)
    CompositionLocalProvider(
        LocalContentColor provides contentColor,
        LocalTextStyle provides mergedStyle,
        content = content,
    )
}

/**
 * Provide [LocalContentColor] to the content hierarchy.
 */
@Composable
fun ProvideContentColor(
    contentColor: Color,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalContentColor provides contentColor,
        content = content,
    )
}

/**
 * Provide [LocalTextStyle] to the content hierarchy.
 */
@Composable
fun ProvideTextStyle(
    textStyle: TextStyle,
    content: @Composable () -> Unit,
) {
    val mergedStyle = LocalTextStyle.current.merge(textStyle)
    CompositionLocalProvider(
        LocalTextStyle provides mergedStyle,
        content = content,
    )
}
