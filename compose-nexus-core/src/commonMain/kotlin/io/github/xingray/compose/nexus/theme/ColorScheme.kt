package io.github.xingray.compose.nexus.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import io.github.xingray.compose.nexus.theme.mix

// ============================================================================
// Color mixing utility (replicates Element Plus SCSS mix function)
// ============================================================================

/**
 * Mix two colors by [weight]. weight=0.0 → 100% of [this], weight=1.0 → 100% of [other].
 *
 * Element Plus uses SCSS `mix(white, base, N%)` where N% is the weight of white.
 */
fun Color.mix(other: Color, weight: Float): Color {
    val w = weight.coerceIn(0f, 1f)
    return Color(
        red = this.red * (1 - w) + other.red * w,
        green = this.green * (1 - w) + other.green * w,
        blue = this.blue * (1 - w) + other.blue * w,
        alpha = this.alpha * (1 - w) + other.alpha * w,
    )
}

/**
 * Generate a complete [io.github.xingray.compose.nexus.theme.TypeColor] from a [base] color using Element Plus mixing rules:
 * - light-N: mix base with white at N*10%
 * - dark-2: mix base with black at 20%
 */
fun generateTypeColor(
    base: Color,
    mixLight: Color = Color.White,
    mixDark: Color = Color.Black,
): io.github.xingray.compose.nexus.theme.TypeColor = _root_ide_package_.io.github.xingray.compose.nexus.theme.TypeColor(
    base = base,
    light1 = base.mix(mixLight, 0.1f),
    light2 = base.mix(mixLight, 0.2f),
    light3 = base.mix(mixLight, 0.3f),
    light4 = base.mix(mixLight, 0.4f),
    light5 = base.mix(mixLight, 0.5f),
    light6 = base.mix(mixLight, 0.6f),
    light7 = base.mix(mixLight, 0.7f),
    light8 = base.mix(mixLight, 0.8f),
    light9 = base.mix(mixLight, 0.9f),
    dark2 = base.mix(mixDark, 0.2f),
)

// ============================================================================
// Type color: a base color with 10 light/dark variants
// ============================================================================

@Immutable
data class TypeColor(
    val base: Color,
    val light1: Color,
    val light2: Color,
    val light3: Color,
    val light4: Color,
    val light5: Color,
    val light6: Color,
    val light7: Color,
    val light8: Color,
    val light9: Color,
    val dark2: Color,
)

// ============================================================================
// Semantic color groups
// ============================================================================

@Immutable
data class TextColors(
    val primary: Color,
    val regular: Color,
    val secondary: Color,
    val placeholder: Color,
    val disabled: Color,
)

@Immutable
data class BorderColors(
    val base: Color,
    val light: Color,
    val lighter: Color,
    val extraLight: Color,
    val dark: Color,
    val darker: Color,
)

@Immutable
data class FillColors(
    val base: Color,
    val light: Color,
    val lighter: Color,
    val extraLight: Color,
    val dark: Color,
    val darker: Color,
    val blank: Color,
)

@Immutable
data class BackgroundColors(
    val base: Color,
    val page: Color,
    val overlay: Color,
)

@Immutable
data class OverlayColors(
    val base: Color,
    val light: Color,
    val lighter: Color,
)

@Immutable
data class MaskColors(
    val base: Color,
    val extraLight: Color,
)

@Immutable
data class DisabledColors(
    val background: Color,
    val text: Color,
    val border: Color,
)

// ============================================================================
// NexusColorScheme: the complete color system
// ============================================================================

