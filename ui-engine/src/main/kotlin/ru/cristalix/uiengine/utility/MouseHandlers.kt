@file:Suppress("PackageDirectoryMismatch")

package ru.cristalix.uiengine

import ru.cristalix.uiengine.element.AbstractElement
import ru.cristalix.uiengine.utility.MouseButton
import ru.cristalix.uiengine.utility.V2

typealias ClickHandler = ClickEvent.() -> Unit
typealias HoverHandler = AbstractElement.() -> Unit

inline fun AbstractElement.onMouseDown(crossinline action: ClickHandler) {
    onClick = {
        if (down) action()
    }
}

inline fun AbstractElement.onMouseUp(crossinline action: ClickHandler) {
    onClick = {
        if (!down) action()
    }
}

data class ClickEvent(
    val down: Boolean,
    val button: MouseButton
)
