package ru.cristalix.uiengine.utility

import ru.cristalix.uiengine.element.ItemElement
import ru.cristalix.uiengine.element.RectangleElement
import ru.cristalix.uiengine.element.TextElement

inline fun rectangle(setup: RectangleElement.() -> Unit): RectangleElement {
    val element = RectangleElement()
    setup(element)
    return element
}

inline fun text(setup: TextElement.() -> Unit): TextElement {
    val element = TextElement()
    setup(element)
    return element
}

inline fun item(setup: ItemElement.() -> Unit): ItemElement {
    val element = ItemElement()
    setup(element)
    return element
}