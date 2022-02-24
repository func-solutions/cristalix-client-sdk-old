package ru.cristalix.uiengine.element

import dev.xdark.clientapi.opengl.GlStateManager
import dev.xdark.clientapi.render.BufferBuilder
import dev.xdark.clientapi.render.DefaultVertexFormats
import org.lwjgl.opengl.GL11
import ru.cristalix.clientapi.JavaMod.clientApi
import kotlin.math.cos
import kotlin.math.sin

class SphereElement : AbstractElement() {

    override fun render() {
        val mc = clientApi.minecraft()
        val entity = mc.renderViewEntity
        val pt = mc.timer.renderPartialTicks
        val prevX = entity.prevX
        val prevY = entity.prevY
        val prevZ = entity.prevZ

        GlStateManager.disableLighting()
        GlStateManager.disableTexture2D()
        GlStateManager.disableAlpha()
        GlStateManager.disableCull()
        GlStateManager.shadeModel(GL11.GL_SMOOTH)
        GlStateManager.enableBlend()
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE)
        GlStateManager.color(color.red.toFloat(), color.green.toFloat(), color.blue.toFloat(), color.alpha.toFloat())

        val accuracy = 20.0
        val dx = offset.x - (entity.x - prevX) * pt - prevX
        val dy = offset.y - (entity.y - prevY) * pt - prevY
        val dz = offset.z - (entity.z - prevZ) * pt - prevZ

        var j: Int
        var i = 0
        while (i <= accuracy) {
            val lat0: Double = Math.PI * (-0.5 + (i - 1).toDouble() / accuracy)
            val z0 = sin(lat0)
            val zr0 = cos(lat0)
            val lat1: Double = Math.PI * (-0.5 + i.toDouble() / accuracy)
            val z1 = sin(lat1)
            val zr1 = cos(lat1)

            val tessellator = clientApi.tessellator()
            val buffer = tessellator.bufferBuilder

            j = 0
            buffer.begin(GL11.GL_QUAD_STRIP, DefaultVertexFormats.POSITION_NORMAL)
            while (j <= accuracy) {
                val lng = 2.0 * Math.PI * (j - 1.0) / accuracy
                val x = cos(lng) * size.x
                val y = sin(lng) * size.y

                writeNormalAndPosition(buffer, dx + x * zr0, dy + y * zr0, dz + size.z * z0)
                writeNormalAndPosition(buffer, dx + x * zr1, dy + y * zr1, dz + size.z * z1)
                j++
            }
            tessellator.draw()
            i++
        }

        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_CONSTANT_COLOR)
        GlStateManager.shadeModel(GL11.GL_FLAT)
        GlStateManager.enableTexture2D()
        GlStateManager.enableAlpha()
        GlStateManager.enableCull()
    }

    private fun writeNormalAndPosition(buffer: BufferBuilder, x: Double, y: Double, z: Double) =
        buffer.normal(x.toFloat(), y.toFloat(), z.toFloat()).pos(x, y, z).endVertex()
}