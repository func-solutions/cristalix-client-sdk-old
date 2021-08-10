package ru.cristalix.uiengine.utility

import ru.cristalix.uiengine.element.AbstractElement

val TOP_LEFT: V3 = V3(0.0, 0.0)
val TOP: V3 = V3(0.5, 0.0)
val TOP_RIGHT: V3 = V3(1.0, 0.0)
val LEFT: V3 = V3(0.0, 0.5)
val CENTER: V3 = V3(0.5, 0.5)
val RIGHT: V3 = V3(1.0, 0.5)
val BOTTOM_LEFT: V3 = V3(0.0, 1.0)
val BOTTOM: V3 = V3(0.5, 1.0)
val BOTTOM_RIGHT: V3 = V3(1.0, 1.0)

inline fun <T: AbstractElement> T.matchParentSize(x: Boolean = false, y: Boolean = false, z: Boolean = false) {
    beforeRender {
        lastParent?.let {
            val size = size
            val osize = it.size
            if (x) size.x = osize.x
            if (y) size.y = osize.y
            if (z) size.z = osize.z
        }
    }
}

object Relative {

    val TOP_LEFT: V3 = V3(0.0, 0.0)
    val TOP: V3 = V3(0.5, 0.0)
    val TOP_RIGHT: V3 = V3(1.0, 0.0)
    val LEFT: V3 = V3(0.0, 0.5)
    val CENTER: V3 = V3(0.5, 0.5)
    val RIGHT: V3 = V3(1.0, 0.5)
    val BOTTOM_LEFT: V3 = V3(0.0, 1.0)
    val BOTTOM: V3 = V3(0.5, 1.0)
    val BOTTOM_RIGHT: V3 = V3(1.0, 1.0)

}
