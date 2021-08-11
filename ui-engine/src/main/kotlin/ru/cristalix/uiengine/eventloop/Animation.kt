package ru.cristalix.uiengine.eventloop

import ru.cristalix.uiengine.Easing
import ru.cristalix.uiengine.Easings
import ru.cristalix.uiengine.element.AbstractElement

data class Animation(val element: AbstractElement, val propertyIndex: Int) {

    var startedTime: Long = 0
    var duration: Long = 0
    var startValue: Double = 0.0
    var targetValue: Double = 0.0
    var lastValue: Double = 0.0
    var easing: Easing = Easings.NONE

    init {
        this.lastValue = element.properties[propertyIndex]
        this.targetValue = this.lastValue
    }

    fun newTarget(value: Double, duration: Long, easing: Easing) {
        this.startValue = this.lastValue
        this.targetValue = value
        this.startedTime = System.currentTimeMillis()
        this.duration = duration
        this.easing = easing
    }

    fun update(time: Long): Boolean {
        var part = (time - this.startedTime).toDouble() / this.duration
        val alive = part <= 1.0
        val value: Double
        if (alive) {
            part = this.easing.ease(part)
            value = startValue + (targetValue - startValue) * part
        } else {
            this.startedTime = 0
            value = targetValue
        }
        this.lastValue = value
        this.element.changeProperty(propertyIndex, value)
        return alive
    }

}