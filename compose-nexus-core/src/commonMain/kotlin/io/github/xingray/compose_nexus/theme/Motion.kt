package io.github.xingray.compose_nexus.theme

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Immutable

/**
 * Element Plus animation / transition system.
 *
 * --el-transition-duration:      0.3s
 * --el-transition-duration-fast: 0.2s
 * --el-transition-function-ease-in-out-bezier: cubic-bezier(0.645, 0.045, 0.355, 1)
 * --el-transition-function-fast-bezier:        cubic-bezier(0.23, 1, 0.32, 1)
 */
@Immutable
data class NexusMotion(
    /** 300ms - Default transition duration */
    val durationDefault: Int = 300,
    /** 200ms - Fast transition duration */
    val durationFast: Int = 200,
    /** Standard ease-in-out: cubic-bezier(0.645, 0.045, 0.355, 1) */
    val easingDefault: Easing = CubicBezierEasing(0.645f, 0.045f, 0.355f, 1f),
    /** Fast overshoot: cubic-bezier(0.23, 1, 0.32, 1) */
    val easingFast: Easing = CubicBezierEasing(0.23f, 1f, 0.32f, 1f),
    /** Linear easing */
    val easingLinear: Easing = Easing { it },
) {
    /** Default tween animation spec: 300ms with ease-in-out */
    fun <T> tweenDefault() = tween<T>(
        durationMillis = durationDefault,
        easing = easingDefault,
    )

    /** Fast tween animation spec: 200ms with ease-in-out */
    fun <T> tweenFast() = tween<T>(
        durationMillis = durationFast,
        easing = easingDefault,
    )

    /** Fade tween: 300ms with fast bezier */
    fun <T> tweenFade() = tween<T>(
        durationMillis = durationDefault,
        easing = easingFast,
    )

    /** Linear fade: 200ms linear */
    fun <T> tweenFadeLinear() = tween<T>(
        durationMillis = durationFast,
        easing = easingLinear,
    )
}
