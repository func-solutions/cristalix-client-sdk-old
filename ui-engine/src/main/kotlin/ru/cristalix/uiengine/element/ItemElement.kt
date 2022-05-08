package ru.cristalix.uiengine.element

import dev.xdark.clientapi.item.ItemStack
import dev.xdark.clientapi.opengl.RenderHelper
import ru.cristalix.uiengine.UIEngine
import ru.cristalix.uiengine.utility.*

open class ItemElement() : AbstractElement() {

    var stack: ItemStack? = null

    init {
        this.color = WHITE
        this.size = V3(16.0, 16.0)
    }

    constructor(setup: ItemElement.() -> Unit): this() {
        setup()
    }

    override fun render() {
        val stack = this.stack
        if (stack != null) {
            RenderHelper.enableGUIStandardItemLighting()
            UIEngine.clientApi.renderItem().renderItemAndEffectIntoGUI(stack, 0, 0)
            RenderHelper.disableStandardItemLighting()
        }
    }
}