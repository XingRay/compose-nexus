package io.github.xingray.compose_nexus.theme

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Element Plus component sizing system.
 *
 * Component heights: large=40dp, default=32dp, small=24dp
 * Input horizontal padding: large=16dp, default=12dp, small=8dp
 * Button padding: large=13x20, default=9x16, small=6x12
 */
@Immutable
data class NexusSizes(
    // ---- Component heights ----
    /** 40dp - Large component height */
    val componentLarge: Dp = 40.dp,
    /** 32dp - Default component height */
    val componentDefault: Dp = 32.dp,
    /** 24dp - Small component height */
    val componentSmall: Dp = 24.dp,

    // ---- Input padding ----
    /** 16dp - Large input horizontal padding */
    val inputPaddingLarge: Dp = 16.dp,
    /** 12dp - Default input horizontal padding */
    val inputPaddingDefault: Dp = 12.dp,
    /** 8dp - Small input horizontal padding */
    val inputPaddingSmall: Dp = 8.dp,

    // ---- Button padding ----
    val buttonPaddingLarge: PaddingValues = PaddingValues(horizontal = 20.dp, vertical = 13.dp),
    val buttonPaddingDefault: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 9.dp),
    val buttonPaddingSmall: PaddingValues = PaddingValues(horizontal = 12.dp, vertical = 6.dp),

    // ---- Spacing ----
    /** 4dp */
    val spacingExtraSmall: Dp = 4.dp,
    /** 8dp */
    val spacingSmall: Dp = 8.dp,
    /** 12dp */
    val spacingDefault: Dp = 12.dp,
    /** 16dp */
    val spacingMedium: Dp = 16.dp,
    /** 20dp */
    val spacingLarge: Dp = 20.dp,
    /** 24dp */
    val spacingExtraLarge: Dp = 24.dp,

    // ---- Layout ----
    /** 60dp - Header/Footer height */
    val headerHeight: Dp = 60.dp,
    /** 20dp - Main area padding */
    val mainPadding: Dp = 20.dp,
) {
    companion object {
        /** Returns the component height for a given [ComponentSize]. */
        fun componentHeight(size: ComponentSize, sizes: NexusSizes = NexusSizes()): Dp =
            when (size) {
                ComponentSize.Large -> sizes.componentLarge
                ComponentSize.Default -> sizes.componentDefault
                ComponentSize.Small -> sizes.componentSmall
            }
    }
}

/**
 * Standard component size variants matching Element Plus.
 */
enum class ComponentSize {
    Large,
    Default,
    Small,
}
