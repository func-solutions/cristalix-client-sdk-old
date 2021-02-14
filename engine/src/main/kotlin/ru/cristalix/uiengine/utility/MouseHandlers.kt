package ru.cristalix.uiengine.utility

import ru.cristalix.uiengine.element.Element

typealias ClickHandler = ((element: Element, buttonDown: Boolean, button: MouseButton) -> Unit)
typealias HoverHandler = ((element: Element, hovered: Boolean) -> Unit)