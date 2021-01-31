package ru.cristalix.uiengine.element

class Text(
    private val text: Text,
) : Element(text), Text {

    override var label: String = text.label
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