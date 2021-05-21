package ru.cristalix.uiengine.eventloop

import ru.cristalix.uiengine.UIEngine
import ru.cristalix.uiengine.element.AbstractElement
import ru.cristalix.uiengine.utility.Easing
import ru.cristalix.uiengine.utility.Easings

data class AnimationContext(
    val duration: Number,
    val easing: Easing
)

inline fun animate(
    duration: Number,
    noinline easing: Easing = Easings.NONE,
    action: () -> Unit
) {

    val previous = UIEngine.animationContext
    val new = AnimationContext(duration, easing)
    UIEngine.animationContext = new
    action()
    UIEngine.animationContext = previous

}

inline fun <T : AbstractElement> T.animate(
    duration: Number,
    noinline easing: Easing = Easings.NONE,
    action: T.() -> Unit
): T {

    val previous = UIEngine.animationContext
    val new = AnimationContext(duration, easing)
    UIEngine.animationContext = new
    action()
    UIEngine.animationContext = previous

    return this
}


