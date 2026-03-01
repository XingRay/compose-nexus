package io.github.xingray.compose_nexus.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * A single shadow layer specification, analogous to one CSS box-shadow value.
 */
@Immutable
data class ShadowLayer(
    val offsetX: Dp = 0.dp,
    val offsetY: Dp = 0.dp,
    val blurRadius: Dp = 0.dp,
    val spreadRadius: Dp = 0.dp,
    val color: Color = Color.Black.copy(alpha = 0.12f),
)

/**
 * A complete shadow definition composed of multiple [ShadowLayer]s.
 * Also provides a simplified [elevation] value for Compose's built-in shadow modifier.
 */
@Immutable
data class NexusShadow(
    val layers: List<ShadowLayer>,
    val elevation: Dp,
)

/**
 * Element Plus shadow system.
 *
 * Light mode:
 *   --el-box-shadow:         0px 12px 32px 4px rgba(0,0,0,0.04), 0px 8px 20px rgba(0,0,0,0.08)
 *   --el-box-shadow-light:   0px 0px 12px rgba(0,0,0,0.12)
 *   --el-box-shadow-lighter: 0px 0px 6px rgba(0,0,0,0.12)
 *   --el-box-shadow-dark:    0px 16px 48px 16px rgba(0,0,0,0.08), 0px 12px 32px rgba(0,0,0,0.12), 0px 8px 16px -8px rgba(0,0,0,0.16)
 */
@Immutable
data class NexusShadows(
    /** No shadow */
    val none: NexusShadow = NexusShadow(layers = emptyList(), elevation = 0.dp),

    /** Subtle shadow: 0px 0px 6px rgba(0,0,0,0.12) */
    val lighter: NexusShadow = NexusShadow(
        layers = listOf(
            ShadowLayer(0.dp, 0.dp, 6.dp, 0.dp, Color.Black.copy(alpha = 0.12f)),
        ),
        elevation = 1.dp,
    ),

    /** Light shadow: 0px 0px 12px rgba(0,0,0,0.12) */
    val light: NexusShadow = NexusShadow(
        layers = listOf(
            ShadowLayer(0.dp, 0.dp, 12.dp, 0.dp, Color.Black.copy(alpha = 0.12f)),
        ),
        elevation = 2.dp,
    ),

    /** Default shadow */
    val default: NexusShadow = NexusShadow(
        layers = listOf(
            ShadowLayer(0.dp, 12.dp, 32.dp, 4.dp, Color.Black.copy(alpha = 0.04f)),
            ShadowLayer(0.dp, 8.dp, 20.dp, 0.dp, Color.Black.copy(alpha = 0.08f)),
        ),
        elevation = 4.dp,
    ),

    /** Heavy shadow */
    val dark: NexusShadow = NexusShadow(
        layers = listOf(
            ShadowLayer(0.dp, 16.dp, 48.dp, 16.dp, Color.Black.copy(alpha = 0.08f)),
            ShadowLayer(0.dp, 12.dp, 32.dp, 0.dp, Color.Black.copy(alpha = 0.12f)),
            ShadowLayer(0.dp, 8.dp, 16.dp, (-8).dp, Color.Black.copy(alpha = 0.16f)),
        ),
        elevation = 8.dp,
    ),
) {
    companion object {
        /** Light mode shadows (default) */
        fun light() = NexusShadows()

        /** Dark mode shadows with stronger opacity */
        fun dark() = NexusShadows(
            lighter = NexusShadow(
                layers = listOf(
                    ShadowLayer(0.dp, 0.dp, 6.dp, 0.dp, Color.Black.copy(alpha = 0.72f)),
                ),
                elevation = 1.dp,
            ),
            light = NexusShadow(
                layers = listOf(
                    ShadowLayer(0.dp, 0.dp, 12.dp, 0.dp, Color.Black.copy(alpha = 0.72f)),
                ),
                elevation = 2.dp,
            ),
            default = NexusShadow(
                layers = listOf(
                    ShadowLayer(0.dp, 12.dp, 32.dp, 4.dp, Color.Black.copy(alpha = 0.36f)),
                    ShadowLayer(0.dp, 8.dp, 20.dp, 0.dp, Color.Black.copy(alpha = 0.72f)),
                ),
                elevation = 4.dp,
            ),
            dark = NexusShadow(
                layers = listOf(
                    ShadowLayer(0.dp, 16.dp, 48.dp, 16.dp, Color.Black.copy(alpha = 0.72f)),
                    ShadowLayer(0.dp, 12.dp, 32.dp, 0.dp, Color.Black),
                    ShadowLayer(0.dp, 8.dp, 16.dp, (-8).dp, Color.Black),
                ),
                elevation = 8.dp,
            ),
        )
    }
}
