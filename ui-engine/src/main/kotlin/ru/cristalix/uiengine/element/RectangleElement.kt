package ru.cristalix.uiengine.element

import dev.xdark.clientapi.opengl.GlStateManager
import dev.xdark.clientapi.resource.ResourceLocation
import org.lwjgl.util.vector.Matrix4f
import org.lwjgl.util.vector.Vector4f
import ru.cristalix.uiengine.UIEngine
import ru.cristalix.uiengine.utility.*

open class RectangleElement : AbstractElement(), Parent {

    var textureLocation: ResourceLocation? = null

    var textureFrom: V2
        get() = ProxiedV2(Property.TextureX.ordinal, this)
        set(value) = value.write(textureFrom)

    var textureSize: V2
        get() = ProxiedV2(Property.TextureWidth.ordinal, this)
        set(value) = value.write(textureSize)

    override val children: MutableList<AbstractElement> = ArrayList()

    override var context: Context?
        get() = super.context
        set(value) {
            super.context = value
            for (child in children) {
                child.context = value
            }
        }

    init {
        this.textureSize = V2(1.0, 1.0)
    }

    override fun removeChild(vararg elements: AbstractElement) {
        this.children.removeAll(elements)
    }

    override fun addChild(vararg elements: AbstractElement) {

        for (element in elements) {
            element.changeProperty(Property.ParentSizeX.ordinal, this.properties[Property.SizeX])
            element.changeProperty(Property.ParentSizeY.ordinal, this.properties[Property.SizeY])
            element.context = this.context
            if (element is RectangleElement) {
                // ToDo: Abstract contextful parents
            }
            this.children.add(element)
        }


    }

    fun captureChildren(): Pair<V3, V3> {

        for (element in children) {

            val size = element.size

            val width = size.x.toFloat()
            val height = size.y.toFloat()

            val bounds = arrayOf(
                Vector4f(0f, 0f, 0f, 1f),
                Vector4f(width, 0f, 0f, 1f),
                Vector4f(width, height, 0f, 1f),
                Vector4f(0f, height, 0f, 1f)
            )

            for (matrix in element.matrices) {
                for (bound in bounds) {
                    Matrix4f.transform(matrix, bound, bound)
                }
            }


        }

        TODO()

    }

    override fun updateMatrix(matrixId: Int) {

        if (matrixId == sizeMatrix) {
            for (child in children) {
                val childProperties = child.properties
                childProperties[Property.ParentSizeX] = properties[Property.SizeX]
                childProperties[Property.ParentSizeY] = properties[Property.SizeY]
                childProperties[Property.ParentSizeZ] = properties[Property.SizeZ]
                child.updateMatrix(alignMatrix)
            }
        }

        super.updateMatrix(matrixId)

    }

    override fun render() {

        val api = UIEngine.clientApi
        GlStateManager.enableBlend()

        if (textureLocation != null) {

            api.renderEngine().bindTexture(textureLocation)

            GlStateManager.enableAlpha()
            val color = color
            GlStateManager.color(color.red / 255f, color.green / 255f, color.blue / 255f, color.alpha.toFloat())

            val precision = 0x4000_0000

            api.overlayRenderer().drawScaledCustomSizeModalRect(
                0, 0,
                (properties[Property.TextureX] * precision).toFloat(),
                (properties[Property.TextureY] * precision).toFloat(),
                (properties[Property.TextureWidth] * precision).toInt(),
                (properties[Property.TextureHeight] * precision).toInt(),
                properties[Property.SizeX].toInt(),
                properties[Property.SizeY].toInt(),
                precision.toFloat(),
                precision.toFloat()
            )

            GlStateManager.color(1f, 1f, 1f, 1f)

        } else {
            api.overlayRenderer().drawRect(
                0, 0,
                properties[Property.SizeX].toInt(),
                properties[Property.SizeY].toInt(),
                this.cachedHexColor
            )
        }

        children.forEach(AbstractElement::transformAndRender)

    }

}