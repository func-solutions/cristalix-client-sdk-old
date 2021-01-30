package ru.cristalix.uiengine.element

import ru.cristalix.uiengine.utility.Color
import ru.cristalix.uiengine.utility.Texture
import ru.cristalix.uiengine.utility.V3

interface Rectangle : Element {

    var size: V3
    var texture: Texture

}

class RectangleData(
    scale: V3,
    offset: V3,
    align: V3,
    origin: V3,
    color: Color,
    override var size: V3,
    override var texture: Texture
) : ElementData(
    scale,
    offset,
    align,
    origin,
    color
), Rectangle

class RectangleView(
    private val rectangle: Rectangle
) : ElementView<RectangleView>(rectangle), Rectangle {

    override var size: V3 = rectangle.size
        get() {
            //updates.add(something)
            return field
        }
        set(value) {
            rectangle.size = value
            field = value
            //updates.add(something)
        }
    override var texture: Texture = rectangle.texture
        get() {
            //updates.add(something)
            return field
        }
        set(value) {
            rectangle.texture = value
            field = value
            //updates.add(something)
        }

}