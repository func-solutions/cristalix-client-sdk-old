package ru.cristalix.uiengine.eventloop

interface EventLoop {
    val runningAnimations: MutableList<Animation>
    var animationContext: AnimationContext?

    fun schedule(delaySeconds: Double, action: () -> Unit): Task
    fun schedule(delaySeconds: Long, action: () -> Unit): Task =
        schedule(delaySeconds.toDouble(), action)

    fun schedule(delaySeconds: Int, action: () -> Unit): Task =
        schedule(delaySeconds.toDouble(), action)

    fun schedule(delaySeconds: Float, action: () -> Unit): Task =
        schedule(delaySeconds.toDouble(), action)

    fun schedule(delaySeconds: Number, action: () -> Unit): Task =
        schedule(delaySeconds.toDouble(), action)

    fun update()
}
