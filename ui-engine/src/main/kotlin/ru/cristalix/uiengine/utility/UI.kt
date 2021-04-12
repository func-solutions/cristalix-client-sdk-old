package ru.cristalix.uiengine.utility

import ru.cristalix.uiengine.element.CuboidElement
import ru.cristalix.uiengine.element.ItemElement
import ru.cristalix.uiengine.element.RectangleElement
import ru.cristalix.uiengine.element.TextElement

inline fun rectangle(setup: RectangleElement.() -> Unit) = RectangleElement().also(setup)

inline fun text(setup: TextElement.() -> Unit) = TextElement().also(setup)

inline fun item(setup: ItemElement.() -> Unit) = ItemElement().also(setup)

inline fun cube(setup: CuboidElement.() -> Unit) =  CuboidElement().also(setup)
