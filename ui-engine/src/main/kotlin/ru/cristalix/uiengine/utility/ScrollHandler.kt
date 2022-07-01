package ru.cristalix.uiengine.utility

import org.lwjgl.input.Mouse
import ru.cristalix.uiengine.element.AbstractElement
import ru.cristalix.uiengine.eventloop.animate

fun AbstractElement.initScroll() {
}

class ScrollHandler(
    val element: AbstractElement,
) {

    var scrollPosition: Double = 0.0
    var limited = true

    fun update() {
        val dWheel = Mouse.getDWheel()
        val lastParent = element.lastParent
        if (dWheel != 0 && lastParent != null) {
            animate(0.1, Easings.SINE_OUT) {
                scrollPosition += if (dWheel > 0) -50 else 50
                val overflow = -(lastParent.size.y - element.size.y)

                scrollPosition =
                    if (overflow <= 0) 0.0 // not scrollable (parent size is larger than self size)
                    else scrollPosition.coerceIn(
                        -overflow * element.origin.y,
                        overflow * (1.0 - element.origin.y)
                    )

                element.offset.y = -scrollPosition
            }
        }
    }
}
