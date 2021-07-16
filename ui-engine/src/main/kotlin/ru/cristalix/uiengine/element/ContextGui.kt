package ru.cristalix.uiengine.element

import dev.xdark.clientapi.gui.Screen
import org.lwjgl.input.Keyboard
import org.lwjgl.input.Mouse
import org.lwjgl.util.vector.Matrix4f
import org.lwjgl.util.vector.Vector2f
import ru.cristalix.uiengine.ClickEvent
import ru.cristalix.uiengine.UIEngine
import ru.cristalix.uiengine.utility.MouseButton
import ru.cristalix.uiengine.utility.V3

inline fun safe(action: () -> Unit) {
    try {
        action()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

open class ContextGui(builder: Screen.Builder = Screen.Builder.builder()) : Context2D(V3()) {

    val keyTypedHandlers = ArrayList<(char: Char, code: Int) -> Unit>()

    init {
    }

    val screen: Screen = builder
        .draw { _, _, _, _ -> safe(this::transformAndRender) }
        .keyTyped { _, key, code ->
            keyTypedHandlers.forEach {
                safe { it(key, code) }
            }
        }
        .mouseClick { _, _, _, button ->
            safe {
                getForemostHovered()?.run {
                    this.onClick?.invoke(ClickEvent(true, MouseButton.values()[button], hoverPosition!!))
                }
            }
        }
        .mouseRelease { _, _, _, button ->
            safe {
                getForemostHovered()?.run {
                    this.onClick?.invoke(ClickEvent(false, MouseButton.values()[button], hoverPosition!!))
                }
            }
        }
        .build()

    fun open() = UIEngine.clientApi.minecraft().displayScreen(screen)

    fun close() = UIEngine.clientApi.minecraft().displayScreen(null)

    fun onKeyTyped(action: (char: Char, code: Int) -> Unit) =
        keyTypedHandlers.add(action)

}
