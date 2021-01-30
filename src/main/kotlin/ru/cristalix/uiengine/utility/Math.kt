package ru.cristalix.uiengine.utility

//open class V2(
//    var x: Number = 0,
//    var y: Number = 0
//)
//
//class V3(
//    x: Number = 0,
//    y: Number = 0,
//    var z: Number = 0
//) : V2(x, y)

open class V2(
    open var x: Double = 0.0,
    open var y: Double = 0.0

) {
    open fun write(another: V2) {
        another.x = x
        another.y = y
    }
}

open class V3(
    x: Double = 0.0,
    y: Double = 0.0,
    open var z: Double = 0.0
) : V2(x, y) {
    override fun write(another: V2) {
        super.write(another)
        if (another is V3) another.z = z
    }
}

