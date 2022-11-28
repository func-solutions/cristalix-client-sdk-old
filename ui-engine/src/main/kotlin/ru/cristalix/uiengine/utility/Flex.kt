package ru.cristalix.uiengine.utility

import ru.cristalix.uiengine.element.RectangleElement
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.math.abs

inline fun flex(setup: Flex.() -> Unit): Flex {
    contract {
        callsInPlace(setup, InvocationKind.EXACTLY_ONCE)
    }
    return Flex().also(setup)
}

enum class FlexDirection(
        val kx: Int,
        val ky: Int,
        val rowWidthProperty: Int,
        val align: Double,
) {

    UP(0, 1, Property.ParentSizeY.ordinal, 1.0),
    DOWN(0, -1, Property.ParentSizeY.ordinal, 0.0),
    LEFT(-1, 0, Property.ParentSizeX.ordinal, 1.0),
    RIGHT(1, 0, Property.ParentSizeX.ordinal, 0.0)

}

open class Flex: RectangleElement() {

    var flexDirection = FlexDirection.RIGHT

    var flexSpacing = 0.0

    var overflowWrap = false

    protected
    var lastOffsetX = Double.NaN
    protected var lastOffsetY = Double.NaN

    init {
        beforeTransform {
            update()
        }
    }

    fun update() {

        val kx = flexDirection.kx
        val ky = flexDirection.ky

        val flexBoxLength = abs(kx * size.x + ky * size.y)

        var rowLength = 0.0
        var rowWidth = 0.0

        var totalWidth = 0.0

        var rowStartIndex = 0

        children.forEachIndexed { currentChildIndex, child ->

            val childLength = abs(child.size.x * kx + child.size.y * ky)
            val childWidth = abs(child.size.y * kx + child.size.x * ky)

            if (childWidth > rowWidth) rowWidth = childWidth

            if (kx != 0) {
                child.align.x = flexDirection.align
                child.origin.x = flexDirection.align
            } else {
                child.align.y = flexDirection.align
                child.origin.y = flexDirection.align
            }

            // ToDo: This is probably bad because it doesn't account for zero-width elements
            // Not that anyone would use flexbox with empty elements, so I will leave it like this for now
            if (rowLength != 0.0) {

                rowLength += flexSpacing

                if (overflowWrap && rowLength + childLength > flexBoxLength) {

                    rowLength = 0.0

                    totalWidth += rowWidth + flexSpacing

                    for (prevChildIndex in rowStartIndex..currentChildIndex) {
                        children[prevChildIndex].changeProperty(flexDirection.rowWidthProperty, rowWidth)
                    }

                    rowWidth = 0.0
                    rowStartIndex = currentChildIndex + 1

                }
            }

            child.offset.x = if (kx != 0) rowLength else totalWidth
            child.offset.y = if (ky != 0) rowLength else totalWidth

            rowLength += childLength

        }

        totalWidth += rowWidth

        if (kx == 0) size.x = totalWidth
        else size.y = totalWidth

        if (!overflowWrap) {
            if (kx != 0) size.x = rowLength
            else size.y = rowLength
        }

    }

}