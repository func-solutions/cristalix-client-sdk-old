package ru.cristalix.uiengine.utility

import kotlin.math.*

typealias Easing = (Double) -> Double

fun powIn(n: Int): Easing = { it.pow(n) }
fun powOut(n: Int): Easing = { 1 - (1 - it).pow(n) }
fun powBoth(n: Int): Easing = {
    if (it < 0.5)
        2.0.pow(n - 1) * it.pow(n)
    else
        1 - (-2 * it + 2).pow(n) / 2.0
}

object Easings {

    private const val c1 = 1.70158
    private const val c2 = c1 * 1.525
    private const val c3 = c1 + 1
    private const val c4 = 2 * PI / 3
    private const val c5 = 2 * PI / 4.5

    val NONE: Easing = { it }

    val QUAD_IN = powIn(2)
    val QUAD_OUT = powOut(2)
    val QUAD_BOTH = powBoth(2)

    val CUBIC_IN = powIn(3)
    val CUBIC_OUT = powOut(3)
    val CUBIC_BOTH = powBoth(3)

    val QUART_IN = powIn(4)
    val QUART_OUT = powOut(4)
    val QUART_BOTH = powBoth(4)

    val QUINT_IN = powIn(5)
    val QUINT_OUT = powOut(5)
    val QUINT_BOTH = powBoth(5)

    val SINE_IN: Easing = { 1 - cos(it * PI / 2) }
    val SINE_OUT: Easing = { sin(it * PI / 2) }
    val SINE_BOTH: Easing = { -(cos(PI * it) - 1) / 2 }

    val CIRC_IN: Easing = { 1 - sqrt(1 - it.pow(2)) }
    val CIRC_OUT: Easing = { sqrt(1 - (it - 1).pow(2)) }
    val CIRC_BOTH: Easing = {
        if (it < 0.5)
            (1 - sqrt(1 - (2 * it).pow(2))) / 2
        else
            (sqrt(1 - (-2 * it + 2).pow(2)) + 1) / 2
    }

    val ELASTIC_IN: Easing = {
        if (it == 0.0 || it == 1.0)
            it
        else
            (-2.0).pow(10 * it - 10) * sin((it * 10 - 10.75) * c4)
    }
    val ELASTIC_OUT: Easing = {
        if (it == 0.0 || it == 1.0)
            it
        else
            (2.0).pow(-10 * it) * sin((it * 10 - 0.75) * c4) + 1
    }
    val ELASTIC_BOTH: Easing = {
        if (it == 0.0 || it == 1.0)
            it
        else if (it < 0.5)
            -((2.0).pow(20 * it - 10) * sin((20 * it - 11.125) * c5)) / 2
        else
            (2.0).pow(-20 * it + 10) * sin((20 * it - 11.125) * c5) / 2 + 1
    }

    val EXPO_IN: Easing = {
        if (it != 0.0)
            (2.0).pow(10 * it - 10)
        else it
    }
    val EXPO_OUT: Easing = {
        if (it != 1.0)
            1 - (2.0).pow(-10 * it)
        else it
    }
    val EXPO_BOTH: Easing = {
        if (it == 0.0 || it == 1.0)
            it
        else if (it < 0.5)
            (2.0).pow(20 * it - 10) / 2
        else
            (2 - (2.0).pow(-20 * it + 10)) / 2
    }

    val BACK_IN: Easing = { c3 * it.pow(3) - c1 * it.pow(2) }
    val BACK_OUT: Easing = { 1 + c3 * (it - 1).pow(3) + c1 * (it - 1).pow(2) }
    val BACK_BOTH: Easing = {
        if (it < 0.5)
            (2 * it).pow(2) * ((c2 + 1) * 2 * it - c2) / 2
        else
            ((2 * it - 2).pow(2) * ((c2 + 1) * (it * 2 - 2) + c2) + 2) / 2
    }

    val BOUNCE_OUT: Easing = { x ->
        val n1 = 7.5625
        val d1 = 2.75
        if (x < 1 / d1) {
            n1 * x.pow(2);
        } else if (x < 2 / d1) {
            n1 * (x - 1.5 / d1).pow(2) + 0.75;
        } else if (x < 2.5 / d1) {
            n1 * (x - 2.25 / d1).pow(2) + 0.9375;
        } else {
            n1 * (x - 2.625 / d1).pow(2) + 0.984375;
        }
    }
    val BOUNCE_IN: Easing = {
        1 - BOUNCE_OUT(1 - it)
    }
    val BOUNCE_BOTH: Easing = {
        if (it < 0.5)
            (1 - BOUNCE_OUT(1 - 2 * it)) / 2
        else
            (1 + BOUNCE_OUT(2 * it - 1)) / 2
    }


}