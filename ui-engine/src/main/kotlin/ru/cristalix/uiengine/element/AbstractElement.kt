package ru.cristalix.uiengine.element

import dev.xdark.clientapi.opengl.GlStateManager
import org.lwjgl.util.vector.Matrix4f
import org.lwjgl.util.vector.Vector4f
import ru.cristalix.uiengine.UIEngine
import ru.cristalix.uiengine.UIEngine.matrixBuffer
import ru.cristalix.uiengine.eventloop.Animation
import ru.cristalix.uiengine.utility.*
import ru.cristalix.uiengine.utility.Property.*

abstract class AbstractElement() {

    internal val properties: DoubleArray = DoubleArray(Property.VALUES.size)
    val matrices: Array<Matrix4f?> = arrayOfNulls(matrixFields)

    open var context: Context? = null

    private var dirtyMatrices: MutableList<Int>? = null
    protected var cachedHexColor: Int = 0
    internal var hovered: Boolean = false
    internal var interactive: Boolean = false

    var beforeRender: (() -> Unit)? = null
    var afterRender: (() -> Unit)? = null

    var enabled: Boolean = true
    var onClick: ClickHandler? = null
    var onHover: HoverHandler? = null

    var offset: V3 = ProxiedV3(OffsetX.ordinal, this)
        set(value) = value.write(field)

    var scale: V3 = ProxiedV3(ScaleX.ordinal, this)
        set(value) = value.write(field)

    var align: V3 = ProxiedV3(AlignX.ordinal, this)
        set(value) = value.write(field)

    var origin: V3 = ProxiedV3(OriginX.ordinal, this)
        set(value) = value.write(field)

    var color: Color = ProxiedColor(this)
        set(value) = value.write(field)

    var rotation: Rotation = ProxiedRotation(this)
        set(value) = value.write(field)

    var size: V3 = ProxiedV3(SizeX.ordinal, this)
        set(value) = value.write(field)

    init {
        this.scale = V3(1.0, 1.0, 1.0)
        this.changeProperty(RotationZ.ordinal, 1.0)
    }

    constructor(setup: AbstractElement.() -> Unit) : this() {
        setup()
    }

    internal fun changeProperty(index: Int, value: Number) {

        val property = Property.VALUES[index]

        val animationContext = UIEngine.animationContext
        if (animationContext != null) {
            var animation: Animation? = null
            for (existing in UIEngine.runningAnimations) {
                if (existing.element === this && existing.property.ordinal == index) {
                    animation = existing
                    break
                }
            }
            if (animation == null) animation = Animation(this, property)
            animation.newTarget(
                value.toDouble(),
                (animationContext.duration.toDouble() * 1000).toLong(),
                animationContext.easing
            )

            UIEngine.runningAnimations.add(animation)

            return
        }
        // stdout.println('setting ' + propertyId + ' to ' + value);
        this.properties[index] = value.toDouble()
        for (matrix in property.matrixInfluence) {
            this.markDirty(matrix)
        }

    }

    open fun updateInteractiveState() {
        this.interactive = enabled && (onHover != null || onClick != null)
    }

    open fun updateHoverState(mouseMatrix: Matrix4f) {

        for (m in matrices) {
            Matrix4f.mul(mouseMatrix, m, mouseMatrix)
        }

        val hitbox = arrayOf(
            Vector4f(0f, 0f, 0f, 0f),
            Vector4f(0f, size.y.toFloat(), 0f, 0f),
            Vector4f(size.x.toFloat(), size.y.toFloat(), 0f, 0f),
            Vector4f(size.x.toFloat(), 0f, 0f, 0f),
        )

        for (vertex in hitbox) {
            Matrix4f.transform(mouseMatrix, vertex, vertex)
        }

        val hovered = containsZero(hitbox)
        if (this.hovered != hovered) {
            this.onHover?.invoke(this, hovered)
            this.hovered = hovered
        }

    }

    fun containsZero(convex: Array<Vector4f>): Boolean {
        convex.forEachIndexed { i, v ->
            val j = (i + 1) % convex.size
            val p = convex[j]
            val edgeX = p.x - v.x
            val edgeY = p.y - v.y

            val diffX = -v.x
            val diffY = -v.y

            val dot = -edgeY*diffX+edgeX*diffY

            if (dot < 0) return false

        }
        return true
    }

    fun cleanMatrices() {
        val dirty = this.dirtyMatrices ?: return
        this.dirtyMatrices = null
        for (matrix in dirty.toIntArray()) {
            this.updateMatrix(matrix)
        }
    }

    open fun updateMatrix(matrixId: Int) {
        val properties = properties
        dirtyMatrices?.remove(matrixId)

        if (matrixId == colorMatrix) {
            this.cachedHexColor = this.color.toGuiHex()
        }

        if (matrixId >= 0) {
            val matrix: Matrix4f = this.matrices[matrixId] ?: Matrix4f()
            matrix.setIdentity()

            when (matrixId) {
                alignMatrix -> matrix.translate(
                    properties[AlignX] * properties[ParentSizeX],
                    properties[AlignY] * properties[ParentSizeY],
                    properties[AlignZ] * properties[ParentSizeZ]
                )
                rotationMatrix -> matrix.rotate(
                    properties[RotationAngle].toFloat(),
                    properties[RotationX],
                    properties[RotationY],
                    properties[RotationZ]
                )
                offsetMatrix -> matrix.translate(
                    properties[OffsetX],
                    properties[OffsetY],
                    properties[OffsetZ]
                )
                scaleMatrix -> matrix.scale(
                    properties[ScaleX],
                    properties[ScaleY],
                    properties[ScaleZ]
                )
                originMatrix -> matrix.translate(
                    -properties[OriginX] * properties[SizeX],
                    -properties[OriginY] * properties[SizeY],
                    -properties[OriginZ] * properties[SizeZ]
                )
            }
            this.matrices[matrixId] = matrix
        }
    }

    fun markDirty(matrix: Int) {
        val matrices = dirtyMatrices ?: ArrayList()
        if (!matrices.contains(matrix)) matrices.add(matrix)
        dirtyMatrices = matrices

        if (matrix == sizeMatrix) {
            markDirty(originMatrix)
        }
    }

    open fun transformAndRender() {
        if (!this.enabled) return

        GlStateManager.pushMatrix()
        this.applyTransformations()

        this.beforeRender?.invoke()
        this.render()
        this.afterRender?.invoke()

        GlStateManager.popMatrix()
    }

    open fun applyTransformations() {

        if (!this.enabled) return

        this.cleanMatrices()

        val properties = properties
        GlStateManager.color(
            properties[ColorR].toFloat(),
            properties[ColorG].toFloat(),
            properties[ColorB].toFloat(),
            properties[ColorA].toFloat(),
        )

        val matrixBuffer = matrixBuffer
        for (matrix in this.matrices) {
            if (matrix == null) continue
            matrix.store(matrixBuffer)
            matrixBuffer.flip()
            GlStateManager.multMatrix(matrixBuffer)
        }
    }

//    fun update(action: Element.() -> Unit): Element {
//        TODO("Not yet implemented")
//        return getThis()
//    }
//
//    infix fun then(action: () -> Unit): Element {
//        TODO("Not yet implemented")
//        return getThis()
//    }
//
//    fun then(delay: Number, action: () -> Unit): Element {
//        TODO("Not yet implemented")
//        return getThis()
//    }

    abstract fun render()
}
