package ru.cristalix.uiengine.element

import dev.xdark.clientapi.opengl.GlStateManager
import dev.xdark.clientapi.resource.ResourceLocation
import ru.cristalix.uiengine.UIEngine
import ru.cristalix.uiengine.utility.*

open class Rectangle : Element {

    var size: V2
        get() = ProxiedV2(Property.SizeX.ordinal, this)
        set(value) = value.write(size)

    var textureLocation: ResourceLocation?

    var textureFrom: V2
        get() = ProxiedV2(Property.TextureX.ordinal, this)
        set(value) = value.write(textureFrom)

    var textureSize: V2
        get() = ProxiedV2(Property.TextureWidth.ordinal, this)
        set(value) = value.write(textureSize)

    val children: MutableList<Element>

    constructor(
        scale: V3 = V3(1.0, 1.0, 1.0),
        offset: V3 = V3(),
        align: V3 = V3(),
        origin: V3 = V3(),
        color: Color = TRANSPARENT,
        rotation: Rotation = Rotation(),
        enabled: Boolean = true,
        onClick: ClickHandler = null,
        onHover: HoverHandler = null,
        size: V2 = V2(),
        textureLocation: ResourceLocation? = null,
        textureFrom: V2 = V2(),
        textureSize: V2 = V2(1.0, 1.0),
        children: Collection<Element> = ArrayList()
    ) : super(scale, offset, align, origin, color, rotation, enabled, onClick, onHover) {
        this.size = size
        this.textureLocation = textureLocation
        this.textureFrom = textureFrom
        this.textureSize = textureSize
        this.children = if (children is MutableList) children else ArrayList(children)
    }

    fun removeChild(vararg elements: Element) {
        this.children.removeAll(elements)
    }

    fun addChild(vararg elements: Element) {

        for (element in elements) {
            element.changeProperty(Property.ParentSizeX.ordinal, this.properties[Property.SizeX])
            element.changeProperty(Property.ParentSizeY.ordinal, this.properties[Property.SizeY])
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

        if (textureLocation != null) {
            GlStateManager.enableBlend()

            api.renderEngine().bindTexture(textureLocation)

            GlStateManager.enableAlpha()

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

        } else {
            api.overlayRenderer().drawRect(
                0, 0,
                properties[Property.SizeX].toInt(),
                properties[Property.SizeY].toInt(),
                this.cachedHexColor
            )
        }

        children.forEach(Element::transformAndRender)

    }

}