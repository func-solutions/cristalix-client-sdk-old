package ru.cristalix.uiengine.utility

import ru.cristalix.uiengine.element.AbstractElement
import ru.cristalix.uiengine.utility.Property.ColorA
import ru.cristalix.uiengine.utility.Property.ColorB
import ru.cristalix.uiengine.utility.Property.ColorG
import ru.cristalix.uiengine.utility.Property.ColorR
import ru.cristalix.uiengine.utility.Property.RotationAngle
import ru.cristalix.uiengine.utility.Property.RotationX
import ru.cristalix.uiengine.utility.Property.RotationY
import ru.cristalix.uiengine.utility.Property.RotationZ

open class ProxiedV2(
    private val offset: Int,
    private val element: AbstractElement,
) : V2() {
    private val properties = element.properties

    override var x: Double
        get() = properties[offset]
        set(value) = element.changeProperty(offset, value)
    override var y: Double
        get() = properties[offset + 1]
        set(value) = element.changeProperty(offset + 1, value)
}

class ProxiedV3(
    private val offset: Int,
    private val element: AbstractElement,
) : V3() {
    private val properties = element.properties

    override var x: Double
        get() = properties[offset]
        set(value) = element.changeProperty(offset, value)
    override var y: Double
        get() = properties[offset + 1]
        set(value) = element.changeProperty(offset + 1, value)
    override var z: Double
        get() = properties[offset + 2]
        set(value) = element.changeProperty(offset + 2, value)
}

class ProxiedRotation(
    private val element: AbstractElement,
) : Rotation() {

    private val properties = element.properties

    override var x: Double
        get() = properties[RotationX]
        set(value) = element.changeProperty(RotationX.ordinal, value)
    override var y: Double
        get() = properties[RotationY]
        set(value) = element.changeProperty(RotationY.ordinal, value)
    override var z: Double
        get() = properties[RotationZ]
        set(value) = element.changeProperty(RotationZ.ordinal, value)
    override var degrees: Double
        get() = properties[RotationAngle]
        set(value) = element.changeProperty(RotationAngle.ordinal, value)
}

open class ProxiedColor(
    private val element: AbstractElement,
) : Color() {

    private val properties = element.properties

    override var red: Int
        get() = properties[ColorR].toInt()
        set(value) = element.changeProperty(ColorR.ordinal, value)

    override var green: Int
        get() = properties[ColorG].toInt()
        set(value) = element.changeProperty(ColorG.ordinal, value)

    override var blue: Int
        get() = properties[ColorB].toInt()
        set(value) = element.changeProperty(ColorB.ordinal, value)

    override var alpha: Double
        get() = properties[ColorA]
        set(value) = element.changeProperty(ColorA.ordinal, value)
}



