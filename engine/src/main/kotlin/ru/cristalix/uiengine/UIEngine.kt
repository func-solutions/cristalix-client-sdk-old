package ru.cristalix.uiengine

import dev.xdark.clientapi.ClientApi
import dev.xdark.clientapi.event.Listener
import dev.xdark.clientapi.event.lifecycle.GameLoop
import dev.xdark.clientapi.event.render.GuiOverlayRender
import dev.xdark.clientapi.event.window.WindowResize
import dev.xdark.clientapi.opengl.GLAllocation
import org.lwjgl.input.Mouse
import ru.cristalix.uiengine.element.Context
import ru.cristalix.uiengine.element.Element
import ru.cristalix.uiengine.element.Rectangle
import ru.cristalix.uiengine.utility.MouseButton
import ru.cristalix.uiengine.utility.V2
import ru.cristalix.uiengine.utility.V3
import java.nio.FloatBuffer

object UIEngine {

    val matrixBuffer: FloatBuffer = GLAllocation.createDirectFloatBuffer(16)

    lateinit var clientApi: ClientApi
    lateinit var listener: Listener

    val overlayContext: Context = Context(size = V3())

    var lastMouseState: BooleanArray = booleanArrayOf(false, false, false)

    fun initialize(clientApi: ClientApi) {
        this.clientApi = clientApi
        val eventBus = clientApi.eventBus()
        this.listener = eventBus.createListener()
        eventBus.register(listener, GuiOverlayRender::class.java, { renderOverlay() }, 1)
        eventBus.register(listener, GameLoop::class.java, { gameLoop() }, 1)
        updateResolution()
        eventBus.register(listener, WindowResize::class.java, { updateResolution() }, 1)
    }

    private fun updateResolution() {
        val resolution = clientApi.resolution()
        overlayContext.size = V3(resolution.scaledWidth_double, resolution.scaledHeight_double)
    }

    private fun renderOverlay() {
        overlayContext.transformAndRender()
    }

    fun uninitialize() {
        clientApi.eventBus().unregisterAll(listener)
        GLAllocation.freeBuffer(matrixBuffer)
    }

    private fun findLastClickable(elements: Collection<Element>): Element? {
        var lastClickable: Element? = null
        for (element in elements) {
            // stdout.println(element.hovered + " " + element.passedHoverCulling + " " + (element.onClick != null))
            if (!element.passedHoverCulling) continue
            if (element.hovered && element.onClick != null) lastClickable = element
            if (element is Rectangle) {
                lastClickable = findLastClickable(element.children) ?: lastClickable
            }
        }
        return lastClickable
    }

    // Avoid usage of forbidden Class class.
    @Suppress("NOTHING_TO_INLINE")
    inline fun <T> registerHandler(type: Class<T>, noinline handler: T.() -> Unit, priority: Int = 1) {
        clientApi.eventBus().register(listener, type, handler, priority)
    }

    private fun gameLoop() {
        for (button  in MouseButton.VALUES) {
            val idx = button.ordinal
            val oldState = lastMouseState[idx]
            val newState = Mouse.isButtonDown(idx)
            if (oldState != newState) {
                val lastClickable = findLastClickable(overlayContext.children)
                lastClickable?.onClick?.invoke(lastClickable, newState, button)
                lastMouseState[idx] = newState
            }
        }
    }

}
