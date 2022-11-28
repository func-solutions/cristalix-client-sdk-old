package ru.cristalix.uiengine.element

import dev.xdark.clientapi.opengl.GlStateManager
import org.lwjgl.util.vector.Matrix4f
import ru.cristalix.uiengine.UIEngine
import ru.cristalix.uiengine.utility.V2
import ru.cristalix.uiengine.utility.V3
class Context3D(offset: V3) : Context() {

    init {
        this.offset = offset
        this.scale = V3(1.0 / 16, 1.0 / 16, 1.0 / 16)
    }

    @JvmField
    var renderDistance: Double = 64.0
    private val mouse = V2()

    override fun applyTransformations() {
        val mc = UIEngine.clientApi.minecraft()
        val entity = mc.renderViewEntity
        val pt = mc.timer.renderPartialTicks
        val prevX = entity.prevX
        val prevY = entity.prevY
        val prevZ = entity.prevZ
        GlStateManager.translate(
            -(entity.x - prevX) * pt - prevX,
            -(entity.y - prevY) * pt - prevY,
            -(entity.z - prevZ) * pt - prevZ,
        )
//        GlStateManager.scale(1.0, -1.0, -1.0)
        super.applyTransformations()
        GlStateManager.rotate(180f, 1f, 0f, 0f)
    }

    fun copy(): Context3D {
        val v = Context3D(this.offset)
        this.matrices.forEachIndexed { index, matrix4f -> v.matrices[index] = matrix4f?.let { Matrix4f(it) } }
        System.arraycopy(this.properties, 0, v.properties, 0, this.properties.size)
        v.onClick = this.onClick
        v.onHover = this.onHover
        v.enabled = this.enabled
        v.cachedHexColor = this.cachedHexColor
        v.beforeTransform = this.beforeTransform
        v.beforeRender = this.beforeRender
        v.afterTransform = this.afterTransform
        v.afterRender = this.afterRender
        return v
    }

    override fun transformViewportAndMouse(): V2 {
        /*
        val entity = UIEngine.clientApi.minecraft().renderViewEntity
        if (V3(entity.x, entity.y, entity.z).distanceSquared3(offset) > renderDistance * renderDistance)
            return null
//        GlStateManager.translate()
        //return V2()
         */
        return mouse
    }
}