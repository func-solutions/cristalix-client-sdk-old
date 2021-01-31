package ru.cristalix.uiengine.element

import ru.cristalix.uiengine.utility.*

open class Rectangle : Element {

    var size: V2
        get() = ProxiedV2(Property.SizeX.ordinal, this)
        set(value) = value.write(size)

    var textureLocation: String?

    var textureFrom: V2
        get() = ProxiedV2(Property.TextureX.ordinal, this)
        set(value) = value.write(textureFrom)

    var textureSize: V2
        get() = ProxiedV2(Property.TextureWidth.ordinal, this)
        set(value) = value.write(textureSize)

    var children: Collection<Element>

    constructor(
        scale: V3 = V3(1.0, 1.0, 1.0),
        offset: V3 = V3(),
        align: V3 = V3(),
        origin: V3 = V3(),
        color: Color = TRANSPARENT,
        rotation: Rotation = Rotation(),
        enabled: Boolean = true,
        onClick: ClickHandler = null,
        onHover: HoverHandler = null,
        size: V2 = V2(),
        textureLocation: String? = null,
        textureFrom: V2 = V2(),
        textureSize: V2 = V2(1.0, 1.0),
        children: Collection<Element> = ArrayList()
    ) : super(scale, offset, align, origin, color, rotation, enabled, onClick, onHover) {
        this.size = size
        this.textureLocation = textureLocation
        this.textureFrom = textureFrom
        this.textureSize = textureSize
        this.children = children
    }
}