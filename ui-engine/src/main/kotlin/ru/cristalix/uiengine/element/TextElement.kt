package ru.cristalix.uiengine.element

import dev.xdark.clientapi.opengl.GlStateManager
import ru.cristalix.uiengine.UIEngine
import ru.cristalix.uiengine.utility.V3
import ru.cristalix.uiengine.utility.WHITE


open class TextElement() : AbstractElement() {

    var content: String = ""
        set(value) {
            field = value

            val lines = value.split("\n").toTypedArray()
            this.lines = lines
            val fontRenderer = UIEngine.clientApi.fontRenderer()
            var emptyLines = 0
            val lineWidthCache = IntArray(lines.size) {
                val line = lines[it]
                if (line.isBlank()) {
                    emptyLines++
                }
                fontRenderer.getStringWidth(line)
            }

            this.size = V3(
                lineWidthCache.max().toDouble(),
                lineHeight * (lines.size - emptyLines) + emptyLineHeight * emptyLines
            )
            this.lineWidthCache = lineWidthCache
        }

    var lineHeight = 9.0
        set(value) {
            field = value
            content = content
        }

    @JvmField var emptyLineHeight = 9.0

    private var lines: Array<String> = arrayOf()

    private var lineWidthCache: IntArray = intArrayOf()

    @JvmField var shadow = false

    init {
        color = WHITE
    }

    constructor(setup: TextElement.() -> Unit) : this() {
        setup()
    }

    override fun render() {

        val cachedHexColor = cachedHexColor
        val alpha: Int = cachedHexColor ushr 24

        // There is a weird behaviour of vanilla fontRenderer that disables transparency if the alpha value is lower than 5.
        // Perhaps that is yet another bodge by mojang to quickly implement some cool font-related effects.
        // Anyways, that is quite unnoticeable, so ui engine just discards these barely visible text elements.
        if (alpha < 5) return

        if (alpha != 255) GlStateManager.enableBlend()
        val fontRenderer = UIEngine.clientApi.fontRenderer()

        val lineWidthCache = lineWidthCache
        val sizeX = size.x
        val y = if (fontRenderer.unicodeFlag) 0f else 1f
        val originX = origin.x
        val lines = lines
        val emptyLineHeight = emptyLineHeight
        val lineHeight = lineHeight
        val shadow = shadow
        val size = lines.size
        for (i in 0 until size) {
            val line = lines[i]
            fontRenderer.drawString(
                line,
                ((sizeX - lineWidthCache[i]) * originX).toFloat(),
                y, cachedHexColor, shadow
            )
            GlStateManager.translate(0.0, if (line.isBlank()) emptyLineHeight else lineHeight, 0.0)
        }
    }

    private fun IntArray.max(): Int {
        val size = size
        if (size == 0) return 0
        var max = this[0]
        for (i in 1 until size) {
            val e = this[i]
            max = maxOf(max, e)
        }
        return max
    }

}
