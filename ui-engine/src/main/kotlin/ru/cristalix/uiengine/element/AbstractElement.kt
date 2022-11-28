package ru.cristalix.uiengine.element

import dev.xdark.clientapi.opengl.GlStateManager
import org.lwjgl.util.vector.Matrix4f
import org.lwjgl.util.vector.Vector4f
import ru.cristalix.uiengine.ClickHandler
import ru.cristalix.uiengine.HoverHandler
import ru.cristalix.uiengine.UIEngine
import ru.cristalix.uiengine.UIEngine.matrixBuffer
import ru.cristalix.uiengine.eventloop.Animation
import ru.cristalix.uiengine.utility.*
import ru.cristalix.uiengine.utility.Property.*

@Suppress("LeakingThis")
abstract class AbstractElement() : IElement {

    @JvmField
    val properties: DoubleArray = DoubleArray(Property.VALUES.size)
    @JvmField
    val matrices: Array<Matrix4f?> = arrayOfNulls(matrixFields)

    private var dirtyMatrices = BooleanArray(MATRIX_COUNT)
    @JvmField
    protected var cachedHexColor: Int = 0

    var hovered: Boolean = false
        internal set

    var hoverPosition: V2 = V2()
        protected set

    var leftPressed = false
    var rightPressed = false

    internal var interactive: Boolean = false

    var beforeRender: (() -> Unit)? = null
        set(value) {
            val prev = field
            field = {
                prev?.invoke()
                value?.invoke()
            }
        }

    var afterRender: (() -> Unit)? = null
        set(value) {
            val prev = field
            field = {
                prev?.invoke()
                value?.invoke()
            }
        }

    var beforeTransform: (() -> Unit)? = null
        set(value) {
            val prev = field
            field = {
                prev?.invoke()
                value?.invoke()
            }
        }

    var afterTransform: (() -> Unit)? = null
        set(value) {
            val prev = field
            field = {
                prev?.invoke()
                value?.invoke()
            }
        }

    @JvmField
    var enabled: Boolean = true
    @JvmField
    var onClick: ClickHandler? = null
    @JvmField
    var onHover: HoverHandler? = null
    @JvmField
    var lastParent: AbstractElement? = null

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

    override var size: V3 = ProxiedV3(SizeX.ordinal, this)
        set(value) = value.write(field)

    init {
        this.scale = V3(1.0, 1.0, 1.0)
        this.changeProperty(RotationZ.ordinal, 1.0)
    }

    constructor(setup: AbstractElement.() -> Unit) : this() {
        setup()
    }

    fun onLeftClick(action: () -> Unit) = onMouseStateChange {
        if (button == MouseButton.LEFT) {
            if (down) leftPressed = true
            else if (leftPressed) {
                action()
                leftPressed = false
            }
        }
    }

    fun onRightClick(action: () -> Unit) = onMouseStateChange {
        if (button == MouseButton.RIGHT) {
            if (down) rightPressed = true
            else if (rightPressed) {
                action()
                rightPressed = false
            }
        }
    }

    @Deprecated("Use onLeftClick()")
    fun onClick(action: ClickHandler) = onMouseStateChange(action)

    fun onMouseStateChange(action: ClickHandler) {
        val prev = onClick
        onClick = {
            prev?.invoke(this)
            action.invoke(this)
        }
    }

    fun onHover(action: HoverHandler) {
        val prev = onHover
        onHover = {
            prev?.invoke(this)
            action.invoke(this)
        }
    }

    fun beforeRender(action: () -> Unit) {
        beforeRender = action
    }

    fun afterRender(action: () -> Unit) {
        afterRender = action
    }

    fun beforeTransform(action: () -> Unit) {
        beforeTransform = action
    }

    fun afterTransform(action: () -> Unit) {
        afterTransform = action
    }

