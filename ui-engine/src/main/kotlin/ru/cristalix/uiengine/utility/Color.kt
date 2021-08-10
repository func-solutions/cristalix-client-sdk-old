package ru.cristalix.uiengine.utility

import kotlin.math.floor

val WHITE = Color(255, 255, 255, 1.0)
val BLACK = Color(0, 0, 0, 1.0)
val TRANSPARENT = Color(0, 0, 0, 0.0)

open class Color(
    open var red: Int = 0,
    open var green: Int = 0,
    open var blue: Int = 0,
    open var alpha: Double = 1.0
) {
    open fun write(another: Color) {
        another.red = this.red
        another.green = this.green
        another.blue = this.blue
        another.alpha = this.alpha
    }

    fun toGuiHex(): Int {
        return (((alpha * 255).toInt() and 0xFF) shl 24).or(red shl 16).or(green shl 8).or(blue)
    }
}
