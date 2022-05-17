package ru.cristalix.uiengine.element

import ru.cristalix.uiengine.utility.LEFT
import ru.cristalix.uiengine.utility.RIGHT
import ru.cristalix.uiengine.utility.rectangle

open class CarvedRectangle(
    var carveSize: Double = 1.0,
): RectangleElement() {
    val main = +rectangle {}
    val left = +rectangle {
        origin = LEFT
        align = LEFT
    }
    val right = +rectangle {
        origin = RIGHT
        align = RIGHT
    }
    var lastAlpha: Double = 0.0

    init {
        beforeRender {
            lastAlpha = color.alpha
            color.alpha = 0.0
        }
        afterRender {
            color.alpha = lastAlpha
        }
        beforeTransform {
            main.color = color
            main.offset.x = carveSize
            right.color = color
            left.color = color
            main.size.x = size.x - carveSize * 2
            main.size.y = size.y
            left.size.x = carveSize
            left.size.y = size.y - carveSize * 2
            right.size.y = size.y - carveSize * 2
            right.size.x = carveSize
        }
    }
}
