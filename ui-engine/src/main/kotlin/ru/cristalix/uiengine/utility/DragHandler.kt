package ru.cristalix.uiengine.utility

import ru.cristalix.clientapi.JavaMod
import ru.cristalix.uiengine.UIEngine
import ru.cristalix.uiengine.element.AbstractElement

class DragHandler(
    val element: AbstractElement,
    var stickToAligns: Boolean = false,
    var snapToResolution: Boolean = false,
    var allowOverflow: Boolean = false
) {

    var draggingPosition: V2? = null

    var dragging: Boolean
        get() = draggingPosition != null
        set(value) {
            this.draggingPosition = if (value) element.hoverPosition else null
        }

    fun update() {
        val draggingPosition = this.draggingPosition ?: return

        val resolution = UIEngine.clientApi.resolution()
        val factor = resolution.scaleFactor

        val lastParent = element.lastParent ?: return

        val parentX = lastParent.size.x
        val parentY = lastParent.size.y

        val hoverX = lastParent.hoverPosition.x
        val hoverY = lastParent.hoverPosition.y

        if (stickToAligns) {

            val px = (hoverX - draggingPosition.x) / (parentX - element.size.x)
            val py = (hoverY - draggingPosition.y) / (parentY - element.size.y)

            val alignX = when {
                px < 0.33 -> 0.0
                px > 0.66 -> 1.0
                else -> 0.5
            }
            val alignY = when {
                py < 0.33 -> 0.0
                py > 0.66 -> 1.0
                else -> 0.5
            }

            element.align = V3(alignX, alignY)
            element.origin = V3(alignX, alignY)
        }


        val alignX = element.align.x
        val alignY = element.align.y
        val originX = element.origin.x
        val originY = element.origin.y
        var dx = hoverX - draggingPosition.x + element.size.x * originX - parentX * alignX
        var dy = hoverY - draggingPosition.y + element.size.y * originY - parentY * alignY

        if (!allowOverflow) {
            dx = dx.coerceAtLeast(originX * element.size.x - alignX * parentX)
                .coerceAtMost((1 - alignX) * parentX - (1 - originX) * element.size.x)

            dy = dy.coerceAtLeast(originY * element.size.y - alignY * parentY)
                .coerceAtMost((1 - alignY) * parentY - (1 - originY) * element.size.y)

//            dx = dx.coerceAtLeast(-alignX * parentX).coerceAtMost()
//            dy = dy.coerceIn(-alignY * parentY, (-alignY + 1) * parentY)
        }

        if (snapToResolution) {
            dx = (dx * factor).toInt().toDouble() / factor
            dy = (dy * factor).toInt().toDouble() / factor
        }

        element.offset.x = dx
        element.offset.y = dy

    }

}