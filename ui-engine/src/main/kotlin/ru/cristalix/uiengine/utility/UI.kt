package ru.cristalix.uiengine.utility

import dev.xdark.clientapi.item.ItemStack
import ru.cristalix.clientapi.JavaMod
import ru.cristalix.uiengine.element.*

inline fun rectangle(setup: RectangleElement.() -> Unit) = RectangleElement().also(setup)

inline fun text(setup: TextElement.() -> Unit) = TextElement().also(setup)

inline fun item(setup: ItemElement.() -> Unit) = ItemElement().also(setup)

inline fun cube(setup: CuboidElement.() -> Unit) = CuboidElement().also(setup)

context(JavaMod)
inline fun sphere(setup: SphereElement.() -> Unit) = SphereElement().also(setup)
