package ru.cristalix.uiengine.element

import dev.xdark.clientapi.opengl.GlStateManager
import dev.xdark.clientapi.render.BufferBuilder
import dev.xdark.clientapi.render.DefaultVertexFormats
import dev.xdark.clientapi.render.Tessellator
import dev.xdark.clientapi.resource.ResourceLocation
import org.lwjgl.opengl.GL11
import org.lwjgl.util.vector.Matrix4f
import org.lwjgl.util.vector.Vector4f
import ru.cristalix.uiengine.UIEngine
import ru.cristalix.uiengine.utility.Property
import ru.cristalix.uiengine.utility.ProxiedV2
import ru.cristalix.uiengine.utility.V2
import ru.cristalix.uiengine.utility.V3
import ru.cristalix.uiengine.utility.alignMatrix
import ru.cristalix.uiengine.utility.get
import ru.cristalix.uiengine.utility.set
import ru.cristalix.uiengine.utility.sizeMatrix
import kotlin.math.abs

@JvmField
var depth = 0
@JvmField
var debug = false

@JvmField
val debugColors = arrayOf(
    floatArrayOf(1.0f, 0.1f, 0.1f, 1.0f),
    floatArrayOf(1.0f, 1.0f, 0.1f, 1.0f),
    floatArrayOf(0.1f, 1.0f, 0.1f, 1.0f),
    floatArrayOf(0.1f, 1.0f, 1.0f, 1.0f),
    floatArrayOf(0.1f, 0.1f, 1.0f, 1.0f),
    floatArrayOf(1.0f, 0.1f, 1.0f, 1.0f),
)

open class RectangleElement : AbstractElement(), Parent {

    @JvmField
    var textureLocation: ResourceLocation? = null

    var textureFrom: V2 = ProxiedV2(Property.TextureX.ordinal, this)
        set(value) = value.write(field)

    var textureSize: V2 = ProxiedV2(Property.TextureWidth.ordinal, this)
        set(value) = value.write(field)

    @JvmField
    var mask: Boolean = false

    @JvmField
    var layering: Boolean = false

    override val children: MutableList<AbstractElement> = ArrayList()

    init {
        this.textureSize = V2(1.0, 1.0)
    }

    override fun removeChild(element: AbstractElement) {
        this.children.remove(element)
    }

    override fun removeChild(vararg elements: AbstractElement) {
        this.children.removeAll(elements.asList())
    }

    override fun addChild(element: AbstractElement) {
        val properties = properties
        val x = properties[Property.SizeX]
        val y = properties[Property.SizeY]
        element.changeProperty(Property.ParentSizeX.ordinal, x)
        element.changeProperty(Property.ParentSizeY.ordinal, y)
//            if (element is RectangleElement) {
        // ToDo: Abstract contextful parents
//            }
        element.lastParent = this
        this.children.add(element)
    }

    override fun syncChild(vararg elements: AbstractElement) {
        val properties = properties
        val x = properties[Property.SizeX]
        val y = properties[Property.SizeY]
        for (element in elements) {
            element.changeProperty(Property.ParentSizeX.ordinal, x)
            element.changeProperty(Property.ParentSizeY.ordinal, y)
            element.lastParent = this
        }
    }

    override fun addChild(vararg elements: AbstractElement) {
        val properties = properties
        val x = properties[Property.SizeX]
        val y = properties[Property.SizeY]
        for (element in elements) {
            element.changeProperty(Property.ParentSizeX.ordinal, x)
            element.changeProperty(Property.ParentSizeY.ordinal, y)
//            if (element is RectangleElement) {
            // ToDo: Abstract contextful parents
//            }
            element.lastParent = this
            this.children.add(element)
        }
    }

    override fun updateInteractiveState() {

        super.updateInteractiveState()

        if (!enabled) return

        val children = children
        if (children.isEmpty()) return

        for (child in children) {
            child.updateInteractiveState()
            interactive = interactive || child.interactive
        }
    }

