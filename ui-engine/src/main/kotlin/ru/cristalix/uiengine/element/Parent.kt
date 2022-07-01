package ru.cristalix.uiengine.element

import ru.cristalix.uiengine.utility.V3

interface Parent : IElement {

    val children: List<AbstractElement>

    fun addChild(element: AbstractElement)

    fun removeChild(element: AbstractElement)

    fun addChild(vararg elements: AbstractElement)

    fun removeChild(vararg elements: AbstractElement)

    fun syncChild(vararg elements: AbstractElement)

    operator fun <T : AbstractElement> T.unaryPlus(): T {
        this@Parent.addChild(this)
        return this
    }

    operator fun <T : AbstractElement> plus(element: T): T {
        this@Parent.addChild(element)
        return element
    }

    /**
     * Assumes that all children are strictly inside the parent.
     * Does not account for the rotation.
     */
    fun adjustSizeToChildren(padding: V3) {

        val children = children
        val count = children.size
        var xmin = 0.0
        var ymin = 0.0
        var zmin = 0.0
        var xmax = 0.0
        var ymax = 0.0
        var zmax = 0.0

        for (i in 0 until count) {
            val child = children[i]

            val x1 = child.size.x * child.scale.x + child.offset.x
            val y1 = child.size.y * child.scale.y + child.offset.y
            val z1 = child.size.z * child.scale.z + child.offset.z

            if (xmax < x1) xmax = x1
            if (ymax < y1) ymax = y1
            if (zmax < z1) zmax = z1
        }

        size.x = xmax
        size.y = ymax
        size.z = zmax
    }
}
