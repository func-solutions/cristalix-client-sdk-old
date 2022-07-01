package ru.cristalix.uiengine.utility

val WHITE = Color(255, 255, 255, 1.0)
val BLACK = Color(0, 0, 0, 1.0)
val TRANSPARENT = Color(0, 0, 0, 0.0)

open class Color(
    open var red: Int = 0,
    open var green: Int = 0,
    open var blue: Int = 0,
    open var alpha: Double = 1.0,
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

fun hex(hex: String, alpha: Double = 1.0): Color {

    if (hex.length == 6) {
        val r = "0123456789abcdef".indexOf(hex[0]) shl 4 or "0123456789abcdef".indexOf(hex[1])
        val g = "0123456789abcdef".indexOf(hex[2]) shl 4 or "0123456789abcdef".indexOf(hex[3])
        val b = "0123456789abcdef".indexOf(hex[4]) shl 4 or "0123456789abcdef".indexOf(hex[5])
        return Color(r, g, b, alpha)
    } else if (hex.length == 8) {
        val a = "0123456789abcdef".indexOf(hex[0]) shl 4 or "0123456789abcdef".indexOf(hex[1])
        val r = "0123456789abcdef".indexOf(hex[2]) shl 4 or "0123456789abcdef".indexOf(hex[3])
        val g = "0123456789abcdef".indexOf(hex[4]) shl 4 or "0123456789abcdef".indexOf(hex[5])
        val b = "0123456789abcdef".indexOf(hex[6]) shl 4 or "0123456789abcdef".indexOf(hex[7])
        return Color(r, g, b, a / 255.0)
    }

    return BLACK
}
