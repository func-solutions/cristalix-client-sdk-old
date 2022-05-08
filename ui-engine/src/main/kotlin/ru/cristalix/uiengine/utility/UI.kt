package ru.cristalix.uiengine.utility

import ru.cristalix.uiengine.element.*

inline fun rectangle(setup: RectangleElement.() -> Unit) = RectangleElement().also(setup)

inline fun text(setup: TextElement.() -> Unit) = TextElement().also(setup)

inline fun item(setup: ItemElement.() -> Unit) = ItemElement().also(setup)

inline fun cube(setup: CuboidElement.() -> Unit) = CuboidElement().also(setup)

inline fun sphere(setup: SphereElement.() -> Unit) = SphereElement().also(setup)

inline fun carved(setup: CarvedRectangle.() -> Unit) = CarvedRectangle().also(setup)
