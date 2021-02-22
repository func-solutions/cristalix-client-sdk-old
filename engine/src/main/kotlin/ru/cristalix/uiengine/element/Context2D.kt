package ru.cristalix.uiengine.element

import org.lwjgl.input.Mouse
import org.lwjgl.opengl.Display
import ru.cristalix.uiengine.UIEngine
import ru.cristalix.uiengine.utility.V2
import ru.cristalix.uiengine.utility.V3

class Context2D(size: V3) : Context() {

    init {
        this.size = size
    }

    override fun transformViewportAndMouse(): V2 {

        val resolution = UIEngine.clientApi.resolution()

        return V2(
            (Mouse.getX() / resolution.scaleFactor).toDouble(),
            ((Display.getHeight() - Mouse.getY()) / resolution.scaleFactor).toDouble()
        )

    }
}