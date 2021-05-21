package ru.cristalix.uiengine.element

import dev.xdark.clientapi.gui.Screen
import org.lwjgl.input.Keyboard
import ru.cristalix.uiengine.UIEngine
import ru.cristalix.uiengine.utility.V3

class ContextGui: Context2D(V3()) {

    val keyTypedHandlers = ArrayList<(char: Char, code: Int) -> Unit>()

    init {
        onKeyTyped { _, code ->
            if (code == Keyboard.KEY_ESCAPE) close()
        }
    }

    val screen: Screen = Screen.Builder.builder()
        .draw { _, _, _, _ -> this.render() }
        .keyTyped { _, key, code -> keyTypedHandlers.forEach { it(key, code) } }
        .build()

    fun open() = UIEngine.clientApi.minecraft().displayScreen(screen)

    fun close() = UIEngine.clientApi.minecraft().displayScreen(null)

    fun onKeyTyped(action: (char: Char, code: Int) -> Unit) =
        keyTypedHandlers.add(action)

}
