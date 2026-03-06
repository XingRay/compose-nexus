package io.github.xingray.compose.nexus.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import io.github.xingray.compose.nexus.foundation.LocalContentColor
import io.github.xingray.compose.nexus.foundation.LocalTextStyle

// ============================================================================
// CompositionLocals
// ============================================================================

val LocalNexusColorScheme = staticCompositionLocalOf { _root_ide_package_.io.github.xingray.compose.nexus.theme.lightColorScheme() }
val LocalNexusTypography = staticCompositionLocalOf { _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTypography() }
val LocalNexusSizes = staticCompositionLocalOf { _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusSizes() }
val LocalNexusShapes = staticCompositionLocalOf { _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusShapes() }
val LocalNexusShadows = staticCompositionLocalOf { _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusShadows() }
val LocalNexusMotion = staticCompositionLocalOf { _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusMotion() }

// ============================================================================
// NexusTheme composable
// ============================================================================

/**
 * Nexus theme composable that provides all design tokens to the content hierarchy.
 *
 * Usage:
 * ```
 * NexusTheme {
 *     // All Nexus components inherit the theme
 *     NexusButton(onClick = {}) { Text("Click") }
 * }
 *
 * // Dark mode:
 * NexusTheme(colorScheme = darkColorScheme()) {
 *     // ...
 * }
 *
 * // Custom primary color:
 * NexusTheme(
 *     colorScheme = lightColorScheme(
 *         primary = generateTypeColor(Color(0xFF6366F1))
 *     )
 * ) {
 *     // ...
 * }
 * ```
 */
@Composable
fun NexusTheme(
    colorScheme: io.github.xingray.compose.nexus.theme.NexusColorScheme = _root_ide_package_.io.github.xingray.compose.nexus.theme.lightColorScheme(),
    typography: io.github.xingray.compose.nexus.theme.NexusTypography = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTypography(),
    sizes: io.github.xingray.compose.nexus.theme.NexusSizes = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusSizes(),
    shapes: io.github.xingray.compose.nexus.theme.NexusShapes = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusShapes(),
    shadows: io.github.xingray.compose.nexus.theme.NexusShadows = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusShadows(),
    motion: io.github.xingray.compose.nexus.theme.NexusMotion = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusMotion(),
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        _root_ide_package_.io.github.xingray.compose.nexus.theme.LocalNexusColorScheme provides colorScheme,
        _root_ide_package_.io.github.xingray.compose.nexus.theme.LocalNexusTypography provides typography,
        _root_ide_package_.io.github.xingray.compose.nexus.theme.LocalNexusSizes provides sizes,
        _root_ide_package_.io.github.xingray.compose.nexus.theme.LocalNexusShapes provides shapes,
        _root_ide_package_.io.github.xingray.compose.nexus.theme.LocalNexusShadows provides shadows,
        _root_ide_package_.io.github.xingray.compose.nexus.theme.LocalNexusMotion provides motion,
        // Set foundation locals to match the theme
        _root_ide_package_.io.github.xingray.compose.nexus.foundation.LocalContentColor provides colorScheme.text.primary,
        _root_ide_package_.io.github.xingray.compose.nexus.foundation.LocalTextStyle provides typography.base,
    ) {
        content()
    }
}

// ============================================================================
// NexusTheme accessor object
// ============================================================================

/**
 * Convenient accessor for current theme values within a [NexusTheme] scope.
 *
 * Usage:
 * ```
 * val primaryColor = NexusTheme.colorScheme.primary.base
 * val baseTextStyle = NexusTheme.typography.base
 * val defaultHeight = NexusTheme.sizes.componentDefault
 * ```
 */
object NexusTheme {

    /** Current [io.github.xingray.compose.nexus.theme.NexusColorScheme] from the composition. */
    val colorScheme: io.github.xingray.compose.nexus.theme.NexusColorScheme
        @Composable
        @ReadOnlyComposable
        get() = _root_ide_package_.io.github.xingray.compose.nexus.theme.LocalNexusColorScheme.current

    /** Current [io.github.xingray.compose.nexus.theme.NexusTypography] from the composition. */
    val typography: io.github.xingray.compose.nexus.theme.NexusTypography
        @Composable
        @ReadOnlyComposable
        get() = _root_ide_package_.io.github.xingray.compose.nexus.theme.LocalNexusTypography.current

    /** Current [io.github.xingray.compose.nexus.theme.NexusSizes] from the composition. */
    val sizes: io.github.xingray.compose.nexus.theme.NexusSizes
        @Composable
        @ReadOnlyComposable
        get() = _root_ide_package_.io.github.xingray.compose.nexus.theme.LocalNexusSizes.current

    /** Current [io.github.xingray.compose.nexus.theme.NexusShapes] from the composition. */
    val shapes: io.github.xingray.compose.nexus.theme.NexusShapes
        @Composable
        @ReadOnlyComposable
        get() = _root_ide_package_.io.github.xingray.compose.nexus.theme.LocalNexusShapes.current

    /** Current [io.github.xingray.compose.nexus.theme.NexusShadows] from the composition. */
    val shadows: io.github.xingray.compose.nexus.theme.NexusShadows
        @Composable
        @ReadOnlyComposable
        get() = _root_ide_package_.io.github.xingray.compose.nexus.theme.LocalNexusShadows.current

    /** Current [io.github.xingray.compose.nexus.theme.NexusMotion] from the composition. */
    val motion: io.github.xingray.compose.nexus.theme.NexusMotion
        @Composable
        @ReadOnlyComposable
        get() = _root_ide_package_.io.github.xingray.compose.nexus.theme.LocalNexusMotion.current
}