    internal fun changeProperty(index: Int, value: Double) {
        val engine = UIEngine
        val animationContext = engine.animationContext
        if (animationContext != null) {
            var animation: Animation? = null
            val runningAnimations = engine.runningAnimations
            for (existing in runningAnimations) {
                if (existing.element === this && existing.propertyIndex == index) {
                    animation = existing
                    break
                }
            }
            if (animation == null) animation = Animation(this, index)
            animation.newTarget(
                value,
                animationContext.durationMillis,
                animationContext.easing
            )
            runningAnimations.add(animation)
            return
        }

        val properties = properties
        if (properties[index] != value) {
            properties[index] = value
            for (matrix in Property.VALUES[index].matrixInfluence) {
                this.markDirty(matrix)
            }
        }
    }

    internal fun changeProperty(index: Int, value: Long) =
        changeProperty(index, value.toDouble())

    internal fun changeProperty(index: Int, value: Int) =
        changeProperty(index, value.toDouble())

    internal fun changeProperty(index: Int, value: Float) =
        changeProperty(index, value.toDouble())

    internal fun changeProperty(index: Int, value: Number) =
        changeProperty(index, value.toDouble())

    open fun updateInteractiveState() {
        this.interactive = enabled && (onHover != null || onClick != null)
    }

    open fun updateHoverState(mouseMatrix: Matrix4f) {
        val matrices = matrices
        val count = matrices.size
        for (i in 0 until count) {
            val m = matrices[i]
            if (m != null) {
                Matrix4f.mul(mouseMatrix, m, mouseMatrix)
            }
        }
        val size = size
        val hitbox = arrayOf(
            Vector4f(0f, 0f, 0f, 1f),
            Vector4f(0f, size.y.toFloat(), 0f, 1f),
            Vector4f(size.x.toFloat(), size.y.toFloat(), 0f, 1f),
            Vector4f(size.x.toFloat(), 0f, 0f, 1f),
        )

        for (vertex in hitbox) {
            Matrix4f.transform(mouseMatrix, vertex, vertex)
        }

        val hovered = containsZero(hitbox)

        val hoverPosition = hoverPosition
        hoverPosition.x = (-hitbox[0].x).toDouble()
        hoverPosition.y = (-hitbox[0].x).toDouble()

        if (this.hovered != hovered) {
            this.hovered = hovered
            this.onHover?.invoke(this)
        }

    }

    open fun getForemostHovered(): AbstractElement? {
        return if (interactive && hovered) this else null
    }

    fun containsZero(convex: Array<Vector4f>): Boolean {
        convex.forEachIndexed { i, v ->
            val j = (i + 1) % convex.size
            val p = convex[j]
            val edgeX = p.x - v.x
            val edgeY = p.y - v.y

            val diffX = -v.x
            val diffY = -v.y

            val dot = -edgeY * diffX + edgeX * diffY

            if (dot >= 0) return false
        }
        return true
    }

    fun cleanMatrices() {
        val dirty = this.dirtyMatrices
        for (i in dirty.indices) {
            if (dirty[i]) {
                this.updateMatrix(i - MATRIX_OFFSET)
            }
        }
    }

    open fun updateMatrix(matrixId: Int) {
        val properties = properties
        dirtyMatrices[matrixId + MATRIX_OFFSET] = false

        if (matrixId == colorMatrix) {
            this.cachedHexColor = this.color.toGuiHex()
        }

        if (matrixId >= 0) {
            val matrices = matrices
            var matrix = matrices[matrixId]
            if (matrix == null) {
                matrix = Matrix4f()
                matrices[matrixId] = matrix
            } else {
                matrix.setIdentity()
            }

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
        }
    }

    fun markDirty(matrix: Int) {
        dirtyMatrices[matrix + MATRIX_OFFSET] = true

        if (matrix == sizeMatrix) {
            markDirty(originMatrix)
        }
    }

    open fun transformAndRender() {
        if (!this.enabled) return

        this.beforeTransform?.invoke()
        GlStateManager.pushMatrix()
        this.applyTransformations()

        this.beforeRender?.invoke()
        this.render()
        this.afterRender?.invoke()

        GlStateManager.popMatrix()
        this.afterTransform?.invoke()
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

    abstract fun render()
}
