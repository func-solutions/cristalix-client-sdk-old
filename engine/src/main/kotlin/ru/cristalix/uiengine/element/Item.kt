package ru.cristalix.uiengine.element

import dev.xdark.clientapi.item.ItemStack
import ru.cristalix.uiengine.UIEngine
import ru.cristalix.uiengine.utility.*

open class Item(
    var stack: ItemStack,
    scale: V3 = V3(1.0, 1.0, 1.0),
    offset: V3 = V3(0.0,0.0,0.0),
    align: V3 = V3(0.0,0.0,0.0),
    origin: V3 = V3(0.0,0.0,0.0),
    color: Color = WHITE,
    rotation: Rotation = Rotation(0.0,0.0,0.0,1.0),
    enabled: Boolean = true,
    onClick: ClickHandler? = null,
    onHover: HoverHandler? = null
) : Element(scale, offset, align, origin, color, rotation, enabled, onClick, onHover) {

    init {
        this.size = V3(16.0, 16.0)
    }

    override fun render() {
        UIEngine.clientApi.renderItem().renderItemAndEffectIntoGUI(stack, 0, 0)
    }

}