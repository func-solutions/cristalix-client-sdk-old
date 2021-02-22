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


    override fun applyTransformations() {
        val player = UIEngine.clientApi.minecraft().player
        val pt = UIEngine.clientApi.minecraft().timer.renderPartialTicks
        GlStateManager.translate(
            -(player.x - player.prevX) * pt - player.prevX,
            -(player.y - player.prevY) * pt - player.prevY,
            -(player.z - player.prevZ) * pt - player.prevZ,
        )
        GlStateManager.scale(1.0, -1.0, -1.0)
        super.applyTransformations()
    }

    override fun transformViewportAndMouse(): V2? {
        val player = UIEngine.clientApi.minecraft().player

        if (V3(player.x, player.y, player.z).distanceSquared3(offset) > renderDistance * renderDistance)
            return null

//        GlStateManager.translate()

        return V2()
    }

}