package ru.cristalix.uiengine.utility

const val MATRIX_COUNT = 9
const val MATRIX_OFFSET = 3

// Positive numbers are matrices that are directly converted to GL transformations
const val alignMatrix = 0
const val offsetMatrix = 1
const val scaleMatrix = 2
const val rotationMatrix = 3
const val originMatrix = 4

// Amount of direct GL matrices for preallocations
const val matrixFields = 5
const val sizeMatrix = -1
const val colorMatrix = -2
const val uvMatrix = -3

val allMatrices =
    intArrayOf(alignMatrix, rotationMatrix, offsetMatrix, scaleMatrix, originMatrix, sizeMatrix, colorMatrix, uvMatrix)

inline operator fun DoubleArray.get(property: Property): Double {
    return this[property.ordinal]
}

inline operator fun DoubleArray.set(property: Property, value: Double): Double {
    this[property.ordinal] = value
    return value
}

inline operator fun DoubleArray.set(property: Property, value: Long): Double =
    set(property, value.toDouble())

inline operator fun DoubleArray.set(property: Property, value: Int): Double =
    set(property, value.toDouble())

inline operator fun DoubleArray.set(property: Property, value: Float): Double =
    set(property, value.toDouble())

inline operator fun DoubleArray.set(property: Property, value: Number): Double =
    set(property, value.toDouble())

enum class Property(
    vararg val matrixInfluence: Int
) {

    OffsetX(offsetMatrix),
    OffsetY(offsetMatrix),
    OffsetZ(offsetMatrix),

    ScaleX(scaleMatrix),
    ScaleY(scaleMatrix),
    ScaleZ(scaleMatrix),

    AlignX(alignMatrix),
    AlignY(alignMatrix),
    AlignZ(alignMatrix),

    OriginX(originMatrix),
    OriginY(originMatrix),
    OriginZ(originMatrix),

    RotationX(rotationMatrix),
    RotationY(rotationMatrix),
    RotationZ(rotationMatrix),
    RotationAngle(rotationMatrix),

    ColorA(colorMatrix),
    ColorR(colorMatrix),
    ColorG(colorMatrix),
    ColorB(colorMatrix),

    ParentSizeX(alignMatrix),
    ParentSizeY(alignMatrix),
    ParentSizeZ(alignMatrix),

    SizeX(sizeMatrix, originMatrix),
    SizeY(sizeMatrix, originMatrix),
    SizeZ(sizeMatrix, originMatrix),

    TextureX(uvMatrix),
    TextureY(uvMatrix),
    TextureWidth(uvMatrix),
    TextureHeight(uvMatrix),

    ;

    companion object {
        @JvmField
        val VALUES = values()
    }
}
