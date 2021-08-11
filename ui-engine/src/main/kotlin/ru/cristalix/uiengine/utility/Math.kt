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
    @JvmField open var x: Double = 0.0,
    @JvmField open var y: Double = 0.0
) {
    open fun write(another: V2) {
        another.x = x
        another.y = y
    }
}

open class V3(
    x: Double = 0.0,
    y: Double = 0.0,
    @JvmField open var z: Double = 0.0
) : V2(x, y) {
    override fun write(another: V2) {
        super.write(another)
        if (another is V3) another.z = z
    }

    fun distanceSquared3(another: V3): Double {
        val x = another.x - this.x
        val y = another.y - this.y
        val z = another.z - this.z
        return x * x + y * y + z * z
    }
}

open class Rotation(
    @JvmField open var degrees: Double = 0.0,
    x: Double = 0.0,
    y: Double = 0.0,
    z: Double = 1.0,
) : V3(x, y, z) {
    override fun write(another: V2) {
        super.write(another)
        if (another is Rotation) another.degrees = degrees
    }
}