@Immutable
data class NexusColorScheme(
    val primary: io.github.xingray.compose.nexus.theme.TypeColor,
    val success: io.github.xingray.compose.nexus.theme.TypeColor,
    val warning: io.github.xingray.compose.nexus.theme.TypeColor,
    val danger: io.github.xingray.compose.nexus.theme.TypeColor,
    val info: io.github.xingray.compose.nexus.theme.TypeColor,
    val white: Color,
    val black: Color,
    val text: io.github.xingray.compose.nexus.theme.TextColors,
    val border: io.github.xingray.compose.nexus.theme.BorderColors,
    val fill: io.github.xingray.compose.nexus.theme.FillColors,
    val background: io.github.xingray.compose.nexus.theme.BackgroundColors,
    val overlay: io.github.xingray.compose.nexus.theme.OverlayColors,
    val mask: io.github.xingray.compose.nexus.theme.MaskColors,
    val disabled: io.github.xingray.compose.nexus.theme.DisabledColors,
)

// ============================================================================
// Light color scheme (Element Plus default)
// ============================================================================

fun lightColorScheme(
    primary: io.github.xingray.compose.nexus.theme.TypeColor = _root_ide_package_.io.github.xingray.compose.nexus.theme.TypeColor(
        base = Color(0xFF409EFF),
        light1 = Color(0xFF53A8FF),
        light2 = Color(0xFF66B1FF),
        light3 = Color(0xFF79BBFF),
        light4 = Color(0xFF8CC5FF),
        light5 = Color(0xFFA0CEFF),
        light6 = Color(0xFFB3D8FF),
        light7 = Color(0xFFC6E2FF),
        light8 = Color(0xFFD9ECFF),
        light9 = Color(0xFFECF5FF),
        dark2 = Color(0xFF337ECC),
    ),
    success: io.github.xingray.compose.nexus.theme.TypeColor = _root_ide_package_.io.github.xingray.compose.nexus.theme.TypeColor(
        base = Color(0xFF67C23A),
        light1 = Color(0xFF76C84E),
        light2 = Color(0xFF85CE61),
        light3 = Color(0xFF95D475),
        light4 = Color(0xFFA4DA89),
        light5 = Color(0xFFB3E09C),
        light6 = Color(0xFFC2E7B0),
        light7 = Color(0xFFD1EDC4),
        light8 = Color(0xFFE1F3D8),
        light9 = Color(0xFFF0F9EB),
        dark2 = Color(0xFF529B2E),
    ),
    warning: io.github.xingray.compose.nexus.theme.TypeColor = _root_ide_package_.io.github.xingray.compose.nexus.theme.TypeColor(
        base = Color(0xFFE6A23C),
        light1 = Color(0xFFE8AB50),
        light2 = Color(0xFFEBB563),
        light3 = Color(0xFFEEBE76),
        light4 = Color(0xFFF0C78A),
        light5 = Color(0xFFF2D09E),
        light6 = Color(0xFFF5DAB1),
        light7 = Color(0xFFF8E3C4),
        light8 = Color(0xFFFAECD8),
        light9 = Color(0xFFFCF6EC),
        dark2 = Color(0xFFB88230),
    ),
    danger: io.github.xingray.compose.nexus.theme.TypeColor = _root_ide_package_.io.github.xingray.compose.nexus.theme.TypeColor(
        base = Color(0xFFF56C6C),
        light1 = Color(0xFFF67B7B),
        light2 = Color(0xFFF78989),
        light3 = Color(0xFFF89898),
        light4 = Color(0xFFF9A7A7),
        light5 = Color(0xFFFAB6B6),
        light6 = Color(0xFFFBC4C4),
        light7 = Color(0xFFFCD3D3),
        light8 = Color(0xFFFDE2E2),
        light9 = Color(0xFFFEF0F0),
        dark2 = Color(0xFFC45656),
    ),
    info: io.github.xingray.compose.nexus.theme.TypeColor = _root_ide_package_.io.github.xingray.compose.nexus.theme.TypeColor(
        base = Color(0xFF909399),
        light1 = Color(0xFF9B9EA3),
        light2 = Color(0xFFA6A9AD),
        light3 = Color(0xFFB1B3B8),
        light4 = Color(0xFFBCBEC2),
        light5 = Color(0xFFC8C9CC),
        light6 = Color(0xFFD3D4D6),
        light7 = Color(0xFFDEDFE0),
        light8 = Color(0xFFE9E9EB),
        light9 = Color(0xFFF4F4F5),
        dark2 = Color(0xFF73767A),
    ),
    white: Color = Color(0xFFFFFFFF),
    black: Color = Color(0xFF000000),
    text: io.github.xingray.compose.nexus.theme.TextColors = _root_ide_package_.io.github.xingray.compose.nexus.theme.TextColors(
        primary = Color(0xFF303133),
        regular = Color(0xFF606266),
        secondary = Color(0xFF909399),
        placeholder = Color(0xFFA8ABB2),
        disabled = Color(0xFFC0C4CC),
    ),
    border: io.github.xingray.compose.nexus.theme.BorderColors = _root_ide_package_.io.github.xingray.compose.nexus.theme.BorderColors(
        base = Color(0xFFDCDFE6),
        light = Color(0xFFE4E7ED),
        lighter = Color(0xFFEBEEF5),
        extraLight = Color(0xFFF2F6FC),
        dark = Color(0xFFD4D7DE),
        darker = Color(0xFFCDD0D6),
    ),
    fill: io.github.xingray.compose.nexus.theme.FillColors = _root_ide_package_.io.github.xingray.compose.nexus.theme.FillColors(
        base = Color(0xFFF0F2F5),
        light = Color(0xFFF5F7FA),
        lighter = Color(0xFFFAFAFA),
        extraLight = Color(0xFFFAFCFF),
        dark = Color(0xFFEBEDF0),
        darker = Color(0xFFE6E8EB),
        blank = Color(0xFFFFFFFF),
    ),
    background: io.github.xingray.compose.nexus.theme.BackgroundColors = _root_ide_package_.io.github.xingray.compose.nexus.theme.BackgroundColors(
        base = Color(0xFFFFFFFF),
        page = Color(0xFFF2F3F5),
        overlay = Color(0xFFFFFFFF),
    ),
    overlay: io.github.xingray.compose.nexus.theme.OverlayColors = _root_ide_package_.io.github.xingray.compose.nexus.theme.OverlayColors(
        base = Color(0xCC000000),       // rgba(0,0,0,0.8)
        light = Color(0xB3000000),      // rgba(0,0,0,0.7)
        lighter = Color(0x80000000),    // rgba(0,0,0,0.5)
    ),
    mask: io.github.xingray.compose.nexus.theme.MaskColors = _root_ide_package_.io.github.xingray.compose.nexus.theme.MaskColors(
        base = Color(0xE6FFFFFF),       // rgba(255,255,255,0.9)
        extraLight = Color(0x4DFFFFFF), // rgba(255,255,255,0.3)
    ),
    disabled: io.github.xingray.compose.nexus.theme.DisabledColors = _root_ide_package_.io.github.xingray.compose.nexus.theme.DisabledColors(
        background = Color(0xFFF5F7FA),   // fill-color-light
        text = Color(0xFFA8ABB2),         // text-color-placeholder
        border = Color(0xFFE4E7ED),       // border-color-light
    ),
): io.github.xingray.compose.nexus.theme.NexusColorScheme = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusColorScheme(
    primary = primary,
    success = success,
    warning = warning,
    danger = danger,
    info = info,
    white = white,
    black = black,
    text = text,
    border = border,
    fill = fill,
    background = background,
    overlay = overlay,
    mask = mask,
    disabled = disabled,
)

