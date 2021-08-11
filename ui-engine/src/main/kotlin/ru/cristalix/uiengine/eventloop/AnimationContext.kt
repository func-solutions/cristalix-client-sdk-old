package ru.cristalix.uiengine.eventloop

import ru.cristalix.uiengine.Easing
import ru.cristalix.uiengine.Easings
import ru.cristalix.uiengine.UIEngine
import ru.cristalix.uiengine.element.AbstractElement

data class AnimationContext(
    val durationMillis: Long,
    val easing: Easing
)

data class AnimationPipeline<T : AbstractElement?>(
    val element: T,
    var currentDelay: Long
)

fun secondsToMillis(seconds: Double): Long = (seconds * 1000).toLong()

fun secondsToMillis(seconds: Long): Long = seconds * 1000L

fun secondsToMillis(seconds: Int): Long = seconds * 1000L

fun secondsToMillis(seconds: Float): Long = (seconds * 1000.0F).toLong()

fun secondsToMillis(seconds: Number): Long = secondsToMillis(seconds.toDouble())

inline fun animate(
    seconds: Double,
    easing: Easing = Easings.NONE,
    crossinline action: () -> Unit
): AnimationPipeline<Nothing?> {
    animateImpl(seconds, easing, action)
    return AnimationPipeline(null, secondsToMillis(seconds))
}

inline fun animate(
    seconds: Long,
    easing: Easing = Easings.NONE,
    crossinline action: () -> Unit
): AnimationPipeline<Nothing?> = animate(seconds.toDouble(), easing, action)

inline fun animate(
    seconds: Int,
    easing: Easing = Easings.NONE,
    crossinline action: () -> Unit
): AnimationPipeline<Nothing?> = animate(seconds.toDouble(), easing, action)

inline fun animate(
    seconds: Float,
    easing: Easing = Easings.NONE,
    crossinline action: () -> Unit
): AnimationPipeline<Nothing?> = animate(seconds.toDouble(), easing, action)

inline fun animate(
    seconds: Number,
    easing: Easing = Easings.NONE,
    crossinline action: () -> Unit
): AnimationPipeline<Nothing?> = animate(seconds.toDouble(), easing, action)

inline fun <T : AbstractElement> T.animate(
    seconds: Double,
    easing: Easing = Easings.NONE,
    crossinline action: T.() -> Unit
): AnimationPipeline<T> {
    animateImpl(seconds, easing) { action() }
    return AnimationPipeline(this, secondsToMillis(seconds))
}

inline fun <T : AbstractElement> T.animate(
    seconds: Long,
    easing: Easing = Easings.NONE,
    crossinline action: T.() -> Unit
): AnimationPipeline<T> = animate(seconds.toDouble(), easing, action)

inline fun <T : AbstractElement> T.animate(
    seconds: Int,
    easing: Easing = Easings.NONE,
    crossinline action: T.() -> Unit
): AnimationPipeline<T> = animate(seconds.toDouble(), easing, action)

inline fun <T : AbstractElement> T.animate(
    seconds: Float,
    easing: Easing = Easings.NONE,
    crossinline action: T.() -> Unit
): AnimationPipeline<T> = animate(seconds.toDouble(), easing, action)

inline fun <T : AbstractElement> T.animate(
    seconds: Number,
    easing: Easing = Easings.NONE,
    crossinline action: T.() -> Unit
): AnimationPipeline<T> = animate(seconds.toDouble(), easing, action)

inline fun <T : AbstractElement> AnimationPipeline<T>.thenAnimate(
    seconds: Double,
    easing: Easing = Easings.NONE,
    crossinline action: T.() -> Unit
): AnimationPipeline<T> = also {
    UIEngine.schedule(currentDelay / 1000.0) {
        animateImpl(seconds, easing) { action(element) }
    }
    currentDelay += secondsToMillis(seconds)
}

inline fun <T : AbstractElement> AnimationPipeline<T>.thenAnimate(
    seconds: Long,
    easing: Easing = Easings.NONE,
    crossinline action: T.() -> Unit
): AnimationPipeline<T> = thenAnimate(seconds.toDouble(), easing, action)

inline fun <T : AbstractElement> AnimationPipeline<T>.thenAnimate(
    seconds: Int,
    easing: Easing = Easings.NONE,
    crossinline action: T.() -> Unit
): AnimationPipeline<T> = thenAnimate(seconds.toDouble(), easing, action)

inline fun <T : AbstractElement> AnimationPipeline<T>.thenAnimate(
    seconds: Float,
    easing: Easing = Easings.NONE,
    crossinline action: T.() -> Unit
): AnimationPipeline<T> = thenAnimate(seconds.toDouble(), easing, action)

inline fun <T : AbstractElement> AnimationPipeline<T>.thenAnimate(
    seconds: Number,
    easing: Easing = Easings.NONE,
    crossinline action: T.() -> Unit
): AnimationPipeline<T> = thenAnimate(seconds.toDouble(), easing, action)

inline fun animateImpl(
    seconds: Double,
    easing: Easing = Easings.NONE,
    action: () -> Unit
) {
    val previous = UIEngine.animationContext
    val durationMillis = secondsToMillis(seconds)
    val new = AnimationContext(durationMillis, easing)
    UIEngine.animationContext = new
    try {
        action()
    } finally {
        UIEngine.animationContext = previous
    }
}

inline fun animateImpl(
    seconds: Long,
    easing: Easing = Easings.NONE,
    action: () -> Unit
) = animateImpl(seconds.toDouble(), easing, action)

inline fun animateImpl(
    seconds: Int,
    easing: Easing = Easings.NONE,
    action: () -> Unit
) = animateImpl(seconds.toDouble(), easing, action)

inline fun animateImpl(
    seconds: Float,
    easing: Easing = Easings.NONE,
    action: () -> Unit
) = animateImpl(seconds.toDouble(), easing, action)

inline fun animateImpl(
    seconds: Number,
    easing: Easing = Easings.NONE,
    action: () -> Unit
) = animateImpl(seconds.toDouble(), easing, action)

fun <T : AbstractElement> AnimationPipeline<T>.thenWait(seconds: Double): AnimationPipeline<T> =
    also { currentDelay += secondsToMillis(seconds) }

fun <T : AbstractElement> AnimationPipeline<T>.thenWait(seconds: Long): AnimationPipeline<T> =
    also { currentDelay += secondsToMillis(seconds) }

fun <T : AbstractElement> AnimationPipeline<T>.thenWait(seconds: Int): AnimationPipeline<T> =
    also { currentDelay += secondsToMillis(seconds) }

fun <T : AbstractElement> AnimationPipeline<T>.thenWait(seconds: Float): AnimationPipeline<T> =
    also { currentDelay += secondsToMillis(seconds) }

fun <T : AbstractElement> AnimationPipeline<T>.thenWait(seconds: Number): AnimationPipeline<T> =
    thenWait(seconds.toDouble())




