package ru.cristalix.uiengine.utility

import ru.cristalix.uiengine.element.AbstractElement

@JvmField val TOP_LEFT: V3 = V3(0.0, 0.0)
@JvmField val TOP: V3 = V3(0.5, 0.0)
@JvmField val TOP_RIGHT: V3 = V3(1.0, 0.0)
@JvmField val LEFT: V3 = V3(0.0, 0.5)
@JvmField val CENTER: V3 = V3(0.5, 0.5)
@JvmField val RIGHT: V3 = V3(1.0, 0.5)
@JvmField val BOTTOM_LEFT: V3 = V3(0.0, 1.0)
@JvmField val BOTTOM: V3 = V3(0.5, 1.0)
@JvmField val BOTTOM_RIGHT: V3 = V3(1.0, 1.0)

fun <T: AbstractElement> T.matchParentSize(x: Boolean = false, y: Boolean = false, z: Boolean = false) {
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
    @JvmField val TOP_LEFT: V3 = V3(0.0, 0.0)
    @JvmField val TOP: V3 = V3(0.5, 0.0)
    @JvmField val TOP_RIGHT: V3 = V3(1.0, 0.0)
    @JvmField val LEFT: V3 = V3(0.0, 0.5)
    @JvmField val CENTER: V3 = V3(0.5, 0.5)
    @JvmField val RIGHT: V3 = V3(1.0, 0.5)
    @JvmField  val BOTTOM_LEFT: V3 = V3(0.0, 1.0)
    @JvmField val BOTTOM: V3 = V3(0.5, 1.0)
    @JvmField val BOTTOM_RIGHT: V3 = V3(1.0, 1.0)
}
