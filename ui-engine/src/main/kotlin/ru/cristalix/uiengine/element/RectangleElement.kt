package ru.cristalix.uiengine.element

import dev.xdark.clientapi.opengl.GlStateManager
import dev.xdark.clientapi.resource.ResourceLocation
import org.lwjgl.opengl.GL11
import org.lwjgl.util.vector.Matrix4f
import org.lwjgl.util.vector.Vector4f
import ru.cristalix.uiengine.UIEngine
import ru.cristalix.uiengine.utility.*

open class RectangleElement : AbstractElement(), Parent {

    var textureLocation: ResourceLocation? = null

    var textureFrom: V2 = ProxiedV2(Property.TextureX.ordinal, this)
        set(value) = value.write(field)

    var textureSize: V2 = ProxiedV2(Property.TextureWidth.ordinal, this)
        set(value) = value.write(field)

    var mask: Boolean = false

    override val children: MutableList<AbstractElement> = ArrayList()

    override var context: Context?
        get() = super.context
        set(value) {
            super.context = value
            if (children.isNotEmpty()) {
                for (child in children) {
                    child.context = value
                }
            }
        }

    init {
        this.textureSize = V2(1.0, 1.0)
    }

    override fun removeChild(vararg elements: AbstractElement) {
        this.children.removeAll(elements)
    }

    override fun addChild(vararg elements: AbstractElement) {
        val x = properties[Property.SizeX]
        val y = properties[Property.SizeY]
        for (element in elements) {
            element.changeProperty(Property.ParentSizeX.ordinal, x)
            element.changeProperty(Property.ParentSizeY.ordinal, y)
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
            if (children.isNotEmpty()) {
                val properties = properties
                val x = properties[Property.SizeX]
                val y = properties[Property.SizeY]
                val z = properties[Property.SizeZ]
                for (child in children) {
                    val childProperties = child.properties
                    childProperties[Property.ParentSizeX] = x
                    childProperties[Property.ParentSizeY] = y
                    childProperties[Property.ParentSizeZ] = z
                    child.updateMatrix(alignMatrix)
                }
            }
        }

        super.updateMatrix(matrixId)

    }

    override fun render() {
        val api = UIEngine.clientApi
        GlStateManager.enableBlend()

        val mask = mask

        if (mask) {
            GlStateManager.enableStencil()
            GlStateManager.stencilFunc(GL11.GL_GREATER, context!!.stencilPos, 0xFF)
            GlStateManager.stencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_INCR)
            GlStateManager.disableDepth()
            GlStateManager.disableAlpha()
            api.overlayRenderer().drawRect(
                0, 0,
                properties[Property.SizeX].toInt(),
                properties[Property.SizeY].toInt(),
                0
            )
            GlStateManager.enableDepth()

            context!!.stencilPos++
            GlStateManager.stencilFunc(GL11.GL_GREATER, context!!.stencilPos, 0xFF)
            GlStateManager.stencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_KEEP)
        }

        val properties = properties
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

        if (children.isNotEmpty()) {
            for (child in children) {
                child.transformAndRender()
            }
        }

        if (mask) {
            context!!.stencilPos--
            GlStateManager.stencilFunc(GL11.GL_ALWAYS, 0, 0xFF)
            GlStateManager.stencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_DECR)
            GlStateManager.disableDepth()
            GlStateManager.disableAlpha()
            api.overlayRenderer().drawRect(
                0, 0,
                properties[Property.SizeX].toInt(),
                properties[Property.SizeY].toInt(),
                0
            )
            GlStateManager.enableDepth()
            GlStateManager.stencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_KEEP)
        }
    }

}