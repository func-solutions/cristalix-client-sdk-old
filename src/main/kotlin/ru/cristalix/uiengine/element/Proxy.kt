package ru.cristalix.uiengine.element

import ru.cristalix.uiengine.element.Property.*
import ru.cristalix.uiengine.utility.Color
import ru.cristalix.uiengine.utility.V2
import ru.cristalix.uiengine.utility.V3

class ProxiedV3(
    private val offset: Int,
    private val element: Element
) : V3() {
    override var x: Double
        get() = element.properties[offset]
        set(value) = element.changeProperty(offset, value)
    override var y: Double
        get() = element.properties[offset + 1]
        set(value) = element.changeProperty(offset + 1, value)
    override var z: Double
        get() = element.properties[offset + 2]
        set(value) = element.changeProperty(offset + 2, value)
}

open class ProxiedV2(
    private val offset: Int,
    private val element: Element
) : V2() {
    override var x: Double
        get() = element.properties[offset]
        set(value) = element.changeProperty(offset, value)
    override var y: Double
        get() = element.properties[offset + 1]
        set(value) = element.changeProperty(offset + 1, value)
}

open class ProxiedColor(
    private val element: Element
): Color() {

    override var red: Int
        get() = element.properties[ColorR].toInt()
        set(value) = element.changeProperty(ColorR.ordinal, value)

    override var green: Int
        get() = element.properties[ColorG].toInt()
        set(value) = element.changeProperty(ColorG.ordinal, value)

    override var blue: Int
        get() = element.properties[ColorB].toInt()
        set(value) = element.changeProperty(ColorB.ordinal, value)

    override var alpha: Double
        get() = element.properties[ColorA]
        set(value) = element.changeProperty(ColorA.ordinal, value)

}



