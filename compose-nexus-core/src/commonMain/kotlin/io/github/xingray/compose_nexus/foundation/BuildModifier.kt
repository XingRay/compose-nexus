package io.github.xingray.compose_nexus.foundation

import androidx.compose.ui.Modifier

/**
 * Conditionally compose a [Modifier] chain.
 *
 * Usage:
 * ```
 * modifier then buildModifier {
 *     add(Modifier.clip(shape))
 *     if (hasBorder) add(Modifier.border(borderWidth, borderColor, shape))
 * }
 * ```
 */
inline fun buildModifier(builderAction: MutableList<Modifier>.() -> Unit): Modifier {
    return buildList(builderAction).fold(Modifier as Modifier) { acc, modifier ->
        acc then modifier
    }
}
