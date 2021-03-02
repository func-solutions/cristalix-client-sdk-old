package ru.cristalix.uiengine.element;

import dev.xdark.clientapi.opengl.GlStateManager
import dev.xdark.clientapi.render.DefaultVertexFormats
import dev.xdark.clientapi.resource.ResourceLocation
import org.lwjgl.opengl.GL11
import ru.cristalix.uiengine.UIEngine
import ru.cristalix.uiengine.UIEngine.clientApi
import ru.cristalix.uiengine.utility.*

class CuboidElement : AbstractElement(), Parent {

    var textureLocation: ResourceLocation? = null

    var textureFrom: V2
        get() = ProxiedV2(Property.TextureX.ordinal, this)
        set(value) = value.write(textureFrom)

    var textureSize: V2
        get() = ProxiedV2(Property.TextureWidth.ordinal, this)
        set(value) = value.write(textureSize)

    override val children: MutableList<AbstractElement> = ArrayList()

    init {
        color = WHITE
    }

    override fun removeChild(vararg elements: AbstractElement) {
        this.children.removeAll(elements)
    }

    override fun addChild(vararg elements: AbstractElement) {

        for (element in elements) {
            element.changeProperty(Property.ParentSizeX.ordinal, this.properties[Property.SizeX])
            element.changeProperty(Property.ParentSizeY.ordinal, this.properties[Property.SizeY])
            element.changeProperty(Property.ParentSizeZ.ordinal, this.properties[Property.SizeZ])
            element.context = this.context
            this.children.add(element)
        }


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

        val tessellator = UIEngine.clientApi.tessellator()
        val bufferBuilder = tessellator.bufferBuilder

        val sx = size.x
        val sy = size.y
        val sz = size.z




        if (textureLocation != null) {

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

            bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX)

            // Front
            bufferBuilder.pos(0.0, sy, 0.0).tex(u2, v3).endVertex()
            bufferBuilder.pos(sx, sy, 0.0).tex(u3, v3).endVertex()
            bufferBuilder.pos(sx, 0.0, 0.0).tex(u3, v2).endVertex()
            bufferBuilder.pos(0.0, 0.0, 0.0).tex(u2, v2).endVertex()

            // Left
            bufferBuilder.pos(0.0, sy, 0.0).tex(u2, v3).endVertex()
            bufferBuilder.pos(0.0, 0.0, 0.0).tex(u2, v2).endVertex()
            bufferBuilder.pos(0.0, 0.0, sz).tex(u1, v2).endVertex()
            bufferBuilder.pos(0.0, sy, sz).tex(u1, v3).endVertex()

            // Right
            bufferBuilder.pos(sx, sy, 0.0).tex(u3, v3).endVertex()
            bufferBuilder.pos(sx, sy, sz).tex(u4, v3).endVertex()
            bufferBuilder.pos(sx, 0.0, sz).tex(u4, v2).endVertex()
            bufferBuilder.pos(sx, 0.0, 0.0).tex(u3, v2).endVertex()

            // Back
            bufferBuilder.pos(0.0, sy, sz).tex(u6, v3).endVertex()
            bufferBuilder.pos(0.0, 0.0, sz).tex(u6, v2).endVertex()
            bufferBuilder.pos(sx, 0.0, sz).tex(u4, v2).endVertex()
            bufferBuilder.pos(sx, sy, sz).tex(u4, v3).endVertex()

            // Top
            bufferBuilder.pos(0.0, 0.0, 0.0).tex(u2, v2).endVertex()
            bufferBuilder.pos(sx, 0.0, 0.0).tex(u3, v2).endVertex()
            bufferBuilder.pos(sx, 0.0, sz).tex(u3, v1).endVertex()
            bufferBuilder.pos(0.0, 0.0, sz).tex(u2, v1).endVertex()

            // Bottom
            bufferBuilder.pos(0.0, sy, sz).tex(u3, v1).endVertex()
            bufferBuilder.pos(sx, sy, sz).tex(u5, v1).endVertex()
            bufferBuilder.pos(sx, sy, 0.0).tex(u5, v2).endVertex()
            bufferBuilder.pos(0.0, sy, 0.0).tex(u3, v2).endVertex()

            tessellator.draw()

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
