package ru.cristalix.uiengine

import dev.xdark.clientapi.ClientApi
import dev.xdark.clientapi.event.Listener
import dev.xdark.clientapi.event.lifecycle.GameLoop
import dev.xdark.clientapi.event.render.GuiOverlayRender
import dev.xdark.clientapi.opengl.GLAllocation
import org.lwjgl.input.Mouse
import ru.cristalix.uiengine.element.Context
import ru.cristalix.uiengine.element.Element
import ru.cristalix.uiengine.element.Rectangle
import ru.cristalix.uiengine.utility.MouseButton
import ru.cristalix.uiengine.utility.V2
import ru.cristalix.uiengine.utility.V3

object UIEngine {

    val matrixBuffer = GLAllocation.createDirectFloatBuffer(16)!!

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

    }

    private fun renderOverlay() {
//        if (Math.random() < 0.01) println("hello")
        overlayContext.transformAndRender()
    }

    fun uninitialize() {
        this.clientApi.eventBus().unregisterAll(listener)
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

        IntRange(0, 2).forEach { button ->

            val oldState = lastMouseState[button]
            val newState = Mouse.isButtonDown(button)
            if (oldState != newState) {
                val lastClickable = findLastClickable(overlayContext.children)
                lastClickable?.onClick?.invoke(lastClickable, newState, MouseButton.values()[button])
            }
            lastMouseState[button] = newState
        }

    }

}
