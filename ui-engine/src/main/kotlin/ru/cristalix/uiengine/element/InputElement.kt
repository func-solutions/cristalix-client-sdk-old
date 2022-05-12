package ru.cristalix.uiengine.element

import org.lwjgl.input.Keyboard
import ru.cristalix.uiengine.UIEngine
import ru.cristalix.uiengine.onMouseDown
import ru.cristalix.uiengine.utility.CENTER
import ru.cristalix.uiengine.utility.flex
import ru.cristalix.uiengine.utility.text
import kotlin.math.pow

open class InputElement(gui: ContextGui) : CarvedRectangle() {
    var typing = false
    var hint = ""
        set(value) {
            hintText.content = "§7$value"
            field = value
        }
    var content = contentText.content
        set(value) {
            contentText.content = value
            field = value
        }

    var allowSymbols = "qwertyuiopasdfghjklzxcvbnmйцукенгшщзхъфывапролджэячсмитьбю -0123456789"
    var allowMultiline = true

    private var lastUpdate = 0L
    private lateinit var contentText: TextElement
    private var container = +flex {
        align = CENTER
        origin = CENTER
        flexSpacing = 1.0
        contentText = +text {
            content = ""
        }
    }
    private val hintText = +text {
        align = CENTER
        origin = CENTER
    }

    init {
        onClick {
            typing = true
        }
        onHover {
            if (!typing)
                removeSuffix()
        }
        beforeRender {
            val now = System.currentTimeMillis()
            if (now - lastUpdate > 500 && typing) {
                lastUpdate = now
                if (!removeSuffix())
                    contentText.content += "|"
            }
            hintText.enabled = !typing && contentText.content.isEmpty()
        }
        beforeTransform {
            container.size = size
        }

        gui.onKeyTyped { char, code ->
            if (!typing)
                return@onKeyTyped
            removeSuffix()
            if (code == Keyboard.KEY_BACK) {
                back()
            } else if (allowSymbols.contains(char)) {
                if (UIEngine.clientApi.fontRenderer()
                        .getStringWidth(contentText.content.split("\n").last()) + 12 >= size.x
                )
                    nextLine()
                else
                    return@onKeyTyped
                contentText.content += char
            } else if (code == Keyboard.KEY_RETURN) {
                nextLine()
            }
        }

        gui.onMouseDown {
            typing = typing && hovered
            if (!typing)
                removeSuffix()
        }
    }

    private fun removeSuffix(): Boolean {
        val had = contentText.content.contains("|")
        contentText.content = contentText.content.removeSuffix("|")
        return had
    }

    private fun nextLine() {
        if (allowMultiline) contentText.content += "\n"
    }

    private fun back(iteration: Int = 1) {
        if (Keyboard.isKeyDown(Keyboard.KEY_BACK) && typing) {
            contentText.content = contentText.content.dropLast(1)
            UIEngine.schedule(0.14 / (iteration * 1.0).pow(0.7)) { back(iteration + 1) }
        }
    }
}