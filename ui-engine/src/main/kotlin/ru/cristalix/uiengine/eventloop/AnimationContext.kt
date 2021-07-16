package ru.cristalix.uiengine.eventloop

import ru.cristalix.uiengine.UIEngine
import ru.cristalix.uiengine.element.AbstractElement
import ru.cristalix.uiengine.utility.Easing
import ru.cristalix.uiengine.utility.Easings

data class AnimationContext(
    val durationMillis: Long,
    val easing: Easing
)

data class AnimationPipeline<T : AbstractElement?>(
    val element: T,
    var currentDelay: Long
)

fun secondsToMillis(seconds: Number) = (seconds.toDouble() * 1000).toLong()

inline fun animate(
    seconds: Number,
    noinline easing: Easing = Easings.NONE,
    crossinline action: () -> Unit
): AnimationPipeline<Nothing?> {
    animateImpl(seconds, easing, action)
    return AnimationPipeline(null, secondsToMillis(seconds))
}


inline fun <T : AbstractElement> T.animate(
    seconds: Number,
    noinline easing: Easing = Easings.NONE,
    crossinline action: T.() -> Unit
): AnimationPipeline<T> {
    animateImpl(seconds, easing, { action() })
    return AnimationPipeline(this, secondsToMillis(seconds))
}


inline fun <T : AbstractElement> AnimationPipeline<T>.thenAnimate(
    seconds: Number,
    noinline easing: Easing = Easings.NONE,
    crossinline action: T.() -> Unit
): AnimationPipeline<T> = also {
    UIEngine.schedule(currentDelay / 1000.0) {
        animateImpl(seconds, easing, { action(element) })
    }
    currentDelay += secondsToMillis(seconds)
}


inline fun animateImpl(
    seconds: Number,
    noinline easing: Easing = Easings.NONE,
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


fun <T : AbstractElement> AnimationPipeline<T>.thenWait(seconds: Number): AnimationPipeline<T> =
    also { currentDelay += secondsToMillis(seconds) }




