package ru.cristalix.uiengine.eventloop

interface EventLoop {

    val runningAnimations: MutableList<Animation>

    var animationContext: AnimationContext?

    fun schedule(delaySeconds: Number, action: () -> Unit): Task

    fun update()
}