package ru.cristalix.uiengine.utility

import ru.cristalix.uiengine.element.AbstractElement

typealias ClickHandler = ((element: AbstractElement, buttonDown: Boolean, button: MouseButton) -> Unit)
typealias HoverHandler = ((element: AbstractElement, hovered: Boolean) -> Unit)