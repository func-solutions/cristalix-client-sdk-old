package ru.cristalix.uiengine.element

import ru.cristalix.uiengine.utility.*

open class CarvedRectangle(
    var carveSize: Double = 1.0,
    var carvedSide: CarvedSide = CarvedSide.ALL
): RectangleElement() {
    val main = +rectangle {}
    val left = +rectangle {
        when (carvedSide) {
            CarvedSide.BOTTOM -> {
                origin = TOP_LEFT
                align = TOP_LEFT
            }
            CarvedSide.ALL -> {
                origin = LEFT
                align = LEFT
            }
            CarvedSide.TOP -> {
                origin = BOTTOM_LEFT
                align = BOTTOM_LEFT
            }
            else -> {
                origin = LEFT
                align = LEFT
            }
        }
    }
    val right = +rectangle {
        when (carvedSide) {
            CarvedSide.BOTTOM -> {
                origin = TOP_RIGHT
                align = TOP_RIGHT
            }
            CarvedSide.ALL -> {
                origin = RIGHT
                align = RIGHT
            }
            CarvedSide.TOP -> {
                origin = BOTTOM_RIGHT
                align = BOTTOM_RIGHT
            }
            else -> {
                origin = RIGHT
                align = RIGHT
            }
        }
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
            when (carvedSide) {
                CarvedSide.BOTTOM -> {
                    left.size.y = size.y - carveSize
                    right.size.y = size.y - carveSize
                }
                CarvedSide.ALL -> {
                    left.size.y = size.y - carveSize * 2
                    right.size.y = size.y - carveSize * 2
                }
                CarvedSide.TOP -> {
                    left.size.y = size.y - carveSize
                    right.size.y = size.y - carveSize
                }
                else -> {
                    left.size.y = size.y - carveSize * 2
                    right.size.y = size.y - carveSize * 2
                }
            }
            right.size.x = carveSize
        }
    }
}
