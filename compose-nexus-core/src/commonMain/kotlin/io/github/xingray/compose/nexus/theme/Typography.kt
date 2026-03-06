package io.github.xingray.compose.nexus.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Element Plus typography system.
 *
 * Font family: 'Helvetica Neue', Helvetica, 'PingFang SC',
 *              'Hiragino Sans GB', 'Microsoft YaHei', Arial, sans-serif
 *
 * Font sizes:
 *   extra-large: 20px, large: 18px, medium: 16px,
 *   base: 14px, small: 13px, extra-small: 12px
 */
@Immutable
data class NexusTypography(
    /** 20sp - Used for page titles, large headings */
    val extraLarge: TextStyle = TextStyle(
        fontSize = 20.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 28.sp,
    ),
    /** 18sp - Used for section headings */
    val large: TextStyle = TextStyle(
        fontSize = 18.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 26.sp,
    ),
    /** 16sp - Used for sub-headings, emphasized text */
    val medium: TextStyle = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 24.sp,
    ),
    /** 14sp - Default body text, form labels, buttons */
    val base: TextStyle = TextStyle(
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 22.sp,
    ),
    /** 13sp - Used for secondary text, helper text */
    val small: TextStyle = TextStyle(
        fontSize = 13.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 20.sp,
    ),
    /** 12sp - Used for captions, tags, badges */
    val extraSmall: TextStyle = TextStyle(
        fontSize = 12.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 18.sp,
    ),
    /** Default font family matching Element Plus */
    val fontFamily: FontFamily = FontFamily.SansSerif,
    /** Primary font weight (500) for emphasized text */
    val fontWeightPrimary: FontWeight = FontWeight.Medium,
)
