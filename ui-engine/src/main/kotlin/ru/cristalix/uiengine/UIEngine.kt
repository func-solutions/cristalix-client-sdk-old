package ru.cristalix.uiengine

import dev.xdark.clientapi.ClientApi
import dev.xdark.clientapi.event.Listener
import dev.xdark.clientapi.event.lifecycle.GameLoop
import dev.xdark.clientapi.event.render.GuiOverlayRender
import dev.xdark.clientapi.event.render.RenderPass
import dev.xdark.clientapi.event.render.RenderTickPost
import dev.xdark.clientapi.event.window.WindowResize
import dev.xdark.clientapi.opengl.GLAllocation
import org.lwjgl.input.Mouse
import ru.cristalix.clientapi.JavaMod
import ru.cristalix.uiengine.element.*
import ru.cristalix.uiengine.utility.MouseButton
import ru.cristalix.uiengine.utility.V3
import java.nio.FloatBuffer

object UIEngine {

    val matrixBuffer: FloatBuffer = GLAllocation.createDirectFloatBuffer(16)

    /**
     * Instance of ClientApi.
     * You can reference that for any purposes.
     */
    lateinit var clientApi: ClientApi

    /**
     * This mod's listener.
     * Cristalix client is known to have some problems when registering mutiple listeners from a single mod.
     * Please, do not create your own listeners and stick to using this one.
     */
    lateinit var listener: Listener

    /**
     * Ingame HUD context that renders like chat, hotbar, etc.
     */
    val overlayContext: Context2D = Context2D(size = V3())

    /**
     * Ingame HUD context that renders after chests, pause menu and everything else
     */
    val postOverlayContext: Context2D = Context2D(size = V3())

    /**
     * World contexts for stuff like holograms.
     * You can add your own Context3D here.
     * Please note that worldContexts is being cleared on respawns / world changes
     */
    val worldContexts: MutableList<Context3D> = ArrayList()

    internal var lastMouseState: BooleanArray = booleanArrayOf(false, false, false)

    fun initialize(mod: JavaMod) {
        initialize(mod.listener, JavaMod.clientApi)
        mod.onDisable.add { uninitialize() }
    }

    /**
     * Main cristalix UI engine entrypoint.
     * It is recommended for every mod to call this as the first statement inside ModMain#load.
     */
    fun initialize(clientApi: ClientApi) {
        initialize(clientApi.eventBus().createListener(), clientApi)
    }

    fun initialize(listener: Listener, clientApi: ClientApi) {
        this.clientApi = clientApi
        this.listener = listener

        val eventBus = clientApi.eventBus()
        eventBus.register(listener, GuiOverlayRender::class.java, { renderOverlay() }, 1)
        if (!JavaMod.isClientMod()) {
            eventBus.register(listener, RenderTickPost::class.java, { renderPost() }, 1)
        }
        eventBus.register(listener, GameLoop::class.java, { gameLoop() }, 1)
        updateResolution()
        eventBus.register(listener, WindowResize::class.java, { updateResolution() }, 1)
        if (!JavaMod.isClientMod()) {
            eventBus.register(listener, RenderPass::class.java, { renderWorld(it) }, 1)
        }
    }

    fun uninitialize() {
        GLAllocation.freeBuffer(matrixBuffer)
    }

    private fun renderWorld(renderPass: RenderPass) {
        if (renderPass.pass != 2) return
        worldContexts.forEach { it.transformAndRender() }
    }

    private fun updateResolution() {
        val resolution = clientApi.resolution()
        overlayContext.size = V3(resolution.scaledWidth_double, resolution.scaledHeight_double)
        postOverlayContext.size = V3(resolution.scaledWidth_double, resolution.scaledHeight_double)
    }

    private fun renderOverlay() {
        overlayContext.transformAndRender()
    }

    private fun renderPost() {
        postOverlayContext.transformAndRender()
    }

    private fun findLastClickable(elements: Collection<AbstractElement>): AbstractElement? {
        var lastClickable: AbstractElement? = null
        for (element in elements) {
            // stdout.println(element.hovered + " " + element.passedHoverCulling + " " + (element.onClick != null))
            if (!element.passedHoverCulling) continue
            if (element.hovered && element.onClick != null) lastClickable = element
            if (element is RectangleElement) {
                lastClickable = findLastClickable(element.children) ?: lastClickable
            }
        }
        return lastClickable
    }

    /**
     * Convenient event handler registration.
     */
    // Avoid usage of forbidden Class class.
    @Suppress("NOTHING_TO_INLINE")
    inline fun <T> registerHandler(type: Class<T>, priority: Int = 1, noinline handler: T.() -> Unit) {
        clientApi.eventBus().register(listener, type, handler, priority)
    }

    private fun gameLoop() {
        for (button in MouseButton.VALUES) {
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

