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
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
        GlStateManager.color(color.red.toFloat() / 255f, color.green.toFloat()  / 255f, color.blue.toFloat()  / 255f, color.alpha.toFloat())

        // Количество вершин на срезе (можно разбить на два измерения)
        val accuracy = 20.0

        val dx = size.x / 2
        val dy = size.y / 2
        val dz = size.z / 2

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
                val x = cos(lng) * size.x * 10.0 / 2
                val y = sin(lng) * size.y * 10.0 / 2

                // Заполнение буффера вершинами и нормалями
                writeNormalAndPosition(buffer, dx + x * zr0, dy + y * zr0, dz + size.z / 2 * 10.0 * z0)
                writeNormalAndPosition(buffer, dx + x * zr1, dy + y * zr1, dz + size.z / 2 * 10.0 * z1)
                j++
            }
            // Отрисовка и чистка буффера
            tessellator.draw()
            i++
        }

        // GL конец
        GlStateManager.color(1f, 1f, 1f, 1f)
        GlStateManager.shadeModel(GL11.GL_FLAT)
        GlStateManager.enableTexture2D()
        GlStateManager.enableAlpha()
        GlStateManager.enableCull()
    }

    private fun writeNormalAndPosition(buffer: BufferBuilder, x: Double, y: Double, z: Double) =
        buffer.normal(x.toFloat(), y.toFloat(), z.toFloat()).pos(x, y, z).endVertex()
}
