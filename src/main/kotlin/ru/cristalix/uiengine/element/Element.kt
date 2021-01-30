package ru.cristalix.uiengine.element

import org.lwjgl.util.vector.Matrix4f
import org.lwjgl.util.vector.Vector3f
import ru.cristalix.uiengine.element.Property.*
import ru.cristalix.uiengine.utility.Color
import ru.cristalix.uiengine.utility.Easing
import ru.cristalix.uiengine.utility.Easings
import ru.cristalix.uiengine.utility.V3

internal data class Animation(val element: Element, val property: Property) {

    var startedTime: Long = 0
    var duration: Long = 0
    var startValue: Double = 0.0
    var targetValue: Double = 0.0
    var lastValue: Double = 0.0
    var easing: Easing = Easings.NONE

    init {
        this.lastValue = element.properties[property]
        this.targetValue = this.lastValue
    }

    fun newTarget(value: Double, duration: Long, easing: Easing) {

        this.startValue = this.lastValue
        this.targetValue = value
        this.startedTime = System.currentTimeMillis()
        this.duration = duration
        this.easing = easing

    }

    fun update(time: Long): Boolean {
        var part = (time - this.startedTime).toDouble() / this.duration
        val alive = part <= 1.0;
        val value: Double
        if (alive) {
            part = this.easing(part)
            value = startValue + (targetValue - startValue) * part
        } else {
            this.startedTime = 0
            value = targetValue
        }
        this.lastValue = value
        this.element.properties[property] = value
        return alive;
    }

}

private var runningAnimations: MutableList<Animation> = ArrayList()

data class AnimationContext(
    val duration: Number,
    val easing: Easing
)

open class Element {

    internal val properties: DoubleArray = DoubleArray(Property.values().size)

    internal val matrices: Array<Matrix4f?> = arrayOfNulls(matrixFields)

    private var dirtyMatrices: MutableList<Int>? = null

    private var animationContext: AnimationContext? = null

    internal var cachedHexColor: Int = 0

    internal fun changeProperty(index: Int, value: Number) {

        val property = Property.values()[index]

        val animationContext = this.animationContext
        if (animationContext != null) {
            var animation: Animation? = null
            for (existing in runningAnimations) {
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
            runningAnimations.add(animation)
            return
        }
        // stdout.println('setting ' + propertyId + ' to ' + value);
        this.properties[index] = value.toDouble()
        for (matrix in property.matrixInfluence) {
            this.markDirty(matrix)
        }

    }

    fun cleanMatrices() {
        val dirty = this.dirtyMatrices ?: return
        for (matrix in dirty) {
            this.updateMatrix(matrix)
        }
        this.dirtyMatrices = null
    }

    private fun vec(x: Number, y: Number, z: Number): Vector3f {
        return Vector3f(x.toFloat(), y.toFloat(), z.toFloat())
    }

    public fun updateMatrix(matrixId: Int) {
        val properties = this.properties
        dirtyMatrices?.remove(matrixId)


        if (matrixId == colorMatrix) {
            this.cachedHexColor = this.color.toGuiHex()
        }

        if (matrixId >= 0) {
            val matrix: Matrix4f = Matrix4f().setIdentity() as Matrix4f


            when (matrixId) {
                alignMatrix -> matrix.translate(
                    vec(
                        properties[AlignX] * properties[ParentSizeX],
                        properties[AlignY] * properties[ParentSizeY],
                        properties[AlignZ] * properties[ParentSizeZ]
                    )
                )
                rotationMatrix -> matrix.rotate(
                    properties[RotationAngle].toFloat(), vec(
                        properties[RotationX],
                        properties[RotationY],
                        properties[RotationZ]
                    )
                )
                offsetMatrix -> matrix.translate(
                    vec(
                        properties[OffsetX],
                        properties[OffsetY],
                        properties[OffsetZ]
                    )
                )
                scaleMatrix -> matrix.scale(
                    vec(
                        properties[ScaleX],
                        properties[ScaleY],
                        properties[ScaleZ]
                    )
                )
                originMatrix -> matrix.translate(
                    vec(
                        -properties[OriginX] * properties[SizeX],
                        -properties[OriginY] * properties[SizeY],
                        -properties[OriginZ] * properties[SizeZ]
                    )
                )
            }
            this.matrices[matrixId] = matrix
        }
    }

    fun markDirty(matrix: Int) {
        val matrices = dirtyMatrices ?: ArrayList()
        if (!matrices.contains(matrix)) matrices.add(matrix)
        dirtyMatrices = matrices
    }

    var offset: V3
        get() = ProxiedV3(OffsetX.ordinal, this)
        set(value) = value.write(offset)

    var scale: V3
        get() = ProxiedV3(ScaleX.ordinal, this)
        set(value) = value.write(scale)

    var align: V3
        get() = ProxiedV3(AlignX.ordinal, this)
        set(value) = value.write(align)

    var origin: V3
        get() = ProxiedV3(OriginX.ordinal, this)
        set(value) = value.write(origin)

    var color: Color
        get() = ProxiedColor(this)
        set(value) = value.write(color)

    fun animate(
        duration: Number,
        easing: Easing? = null,
        action: Element.() -> Unit
    ): Element {
        // do something
        return getThis()
    }

    fun update(action: Element.() -> Unit): Element {
        TODO("Not yet implemented")
        return getThis()
    }

    infix fun then(action: () -> Unit): Element {
        TODO("Not yet implemented")
        return getThis()
    }

    fun then(delay: Number, action: () -> Unit): Element {
        TODO("Not yet implemented")
        return getThis()
    }

    @Suppress("UNCHECKED_CAST")
    fun getThis() = this as Element

}

