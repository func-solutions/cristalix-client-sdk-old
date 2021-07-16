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

typealias Position = V

inline var Position.x: Double
    get() = this[0]
    set(value) = set(0, value)

inline var Position.y: Double
    get() = this[1]
    set(value) = set(1, value)

inline var Position.z: Double
    get() = this[2]
    set(value) = set(2, value)

typealias V3 = Position
typealias V2 = Position

typealias Rotation = V

inline var Rotation.degrees: Double
    get() = this[3]
    set(value) = set(3, value)




typealias Color = V

inline var Color.alpha: Double
    get() = this[0]
    set(value) = set(0, value)

inline var Color.red: Int
    get() = this[1].toInt()
    set(value) = set(1, value)

inline var Color.green: Int
    get() = this[2].toInt()
    set(value) = set(2, value)

inline var Color.blue: Int
    get() = this[3].toInt()
    set(value) = set(3, value)



open class V : Iterable<Double> {

    var numbers: DoubleArray
    val size: Int
        get() = numbers.size

    constructor(vararg numbers: Double) {
        this.numbers = numbers
    }

    constructor(vararg numbers: Number) {
        this.numbers = numbers.map { it.toDouble() }.toDoubleArray()
    }

    constructor(x: Number = 0, y: Number = 0, z: Number = 0) :
            this(x.toDouble(), y.toDouble(), z.toDouble())

    fun clone(): V = V(*numbers.clone())

    open operator fun get(index: Int) = numbers[index]

    open operator fun set(index: Int, value: Number) {
        numbers[index] = value.toDouble()
    }

    infix fun fill(value: Number) {
        numbers.fill(value.toDouble())
    }

    fun write(other: V) {
        numbers.forEachIndexed { i, v ->
            other[i] = v
        }
    }

    inline fun map(crossinline function: (value: Double) -> Number): V {
        val clone = clone()
        clone.numbers.forEachIndexed { i, d -> clone.numbers[i] = function(d).toDouble() }
        return clone
    }

    inline fun mapIndexed(crossinline function: (value: Double, index: Int) -> Number): V {
        val clone = clone()
        clone.numbers.forEachIndexed { i, d -> clone.numbers[i] = function(d, i).toDouble() }
        return clone
    }

    operator fun plus(v: V): V {
        val a = if (v.size > size) v else this
        val b = if (a == this) v else this

        return a.mapIndexed { value, index -> value + b.numbers[index] }
    }

    operator fun minus(v: V): V = plus(-v)

    operator fun unaryMinus(): V = map { -it }

    operator fun times(n: Number): V = map { it * n.toDouble() }

    operator fun div(n: Number): V = map { it / n.toDouble() }

    override fun iterator(): Iterator<Double> {
        return numbers.toList().iterator()
    }


}

//open class V2(
//    open var x: Double = 0.0,
//    open var y: Double = 0.0
//
//) {
//    open fun write(another: V2) {
//        another.x = x
//        another.y = y
//    }
//}

//open class V3(
//    x: Double = 0.0,
//    y: Double = 0.0,
//    open var z: Double = 0.0
//) : V2(x, y) {
//    override fun write(another: V2) {
//        super.write(another)
//        if (another is V3) another.z = z
//    }
//
//    fun distanceSquared3(another: V3): Double {
//        val x = another.x - this.x
//        val y = another.y - this.y
//        val z = another.z - this.z
//        return x * x + y * y + z * z
//    }
//}

//open class Rotation(
//    open var degrees: Double = 0.0,
//    x: Double = 0.0,
//    y: Double = 0.0,
//    z: Double = 1.0,
//) : V3(x, y, z) {
//    override fun write(another: V2) {
//        super.write(another)
//        if (another is Rotation) another.degrees = degrees
//    }
//}
