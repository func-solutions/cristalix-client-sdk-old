package ru.cristalix.uiengine.utility

import dev.xdark.clientapi.math.MathHelper
import org.lwjgl.util.vector.Matrix4f

fun Matrix4f.translate(x: Float, y: Float, z: Float) {
    m30 += m00 * x + m10 * y + m20 * z
    m31 += m01 * x + m11 * y + m21 * z
    m32 += m02 * x + m12 * y + m22 * z
    m33 += m03 * x + m13 * y + m23 * z
}

inline fun Matrix4f.translate(x: Double, y: Double, z: Double) {
    translate(x.toFloat(), y.toFloat(), z.toFloat())
}

fun Matrix4f.rotate(angle: Float, x: Float, y: Float, z: Float) {
    val c = MathHelper.cos(angle)
    val s = MathHelper.sin(angle)
    val oneminusc = 1.0f - c
    val xy = x * y
    val yz = y * z
    val xz = x * z
    val xs = x * s
    val ys = y * s
    val zs = z * s

    val f00 = x * x * oneminusc + c
    val f01 = xy * oneminusc + zs
    val f02 = xz * oneminusc - ys
    val f10 = xy * oneminusc - zs
    val f11 = y * y * oneminusc + c
    val f12 = yz * oneminusc + xs
    val f20 = xz * oneminusc + ys
    val f21 = yz * oneminusc - xs
    val f22 = z * z * oneminusc + c

    val t00 = m00 * f00 + m10 * f01 + m20 * f02
    val t01 = m01 * f00 + m11 * f01 + m21 * f02
    val t02 = m02 * f00 + m12 * f01 + m22 * f02
    val t03 = m03 * f00 + m13 * f01 + m23 * f02
    val t10 = m00 * f10 + m10 * f11 + m20 * f12
    val t11 = m01 * f10 + m11 * f11 + m21 * f12
    val t12 = m02 * f10 + m12 * f11 + m22 * f12
    val t13 = m03 * f10 + m13 * f11 + m23 * f12
    m20 = m00 * f20 + m10 * f21 + m20 * f22
    m21 = m01 * f20 + m11 * f21 + m21 * f22
    m22 = m02 * f20 + m12 * f21 + m22 * f22
    m23 = m03 * f20 + m13 * f21 + m23 * f22
    m00 = t00
    m01 = t01
    m02 = t02
    m03 = t03
    m10 = t10
    m11 = t11
    m12 = t12
    m13 = t13
}

inline fun Matrix4f.rotate(angle: Double, x: Double, y: Double, z: Double) {
    rotate(angle.toFloat(), x.toFloat(), y.toFloat(), z.toFloat())
}

inline fun Matrix4f.rotate(angle: Float, x: Double, y: Double, z: Double) {
    rotate(angle, x.toFloat(), y.toFloat(), z.toFloat())
}

fun Matrix4f.scale(x: Float, y: Float, z: Float) {
    m00 *= x
    m01 *= x
    m02 *= x
    m03 *= x
    m10 *= y
    m11 *= y
    m12 *= y
    m13 *= y
    m20 *= z
    m21 *= z
    m22 *= z
    m23 *= z
}

inline fun Matrix4f.scale(x: Double, y: Double, z: Double) {
    scale(x.toFloat(), y.toFloat(), z.toFloat())
}