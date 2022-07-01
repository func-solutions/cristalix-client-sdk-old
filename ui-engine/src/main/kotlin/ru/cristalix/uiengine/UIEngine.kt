package ru.cristalix.uiengine

import dev.xdark.clientapi.ClientApi
import dev.xdark.clientapi.event.Listener
import dev.xdark.clientapi.event.lifecycle.GameLoop
import dev.xdark.clientapi.event.render.*
import dev.xdark.clientapi.event.window.WindowResize
import dev.xdark.clientapi.opengl.GLAllocation
import org.lwjgl.input.Mouse
import ru.cristalix.clientapi.JavaMod
import ru.cristalix.clientapi.registerHandler
import ru.cristalix.uiengine.element.*
import ru.cristalix.uiengine.eventloop.EventLoop
import ru.cristalix.uiengine.eventloop.EventLoopImpl
import ru.cristalix.uiengine.utility.MouseButton
import ru.cristalix.uiengine.utility.V3
import java.nio.FloatBuffer
import kotlin.properties.Delegates.notNull

object UIEngine : EventLoop by EventLoopImpl() {

    @JvmField
    val matrixBuffer: FloatBuffer = GLAllocation.createDirectFloatBuffer(16)

    /**
     * Instance of ClientApi.
     * You can reference that for any purposes.
     */
    var clientApi: ClientApi by notNull()

    /**
     * This mod's listener.
     * Cristalix client is known to have some problems when registering mutiple listeners from a single mod.
     * Please, do not create your own listeners and stick to using this one.
     */
    var listener: Listener by notNull()

    /**
     * Ingame HUD context that renders like chat, hotbar, etc.
     */
    @JvmField
    val overlayContext: Context2D = Context2D(size = V3())

    /**
     * Ingame HUD context that renders after chests, pause menu and everything else
     */
    @JvmField
    val postOverlayContext: Context2D = Context2D(size = V3())

    /**
     * World contexts for stuff like holograms.
     * You can add your own Context3D here.
     * Please note that worldContexts is being cleared on respawns / world changes
     */
    @JvmField
    val worldContexts: MutableList<Context3D> = ArrayList()

    private var lastMouseState: BooleanArray = booleanArrayOf(false, false, false)

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

        registerHandler<GuiOverlayRender>(listener) { renderOverlay() }

        registerHandler<RenderTickPost>(listener) { renderPost() }

        registerHandler<GameLoop>(listener) { gameLoop() }
        updateResolution()
        registerHandler<WindowResize>(listener) { updateResolution() }
        registerHandler<ScaleChange>(listener) { updateResolution() }
        registerHandler<RenderPass>(listener) { renderWorld() }
    }

    fun uninitialize() {
        // Close any GUIs before disabling
        if (clientApi.minecraft().currentScreen() is ContextGui)
            clientApi.minecraft().displayScreen(null)
        GLAllocation.freeBuffer(matrixBuffer)
    }

    private fun renderWorld() {
        val worldContexts = worldContexts
        val size = worldContexts.size
        for (i in 0 until size) worldContexts[i].transformAndRender()
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

    fun findLastClickable(elements: Collection<AbstractElement>): AbstractElement? {
        var lastClickable: AbstractElement? = null
        for (element in elements) {
            // stdout.println(element.hovered + " " + element.passedHoverCulling + " " + (element.onClick != null))
            if (!element.interactive) continue
            if (element.hovered && element.onClick != null) lastClickable = element
            if (element is RectangleElement) {
                lastClickable = findLastClickable(element.children) ?: lastClickable
            }
        }
        return lastClickable
    }

    private fun gameLoop() {
        update()
        val lastMouseState = lastMouseState
        for (button in MouseButton.values()) {
            val idx = button.ordinal
            val oldState = lastMouseState[idx]
            val newState = Mouse.isButtonDown(idx)
            if (oldState != newState) {
                overlayContext.getForemostHovered()?.run {
                    onClick?.invoke(ClickEvent(newState, button))
                }
                lastMouseState[idx] = newState
            }
        }
    }
}

