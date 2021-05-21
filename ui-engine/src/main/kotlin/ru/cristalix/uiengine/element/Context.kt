package ru.cristalix.uiengine.element

import org.lwjgl.util.vector.Matrix4f
import org.lwjgl.util.vector.Vector4f
import ru.cristalix.uiengine.utility.Property
import ru.cristalix.uiengine.utility.V2

abstract class Context : RectangleElement() {

    private val baseMatrix = Matrix4f()
    private val invMatrix = Matrix4f()
    private val hoverVector = Vector4f()

    var stencilPos = 0

    init {
        this.context = this
    }

    fun hoverCulling(element: AbstractElement): Boolean {
        var passed = false
        if (element.onHover != null || element.onClick != null) {
            passed = true
        }
        if (element is RectangleElement) {
            val children = element.children
            if (children.isNotEmpty()) {
                for (child in children) {
                    if (hoverCulling(child)) {
                        passed = true
                    }
                }
            }
        }

        element.interactive = passed
        return passed

    }

    fun updateHoverStates(element: AbstractElement, baseMatrix: Matrix4f, mouse: V2) {

        if (!element.interactive) return

        // ToDo: Optimize hover detection (cache this matrix inside element)
        val matrix = Matrix4f()
        matrix.load(baseMatrix)

        for (m in element.matrices) {
            if (m != null) Matrix4f.mul(matrix, m, matrix)
        }

        if (element.onHover != null || element.onClick != null) {
            val sizeX = element.properties[Property.SizeX.ordinal]
            val sizeY = element.properties[Property.SizeY.ordinal]

            val vector = hoverVector
            vector.x = mouse.x.toFloat()
            vector.y = mouse.y.toFloat()
            vector.z = 0.0f
            vector.w = 1.0f

            val inv = invMatrix
            inv.setIdentity()
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

        if (element is RectangleElement) {
            val children = element.children
            if (children.isNotEmpty()) {
                for (child in children) {
                    updateHoverStates(child, matrix, mouse)
                }
            }
        }

    }


    override fun transformAndRender() {

        val mouse = transformViewportAndMouse() ?: return

        if (children.isNotEmpty()) {
            val baseMatrix = baseMatrix
            for (element in children) {
                baseMatrix.setIdentity()
                this.hoverCulling(element)
                this.updateHoverStates(element, baseMatrix, mouse)
            }
        }

        super.transformAndRender()
    }

    abstract fun transformViewportAndMouse(): V2?


}