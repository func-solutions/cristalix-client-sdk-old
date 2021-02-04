package ru.cristalix.uiengine.element

import ru.cristalix.uiengine.UIEngine
import ru.cristalix.uiengine.utility.*
import dev.xdark.clientapi.opengl.GlStateManager




class Text(
    scale: V3 = V3(1.0, 1.0, 1.0),
    offset: V3 = V3(),
    align: V3 = V3(),
    origin: V3 = V3(),
    color: Color = WHITE,
    rotation: Rotation = Rotation(),
    enabled: Boolean = true,
    onClick: ClickHandler = null,
    onHover: HoverHandler = null,
    content: String,
//    autoFit: Boolean = false,
    var shadow: Boolean = false,
) : Element(scale, offset, align, origin, color, rotation, enabled, onClick, onHover) {

    var content: String = content
        set(value) {
            field = value
            this.size = V3(UIEngine.clientApi.fontRenderer().getStringWidth(value).toDouble(), 9.0)
        }

    init {
        this.content = content
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
