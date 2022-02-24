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
        // GL начало
        GlStateManager.disableLighting()
        GlStateManager.disableTexture2D()
        GlStateManager.disableAlpha()
        GlStateManager.disableCull()
        GlStateManager.shadeModel(GL11.GL_SMOOTH)
        GlStateManager.enableBlend()
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE)
        GlStateManager.color(color.red.toFloat(), color.green.toFloat(), color.blue.toFloat(), color.alpha.toFloat())

        // Количество вершин на срезе (можно разбить на два измерения)
        val accuracy = 20.0

        // Координаты сферы (если они есть и у родителя - координаты самой сферы становятся относительными)
        val dx = offset.x
        val dy = offset.y
        val dz = offset.z

        // Начало отрисовки сферы
        var j: Int
        var i = 0
        while (i <= accuracy) {
            // Нахождение двух углов
            val lat0 = Math.PI * (-0.5 + (i - 1).toDouble() / accuracy)
            val z0 = sin(lat0)
            val zr0 = cos(lat0)
            val lat1 = Math.PI * (-0.5 + i.toDouble() / accuracy)
            val z1 = sin(lat1)
            val zr1 = cos(lat1)

            // Создание буффера
            val tessellator = clientApi.tessellator()
            val buffer = tessellator.bufferBuilder

            // Определение типа отрисовки
            buffer.begin(GL11.GL_QUAD_STRIP, DefaultVertexFormats.POSITION_NORMAL)
            j = 0
            while (j <= accuracy) {
                val lng = 2.0 * Math.PI * (j - 1.0) / accuracy
                // Прочет x, y координат точек сферы
                val x = cos(lng) * size.x * 10.0
                val y = sin(lng) * size.y * 10.0

                // Заполнение буффера вершинами и нормалями
                writeNormalAndPosition(buffer, dx + x * zr0, dy + y * zr0, dz + size.z * 10.0 * z0)
                writeNormalAndPosition(buffer, dx + x * zr1, dy + y * zr1, dz + size.z * 10.0 * z1)
                j++
            }
            // Отрисовка и чистка буффера
            tessellator.draw()
            i++
        }

        // GL конец
        GlStateManager.color(1f, 1f, 1f, 1f)
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_CONSTANT_COLOR)
        GlStateManager.shadeModel(GL11.GL_FLAT)
        GlStateManager.enableTexture2D()
        GlStateManager.enableAlpha()
        GlStateManager.enableCull()
    }

    private fun writeNormalAndPosition(buffer: BufferBuilder, x: Double, y: Double, z: Double) =
        buffer.normal(x.toFloat(), y.toFloat(), z.toFloat()).pos(x, y, z).endVertex()
}