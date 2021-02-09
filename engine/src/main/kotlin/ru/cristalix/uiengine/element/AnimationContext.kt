package ru.cristalix.uiengine.element

import ru.cristalix.uiengine.utility.Easing
import ru.cristalix.uiengine.utility.Easings

data class AnimationContext(
    val duration: Number,
    val easing: Easing
)

fun <T : Element> T.animate(
    duration: Number,
    easing: Easing = Easings.NONE,
    action: T.() -> Unit
): T {
    val previous = this.animationContext
    val new = AnimationContext(duration, easing)
    this.animationContext = new
    action()
    this.animationContext = previous

    return this
}


