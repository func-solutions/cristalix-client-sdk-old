package ru.cristalix.uiengine.element

import org.lwjgl.util.vector.Matrix4f
import org.lwjgl.util.vector.Vector4f
import ru.cristalix.uiengine.utility.Property
import ru.cristalix.uiengine.utility.V2

abstract class Context : RectangleElement() {

    private val runningTasks: MutableList<Task> = ArrayList()
    internal val runningAnimations: MutableList<Animation> = ArrayList()
    private val baseMatrix = Matrix4f()
    private val hoverMatrix = Matrix4f()
    private val invMatrix = Matrix4f()

    init {
        this.context = this
    }

    fun schedule(delaySeconds: Number, action: () -> Unit): Task {
        val task = Task(System.currentTimeMillis() + (delaySeconds.toDouble() * 1000).toInt(), action)
        runningTasks.add(task)
        return task
    }

    fun updateAnimations() {

        val time = System.currentTimeMillis()

        if (runningTasks.isNotEmpty()) {
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
        }

        if (runningAnimations.isNotEmpty()) {
            with(runningAnimations.iterator()) {
                while (hasNext()) {
                    if (!next().update(time)) remove()
                }
            }
        }
    }


    fun hoverCulling(element: AbstractElement): Boolean {
        var passed = false
        if (element.onHover != null || element.onClick != null) {
            passed = true
        } else if (element is RectangleElement) {
            val children = element.children
            if (children.isNotEmpty()) {
                for (child in children) {
                    if (hoverCulling(child)) {
                        passed = true
                    }
                }
            }
        }

        element.passedHoverCulling = passed
        return passed

    }

    fun updateHoverStates(element: AbstractElement, baseMatrix: Matrix4f, mouse: V2) {

        if (!element.passedHoverCulling) return

        val matrix = hoverMatrix
        matrix.load(baseMatrix)

        for (m in element.matrices) {
            if (m != null) Matrix4f.mul(matrix, m, matrix)
        }

        if (element.onHover != null || element.onClick != null) {
            val sizeX = element.properties[Property.SizeX.ordinal]
            val sizeY = element.properties[Property.SizeY.ordinal]

            val vector = Vector4f(mouse.x.toFloat(), mouse.y.toFloat(), 0.0f, 1.0f)

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
        this.updateAnimations()

        val mouse = transformViewportAndMouse() ?: return

        if (children.isNotEmpty()) {
            val baseMatrix = baseMatrix
            baseMatrix.setIdentity()
            for (element in children) {
                this.hoverCulling(element)
                this.updateHoverStates(element, baseMatrix, mouse)
            }
        }

        super.transformAndRender()
    }

    abstract fun transformViewportAndMouse(): V2?


}