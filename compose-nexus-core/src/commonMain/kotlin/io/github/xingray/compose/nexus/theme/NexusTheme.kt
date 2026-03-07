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

val LocalNexusColorScheme = staticCompositionLocalOf { lightColorScheme() }
val LocalNexusTypography = staticCompositionLocalOf { NexusTypography() }
val LocalNexusSizes = staticCompositionLocalOf { NexusSizes() }
val LocalNexusShapes = staticCompositionLocalOf { NexusShapes() }
val LocalNexusShadows = staticCompositionLocalOf { NexusShadows() }
val LocalNexusMotion = staticCompositionLocalOf { NexusMotion() }

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
    colorScheme: NexusColorScheme = lightColorScheme(),
    typography: NexusTypography = NexusTypography(),
    sizes: NexusSizes = NexusSizes(),
    shapes: NexusShapes = NexusShapes(),
    shadows: NexusShadows = NexusShadows(),
    motion: NexusMotion = NexusMotion(),
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalNexusColorScheme provides colorScheme,
        LocalNexusTypography provides typography,
        LocalNexusSizes provides sizes,
        LocalNexusShapes provides shapes,
        LocalNexusShadows provides shadows,
        LocalNexusMotion provides motion,
        // Set foundation locals to match the theme
        LocalContentColor provides colorScheme.text.primary,
        LocalTextStyle provides typography.base,
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
    val colorScheme: NexusColorScheme
        @Composable
        @ReadOnlyComposable
        get() = LocalNexusColorScheme.current

    /** Current [io.github.xingray.compose.nexus.theme.NexusTypography] from the composition. */
    val typography: NexusTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalNexusTypography.current

    /** Current [io.github.xingray.compose.nexus.theme.NexusSizes] from the composition. */
    val sizes: NexusSizes
        @Composable
        @ReadOnlyComposable
        get() = LocalNexusSizes.current

    /** Current [io.github.xingray.compose.nexus.theme.NexusShapes] from the composition. */
    val shapes: NexusShapes
        @Composable
        @ReadOnlyComposable
        get() = LocalNexusShapes.current

    /** Current [io.github.xingray.compose.nexus.theme.NexusShadows] from the composition. */
    val shadows: NexusShadows
        @Composable
        @ReadOnlyComposable
        get() = LocalNexusShadows.current

    /** Current [io.github.xingray.compose.nexus.theme.NexusMotion] from the composition. */
    val motion: NexusMotion
        @Composable
        @ReadOnlyComposable
        get() = LocalNexusMotion.current
}
