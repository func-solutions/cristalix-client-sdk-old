package ru.cristalix.uiengine.element

import ru.cristalix.uiengine.UIEngine
import ru.cristalix.uiengine.utility.*
import dev.xdark.clientapi.opengl.GlStateManager


class TextElement() : AbstractElement() {

    var content: String = ""
        set(value) {
            field = value
            this.size = V3(UIEngine.clientApi.fontRenderer().getStringWidth(value).toDouble(), 9.0)
        }

    var shadow = false

    init {
        color = WHITE
    }

    constructor(setup: TextElement.() -> Unit) : this() {
        setup()
    }


    override fun render() {

        val alpha: Int = cachedHexColor ushr 24

        // There is a weird behaviour of vanilla fontRenderer that disables transparency if the alpha value is lower than 5.
        // Perhaps that is yet another bodge by mojang to quickly implement some cool font-related effects.
        // Anyways, that is quite unnoticable, so ui engine just discards these barely visible text elements.
        if (alpha < 5) return

        if (alpha != 255) GlStateManager.enableBlend()
        val fontRenderer = UIEngine.clientApi.fontRenderer()
        fontRenderer.drawString(this.content, 0f, if (fontRenderer.unicodeFlag) 0f else 1f, cachedHexColor, shadow)
    }

}
