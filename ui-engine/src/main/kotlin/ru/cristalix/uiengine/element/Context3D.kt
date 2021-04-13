package ru.cristalix.uiengine.element

import dev.xdark.clientapi.opengl.GlStateManager
import ru.cristalix.uiengine.UIEngine
import ru.cristalix.uiengine.utility.V2
import ru.cristalix.uiengine.utility.V3

class Context3D(offset: V3) : Context() {

    init {
        this.offset = offset
        this.scale = V3(1.0 / 16, 1.0 / 16, 1.0 / 16)
    }

    var renderDistance: Double = 64.0
    private val mouse = V2()

    override fun applyTransformations() {
        val mc = UIEngine.clientApi.minecraft()
        val entity = mc.renderViewEntity
        val pt = mc.timer.renderPartialTicks
        GlStateManager.translate(
            -(entity.x - entity.prevX) * pt - entity.prevX,
            -(entity.y - entity.prevY) * pt - entity.prevY,
            -(entity.z - entity.prevZ) * pt - entity.prevZ,
        )
//        GlStateManager.scale(1.0, -1.0, -1.0)
        super.applyTransformations()
        GlStateManager.rotate(180f, 1f, 0f, 0f)
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