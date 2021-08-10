package ru.cristalix.uiengine.element

import org.lwjgl.input.Mouse
import org.lwjgl.util.vector.Matrix4f
import org.lwjgl.util.vector.Vector2f
import org.lwjgl.util.vector.Vector4f
import ru.cristalix.uiengine.UIEngine
import ru.cristalix.uiengine.utility.Property
import ru.cristalix.uiengine.utility.V2
import ru.cristalix.uiengine.utility.V3

abstract class Context : RectangleElement() {

    private val baseMatrix = Matrix4f()

    var stencilPos = 0

    override fun transformAndRender() {

        val resolution = UIEngine.clientApi.resolution()

        val width = resolution.scaledWidth_double
        val height = resolution.scaledHeight_double

        if (width != size.x || height != size.y) {
            size = V3(width, height)
        }

        val x = Mouse.getX().toFloat() / resolution.scaleFactor
        val y = height - Mouse.getY().toFloat() / resolution.scaleFactor
        updateInteractiveState()
        if (interactive) {
            val baseMatrix = baseMatrix
            baseMatrix.setIdentity()
            baseMatrix.translate(Vector2f(-x, -y.toFloat()))
            updateHoverState(baseMatrix)
        }

        super.transformAndRender()
    }

    abstract fun transformViewportAndMouse(): V2?


}