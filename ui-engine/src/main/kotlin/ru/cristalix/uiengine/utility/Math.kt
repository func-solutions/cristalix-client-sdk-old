package ru.cristalix.uiengine.utility

import java.util.*

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

open class V() : Iterable<Double> {

    private lateinit var data: DoubleArray
    val size: Int
        get() = data.size

    constructor(vararg data: Double) {
        this.data = data
    }

    constructor(vararg data: Number) {
        this.data = data.map { it.toDouble() }.toDoubleArray()
    }

    constructor(x: Number = 0, y: Number = 0, z: Number = 0) :
            this(x.toDouble(), y.toDouble(), z.toDouble())

    fun clone(): V = V(*data.clone())

    open operator fun get(index: Int) = data[index]

    open operator fun set(index: Int, value: Number) {
        data[index] = value.toDouble()
    }

    infix fun fill(value: Number) {
        data.fill(value.toDouble())
    }

    fun write(other: V) {
        data.forEachIndexed { i, v ->
            other[i] = v
        }
    }

    inline fun map(crossinline function: (value: Double) -> Number): V {
        val clone = clone()
        clone.data.forEachIndexed { i, d -> clone.data[i] = function(d).toDouble() }
        return clone
    }

    inline fun mapIndexed(crossinline function: (value: Double, index: Int) -> Number): V {
        val clone = clone()
        clone.data.forEachIndexed { i, d -> clone.data[i] = function(d, i).toDouble() }
        return clone
    }

    operator fun plus(v: V): V {
        val a = if (v.size > size) v else this
        val b = if (a == this) v else this

        return a.mapIndexed { value, index -> value + b.data[index] }
    }

    operator fun minus(v: V): V = plus(-v)

    operator fun unaryMinus(): V = map { -it }

    operator fun times(n: Number): V = map { it * n.toDouble() }

    operator fun div(n: Number): V = map { it / n.toDouble() }

    override fun iterator(): Iterator<Double> {
        return data.toList().iterator()
    }


}

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

    fun distanceSquared3(another: V3): Double {
        val x = another.x - this.x
        val y = another.y - this.y
        val z = another.z - this.z
        return x * x + y * y + z * z
    }

}

open class Rotation(
    open var degrees: Double = 0.0,
    x: Double = 0.0,
    y: Double = 0.0,
    z: Double = 1.0,
) : V3(x, y, z) {
    override fun write(another: V2) {
        super.write(another)
        if (another is Rotation) another.degrees = degrees
    }
}
