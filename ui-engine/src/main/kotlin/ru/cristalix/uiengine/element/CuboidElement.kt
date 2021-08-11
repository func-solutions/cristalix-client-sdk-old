package ru.cristalix.uiengine.element;

import dev.xdark.clientapi.opengl.GlStateManager
import dev.xdark.clientapi.render.DefaultVertexFormats
import dev.xdark.clientapi.resource.ResourceLocation
import org.lwjgl.opengl.GL11
import ru.cristalix.uiengine.UIEngine
import ru.cristalix.uiengine.UIEngine.clientApi
import ru.cristalix.uiengine.utility.*

class CuboidElement : AbstractElement(), Parent {

    @JvmField var textureLocation: ResourceLocation? = null

    var textureFrom: V2 = ProxiedV2(Property.TextureX.ordinal, this)
        set(value) = value.write(field)

    var textureSize: V2 = ProxiedV2(Property.TextureWidth.ordinal, this)
        set(value) = value.write(field)

    override val children: MutableList<AbstractElement> = ArrayList()

    init {
        color = WHITE
    }

    override fun removeChild(element: AbstractElement) {
        this.children.remove(element)
    }

    override fun removeChild(vararg elements: AbstractElement) {
        this.children.removeAll(elements)
    }

    override fun addChild(element: AbstractElement) {
        val children = this.children
        val properties = properties
        val x = properties[Property.SizeX]
        val y = properties[Property.SizeY]
        val z = properties[Property.SizeZ]
        element.changeProperty(Property.ParentSizeX.ordinal, x)
        element.changeProperty(Property.ParentSizeY.ordinal, y)
        element.changeProperty(Property.ParentSizeZ.ordinal, z)
        children.add(element)
    }

    override fun addChild(vararg elements: AbstractElement) {
        val children = this.children
        val properties = properties
        val x = properties[Property.SizeX]
        val y = properties[Property.SizeY]
        val z = properties[Property.SizeZ]
        for (element in elements) {
            element.changeProperty(Property.ParentSizeX.ordinal, x)
            element.changeProperty(Property.ParentSizeY.ordinal, y)
            element.changeProperty(Property.ParentSizeZ.ordinal, z)
            children.add(element)
        }
    }

    override fun updateMatrix(matrixId: Int) {
        if (matrixId == sizeMatrix) {
            val children = children
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

        val api = clientApi
        GlStateManager.enableBlend()

        val tessellator = api.tessellator()
        val bufferBuilder = tessellator.bufferBuilder

        val size = size
        val sx = size.x
        val sy = size.y
        val sz = size.z

        val textureLocation = textureLocation
        if (textureLocation != null) {
            val textureSize = textureSize
            val textureFrom =  textureFrom
            val uSize = textureSize.x
            val vSize = textureSize.y
            val u1 = textureFrom.x / uSize
            val u2 = u1 + sz / uSize
            val u3 = u2 + sx / uSize
            val u4 = u3 + sz / uSize
            val u5 = u3 + sx / uSize
            val u6 = u4 + sx / uSize

            val v1 = textureFrom.y / vSize
            val v2 = v1 + sz / vSize
            val v3 = v2 + sy / vSize

            val color = color
            GlStateManager.color(color.red / 255f, color.green / 255f, color.blue / 255f, color.alpha.toFloat())
//            GlStateManager.color(1f, 1f, 1f, 1f)

            GlStateManager.enableTexture2D()
            GlStateManager.enableAlpha()
            api.renderEngine().bindTexture(textureLocation)

            bufferBuilder
                .begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_NORMAL)

            // Front
            bufferBuilder.pos(0.0, sy, 0.0).tex(u2, v3).normal(0f, 0f, -1f).endVertex()
            bufferBuilder.pos(sx, sy, 0.0).tex(u3, v3).normal(0f, 0f, -1f).endVertex()
            bufferBuilder.pos(sx, 0.0, 0.0).tex(u3, v2).normal(0f, 0f, -1f).endVertex()
            bufferBuilder.pos(0.0, 0.0, 0.0).tex(u2, v2).normal(0f, 0f, -1f).endVertex()

            // Left
            bufferBuilder.pos(0.0, sy, 0.0).tex(u2, v3).normal(-1f, 0f, 0f).endVertex()
            bufferBuilder.pos(0.0, 0.0, 0.0).tex(u2, v2).normal(-1f, 0f, 0f).endVertex()
            bufferBuilder.pos(0.0, 0.0, sz).tex(u1, v2).normal(-1f, 0f, 0f).endVertex()
            bufferBuilder.pos(0.0, sy, sz).tex(u1, v3).normal(-1f, 0f, 0f).endVertex()

            // Right
            bufferBuilder.pos(sx, sy, 0.0).tex(u3, v3).normal(1f, 0f, 0f).endVertex()
            bufferBuilder.pos(sx, sy, sz).tex(u4, v3).normal(1f, 0f, 0f).endVertex()
            bufferBuilder.pos(sx, 0.0, sz).tex(u4, v2).normal(1f, 0f, 0f).endVertex()
            bufferBuilder.pos(sx, 0.0, 0.0).tex(u3, v2).normal(1f, 0f, 0f).endVertex()

            // Back
            bufferBuilder.pos(0.0, sy, sz).tex(u6, v3).normal(0f, 0f, 1f).endVertex()
            bufferBuilder.pos(0.0, 0.0, sz).tex(u6, v2).normal(0f, 0f, 1f).endVertex()
            bufferBuilder.pos(sx, 0.0, sz).tex(u4, v2).normal(0f, 0f, 1f).endVertex()
            bufferBuilder.pos(sx, sy, sz).tex(u4, v3).normal(0f, 0f, 1f).endVertex()

            // Top
            bufferBuilder.pos(0.0, 0.0, 0.0).tex(u2, v2).normal(0f, -1f, 0f).endVertex()
            bufferBuilder.pos(sx, 0.0, 0.0).tex(u3, v2).normal(0f, -1f, 0f).endVertex()
            bufferBuilder.pos(sx, 0.0, sz).tex(u3, v1).normal(0f, -1f, 0f).endVertex()
            bufferBuilder.pos(0.0, 0.0, sz).tex(u2, v1).normal(0f, -1f, 0f).endVertex()

            // Bottom
            bufferBuilder.pos(0.0, sy, sz).tex(u3, v1).normal(0f, 1f, 0f).endVertex()
            bufferBuilder.pos(sx, sy, sz).tex(u5, v1).normal(0f, 1f, 0f).endVertex()
            bufferBuilder.pos(sx, sy, 0.0).tex(u5, v2).normal(0f, 1f, 0f).endVertex()
            bufferBuilder.pos(0.0, sy, 0.0).tex(u3, v2).normal(0f, 1f, 0f).endVertex()

            tessellator.draw()

            GlStateManager.color(1f, 1f, 1f, 1f)

        } else {
            val properties = properties
            api.overlayRenderer().drawRect(
                0, 0,
                properties[Property.SizeX].toInt(),
                properties[Property.SizeY].toInt(),
                this.cachedHexColor
            )
        }

        children.forEach {
            it.lastParent = this
            it.transformAndRender()
        }

    }


}
