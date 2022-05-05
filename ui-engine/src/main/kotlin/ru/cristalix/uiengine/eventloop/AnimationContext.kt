package ru.cristalix.uiengine.eventloop

import ru.cristalix.uiengine.UIEngine
import ru.cristalix.uiengine.element.AbstractElement
import ru.cristalix.uiengine.utility.Easing
import ru.cristalix.uiengine.utility.Easings

class AnimationContext(
    @JvmField
    val durationMillis: Long,
    @JvmField
    val easing: Easing
)

class AnimationPipeline<T : AbstractElement?>(
    @JvmField
    val element: T,
    @JvmField
    var currentDelay: Long
)

fun secondsToMillis(seconds: Double): Long = (seconds * 1000).toLong()

fun secondsToMillis(seconds: Long): Long = seconds * 1000L

fun secondsToMillis(seconds: Int): Long = seconds * 1000L

fun secondsToMillis(seconds: Float): Long = (seconds * 1000.0F).toLong()

fun secondsToMillis(seconds: Number): Long = secondsToMillis(seconds.toDouble())

fun animate(
    seconds: Double,
    easing: Easing = Easings.NONE,
    action: () -> Unit
): AnimationPipeline<Nothing?> {
    animateImpl(seconds, easing, action)
    return AnimationPipeline(null, secondsToMillis(seconds))
}

fun animate(
    seconds: Long,
    easing: Easing = Easings.NONE,
    action: () -> Unit
): AnimationPipeline<Nothing?> = animate(seconds.toDouble(), easing, action)

fun animate(
    seconds: Int,
    easing: Easing = Easings.NONE,
    action: () -> Unit
): AnimationPipeline<Nothing?> = animate(seconds.toDouble(), easing, action)

fun animate(
    seconds: Float,
    easing: Easing = Easings.NONE,
    action: () -> Unit
): AnimationPipeline<Nothing?> = animate(seconds.toDouble(), easing, action)

fun animate(
    seconds: Number,
    easing: Easing = Easings.NONE,
    action: () -> Unit
): AnimationPipeline<Nothing?> = animate(seconds.toDouble(), easing, action)

fun <T : AbstractElement> T.animate(
    seconds: Double,
    easing: Easing = Easings.NONE,
    action: T.() -> Unit
): AnimationPipeline<T> {
    animateImpl(seconds, easing) { action() }
    return AnimationPipeline(this, secondsToMillis(seconds))
}

fun <T : AbstractElement> T.animate(
    seconds: Long,
    easing: Easing = Easings.NONE,
    action: T.() -> Unit
): AnimationPipeline<T> = animate(seconds.toDouble(), easing, action)

fun <T : AbstractElement> T.animate(
    seconds: Int,
    easing: Easing = Easings.NONE,
    action: T.() -> Unit
): AnimationPipeline<T> = animate(seconds.toDouble(), easing, action)

fun <T : AbstractElement> T.animate(
    seconds: Float,
    easing: Easing = Easings.NONE,
    action: T.() -> Unit
): AnimationPipeline<T> = animate(seconds.toDouble(), easing, action)

fun <T : AbstractElement> T.animate(
    seconds: Number,
    easing: Easing = Easings.NONE,
    action: T.() -> Unit
): AnimationPipeline<T> = animate(seconds.toDouble(), easing, action)

fun <T : AbstractElement> AnimationPipeline<T>.thenAnimate(
    seconds: Double,
    easing: Easing = Easings.NONE,
    action: T.() -> Unit
): AnimationPipeline<T> = apply {
    UIEngine.schedule(currentDelay / 1000.0) {
        animateImpl(seconds, easing) { action(element) }
    }
    currentDelay += secondsToMillis(seconds)
}

fun <T : AbstractElement> AnimationPipeline<T>.thenAnimate(
    seconds: Long,
    easing: Easing = Easings.NONE,
    action: T.() -> Unit
): AnimationPipeline<T> = thenAnimate(seconds.toDouble(), easing, action)

fun <T : AbstractElement> AnimationPipeline<T>.thenAnimate(
    seconds: Int,
    easing: Easing = Easings.NONE,
    action: T.() -> Unit
): AnimationPipeline<T> = thenAnimate(seconds.toDouble(), easing, action)

fun <T : AbstractElement> AnimationPipeline<T>.thenAnimate(
    seconds: Float,
    easing: Easing = Easings.NONE,
    action: T.() -> Unit
): AnimationPipeline<T> = thenAnimate(seconds.toDouble(), easing, action)

fun <T : AbstractElement> AnimationPipeline<T>.thenAnimate(
    seconds: Number,
    easing: Easing = Easings.NONE,
    action: T.() -> Unit
): AnimationPipeline<T> = thenAnimate(seconds.toDouble(), easing, action)

fun animateImpl(
    seconds: Double,
    easing: Easing = Easings.NONE,
    action: () -> Unit
) {
    val engine = UIEngine
    val previous = engine.animationContext
    val durationMillis = secondsToMillis(seconds)
    val new = AnimationContext(durationMillis, easing)
    engine.animationContext = new
    try {
        action()
    } finally {
        engine.animationContext = previous
    }
}

fun animateImpl(
    seconds: Long,
    easing: Easing = Easings.NONE,
    action: () -> Unit
) = animateImpl(seconds.toDouble(), easing, action)

fun animateImpl(
    seconds: Int,
    easing: Easing = Easings.NONE,
    action: () -> Unit
) = animateImpl(seconds.toDouble(), easing, action)

fun animateImpl(
    seconds: Float,
    easing: Easing = Easings.NONE,
    action: () -> Unit
) = animateImpl(seconds.toDouble(), easing, action)

fun animateImpl(
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
