package ru.cristalix.uiengine.element

import org.lwjgl.input.Mouse
import org.lwjgl.opengl.Display
import org.lwjgl.util.vector.Matrix4f
import org.lwjgl.util.vector.Vector4f
import ru.cristalix.uiengine.UIEngine
import ru.cristalix.uiengine.utility.Property
import ru.cristalix.uiengine.utility.V2
import ru.cristalix.uiengine.utility.V3

class Context(size: V3) : Rectangle(size = size) {

    private val runningTasks: MutableList<Task> = ArrayList()
    internal val runningAnimations: MutableList<Animation> = ArrayList()

    init {
        this.context = this
    }

    fun updateAnimations() {

        val time = System.currentTimeMillis()

        with(runningTasks.iterator()) {

            while (hasNext()) {
                val task = next()
                if (task.cancelled) {
                    remove()
                    continue
                }
                if (time >= task.scheduledTo) {
                    remove()
                    try {
                        task.action()
                    } catch (ex: Exception) {
                        RuntimeException("Error while executing task", ex).printStackTrace()
                    }
                }

            }
        }

        with(runningAnimations.iterator()) {
            while (hasNext()) {
                if (!next().update(time)) remove()
            }
        }

    }


    fun hoverCulling(element: Element): Boolean {
        var passed = false
        if (element is Rectangle) {
            for (child in element.children) {
                if (hoverCulling(child)) {
                    passed = true
                }
            }
        }

        if (element.onHover != null || element.onClick != null) {
            passed = true
        }
        element.passedHoverCulling = passed
        return passed

    }

    fun updateHoverStates(element: Element, baseMatrix: Matrix4f, mouse: V2) {

        if (!element.passedHoverCulling) return

        val matrix = Matrix4f()
        matrix.load(baseMatrix)

        for (m in element.matrices) {
            if (m != null) Matrix4f.mul(matrix, m, matrix)
        }

        if (element.onHover != null || element.onClick != null) {
            val sizeX = element.properties[Property.SizeX.ordinal]
            val sizeY = element.properties[Property.SizeY.ordinal]

            val vector = Vector4f(mouse.x.toFloat(), mouse.y.toFloat(), 0.0f, 1.0f)

            val inv = Matrix4f()
            Matrix4f.invert(matrix, inv)
            Matrix4f.transform(inv, vector, vector)

            val x = vector.x
            val y = vector.y

            val hovered = x >= 0 && x < sizeX && y >= 0 && y < sizeY
            if (element.hovered != hovered) {
                element.onHover?.invoke(element, hovered)
                element.hovered = hovered
            }

        }

        if (element is Rectangle) {
            for (child in element.children) {
                updateHoverStates(child, matrix, mouse)
            }
        }

    }


    override fun transformAndRender() {
        this.updateAnimations()

        val resolution = UIEngine.clientApi.resolution()

        val mouse = V2(
            (Mouse.getX() / resolution.scaleFactor).toDouble(),
            ((Display.getHeight() - Mouse.getY()) / resolution.scaleFactor).toDouble()
        )

        for (element in children) {
            this.hoverCulling(element)
            val baseMatrix = Matrix4f().setIdentity() as Matrix4f
            this.updateHoverStates(element, baseMatrix, mouse)
        }

        super.transformAndRender()
    }


}