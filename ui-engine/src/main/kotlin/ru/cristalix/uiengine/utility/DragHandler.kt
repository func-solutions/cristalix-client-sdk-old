package ru.cristalix.uiengine.utility

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

        val element = element
        val lastParent = element.lastParent ?: return

        val parentSize = lastParent.size
        val parentX = parentSize.x
        val parentY = parentSize.y

        val parentHoverPosition = lastParent.hoverPosition
        val hoverX = parentHoverPosition.x
        val hoverY = parentHoverPosition.y

        val size = element.size

        if (stickToAligns) {

            val px = (hoverX - draggingPosition.x) / (parentX - size.x)
            val py = (hoverY - draggingPosition.y) / (parentY - size.y)

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

        val align = element.align
        val origin = element.origin
        val alignX = align.x
        val alignY = align.y
        val originX = origin.x
        val originY = origin.y
        var dx = hoverX - draggingPosition.x + size.x * originX - parentX * alignX
        var dy = hoverY - draggingPosition.y + size.y * originY - parentY * alignY

        if (!allowOverflow) {
            dx = dx.coerceAtLeast(originX * size.x - alignX * parentX)
                .coerceAtMost((1 - alignX) * parentX - (1 - originX) * size.x)

            dy = dy.coerceAtLeast(originY * size.y - alignY * parentY)
                .coerceAtMost((1 - alignY) * parentY - (1 - originY) * size.y)

//            dx = dx.coerceAtLeast(-alignX * parentX).coerceAtMost()
//            dy = dy.coerceIn(-alignY * parentY, (-alignY + 1) * parentY)
        }

        if (snapToResolution) {
            dx = (dx * factor).toInt().toDouble() / factor
            dy = (dy * factor).toInt().toDouble() / factor
        }

        val offset = element.offset
        offset.x = dx
        offset.y = dy
    }
}