package io.github.xingray.compose_nexus.theme

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

/**
 * Element Plus border-radius system.
 *
 * --el-border-radius-base:   4px
 * --el-border-radius-small:  2px
 * --el-border-radius-round:  20px
 * --el-border-radius-circle: 100%
 */
@Immutable
data class NexusShapes(
    /** 2dp - Small border radius (tags, badges) */
    val small: Shape = RoundedCornerShape(2.dp),
    /** 4dp - Base border radius (buttons, inputs, cards) */
    val base: Shape = RoundedCornerShape(4.dp),
    /** 8dp - Medium border radius (dialogs, popovers) */
    val medium: Shape = RoundedCornerShape(8.dp),
    /** 20dp - Round border radius (round buttons, pills) */
    val round: Shape = RoundedCornerShape(20.dp),
    /** 50% - Circle shape (circle buttons, avatars) */
    val circle: Shape = CircleShape,
)
