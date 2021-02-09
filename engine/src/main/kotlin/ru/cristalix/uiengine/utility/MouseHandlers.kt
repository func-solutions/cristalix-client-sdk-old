package ru.cristalix.uiengine.element

import ru.cristalix.uiengine.utility.MouseButton

typealias ClickHandler = ((element: Element, buttonDown: Boolean, button: MouseButton) -> Unit)?
typealias HoverHandler = ((element: Element, hovered: Boolean) -> Unit)?