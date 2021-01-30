package ru.cristalix.uiengine.utility

data class Color(
    var red: Int,
    var green: Int,
    var blue: Int,
    var alpha: Number
) {
    companion object {
        val WHITE = Color(255, 255, 255, 1)
        val BLACK = Color(0, 0, 0, 1)
        val TRANSPARENT = Color(0, 0, 0, 0)
    }
}
