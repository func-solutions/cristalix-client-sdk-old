package ru.cristalix.uiengine.element

import org.lwjgl.input.Mouse
import org.lwjgl.opengl.Display
import ru.cristalix.uiengine.UIEngine
import ru.cristalix.uiengine.utility.V2
import ru.cristalix.uiengine.utility.V3

class Context2D(size: V3) : Context() {

    private val mouse = V2()

    init {
        this.size = size
    }

    override fun transformViewportAndMouse(): V2 {
        val resolution = UIEngine.clientApi.resolution()
        val scaleFactor = resolution.scaleFactor
        val mouse = mouse
        mouse.x =(Mouse.getX() / scaleFactor).toDouble()
        mouse.y = ((Display.getHeight() - Mouse.getY()) / scaleFactor).toDouble()
        return mouse
    }
}