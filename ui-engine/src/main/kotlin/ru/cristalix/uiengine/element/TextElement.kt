package ru.cristalix.uiengine.element

import ru.cristalix.uiengine.UIEngine
import ru.cristalix.uiengine.utility.*
import dev.xdark.clientapi.opengl.GlStateManager


open class TextElement() : AbstractElement() {

    var content: String = ""
        set(value) {
            field = value

            val lines = value.split("\n").toTypedArray()
            this.lines = lines
            lineWidthCache = DoubleArray(lines.size) { UIEngine.clientApi.fontRenderer().getStringWidth(lines[it]).toDouble() }

            val emptyLines = lines.count { it.isBlank() }
            this.size = V3(
                lineWidthCache.max(),
                lineHeight * (lines.size - emptyLines) + emptyLineHeight * emptyLines
            )
        }

    var lineHeight = 9.0
        set(value) {
            field = value
            content = content
        }

    var emptyLineHeight = 9.0

    private var lines: Array<String> = arrayOf()

    private var lineWidthCache: DoubleArray = doubleArrayOf()

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
        // Anyways, that is quite unnoticeable, so ui engine just discards these barely visible text elements.
        if (alpha < 5) return

        if (alpha != 255) GlStateManager.enableBlend()
        val fontRenderer = UIEngine.clientApi.fontRenderer()

        lines.forEachIndexed { index, line ->

            fontRenderer.drawString(
                line,
                ((size.x - lineWidthCache[index]) * origin.x).toFloat(),
                if (fontRenderer.unicodeFlag) 0f else 1f, cachedHexColor, shadow
            )
            GlStateManager.translate(0.0, if (line.isBlank()) emptyLineHeight else lineHeight, 0.0)

        }

    }

    private fun DoubleArray.max(): Double {
        if (isEmpty()) return 0.0
        var max = this[0]
        for (i in 1..lastIndex) {
            val e = this[i]
            max = maxOf(max, e)
        }
        return max
    }

}
