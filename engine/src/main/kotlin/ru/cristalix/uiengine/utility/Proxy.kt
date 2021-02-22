package ru.cristalix.uiengine.element

import ru.cristalix.uiengine.utility.Property.*
import ru.cristalix.uiengine.utility.Color
import ru.cristalix.uiengine.utility.Rotation
import ru.cristalix.uiengine.utility.V2
import ru.cristalix.uiengine.utility.V3


open class ProxiedV2(
    private val offset: Int,
    private val element: AbstractElement
) : V2() {
    override var x: Double
        get() = element.properties[offset]
        set(value) = element.changeProperty(offset, value)
    override var y: Double
        get() = element.properties[offset + 1]
        set(value) = element.changeProperty(offset + 1, value)
}

class ProxiedV3(
    private val offset: Int,
    private val element: AbstractElement
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

class ProxiedRotation(
    private val element: AbstractElement
) : Rotation() {
    override var x: Double
        get() = element.properties[RotationX.ordinal]
        set(value) = element.changeProperty(RotationX.ordinal, value)
    override var y: Double
        get() = element.properties[RotationY.ordinal]
        set(value) = element.changeProperty(RotationY.ordinal, value)
    override var z: Double
        get() = element.properties[RotationZ.ordinal]
        set(value) = element.changeProperty(RotationZ.ordinal, value)
    override var degrees: Double
        get() = element.properties[RotationAngle.ordinal]
        set(value) = element.changeProperty(RotationAngle.ordinal, value)
}

open class ProxiedColor(
    private val element: AbstractElement
): Color() {

    override var red: Int
        get() = element.properties[ColorR.ordinal].toInt()
        set(value) = element.changeProperty(ColorR.ordinal, value)

    override var green: Int
        get() = element.properties[ColorG.ordinal].toInt()
        set(value) = element.changeProperty(ColorG.ordinal, value)

    override var blue: Int
        get() = element.properties[ColorB.ordinal].toInt()
        set(value) = element.changeProperty(ColorB.ordinal, value)

    override var alpha: Double
        get() = element.properties[ColorA.ordinal]
        set(value) = element.changeProperty(ColorA.ordinal, value)

}