// ============================================================================
// Dark color scheme (Element Plus dark mode)
// ============================================================================

fun darkColorScheme(
    primary: io.github.xingray.compose.nexus.theme.TypeColor = _root_ide_package_.io.github.xingray.compose.nexus.theme.generateTypeColor(
        base = Color(0xFF409EFF),
        mixLight = Color(0xFF141414),
    ),
    success: io.github.xingray.compose.nexus.theme.TypeColor = _root_ide_package_.io.github.xingray.compose.nexus.theme.generateTypeColor(
        base = Color(0xFF67C23A),
        mixLight = Color(0xFF141414),
    ),
    warning: io.github.xingray.compose.nexus.theme.TypeColor = _root_ide_package_.io.github.xingray.compose.nexus.theme.generateTypeColor(
        base = Color(0xFFE6A23C),
        mixLight = Color(0xFF141414),
    ),
    danger: io.github.xingray.compose.nexus.theme.TypeColor = _root_ide_package_.io.github.xingray.compose.nexus.theme.generateTypeColor(
        base = Color(0xFFF56C6C),
        mixLight = Color(0xFF141414),
    ),
    info: io.github.xingray.compose.nexus.theme.TypeColor = _root_ide_package_.io.github.xingray.compose.nexus.theme.generateTypeColor(
        base = Color(0xFF909399),
        mixLight = Color(0xFF141414),
    ),
    white: Color = Color(0xFFFFFFFF),
    black: Color = Color(0xFF000000),
    text: io.github.xingray.compose.nexus.theme.TextColors = _root_ide_package_.io.github.xingray.compose.nexus.theme.TextColors(
        primary = Color(0xFFE5EAF3),
        regular = Color(0xFFCFD3DC),
        secondary = Color(0xFFA3A6AD),
        placeholder = Color(0xFF8D9095),
        disabled = Color(0xFF6C6E72),
    ),
    border: io.github.xingray.compose.nexus.theme.BorderColors = _root_ide_package_.io.github.xingray.compose.nexus.theme.BorderColors(
        base = Color(0xFF4C4D4F),
        light = Color(0xFF414243),
        lighter = Color(0xFF363637),
        extraLight = Color(0xFF2A2B2C),
        dark = Color(0xFF58585A),
        darker = Color(0xFF636466),
    ),
    fill: io.github.xingray.compose.nexus.theme.FillColors = _root_ide_package_.io.github.xingray.compose.nexus.theme.FillColors(
        base = Color(0xFF303030),
        light = Color(0xFF262727),
        lighter = Color(0xFF1D1D1D),
        extraLight = Color(0xFF191919),
        dark = Color(0xFF39393A),
        darker = Color(0xFF424243),
        blank = Color(0xFF141414),
    ),
    background: io.github.xingray.compose.nexus.theme.BackgroundColors = _root_ide_package_.io.github.xingray.compose.nexus.theme.BackgroundColors(
        base = Color(0xFF141414),
        page = Color(0xFF0A0A0A),
        overlay = Color(0xFF1D1E1F),
    ),
    overlay: io.github.xingray.compose.nexus.theme.OverlayColors = _root_ide_package_.io.github.xingray.compose.nexus.theme.OverlayColors(
        base = Color(0xCC000000),
        light = Color(0xB3000000),
        lighter = Color(0x80000000),
    ),
    mask: io.github.xingray.compose.nexus.theme.MaskColors = _root_ide_package_.io.github.xingray.compose.nexus.theme.MaskColors(
        base = Color(0xCC000000),       // rgba(0,0,0,0.8)
        extraLight = Color(0x4D000000), // rgba(0,0,0,0.3)
    ),
    disabled: io.github.xingray.compose.nexus.theme.DisabledColors = _root_ide_package_.io.github.xingray.compose.nexus.theme.DisabledColors(
        background = Color(0xFF262727), // fill-color-light (dark)
        text = Color(0xFF8D9095),       // text-color-placeholder (dark)
        border = Color(0xFF414243),     // border-color-light (dark)
    ),
): io.github.xingray.compose.nexus.theme.NexusColorScheme = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusColorScheme(
    primary = primary,
    success = success,
    warning = warning,
    danger = danger,
    info = info,
    white = white,
    black = black,
    text = text,
    border = border,
    fill = fill,
    background = background,
    overlay = overlay,
    mask = mask,
    disabled = disabled,
)
