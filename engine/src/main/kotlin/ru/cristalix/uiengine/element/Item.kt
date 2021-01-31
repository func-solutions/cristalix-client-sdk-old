package ru.cristalix.uiengine.element

import dev.xdark.clientapi.item.ItemStack
import ru.cristalix.uiengine.utility.Color
import ru.cristalix.uiengine.utility.Rotation
import ru.cristalix.uiengine.utility.V3

open class Item(
    scale: V3,
    offset: V3,
    align: V3,
    origin: V3,
    color: Color,
    rotation: Rotation,
    enabled: Boolean,
    onClick: ClickHandler,
    onHover: HoverHandler,
    var stack: ItemStack
) : Element(scale, offset, align, origin, color, rotation, enabled, onClick, onHover) {



}