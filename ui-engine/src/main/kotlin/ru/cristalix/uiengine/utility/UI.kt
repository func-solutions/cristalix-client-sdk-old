package ru.cristalix.uiengine.utility

import ru.cristalix.uiengine.element.*

inline fun rectangle(setup: RectangleElement.() -> Unit) = RectangleElement().also(setup)

inline fun circle(setup: CircleElement.() -> Unit) = CircleElement().also(setup)

inline fun text(setup: TextElement.() -> Unit) = TextElement().also(setup)

inline fun item(setup: ItemElement.() -> Unit) = ItemElement().also(setup)

inline fun cube(setup: CuboidElement.() -> Unit) =  CuboidElement().also(setup)
