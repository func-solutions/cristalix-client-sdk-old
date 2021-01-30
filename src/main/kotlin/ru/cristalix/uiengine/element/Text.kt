package ru.cristalix.uiengine.element

import ru.cristalix.uiengine.utility.Color
import ru.cristalix.uiengine.utility.V3

interface Text : Element {

    var label: String
    var autoFit: Boolean
    var shadow: Boolean

}

class TextData(
    scale: V3,
    offset: V3,
    align: V3,
    origin: V3,
    color: Color,
    override var label: String,
    override var autoFit: Boolean,
    override var shadow: Boolean
) : ElementData(
    scale,
    offset,
    align,
    origin,
    color
), Text

class TextView(
    private val text: Text,
) : ElementView<TextView>(text), Text {

    override var label: String = text.label
        get() {
            //updates.add(something)
            return field
        }
        set(value) {
            text.label = value
            field = value
            //updates.add(something)
        }

    override var autoFit: Boolean = text.autoFit
        get() {
            //updates.add(something)
            return field
        }
        set(value) {
            text.autoFit = value
            field = value
            //updates.add(something)
        }

    override var shadow: Boolean = text.shadow
        get() {
            //updates.add(something)
            return field
        }
        set(value) {
            text.shadow = value
            field = value
            //updates.add(something)
        }

}