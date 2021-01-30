package ru.cristalix.uiengine.element

import ru.cristalix.uiengine.utility.Texture
import ru.cristalix.uiengine.utility.V3

class Rectangle(
    private val rectangle: Rectangle
) : Element(rectangle), Rectangle {

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