    override fun updateHoverState(mouseMatrix: Matrix4f, mouseVector: Vector4f) {
        super.updateHoverState(mouseMatrix, mouseVector)

        val children = children
        if (children.isEmpty()) return

        for (child in children) {
            if (!child.interactive) continue

            val matrix = Matrix4f()
            matrix.load(mouseMatrix)
            child.updateHoverState(matrix, mouseVector)
        }
    }

    override fun getForemostHovered(): AbstractElement? {
        val children = children
        if (enabled && interactive) {
            for (i in children.size - 1 downTo 0) {
                children[i].getForemostHovered()?.let { return it }
            }
        }
        return super.getForemostHovered()
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
        val engine = UIEngine
        val api = engine.clientApi
        GlStateManager.enableBlend()

        val mask = mask
        val properties = properties

        val children = children
        val childrenAmount = children.size
        if (mask) {
            GlStateManager.enableDepth()
            GlStateManager.translate(0f, 0f, 0.97f)
        }

        val color = color

        if (debug) {
            depth++
        }

        if (color.alpha > 0 || mask) {
            val textureLocation = textureLocation
            if (textureLocation != null) {
                api.renderEngine().bindTexture(textureLocation)
                GlStateManager.enableAlpha()
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
                val tessellator: Tessellator = engine.clientApi.tessellator()
                val worldrenderer: BufferBuilder = tessellator.bufferBuilder
                GlStateManager.enableBlend()
                GlStateManager.disableTexture2D()
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)

                GlStateManager.color(color.red / 255f, color.green / 255f, color.blue / 255f, color.alpha.toFloat())

                val size = size
                worldrenderer.begin(7, DefaultVertexFormats.POSITION)
                worldrenderer.pos(0.0, size.y, 0.0).endVertex()
                worldrenderer.pos(size.x, size.y, 0.0).endVertex()
                worldrenderer.pos(size.x, 0.0, 0.0).endVertex()
                worldrenderer.pos(0.0, 0.0, 0.0).endVertex()
                tessellator.draw()
                GlStateManager.enableTexture2D()
                GlStateManager.disableBlend()

//            api.overlayRenderer().drawRect(
//                0, 0,
//                properties[Property.SizeX].toInt(),
//                properties[Property.SizeY].toInt(),
//                this.cachedHexColor
//            )
            }
        }

        if (debug) {
            val d = debugColors[abs(depth) % debugColors.size]
            GlStateManager.color(d[0], d[1], d[2], 0.6f)

            GlStateManager.enableBlend()
            GlStateManager.disableTexture2D()
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)

            val tessellator: Tessellator = UIEngine.clientApi.tessellator()
            val worldrenderer: BufferBuilder = tessellator.bufferBuilder
            GL11.glLineWidth(1.5f)
            worldrenderer.begin(GL11.GL_LINE_LOOP, DefaultVertexFormats.POSITION)
            worldrenderer.pos(0.0, size.y, 0.0).endVertex()
            worldrenderer.pos(size.x, size.y, 0.0).endVertex()
            worldrenderer.pos(size.x, 0.0, 0.0).endVertex()
            worldrenderer.pos(0.0, 0.0, 0.0).endVertex()
            tessellator.draw()

            GL11.glPointSize(4.0f)
            worldrenderer.begin(GL11.GL_POINTS, DefaultVertexFormats.POSITION)
            worldrenderer.pos(size.x * origin.x, size.y * origin.y, 0.0).endVertex()
            tessellator.draw()
            GlStateManager.enableTexture2D()
            GlStateManager.disableBlend()
        }

        if (childrenAmount > 0) {
            if (mask) {
//                GlStateManager.translate(0f, 0f, -1f)
                GlStateManager.depthFunc(GL11.GL_EQUAL)
                GlStateManager.depthMask(false)
            }
            val offset = 1.02f / (childrenAmount + 1)
            for (child in children) {
                if (layering) {
                    GlStateManager.translate(0f, 0f, offset)
                }
                child.lastParent = this
                child.transformAndRender()
            }
            if (mask) {
                GlStateManager.depthMask(true)
                GlStateManager.depthFunc(GL11.GL_LEQUAL)
            }
        }

        if (debug) {
            depth--
        }

        if (mask) {
            GlStateManager.disableDepth()
        }
    }
}
